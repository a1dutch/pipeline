@Library('pipeline@master') _

import uk.co.a1dutch.pipeline.Maven

Maven maven = new Maven(this)

pipeline {
  agent { label 'java8' }
  options {
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
    stage('Build') {
      steps {
        maven.wrapper('clean test')
      }
    }
  }
}
