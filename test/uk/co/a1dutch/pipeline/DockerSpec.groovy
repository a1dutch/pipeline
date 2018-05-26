package uk.co.a1dutch.pipeline

import spock.lang.Specification

public class DockerSpec extends Specification {
  def 'should build docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: true, directory: directory)
    then:
      1 * steps.sh("docker build -t a1dutch/test:1.0.1 test-resources/docker/valid")
      1 * steps.sh("docker tag a1dutch/test:1.0.1 a1dutch/test:latest")
      1 * steps.sh("docker push a1dutch/test:1.0.1")
      1 * steps.sh("docker push a1dutch/test:latest")
      0 * _
  }

  def 'should tag latest by default'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", directory: directory)
    then:
      1 * steps.sh("docker build -t a1dutch/test:1.0.1 test-resources/docker/valid")
      1 * steps.sh("docker tag a1dutch/test:1.0.1 a1dutch/test:latest")
      1 * steps.sh("docker push a1dutch/test:1.0.1")
      1 * steps.sh("docker push a1dutch/test:latest")
      0 * _
  }

  def 'should not tag latest docker image'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "1.0.1", tagLatest: false, directory: directory)
    then:
      1 * steps.sh("docker build -t a1dutch/test:1.0.1 test-resources/docker/valid")
      1 * steps.sh("docker push a1dutch/test:1.0.1")
      0 * _
  }

  def 'should tag latest docker image only'() {
    given:
      GroovyObjectSupport steps = GroovyMock()
      Docker docker = new Docker(steps)
      File directory = new File("test-resources/docker/valid")
    when:
        docker.build(repository: "a1dutch", artifact: "test", version: "latest", tagLatest: true, directory: directory)
    then:
      1 * steps.sh("docker build -t a1dutch/test:latest test-resources/docker/valid")
      1 * steps.sh("docker push a1dutch/test:latest")
      0 * _
  }
}
