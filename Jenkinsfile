pipeline {
  agent any
  stages {
    stage('Build') {
      steps {

        sh 'build_and_test.sh application'
        sh 'build_and_test.sh payments'
        sh 'build_and_test.sh tokens'
        sh 'build_and_test.sh accounts'
        sh 'build_and_test.sh transactions'

      }
    }
    stage('Test'){
        steps{
            dir('./'){
                sh '''
                set -e
                mvn test
                '''
            }
        }
    }
    stage('Deploy'){
        steps{
            sh 'docker-compose -f docker-compose.yml up -d'
        }
    }

  }
}