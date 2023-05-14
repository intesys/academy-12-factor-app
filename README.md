# 12 Factor Demo Deployment guide

## Java fat jar

Download the jar from the [releases](/releases) page.

Create a configuration file (`config/application.properties`) with the following content

```
PORT=8080
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

## Heroku

```
heroku create

git push heroku master

heroku config:set CUSTOM_MESSAGE="Hello World From Heroku"
heroku config:set AWS_REGION="eu-south-1"
heroku config:set AWS_ACCESS_KEY=""
heroku config:set AWS_SECRET_KEY=""
heroku config:set AWS_BUCKET="twelve-factor-demo"
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
