stage('Development') {
    node {
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
        dir('target') {stash name: 'jar', includes: 'app.jar'}
    }
}

stage('QA') {
    parallel([1,2].collectEntries {duration -> ["tests-$duration", {runTests(duration)}]})
//    echo "Test results: ${testResult(currentBuild)}"
}

milestone 1
stage('Staging') {
    lock(resource: 'staging-server', inversePrecedence: true) {
        milestone 2
        node {
//            sh 'docker run --name staging -p 8080:8080 bbvss/springboot-k8s'
        }
        input message: "Does http://localhost:8080 look good?"
    }
    try {
        checkpoint('Before production')
    } catch (NoSuchMethodError ignored) {
        echo 'Checkpoint feature available in CloudBees Jenkins Enterprise.'
    }
}

milestone 3
stage ('Production') {
    lock(resource: 'production-server', inversePrecedence: true) {
        node {
            echo 'Production server looks to be alive'
//            sh 'docker --name production run -p 8080:8080 bbvss/springboot-k8s'
            echo "Deployed to production http://localhost:8080"
        }
    }
}

def runTests( duration) {
    node {
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

stage('Build Docker Image'){
    try{
        DOCKER_IMAGE_NAME="bbvss/springboot-k8s"
        sh 'mvn clean install dockerfile:build'
    } catch(e) {
//        notify("Something failed building Docker Image")
        throw e
    }
}

stage('Push image to container registry'){
    try{
//        sh('docker login ${CONTAINER_REGISTRY_SERVER} -u ${CONTAINER_REGISTRY_USERNAME} -p ${CONTAINER_REGISTRY_PASSWORD}')
        sh('docker login https://hub.docker.com -u bbvss -p GtrtGuNrV8456WJg')
        sh('docker push bbvss/springboot-k8s')
//        sh('docker push ' + DOCKER_IMAGE_NAME)
    } catch(e) {
//        notify("Something failed pushing Docker Image")
        throw e
    }
}


// here the error occurs
stage('Kubernetes Setup'){
    try{
        sh("kubectl create -f app-deployment.yml -v=8")
    } catch(e) {
//        notify("Something failed Kubernetes Setup")
        throw e
    }
}

//notify("Process finish")


//def notify(String message) {
//    slackSend (color: '#FFFF00', message: "${message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
//}
