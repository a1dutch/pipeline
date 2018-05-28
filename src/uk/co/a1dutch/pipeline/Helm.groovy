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
    this(new FilePathFactory(), steps)
  }

  def deploy(Map config = [:]) {
    String namespace = config.namespace

    FilePath charts = filePathFactory.newFilePath(steps.build.workspace, 'charts')
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
