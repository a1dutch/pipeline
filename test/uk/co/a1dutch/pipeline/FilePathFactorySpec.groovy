package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import spock.lang.Specification

public class FilePathFactorySpec extends Specification {

  def 'should create file path'() {
    given:
      VirtualChannel channel = GroovyMock()
      FilePathFactory factory = new FilePathFactory()
    when:
      FilePath path = factory.newFilePath(channel, 'test')
    then:
      path.remote == 'test'
      path.channel == channel
  }
}
