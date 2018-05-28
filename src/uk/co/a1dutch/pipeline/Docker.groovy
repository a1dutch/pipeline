package uk.co.a1dutch.pipeline

public class Docker implements Serializable {
  private steps
  private logger

  Docker(steps) {
    this.steps = steps
    this.logger = new Logger(steps)
  }

  def build(Map config = [:]) {
    String language = 'java8'
    String repository = config.repository
    String artifact = config.artifact
    String version = config.version ?: 'latest'
    boolean tagLatest = config.tagLatest == null ? true : config.tagLatest.toBoolean()

    if (steps.fileExists('Dockerfile')) {
      logger.info("Using existing docker file in repository")
    } else {
      logger.info("Copying Dockerfile-${language}")
      steps.writeFile(file: 'Dockerfile', text: steps.libraryResource("Dockerfile-${language}"))
    }

    steps.sh("docker build -t ${repository}/${artifact}:${version} .")
    if (tagLatest && version != 'latest') {
      steps.sh("docker tag ${repository}/${artifact}:${version} ${repository}/${artifact}:latest")
    }

    steps.sh("docker push ${repository}/${artifact}:${version}")
    if (tagLatest && version != 'latest') {
      steps.sh("docker push ${repository}/${artifact}:latest")
    }
  }
}
