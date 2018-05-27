#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Docker

def call(Map config = [:]) {
  pipeline {
    agent { label 'java8' }
    stages {
      stage('Checkout') {
        steps {
          script {
            env.TIMESTAMP = sh(script: 'date "+%Y%m%d.%H%M%S"', returnStdout: true)
            env.GIT_COMMIT = checkout(scm).GIT_COMMIT
            env.VERSION = "${env.TIMESTAMP}.${env.GIT_COMMIT:0:8}"
          }
        }
      }
      stage('Build') {
        steps {
          sh('./mvnw clean package')
        }
      }
      stage('Docker') {
        steps {
          new Docker(this).build(repository: config.repository, artifact: config.artifact, version: env.VERSION);
        }
      }
    }
  }
}
