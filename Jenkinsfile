pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean'

        // Library should build first, there's no Dockerfile for it, since it is shared
        sh 'bash build_and_test.sh library'

        sh 'bash build_and_test.sh payments'
        sh 'bash build_and_test.sh tokens'
        sh 'bash build_and_test.sh accounts'
        sh 'bash build_and_test.sh transactions'
      }
    }
    stage('Test'){
        steps{
            sh 'bash systemTests.sh application'
        }
    }
    stage('Deploy'){
        steps{
            sh 'docker-compose -f docker-compose.yml up -d'
        }
    }

  }
}