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
          sh "trivy repo https://github.com/2206-devops-batch/e-commerce-backend-blue"
        }
      }
    }  
    stage('Build') {
      steps {
        container('maven') {
          sh 'mvn install'
        }
      }
    }
    stage('Test') {
      steps {
        container('maven') {
          sh 'mvn test'
        }
      }
    }
    /*stage('SonarCloud analysis') {
        steps {       
            script {
                nodejs(nodeJSInstallationName: 'nodejs'){ 
                  def scannerHome = tool 'sonar scanner';             
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
                timeout(time: 5, unit: 'MINUTES') {
                  waitForQualityGate abortPipeline: true
                }
            }
        }
    }*/ 
    stage('Build Image') {
       steps {
         container('docker') {
           withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
             sh 'docker build -t othom/e-commerce-backend-blue:$BUILD_NUMBER .'
             sh 'docker build -t othom/e-commerce-backend-green:$BUILD_NUMBER .'
          }
        }
      }
    }
    stage('Trivy Scan: image') {
      steps {
        container('trivy') {
          sh "trivy image othom/e-commerce-backend-blue:$BUILD_NUMBER"
          sh "trivy image othom/e-commerce-backend-green:$BUILD_NUMBER"
        }
      }
    } 
    stage('Deliver') {
       steps {
         container('docker') {
           withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'password', usernameVariable: 'username')]) {
             sh 'docker login -u ${username} -p ${password}'
             sh 'docker push othom/e-commerce-backend-blue:$BUILD_NUMBER'
             sh 'docker push othom/e-commerce-backend-green:$BUILD_NUMBER'
          }
        }
      }
    }
    stage('Trivy Scan: Misconfigurations') {
      steps {
        container('trivy') {
          sh "trivy config ./kubectl-yaml-files"
        }
      }
    }   
    stage('Deploy') {
      steps {
         container('kubectl') {
               //sh 'kubectl get pods --all-namespaces'
             //sh 'kubectl config set-context --current --namespace=default'
             sh 'kubectl apply -f backendbluedeployment.yaml'
             sh 'kubectl apply -f backendgreendeployment.yaml'
             sh 'kubectl apply -f bluegreenbackendservice.yaml'
           
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
