#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Helm

def call(body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  new Helm(this, config.namespace).deploy();
}
