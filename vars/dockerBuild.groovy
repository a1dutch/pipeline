#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Docker

def call(body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  new Docker(this).build(config.repository, config.artifact, config.version, true);
}
