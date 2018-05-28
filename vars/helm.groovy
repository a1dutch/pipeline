#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Helm

def call(Map config = [:]) {

  Helm helm = new Helm(this)

  pipeline {
    agent { label 'helm' }
    options {
      disableResume()
      skipDefaultCheckout()
      disableConcurrentBuilds()
      timeout(time: 1, unit: 'HOURS')
    }
    stages {
      stage('Checkout') {
        steps {
          checkout(scm)
        }
      }
      stage('Deploy') {
        steps {
          script {
            helm.deploy([namespace: config.namespace])
          }
        }
      }
    }
  }
}
