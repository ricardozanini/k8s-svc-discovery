package org.sandbox.quarkus.k8s.svc.discovery.fabric8.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.services.BaseServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Named("fabric8Locator")
public class Fabric8ClientServiceLocator extends BaseServiceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Fabric8ClientServiceLocator.class);

    @ConfigProperty(name = "kubernetes.master", defaultValue = "")
    String masterUrl;
    @ConfigProperty(name = "kubernetes.auth.basic.username", defaultValue = "")
    String kubernetesUser;
    @ConfigProperty(name = "kubernetes.auth.basic.password", defaultValue = "")
    String kubernetesPwd;
    @ConfigProperty(name = "kubernetes.auth.token", defaultValue = "")
    String kubernetesToken;
    @ConfigProperty(name = "kubernetes.trust.certificates", defaultValue = "")
    Boolean trustCerts;

    private KubernetesClient client;

    @Override
    public void setupClient() throws Exception {
        if (hasUserCredentials()) {
            LOGGER.info("Connecting via username {} and password to cluster {}. Trust certs? {}", this.kubernetesUser, this.masterUrl, this.trustCerts);
            final Config config =
                    new ConfigBuilder()
                                       .withMasterUrl(masterUrl)
                                       .withUsername(kubernetesUser)
                                       .withPassword(kubernetesPwd)
                                       .withDisableHostnameVerification(!trustCerts)
                                       .withTrustCerts(!trustCerts)
                                       .build();

            this.setClient(new DefaultKubernetesClient(config));
        } else if (hasToken()) {
            LOGGER.info("Connecting via token {} to cluster {}. Trust certs? {}", this.kubernetesToken, this.masterUrl, this.trustCerts);
            final Config config = new ConfigBuilder()
                                                     .withMasterUrl(masterUrl)
                                                     .withOauthToken(kubernetesToken)
                                                     .withDisableHostnameVerification(!trustCerts)
                                                     .withTrustCerts(!trustCerts)
                                                     .build();
            this.setClient(new DefaultKubernetesClient(config));
        } else {
            LOGGER.info("Trying to connect to the cluster using environment (kube config, env variables, etc.)");
            this.setClient(new DefaultKubernetesClient());
        }
    }

    protected boolean hasToken() {
        return this.masterUrl != null && !this.masterUrl.isEmpty() && this.kubernetesToken != null && !this.kubernetesToken.isEmpty();
    }

    protected boolean hasUserCredentials() {
        return this.masterUrl != null && !this.masterUrl.isEmpty() && this.kubernetesUser != null && !this.kubernetesUser.isEmpty();
    }

    public KubernetesClient getClient() {
        return client;
    }

    public void setClient(final KubernetesClient client) {
        this.client = client;
    }

    @Override
    public List<ServiceInfo> discoverService(String namespace, Map<String, String> labels) {
        final List<ServiceInfo> serviceInfo = new ArrayList<>();
        ServiceList services = client.services().inNamespace(namespace).withLabels(labels).list();
        serviceInfo.addAll(services.getItems()
                                   .stream()
                                   .map(item -> {
                                       final ServiceInfo service = new ServiceInfo();
                                       service.setName(item.getMetadata().getName());
                                       service.setLabels(item.getMetadata().getLabels());
                                       service.setUrl(String.format("http://%s:%d",
                                                                    item.getSpec().getClusterIP(),
                                                                    item.getSpec().getPorts().get(0).getPort()));
                                       return service;
                                   }).collect(Collectors.toList()));
        return serviceInfo;
    }

    @Override
    protected String getMasterURL() {
        if (this.client != null) {
            return this.client.getMasterUrl().toString();
        }
        return "";
    }

    @Override
    protected void verifyClientConnection() {
        if (client != null) {
            //simple query to check the client connection
            NamespaceList namespaces = client.namespaces().list();
            LOGGER.debug("Succesfully queried namespaces from the cluster to test the connection {}", namespaces.getItems());
        }
    }

}
