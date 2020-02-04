pipeline {
  agent {
    docker {
      image 'gradle:6.0.1-jdk8'
      args '-v /tmp/gradle-caches:/home/gradle/.gradle/caches'
    }
  }

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
        sh 'make image'
        sh 'make publish'
      }
    }
  }
}
