package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class HelmSpec extends Specification {

  def 'should deploy all charts into namspace'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps, "test")
      File directory = new File("test-resources/helm/valid")
    when:
        helm.deploy(directory)
    then:
      1 * steps.sh("helm upgrade nginx --install charts/nginx --debug --namespace=test")
      1 * steps.sh("helm upgrade apache --install charts/apache --debug --namespace=test")
  }

  def 'should deploy all charts'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
      File directory = new File("test-resources/helm/valid")
    when:
        helm.deploy(directory)
    then:
      1 * steps.sh("helm upgrade nginx --install charts/nginx --debug")
      1 * steps.sh("helm upgrade apache --install charts/apache --debug")
  }

  def 'should find no chart directory in current directory'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
    when:
        helm.deploy()
    then:
      thrown(Exception)
  }

  def 'validates root directory'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
      File directory = new File("test-resources/helm/file")
    when:
        helm.deploy(directory)
    then:
      thrown(Exception)
  }

  def 'validates charts directory'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
      File directory = new File("test-resources/helm/no-chart-directory")
    when:
        helm.deploy(directory)
    then:
      thrown(Exception)
  }

  def 'no charts in chart directory'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
      File directory = new File("test-resources/helm/no-charts")
    when:
        helm.deploy(directory)
    then:
      0 * steps._
  }

  def 'should call helm'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
    when:
      helm.install("test")
    then:
      1 * steps.sh("helm upgrade test --install charts/test --debug")
  }

  def 'should call helm with namespace'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps, "test")
    when:
      helm.install("test")
    then:
      1 * steps.sh("helm upgrade test --install charts/test --debug --namespace=test")
  }

  def 'should validate chart param'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Helm helm = new Helm(steps)
    when:
      helm.install(null)
    then:
      thrown(Exception)
  }
}
