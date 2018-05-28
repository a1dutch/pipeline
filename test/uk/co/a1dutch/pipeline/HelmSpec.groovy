package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import spock.lang.Specification

public class HelmSpec extends Specification {

  GroovyObjectSupport steps = GroovyMock()
  GroovyObjectSupport build = GroovyMock()
  GroovyObjectSupport workspace = GroovyMock()
  FilePathFactory factory = GroovyMock()
  FilePath filePath = GroovyMock()
  FilePath chart = GroovyMock()

  def 'should deploy all charts into namspace'() {
    given:
      Helm helm = new Helm(factory, steps)
    when:
        helm.deploy(namespace: "test")
    then:
      interaction {
        deploy()
        noMoreInteractions()
      }
  }

  def 'install chart with namespace'() {
    given:
      Helm.InstallChart chart = new Helm.InstallChart(steps, 'test')
      File file = GroovyMock()
      VirtualChannel channel = GroovyMock()
    when:
      chart.invoke(file, channel)
    then:
      1 * file.name >> 'nginx'
      1 * steps.sh("helm upgrade nginx --install charts/nginx --debug --namespace=test")
  }

  def 'install chart into default namespace'() {
    given:
      Helm.InstallChart chart = new Helm.InstallChart(steps, null)
      File file = GroovyMock()
      VirtualChannel channel = GroovyMock()
    when:
      chart.invoke(file, channel)
    then:
      1 * file.name >> 'nginx'
      1 * steps.sh("helm upgrade nginx --install charts/nginx --debug")
  }

  def deploy() {
    1 * steps.build >> build
    1 * build.workspace >> workspace
    1 * factory.newFilePath(*_) >> filePath
    1 * filePath.isDirectory() >> true
    1 * filePath.listDirectories() >> [chart]
    1 * chart.act(_)
  }

  def noMoreInteractions() {
    _ * steps.echo(_)
    0 * _
  }
}
