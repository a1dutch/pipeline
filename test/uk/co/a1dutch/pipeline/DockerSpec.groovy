package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class DockerSpec extends Specification {

  def 'should use existing dockerfile'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: true)
    then:
      interaction {
        dockerfileExists(steps, true)
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions(steps)
      }
  }

  def 'should build docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: true)
    then:
      interaction {
        dockerfileExists(steps, false)
        dockerfileIsCopied(steps)
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions(steps)
      }
  }

  def 'should tag latest by default'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1")
    then:
      interaction {
        dockerfileExists(steps, true)
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions(steps)
      }
  }

  def 'should not tag latest docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: false)
    then:
      interaction {
        dockerfileExists(steps, true)
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        noMoreInteractions(steps)
      }
  }

  def 'should tag latest docker image only'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "latest", tagLatest: true)
    then:
      interaction {
        dockerfileExists(steps, true)
        dockerIsBuilt(steps, 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions(steps)
      }
  }

  def noMoreInteractions(def steps) {
    _ * steps.echo(_)
    0 * _
  }

  def dockerPush(def steps, String artifact) {
    1 * steps.sh("docker push ${artifact}")
  }

  def dockerTag(def steps, String artifact, String tag) {
    1 * steps.sh("docker tag ${artifact} ${tag}")
  }

  def dockerIsBuilt(def steps, String artifact) {
    1 * steps.sh("docker build -t ${artifact} \\.")
  }

  def dockerfileIsCopied(def steps) {
    1 * steps.writeFile([file: "Dockerfile", text: null])
    1 * steps.libraryResource("Dockerfile-java8")
  }

  def dockerfileExists(def steps, boolean exists) {
    1 * steps.fileExists('Dockerfile') >> exists
  }
}
