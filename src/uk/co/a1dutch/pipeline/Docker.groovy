package uk.co.a1dutch.pipeline

public class Docker implements Serializable {
  private steps

  Docker(steps) {
    this.steps = steps
  }

  def build(Map config) {
    String language = 'java8'
    String repository = config.repository
    String artifact = config.artifact
    String version = config.version ?: 'latest'
    boolean tagLatest = config.tagLatest == null ? true : config.tagLatest.toBoolean()
    File directory = config.directory ?: new File(".")

    if (!new File(directory, 'Dockerfile').exists()) {
      steps.writeFile(file: 'Dockerfile', text: steps.libraryResource("Dockerfile-${language}"))
    }

    steps.sh("docker build -t ${repository}/${artifact}:${version} ${directory}")
    if (tagLatest && version != 'latest') {
      steps.sh("docker tag ${repository}/${artifact}:${version} ${repository}/${artifact}:latest")
    }
    steps.sh("docker push ${repository}/${artifact}:${version}")
    if (tagLatest && version != 'latest') {
      steps.sh("docker push ${repository}/${artifact}:latest")
    }
  }
}
