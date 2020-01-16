pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        dir('./application'){
          sh '''
          set -e

          mvn clean package -pl ../.
          docker-compose -f ../docker-compose.yml build dtupay

          '''
        }
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