pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('Dockerize') {
      steps {
        sh 'docker build -t tw-blockchain/reconciliation-loader .'
        sh 'docker image tag tw-blockchain/reconciliation-loader localhost:5000/reconciliation-loader'
        sh 'docker push localhost:5000/reconciliation-loader'
      }
    }
  }
}
