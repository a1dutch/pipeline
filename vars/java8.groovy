#!/usr/bin/groovy

import uk.co.a1dutch.pipeline.Docker
import uk.co.a1dutch.pipeline.Maven
import uk.co.a1dutch.pipeline.Helm

def call(Map config = [:]) {

  Docker docker = new Docker(this)
  Maven maven = new Maven(this)
  Helm helm = new Helm(this)

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
            env.TIMESTAMP = new Date().format('yyyyMMdd.HHmmss')
            env.GIT_COMMIT = checkout(scm).GIT_COMMIT
            env.VERSION = "${env.TIMESTAMP}.${env.GIT_COMMIT.substring(0, 8)}"
          }
        }
      }
      stage('Build') {
        steps {
          script {
            maven.wrapper('clean package')
            stash(includes: 'target/*.jar', name: 'application')
          }
        }
      }
      stage('Docker') {
        agent { label 'docker' }
        steps {
          script {
            unstash('application')
            docker.build([
                repository: config.repository,
                artifact: config.artifact,
                version: env.VERSION
            ])
          }
        }
      }
      stage('Helm') {
        agent { label 'helm' }
        steps {
          script {
            helm.install([
              artifact: config.artifact,
              version: env.VERSION,
              description: config.description
            ])
          }
        }
      }
    }
  }
}
