package org.sandbox.quarkus.k8s.svc.discovery.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.services.ServiceLocator;

public abstract class BaseServicesResource {

    @Inject
    ServiceLocator officialClient;

    public List<ServiceInfo> queryServices(String namespace, String labelSelector) {
        return officialClient.discoverService(namespace, this.transformLabelsStringToMap(labelSelector));
    }

    protected Map<String, String> transformLabelsStringToMap(final String labels) {
        final Map<String, String> labelsMap = new HashMap<>();
        if (labels != null && !labels.isEmpty()) {
            labelsMap.putAll(Stream.of(labels.split(","))
                                   .collect(
                                            Collectors.toMap(l -> l.toString().split("=")[0],
                                                             l -> l.toString().split("=")[1])));
        }
        return labelsMap;
    }
}
