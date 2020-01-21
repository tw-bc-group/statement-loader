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
        sh 'cp -r ./mock ~/data'
        sh 'make image'
        sh 'make publish'
      }
    }
  }
}
