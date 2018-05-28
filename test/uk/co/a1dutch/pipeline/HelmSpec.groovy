package uk.co.a1dutch.pipeline

import hudson.FilePath
import hudson.remoting.VirtualChannel
import spock.lang.Specification

public class HelmSpec extends Specification {

  GroovyObjectSupport steps = GroovyMock()
  FilePathFactory factory = GroovyMock()
  FilePath filePath = GroovyMock()
  FilePath chart = GroovyMock()

  def 'should build and push chart to internal repository'() {
    given:
      Helm helm = new Helm(factory, steps)
    when:
      helm.install([artifact: 'test', version: '1.0.1', description: 'descripton'])
    then:
      interaction {
        def files = [
          'templates/_helpers.tpl',
          'templates/deployment.yaml',
          'templates/ingress.yaml',
          'templates/Notes.txt',
          'templates/service.yaml',
          '.helmignore',
          'Chart.yaml',
          'values.yaml'
        ]
        for (String file: files) {
          1 * steps.libraryResource("helm/${file}") >> ''
          1 * steps.writeFile([file: "test/${file}", text: ''])
        }

        noMoreInteractions()

        1 * steps.sh('helm package ./test')
        1 * steps.sh('helm s3 push ./test-1.0.1 internal')
      }
  }

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
    1 * factory.newFilePath(_) >> filePath
    1 * filePath.isDirectory() >> true
    1 * filePath.listDirectories() >> [chart]
    1 * chart.act(_)
  }

  def noMoreInteractions() {
    _ * steps.echo(_)
    0 * _
  }
}
