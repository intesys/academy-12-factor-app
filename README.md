# 12 Factor Demo Deployment guide

## Java

Download the jar from the [releases](/releases) page.

Create a configuration file (`config/application.properties`) with the following content

```
SERVER_PORT=8080
CUSTOM_MESSAGE="Hello World From Configs"

# use the endpoint if you are using minio
AWS_ENDPOINT=http://localhost:9000
# use the region if you are using AWS S3
#AWS_REGION=us-east-1

AWS_ACCESS_KEY=myuser
AWS_SECRET_KEY=mypassword
```

Run the minio docker compose file

```bash
docker-compose -f src/main/docker/minio.yml up
```

Run the application

```bash
java -jar 12-factor-app.jar
```

## Docker Compose

Run the entire app using docker compose

```
docker-compose -f src/main/docker/12-factor.yml up
```

## Kubernetes

Run the kustomization file

```
kubectl apply -k src/main/kubernetes/kustomization.yml
```
