pipeline {
     agent {
         kubernetes {
              label 'ci-template'
              yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: amazoncorretto
                    image: amazoncorretto:17
                    command:
                    - cat
                    tty: true
                '''
         }
     }
     options {
        skipStagesAfterUnstable()
     }

     stages{
        stage('Unit testing') {
           steps {
              container('amazoncorretto') {
                sh 'ls -l'
                sh './gradlew check'
              }
           }
        }
         stage('Build') {
             steps {
                 container('amazoncorretto') {
                     script {
                        sh './gradlew build -x test'
                     }
                 }
             }
         }
         stage('Dockerize') {
             steps {
             echo 'docker'
                 }
             }
         }

         stage('Deploy to Kubernetes') {
             steps {
                 container('kubectl') {
                     script {
                         sh 'kubectl apply -f k8s/deployment.yaml'
                     }
                 }
             }
         }
     }

    post {
             always {
                 echo 'This will always run'
                 archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
                 junit 'build/reports/**/*.xml'

             }
             success {
                 echo 'This will run only if successful'
             }
             failure {
                 echo 'This will run only if failed'
             }
             unstable {
                 echo 'This will run only if the run was marked as unstable'
             }
             changed {
                 echo 'This will run only if the state of the Pipeline has changed'
                 echo 'For example, if the Pipeline was previously failing but is now successful'
             }
    }
 }