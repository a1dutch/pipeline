package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class DockerSpec extends Specification {

  def 'should use existing dockerfile'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: true, directory: directory)
    then:
      interaction {
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1', 'test-resources/docker/valid')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions()
      }
  }

  def 'should build docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/no-dockerfile")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: true, directory: directory)
    then:
      interaction {
        dockerfileIsCopied(steps)
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1', 'test-resources/docker/no-dockerfile')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions()
      }
  }

  def 'should tag latest by default'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", directory: directory)
    then:
      interaction {
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1', 'test-resources/docker/valid')
        dockerTag(steps, 'a1dutch/test:1.0.1', 'a1dutch/test:latest')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions()
      }
  }

  def 'should not tag latest docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: false, directory: directory)
    then:
      interaction {
        dockerIsBuilt(steps, 'a1dutch/test:1.0.1', 'test-resources/docker/valid')
        dockerPush(steps, 'a1dutch/test:1.0.1')
        noMoreInteractions()
      }
  }

  def 'should tag latest docker image only'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "latest", tagLatest: true, directory: directory)
    then:
      interaction {
        dockerIsBuilt(steps, 'a1dutch/test:latest', 'test-resources/docker/valid')
        dockerPush(steps, 'a1dutch/test:latest')
        noMoreInteractions()
      }
  }

  def noMoreInteractions() {
    0 * _
  }

  def dockerPush(def steps, String artifact) {
    1 * steps.sh("docker push ${artifact}")
  }

  def dockerTag(def steps, String artifact, String tag) {
    1 * steps.sh("docker tag ${artifact} ${tag}")
  }

  def dockerIsBuilt(def steps, String artifact, String directory) {
    1 * steps.sh("docker build -t ${artifact} ${directory}")
  }

  def dockerfileIsCopied(def steps) {
    1 * steps.writeFile([file: "Dockerfile", text: null])
    1 * steps.libraryResource("Dockerfile-java8")
  }
}
