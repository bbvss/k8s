#!/usr/bin/env groovy
//    agent { node { label 'docker' } }

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
//    parallel([1,2].collectEntries {duration -> ["tests-$duration", {runTests(duration)}]})
        steps {
            echo "Test results for QA: "
        }//${testResult(currentBuild)}"}
    }

//        milestone 1
    stage('Staging') {
        steps {
            lock(resource: 'staging-server', inversePrecedence: true) {
                milestone 2

//            sh 'docker run --name staging -p 8080:8080 bbvss/springboot-k8s'
            }
            input message: "Does http://localhost:8080 look good?"

            checkpoint('Before production')
        }
    }

//        milestone 3
    stage('Production') {
        steps {
            lock(resource: 'production-server', inversePrecedence: true) {

                echo 'Production server looks to be alive'
//            sh 'docker --name production run -p 8080:8080 bbvss/springboot-k8s'
                echo "Deployed to production http://localhost:8080"
            }
        }
    }

    stage('Build Docker Image') {
        steps {
            echo 'Build Docker Image...'
            withMaven(
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: 'M3',
                    // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                    // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                    mavenLocalRepo: '.repository') {

                // Run the maven build
//                sh 'mvn dockerfile:build'
            }
        }
    }

    stage('Push image to container registry') {
        steps {
//        agent { dockerfile true }
//        try {
//            docker.withRegistry('https://hub.docker.com', 'docker-credentials') {
//
//                def customImage = docker.build("bbvss/springboot-k8s:${env.BUILD_ID}")
//
//                /* Push the container to the custom Registry */
//                customImage.push()
            echo 'Docker push...'
//            }
//        sh('docker login ${CONTAINER_REGISTRY_SERVER} -u ${CONTAINER_REGISTRY_USERNAME} -p ${CONTAINER_REGISTRY_PASSWORD}')
//        sh('docker login https://hub.docker.com -u bbvss -p GtrtGuNrV8456WJg')
            script {
                docker login 'https://hub.docker.com -u bbvss -p GtrtGuNrV8456WJg'
                docker push 'bbvss/springboot-k8s'
            }
//        sh('docker push bbvss/springboot-k8s')
//        sh('docker push ' + DOCKER_IMAGE_NAME)
//        } catch (e) {
//        notify("Something failed pushing Docker Image")
//            throw e
//        }
        }
    }

// here the error occurs
    stage('Kubernetes Setup') {
        steps {
            withCredentials([kubeconfigContent(credentialsId: 'acs-ssh-folder', variable: 'KUBECONFIG_CONTENT')]) {
                sh '''echo "$KUBECONFIG_CONTENT" > kubeconfig && cat kubeconfig && rm kubeconfig'''
            }

            sh("kubectl create -f app-deployment.yml -v=8")
//        notify("Something failed Kubernetes Setup")
        }
    }

//notify("Process finish")


//def notify(String message) {
//    slackSend (color: '#FFFF00', message: "${message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
//}

}


def runTests(duration) {
    steps {
        checkout scm
//        mvn "-o -f sometests test -Durl=${jettyUrl}${id}/ -Dduration=${duration}"
        withMaven(
                // Maven installation declared in the Jenkins "Global Tool Configuration"
                maven: 'M3',
                // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                mavenLocalRepo: '.repository') {

            // Run the maven build
            sh "mvn test"

        }
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}