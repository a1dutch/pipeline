package uk.co.a1dutch.pipeline

public class Maven implements Serializable {
  def steps
  
  Maven(def steps) {
    this.steps = steps
  }

  def wrapper(String command) {
    steps.sh("./mvnw ${command}")
  }
}
