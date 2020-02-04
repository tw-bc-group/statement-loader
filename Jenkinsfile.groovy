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
      steps {
        sh './gradlew build'
      }
    }

    stage('Dockerize') {
      steps {
        sh 'make image'
        sh 'make publish'
      }
    }
  }
}
