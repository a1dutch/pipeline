package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel

public class FilePathFactory implements Serializable {
  public FilePath newFilePath(VirtualChannel channel, String path) {
    return new FilePath(channel, path)
  }
}
