package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.util.List;
import java.util.Map;

import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;

public interface ServiceLocator {

    List<ServiceInfo> discoverService(String namespace, Map<String, String> labels);

}
