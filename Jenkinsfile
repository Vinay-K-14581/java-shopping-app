pipeline {
  agent any

  environment {
    APP_NAME   = "java-shopping-app"
    IMAGE_TAG  = "1.0"
    IMAGE_NAME = "${APP_NAME}:${IMAGE_TAG}"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Maven Build') {
      steps {
        sh 'mvn -v'
        sh 'mvn clean package -DskipTests'
        sh 'ls -lh target/*.jar'
      }
    }

    stage('Docker Build') {
      steps {
        sh 'docker --version'
        sh "docker build -t ${IMAGE_NAME} ."
        sh "docker images | grep ${APP_NAME} || true"
      }
    }

    stage('Load Image into Minikube') {
      steps {
        sh 'minikube status'
        sh "minikube image load ${IMAGE_NAME}"
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        sh 'kubectl version --client'
        sh 'kubectl apply -f k8s-deployment.yaml'
        sh 'kubectl rollout status deployment/java-shopping-app --timeout=180s'
        sh 'kubectl get pods -o wide'
        sh 'kubectl get svc'
      }
    }

    stage('App URL (Minikube)') {
      steps {
        sh 'echo "Service URL:"'
        sh 'minikube service java-shopping-service --url'
      }
    }
  }

  post {
    always {
      sh 'echo "Pipeline finished. Current K8s status:"'
      sh 'kubectl get pods -o wide || true'
      sh 'kubectl get svc || true'
    }
  }
}

       
