pipeline {
  agent none
  stages {
    stage('Test') {
      agent {
        docker {
          image 'gradle:6.0.1-jdk8'
          args '-v /tmp/gradle-caches:/home/gradle/.gradle/caches'
        }
      }
      steps {
        sh './gradlew test'
      }
    }

    stage('Build') {
      agent {
        docker {
          image 'gradle:6.0.1-jdk8'
          args '-v /tmp/gradle-caches:/home/gradle/.gradle/caches'
        }
      }
      steps {
        sh './gradlew build'
      }
    }

    stage('Dockerize') {
      agent any
      steps {
        sh 'docker build -t tw-blockchain/reconciliation-loader .'
        sh 'docker image tag tw-blockchain/reconciliation-loader localhost:5000/reconciliation-loader'
        sh 'docker push localhost:5000/reconciliation-loader'
      }
    }
  }
}
