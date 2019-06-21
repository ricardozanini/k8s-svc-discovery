# /bin/bash

cd ../
./mvnw clean package -pl kogito-client --am -Pnative
cd kogito-client
mkdir -p target/javalib/
cp $GRAALVM_HOME/jre/lib/amd64/libsunec.so target/javalib/
cp $GRAALVM_HOME/jre/lib/security/cacerts target/
docker build -f src/main/docker/Dockerfile.native -t quay.io/ricardozanini/kogito-svc-discovery  .
docker push quay.io/ricardozanini/kogito-svc-discovery:latest
