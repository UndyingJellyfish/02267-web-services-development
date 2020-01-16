pipeline {
  agent any
  stages {
    stage('Build') {
      steps {

        sh 'bash build_and_test.sh application'
        sh 'bash build_and_test.sh payments'
        sh 'bash build_and_test.sh tokens'
        sh 'bash build_and_test.sh accounts'
        sh 'bash build_and_test.sh transactions'

      }
    }
    /*stage('Test'){
        steps{
            dir('./system-tests'){
                sh '''
                set -e
                mvn test
                '''
            }
        }
    }*/
    stage('Deploy'){
        steps{
            sh 'docker-compose -f docker-compose.yml up -d'
        }
    }

  }
}