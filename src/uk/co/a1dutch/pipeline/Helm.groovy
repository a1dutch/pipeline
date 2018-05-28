package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import org.jenkinsci.remoting.RoleChecker

public class Helm implements Serializable {
  private FilePathFactory filePathFactory
  private steps
  private Logger logger

  Helm(FilePathFactory factory, def steps) {
    this.filePathFactory = factory
    this.steps = steps
    this.logger = new Logger(steps)
  }

  Helm(def steps) {
    this(new FilePathFactory(steps), steps)
  }

  def install(Map config = [:]) {
    def files = [
      'templates/_helpers.tpl',
      'templates/deployment.yaml',
      'templates/ingress.yaml',
      'templates/Notes.txt',
      'templates/service.yaml',
      '.helmignore',
      'Chart.yaml',
      'values.yaml'
    ]
    for (String file: files) {
      String text = steps.libraryResource("helm/${file}")
        .replaceAll('HELM_APP_ARTIFACT', config.artifact)
        .replaceAll('HELM_APP_VERSION', config.version)
        .replaceAll('HELM_APP_DESCRIPTION', config.description)
      steps.writeFile(file: "${config.artifact}/${file}", text: text)
    }
    steps.sh("helm package ./${config.artifact}")
    steps.sh("helm s3 push ./${config.artifact}-${config.version} internal")
  }

  def deploy(Map config = [:]) {
    String namespace = config.namespace

    FilePath charts = filePathFactory.newFilePath('charts')
    if (!charts.isDirectory()) {
      logger.warn('Charts is not a directory')
      return
    }

    List<FilePath> directories = charts.listDirectories()
    logger.info("found ${directories.size()} charts")
    for (FilePath directory : directories) {
      directory.act(new InstallChart(steps, config.namespace))
    }
  }

  private static final class InstallChart implements FilePath.FileCallable<Void> {
    def steps
    String namespace

    InstallChart(def steps, String namespace) {
      this.steps = steps
      this.namespace = namespace
    }

    public Void invoke(File f, VirtualChannel channel) {
        String chart = f.name
        steps.sh("helm upgrade ${chart} --install charts/${chart} --debug" + (namespace ? " --namespace=${namespace}" : "" ))
        return null;
    }

    public void checkRoles(RoleChecker roleChecker) {
    }

  }
}
