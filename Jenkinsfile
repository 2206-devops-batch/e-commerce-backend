pipeline {
  agent {
    kubernetes {
      yaml '''
      apiVersion: v1
      kind: Pod
      spec:
        containers:
        - name: maven
          image: maven:alpine
          command:
          - cat
          tty: true
        - name: kubectl
          image: gcr.io/cloud-builders/kubectl
          command:
          - cat
          tty: true
        - name: trivy
          image: aquasec/trivy:0.21.1
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
        - name: docker
          image: docker:latest
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
        volumes:
        - name: docker-sock
          hostPath:
            path: /var/run/docker.sock
      '''
    }
  }
  stages {
    stage('Trivy Scan: git') {
      steps {
        container('trivy') {
          sh 'trivy repo https://github.com/2206-devops-batch/e-commerce-backend.git'
        }
      }
    }
    stage('Build & Test') {
      steps {
        container('maven') {
          sh 'mvn install'
          sh 'mvn test'
        }
      }
    }
    stage('SonarCloud Analysis') {
      steps {
        script {
          nodejs(nodeJSInstallationName: 'nodejs') {
            def scannerHome = tool 'sonar scanner'
            withSonarQubeEnv('SonarCloud') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
          }
        }
      }
    }
    stage('Quality gate') {
      steps {
        script {
          timeout(time: 2, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
          }
        }
      }
    }
    stage('Docker Build & Push') {
      steps {
        container('docker') {
          withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
            sh 'docker login -u ${username} -p ${password}'

            // Version -- Backend Docker Images -- Blue & Green -- Based on latest pipeline build
            sh 'docker build -t othom/e-commerce-backend-blue:$BUILD_NUMBER .'
            sh 'docker build -t othom/e-commerce-backend-green:$BUILD_NUMBER .'

            // Version -- Backend Docker Images -- Blue & Green -- Latest Build
            sh 'docker build -t othom/e-commerce-backend-blue:latest .'
            sh 'docker build -t othom/e-commerce-backend-green:latest .'

            // Push All Recent Build
            sh 'docker push othom/e-commerce-backend-blue:$BUILD_NUMBER'
            sh 'docker push othom/e-commerce-backend-blue:latest'

            sh 'docker push othom/e-commerce-backend-green:$BUILD_NUMBER'
            sh 'docker push othom/e-commerce-backend-green:latest'

          // Note these should really be broken up into separete branches pipelines but for demo sake we are running both off latest changes
          // Use Blue  as Stable  / Production Env. e.g. $BUILD_NUMBER 15 works but backend cors issue with backend
          // Use Green as Staging / Dev Environment e.g. $BUILD_NUMBER 55 fixes backend cors issue with backend but has XYZ error
          }
        }
      }
    }
    stage('Trivy Scan: image') {
      steps {
        container('trivy') {
          sh "trivy image othom/e-commerce-backend-blue:$BUILD_NUMBER"
          sh "trivy image othom/e-commerce-backend-green:$BUILD_NUMBER"
          sh 'trivy image othom/e-commerce-backend-blue:latest'
          sh 'trivy image othom/e-commerce-backend-green:latest'
        }
      }
    }
    stage('Trivy Scan: Misconfigurations') {
      steps {
        container('trivy') {
          sh 'trivy config ./kubectl-yaml-files'
        }
      }
    }
    stage('Deploy Image To AWS EKS Cluster') {
      steps {
        container('kubectl') {
          //sh 'kubectl get pods --all-namespaces'

          // Start Service To Host Both Blue & Green Builds
          sh 'kubectl apply -f backend-service.yaml'            // Swap Lines 17 & 18 for Production To Display Green   ---   Lines 36 & 37 for Staging i.e. app: orange
          // Deploy Blue (Stable) Build & Green (Dev) Build
          sh 'kubectl apply -f backend-deployment-blue.yaml'
          sh 'kubectl apply -f backend-deployment-green.yaml'
        }
      }
    }
  }
  post {
    always {
      container('docker') {
        sh 'docker logout'
      }
    }
  }
}
