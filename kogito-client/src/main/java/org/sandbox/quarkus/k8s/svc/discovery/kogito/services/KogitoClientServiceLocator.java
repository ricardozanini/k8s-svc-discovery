package org.sandbox.quarkus.k8s.svc.discovery.kogito.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.fabric8.kubernetes.client.utils.Utils;
import okhttp3.Request;
import okhttp3.Response;
import org.kie.kogito.cloud.kubernetes.client.DefaultKogitoKubeClient;
import org.kie.kogito.cloud.kubernetes.client.KogitoKubeClient;
import org.kie.kogito.cloud.kubernetes.client.operations.MapWalker;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.services.BaseServiceLocator;

@ApplicationScoped
@Named("kogitoLocator")
public class KogitoClientServiceLocator extends BaseServiceLocator {

    private KogitoKubeClient client;

    public KogitoClientServiceLocator() {
        // map the fabric8 properties with ours, this way we can have a common usage interface among implementations
        System.setProperty("kubernetes.master", Utils.getSystemPropertyOrEnvVar("K8S_SVC_CLUSTER_URL", ""));
        System.setProperty("kubernetes.trust.certificates",
                           String.valueOf((Boolean.parseBoolean(Utils.getSystemPropertyOrEnvVar("K8S_SVC_CLUSTER_VALIDATE_CERT", "false")) ? false : true)));
        System.setProperty("kubernetes.auth.basic.username", Utils.getSystemPropertyOrEnvVar("K8S_SVC_CLUSTER_USER", ""));
        System.setProperty("kubernetes.auth.basic.password", Utils.getSystemPropertyOrEnvVar("K8S_SVC_CLUSTER_PWD", ""));
        System.setProperty("kubernetes.auth.token", Utils.getSystemPropertyOrEnvVar("K8S_SVC_CLUSTER_TOKEN", ""));
    }

    @Override
    public List<ServiceInfo> discoverService(String namespace, Map<String, String> labels) {
        final List<ServiceInfo> services = new ArrayList<>();
        final List<Map<String, Object>> servicesMap =
                client.services()
                      .listNamespaced(namespace, labels)
                      .asMapWalker()
                      .mapToListMap("items")
                      .asList();

        servicesMap.forEach(s -> {
            final ServiceInfo serviceInfo = new ServiceInfo();
            final StringBuilder sb = new StringBuilder();
            serviceInfo.setLabels(new MapWalker(s).mapToMap("metadata").mapToMap("labels").asMap());
            serviceInfo.setName((String) new MapWalker(s).mapToMap("metadata").asMap().get("name"));
            sb
              .append("http://")
              .append(new MapWalker(s).mapToMap("spec").asMap().get("clusterIP"))
              .append(":")
              .append(new MapWalker(s).mapToMap("spec")
                                      .mapToListMap("ports")
                                      .listToMap(0)
                                      .asMap().get("port"));
            serviceInfo.setUrl(sb.toString());
            services.add(serviceInfo);
        });

        return services;
    }

    @Override
    public void setupClient() throws Exception {
        this.client = new DefaultKogitoKubeClient();
    }

    // hook for unit tests
    void setClient(KogitoKubeClient client) {
        this.client = client;
    }

    @Override
    protected void verifyClientConnection() throws Exception {
        final Request request = new Request.Builder().url(this.getMasterURL()).build();
        try (final Response response = this.client.getConfig().getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Error trying to connect, response is: " + response);
            }
        }
    }

    @Override
    protected String getMasterURL() {
        return this.client.getConfig().getMasterUrl().toString();
    }

}
