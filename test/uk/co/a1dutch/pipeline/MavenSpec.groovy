package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class MavenSpec extends Specification {

  def 'should execute the wrapper'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Maven maven = new Maven(steps)
    when:
        maven.wrapper('clean package')
    then:
      1 * steps.sh('./mvnw clean package')
  }
}
