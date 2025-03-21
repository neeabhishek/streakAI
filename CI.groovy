pipeline {
    agent any
    parameters{
        string(name: 'GIT_URL', defaultValue: '', description: 'GIT URL')
        string(name: 'DOCKER_USER', defaultValue: '', description: 'Docker User')
        password(name: 'DOCKER_PASS', defaultValue: '', description: 'Docker Password')
    }

    stages{
        stage('Cloning the Repository') {
            steps {
                echo "** Cloning the source-code"
                git branch: 'main', credentialsId: 'GIT-CRED', url: params.GIT_URL 
            }
        }
        stage('Building the Docker Image Locally') {
            steps {
                script {
                    def WORKSPACE = env.WORKSPACE
                    def DOCKER_USER = params.DOCKER_USER
                    def DOCKER_PASS = params.DOCKER_PASS
                    echo "** Building the Docker Image"
                    sh """
                        cd ${WORKSPACE}/ && docker build -t neeabhishek/streak_ai:${env.BUILD_NUMBER} . && \
                        sleep 4 && \
                        docker login -u ${DOCKER_USER} -p ${DOCKER_PASS} && \
                        docker push neeabhishek/streak_ai:${env.BUILD_NUMBER}
                    """    
                }
            }
        }
        stage('Running the Container Locally') {
            steps{
                script {
                    def WORKSPACE = env.WORKSPACE
                    echo "** Starting the container and executing the test case **"
                    sh """
                         cd ${WORKSPACE}/ && \
                         chmod -R 755 test_api.sh && \
                         ./test_api.sh
                    """
                }
            }
        }
        stage('Cleaning Up') {
            steps {
                echo "** Cleaning up the workspace **"
                cleanWs()
            }
        }
    }
}