@Library('pipeline@master') _

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
        sh('./mvnw clean test')
      }
    }
  }
}
