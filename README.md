# Kubernetes Service Discovery on Quarkus

Sandbox project for experimentations with native image on the Kubernetes Service API using the [Official Java Client](https://github.com/kubernetes-client/java) and the [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client). 

## How to play

Just run a couple commands in your terminal and make sure that you have access to a reachable Kubernetes cluster: 

```shell
# Clone and cd to the directory
$ git clone https://github.com/ricardozanini/k8s-svc-discovery.git
$ cd k8s-svc-discovery

# build
./mvnw clean package -Pnative
```

Having the binaries compiled, choose your destiny.

**Kubernetes Official Client**

```
# run
./k8s-client-java/target/k8s-client-java-1.0-SNAPSHOT-runner -DK8S_SVC_CLUSTER_URL=<your cluster url> -DK8S_SVC_CLUSTER_TOKEN=$(oc whoami -t) -DK8S_SVC_CLUSTER_VALIDATE_CERT=false -Djava.library.path=$GRAALVM_HOME/jre/lib/amd64 -Djavax.net.ssl.trustStore=$GRAALVM_HOME/jre/lib/security/cacerts
```

**Fabric8 Client**

```
# run
./fabric8-client/target/fabric8-client-1.0-SNAPSHOT-runner -DK8S_SVC_CLUSTER_URL=<your cluster url> -DK8S_SVC_CLUSTER_TOKEN=$(oc whoami -t) -DK8S_SVC_CLUSTER_VALIDATE_CERT=false -Djava.library.path=$GRAALVM_HOME/jre/lib/amd64 -Djavax.net.ssl.trustStore=$GRAALVM_HOME/jre/lib/security/cacerts
```

**Kogito Client**

```
# run
./kogito-client/target/kogito-client-1.0-SNAPSHOT-runner -DK8S_SVC_CLUSTER_URL=<your cluster url> -DK8S_SVC_CLUSTER_TOKEN=$(oc whoami -t) -DK8S_SVC_CLUSTER_VALIDATE_CERT=false -Djava.library.path=$GRAALVM_HOME/jre/lib/amd64
```

Then go to [http://localhost:8080/services/yournamespacename](http://localhost:8080/services/yournamespacename) and you should see a list of Services deployed into the namespace. :) 


### Notes

1. Make sure to set `K8S_SVC_CLUSTER_VALIDATE_CERT` to **`false`** if your cluster has a self signed certificate.
2. Have the [GraalVM installed](https://gist.github.com/ricardozanini/fa65e485251913e1467837b1c5a8ed28) and the `GRAALVM_HOME` environment variable set to it's home dir.
3. When experimenting with `k8s-client-java` you might face a similar exception like the following: `java.io.IOException: Resource not found: "org/joda/time/tz/data/America/Sao_Paulo" ClassLoader: java.lang.ClassLoader@24afc28`. It's caused by the Joda time library that the Swagger is dependent. Just ignore it for now.

## TODOs

- Create a Docker image to be deployed in a OpenShift cluster
