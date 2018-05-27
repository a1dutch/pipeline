#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Docker
import uk.co.a1dutch.pipeline.Maven

def call(Map config = [:]) {

  Docker docker = new Docker(this)
  Maven maven = new Maven(this)

  pipeline {
    agent { label 'java8' }
    options {
      disableResume()
      skipDefaultCheckout()
      disableConcurrentBuilds()
      timeout(time: 1, unit: 'HOURS')
    }
    stages {
      stage('Checkout') {
        steps {
          script {
            env.TIMESTAMP = sh(script: 'date "+%Y%m%d.%H%M%S"', returnStdout: true)
            env.GIT_COMMIT = checkout(scm).GIT_COMMIT
            env.VERSION = "${env.TIMESTAMP}.${env.GIT_COMMIT.substring(0, 8)}"
          }
        }
      }
      stage('Build') {
        steps {
          script {
            maven.wrapper('clean package')
          }
        }
      }
      stage('Docker') {
        agent { label 'docker' }
        steps {
          script {
            docker.build([repository: config.repository, artifact: config.artifact, version: env.VERSION]);
          }
        }
      }
    }
  }
}
