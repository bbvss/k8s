#!/usr/bin/env groovy
pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "bbvss/springboot-k8s"
    }
    stages {
        stage('Development') {
            steps {
                checkout scm
                withMaven(
                        // Maven installation declared in the Jenkins "Global Tool Configuration"
                        maven: 'M3',
                        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                        // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                        mavenLocalRepo: '.repository') {

                    // Run the maven build
                    sh "mvn clean install"

                }
                dir('target') { stash name: 'jar', includes: 'app.jar' }
            }
        }

        stage('QA') {
            steps {
                echo "SonarQube, Checkstyle, ...: "
                echo "Test results for QA: "
            }
        }

        stage('Development') {
            steps {
                echo "Deploy development"
            }
        }

        stage('Integration / Performance tests') {
            steps {
                echo "Test results ..."
            }
        }

        stage('Staging') {
            steps {
                lock(resource: 'staging-server', inversePrecedence: true) {
                    //            sh 'docker --name staging run -p 8080:8080 bbvss/springboot-k8s'
                }
                input message: "Deploy staging...?"
            }
        }

        stage('Build, tag and push staging Docker Image') {
            steps {
                echo 'Build Docker Image...'
            }
        }

        stage('Staging Kubernetes Setup') {
            steps {
                withCredentials([kubeconfigContent(credentialsId: 'acs-ssh-folder', variable: 'KUBECONFIG_CONTENT')]) {
                    sh '''echo "$KUBECONFIG_CONTENT" > kubeconfig && cat kubeconfig && rm kubeconfig'''
                }
                sh("kubectl create -f app-deployment.yml -v=8")
            }
        }

        stage('Production') {
            steps {
                lock(resource: 'staging-server', inversePrecedence: true) {
                    //            sh 'docker --name production run -p 8080:8080 bbvss/springboot-k8s'
                }
                input message: "Production deployed"
            }
        }

        stage('Build, tag and push production Docker Image') {
            steps {
                echo 'Build Docker Image...'
            }
        }

        stage('Production Kubernetes Setup') {
            steps {
                withCredentials([kubeconfigContent(credentialsId: 'acs-ssh-folder', variable: 'KUBECONFIG_CONTENT')]) {
                    sh '''echo "$KUBECONFIG_CONTENT" > kubeconfig && cat kubeconfig && rm kubeconfig'''
                }
                sh("kubectl create -f app-deployment.yml -v=8")
            }
        }

    }

}