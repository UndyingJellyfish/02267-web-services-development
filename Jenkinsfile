pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh '''#!/bin/bash
set -e
bash build_and_run.sh'''
      }
    }

  }
}