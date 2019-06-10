package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.util.List;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class KubernetesOfficialClientServiceLocatorTest {

    //TODO: mock k8s server
    
    @Inject
    public KubernetesOfficialClientServiceLocator serviceLocator;

    @Test
    void whenServiceIsDiscoveredOnAnUnknowEnv() {
        final List<ServiceInfo> services = serviceLocator.discoverService("", null);
        assertNotNull(services);
        assertFalse(services.isEmpty(), "Service list shouldn't be empty");
    }

}
