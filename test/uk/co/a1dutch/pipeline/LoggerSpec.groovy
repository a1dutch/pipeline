package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class LoggerSpec extends Specification {

  def 'error'() {
    given:
    GroovyObjectSupport steps = GroovyMock()
    Logger logger = new Logger(steps)
    when:
    logger.error('test')
    then:
    1 * steps.echo('[ERROR] test')
  }

  def 'warn'() {
    given:
    GroovyObjectSupport steps = GroovyMock()
    Logger logger = new Logger(steps)
    when:
    logger.warn('test')
    then:
    1 * steps.echo('[WARN] test')
  }

  def 'info'() {
    given:
    GroovyObjectSupport steps = GroovyMock()
    Logger logger = new Logger(steps)
    when:
    logger.info('test')
    then:
    1 * steps.echo('[INFO] test')
  }
  
  def 'debug'() {
    given:
    GroovyObjectSupport steps = GroovyMock()
    Logger logger = new Logger(steps)
    when:
    logger.debug('test')
    then:
    1 * steps.echo('[DEBUG] test')
  }
}
