package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import spock.lang.Specification
import hudson.model.Computer
import jenkins.model.Jenkins

public class FilePathFactorySpec extends Specification {

  def 'should create file path'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Jenkins jenkins = GroovyMock()
      FilePathFactory factory = new FilePathFactory(steps, jenkins)
      VirtualChannel channel = GroovyMock()
      Computer computer = GroovyMock()
    when:
      FilePath path = factory.newFilePath('test')
    then:
      1 * steps.env >> [NODE_NAME:'test']
      1 * jenkins.getComputer(_) >> computer
      1 * computer.channel >> channel
      path.remote == 'test'
      path.channel == channel
  }
}
