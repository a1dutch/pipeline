#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Helm

def call(Map config = [:]) {
  new Helm(this).deploy(config.namespace);
}
