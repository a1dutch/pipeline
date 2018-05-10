#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Docker

def call(Map config = [:]) {
  new Docker(this).build(config.repository, config.artifact, config.version, true);
}
