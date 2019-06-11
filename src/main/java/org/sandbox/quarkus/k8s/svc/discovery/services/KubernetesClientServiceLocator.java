package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.squareup.okhttp.Request;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Config;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Named("kubernetesLocator")
public class KubernetesClientServiceLocator implements ServiceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesClientServiceLocator.class);

    private ApiClient apiClient;

    @PostConstruct
    public void setupClient() throws IOException {
        if (hasToken()) {
            LOGGER.info("Trying to connect to k8s cluster using system properties: {}, {}, {}", ServiceLocatorProperties.K8S_SVC_CLUSTER_URL,
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_TOKEN,
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_VALIDATE_CERT);
            this.apiClient = Config.fromToken(System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_URL),
                                              System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_TOKEN),
                                              Boolean.valueOf(System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_VALIDATE_CERT, "false")));
        } else if (hasCredentials()) {
            LOGGER.info("Trying to connect to k8s cluster using system properties: {}, {}, {}, {}",
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_URL,
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_USER,
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_PWD,
                        ServiceLocatorProperties.K8S_SVC_CLUSTER_VALIDATE_CERT);
            this.apiClient = Config.fromUserPassword(
                                                     System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_URL),
                                                     System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_USER),
                                                     System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_PWD),
                                                     Boolean.valueOf(System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_VALIDATE_CERT, "false")));
        } else {
            this.apiClient = Config.defaultClient();
        }

        verifyClientConnection();
        Configuration.setDefaultApiClient(apiClient);
    }

    @Override
    public List<ServiceInfo> discoverService(String namespace, Map<String, String> labels) {
        final CoreV1Api api = new CoreV1Api();
        final List<ServiceInfo> serviceInfo = new ArrayList<>();
        try {

            final V1ServiceList serviceList = api.listNamespacedService(namespace, true, null, null, null, this.buildLabelSelectorParam(labels), null, null, null, false);
            if (serviceList != null) {
                // TODO: safe gets
                serviceInfo.addAll(serviceList
                                              .getItems()
                                              .stream()
                                              .map(item -> {
                                                  final ServiceInfo svc = new ServiceInfo();
                                                  svc.setName(item.getMetadata().getName());
                                                  svc.setUrl(String.format("http://%s:%d",
                                                                           item.getSpec().getClusterIP(),
                                                                           item.getSpec().getPorts().get(0).getPort()));
                                                  svc.setLabels(item.getMetadata().getLabels());
                                                  return svc;
                                              }).collect(Collectors.toList()));
            }
        } catch (ApiException e) {
            LOGGER.error("Impossible to query Kubernetes cluster", e);
            throw new RuntimeException(e);
        }
        return serviceInfo;
    }

    protected String buildLabelSelectorParam(final Map<String, String> labels) {
        if (labels != null) {
            return labels
                         .entrySet()
                         .stream()
                         .map(label -> {
                             return String.format("%s=%s", label.getKey(), label.getValue());
                         })
                         .collect(Collectors.joining(","));
        }
        return "";
    }

    protected boolean hasCredentials() {
        final String url = System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_URL);
        return url != null && !url.isEmpty();
    }

    protected boolean hasToken() {
        final String token = System.getProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_TOKEN);
        return token != null && !token.isEmpty();
    }

    protected void verifyClientConnection() {
        final Request request = new Request.Builder().get().url(this.apiClient.getBasePath()).build();
        try {
            this.apiClient.getHttpClient().newCall(request).execute();
            LOGGER.info("Successfully connected to remote kubernetes cluster at {}", this.apiClient.getBasePath());
        } catch (IOException e) {
            LOGGER.debug("Failed to connect to Kubernetes cluster", e);
            LOGGER.warn("Client is invalid, tried to connect to {}, got \"{}\"", this.apiClient.getBasePath(), e.getMessage());
        }
    }

}
