package org.sandbox.quarkus.k8s.svc.discovery.fabric8.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;

import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.services.ServiceLocator;

@Alternative
@Priority(1)
@ApplicationScoped
@Named("fabric8Locator")
public class MockFabric8ClientServiceLocator implements ServiceLocator {

    public static final String DEFAULT_NAME = "myappdiscovered";
    public static final String DEFAULT_URL = "http://myappdiscovered.com:8080";
    public static final ServiceInfo DEFAULT_SERVICE = new ServiceInfo(DEFAULT_NAME, DEFAULT_URL);

    private List<ServiceInfo> services = new ArrayList<>();

    /**
     * Default operation, respond with a list with 1 element: {@link ServiceInfo} named {@value #DEFAULT_NAME} with url {@value #DEFAULT_URL}  
     */
    @Override
    public List<ServiceInfo> discoverService(String namespace, Map<String, String> labels) {
        services.add(DEFAULT_SERVICE);
        return services;
    }

    /**
     * Adds a new service to your mock
     * @param serviceInfo
     */
    public void addService(final ServiceInfo serviceInfo) {
        if (serviceInfo != null) {
            this.services.add(serviceInfo);
        }
    }

    /**
     * Please call me on afterAll methods
     */
    public void cleanup() {
        this.services.clear();
    }

}
