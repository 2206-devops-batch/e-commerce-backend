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
    stage('Build-Jar-file') {
      steps {
        container('maven') {
          sh 'mvn --version'
        }
      }
    }
    stage('Build-Docker-Image') {
      steps {
        container('docker') {
          // sh 'docker build -t mshmsudd/testing-image:latest .'
          sh 'docker version'
        }
      }
    }
    stage('Login-Into-Docker') {
      steps {
        container('docker') {
          // sh 'docker login -u <docker_username> -p <docker_password>'
          sh 'docker version'
      }
    }
    }
     stage('Push-Images-Docker-to-DockerHub') {
      steps {
        container('docker') {
          //sh 'docker push mshmsudd/testing-image:latest'
          sh 'docker version'
      }
    }
     }
  }
    post {
      always {
        container('docker') {
          //sh 'docker logout'
          sh 'docker version'
      }
      }
    }
  
    
}
