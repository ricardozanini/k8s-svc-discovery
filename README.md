# Kubernetes Service Discovery on Quarkus

## How to play

```
./mvnw package -Pnative -Dnative-image.docker-build=true

./target/k8s-svc-discovery-1.0-SNAPSHOT-runner -DK8S_SVC_CLUSTER_URL=https://192.168.99.100:8443/ -DK8S_SVC_CLUSTER_TOKEN=$(oc whoami -t) -DK8S_SVC_CLUSTER_VALIDATE_CERT=false -Djava.library.path=$GRAALVM_HOME/jre/lib/amd64 -Djavax.net.ssl.trustStore=$GRAALVM_HOME/jre/lib/security/cacerts
```

```
docker build -f src/main/docker/Dockerfile.native -t k8s-svc-discovery .
```

