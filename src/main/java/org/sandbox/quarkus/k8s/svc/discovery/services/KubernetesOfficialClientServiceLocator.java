package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

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
public class KubernetesOfficialClientServiceLocator implements ServiceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesOfficialClientServiceLocator.class);

    private ApiClient apiClient;

    @PostConstruct
    public void setupClient() throws IOException {
        this.apiClient = Config.defaultClient();
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

}
