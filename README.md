# Kubernetes Service Discovery on Quarkus

Sandbox project for experimentations with native image on the Kubernetes Service API using the [Official Java Client](https://github.com/kubernetes-client/java) and the [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client). 

## How to play

Just run a couple commands in your terminal and make sure that you have access to a reachable Kubernetes cluster: 

```shell
# Clone and cd to the directory
$ git clone https://github.com/ricardozanini/k8s-svc-discovery.git
$ cd k8s-svc-discovery

# build
./mvnw package -Pnative -Dnative-image.docker-build=true

# run
./k8s-client-java/target/k8s-client-java-1.0-SNAPSHOT-runner -DK8S_SVC_CLUSTER_URL=<your cluster url> -DK8S_SVC_CLUSTER_TOKEN=$(oc whoami -t) -DK8S_SVC_CLUSTER_VALIDATE_CERT=false -Djava.library.path=$GRAALVM_HOME/jre/lib/amd64 -Djavax.net.ssl.trustStore=$GRAALVM_HOME/jre/lib/security/cacerts
```

Then go to [http://localhost:8080/services/yournamespacename](http://localhost:8080/services/yournamespacename) and you should see a list of Services deployed into the namespace. :) 

(TBD) To run with Fabric8, just change the command above to `./fabric8-client-java/target/fabric8-client-java-1.0-SNAPSHOT-runner`

### Notes

1. Make sure to set `K8S_SVC_CLUSTER_VALIDATE_CERT` to **`false`** if your cluster has a self signed certificate.
2. Have the [GraalVM installed](https://gist.github.com/ricardozanini/fa65e485251913e1467837b1c5a8ed28) and the `GRAALVM_HOME` environment variable set to it's home dir.

## TODOs

1. Create a Docker image to be deployed in a OpenShift cluster
2. Add the Fabric8 Kubernetes client resource
