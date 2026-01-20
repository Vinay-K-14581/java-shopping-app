pipeline {
  agent any

  environment {
    APP_NAME      = "java-shopping-app"
    IMAGE_TAG     = "1.0"
    IMAGE_NAME    = "${APP_NAME}:${IMAGE_TAG}"

    MINIKUBE_HOME = "/var/lib/jenkins"
    KUBECONFIG    = "/var/lib/jenkins/.kube/config"
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
        sh '''
          minikube status || true
          minikube image load ${IMAGE_NAME}
        '''
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

    stage('App Access Info') {
      steps {
        sh '''
          echo "NodePort service details:"
          kubectl get svc java-shopping-service -o wide

          NODEPORT=$(kubectl get svc java-shopping-service -o jsonpath='{.spec.ports[0].nodePort}')
          echo "NodePort is: ${NODEPORT}"

          if minikube ip >/dev/null 2>&1; then
            MKIP=$(minikube ip)
            echo "Open in browser: http://${MKIP}:${NODEPORT}"
            echo "Quick curl test:"
            curl -I "http://${MKIP}:${NODEPORT}/" || true
          else
            echo "minikube ip not available from Jenkins."
            echo "Run on server shell: minikube ip"
            echo "Then open: http://<minikube-ip>:${NODEPORT}"
          fi
        '''
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

