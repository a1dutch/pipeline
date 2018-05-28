package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import jenkins.model.Jenkins

public class FilePathFactory implements Serializable {
  def steps
  Jenkins jenkins

  FilePathFactory(def steps, Jenkins jenkins) {
    this.steps = steps
    this.jenkins = jenkins
  }

  FilePathFactory(def steps) {
    this(steps, Jenkins.getInstance())
  }

  public FilePath newFilePath(String path) {
    return new FilePath(jenkins.getComputer(steps.env['NODE_NAME']).getChannel(), path)
  }
}
