package uk.co.a1dutch.pipeline

public class Docker implements Serializable {
  private steps

  Docker(steps) {
    this.steps = steps
  }

  def build(String repository, String artifact, String version, Boolean tagLatest, File directory=new File(".")) {
    steps.sh("docker build -t ${repository}/${artifact}:${version} ${directory}")
    if ((tagLatest || tagLatest == null) && version != 'latest') {
      steps.sh("docker tag ${repository}/${artifact}:${version} ${repository}/${artifact}:latest")
    }
    steps.sh("docker push ${repository}/${artifact}:${version}")
    if ((tagLatest || tagLatest == null) && version != 'latest') {
      steps.sh("docker push ${repository}/${artifact}:latest")
    }
  }
}
