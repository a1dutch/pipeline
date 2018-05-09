package uk.co.a1dutch.pipeline

public class Docker implements Serializable {
  private steps

  Docker(steps) {
    this.steps = steps
  }

  def build(String repository, String artifact, String version, boolean tagLatest, File directory=new File(".")) {
    steps.sh("docker build -t ${repository}/${artifact}:${version} ${directory}")
    if (tagLatest) {
      steps.sh("docker tag ${repository}/${artifact}:${version} ${repository}/${artifact}:latest")
    }
    steps.sh("docker push ${repository}/${artifact}:${version}")
    if (tagLatest) {
      steps.sh("docker push ${repository}/${artifact}:latest")
    }
  }
}
