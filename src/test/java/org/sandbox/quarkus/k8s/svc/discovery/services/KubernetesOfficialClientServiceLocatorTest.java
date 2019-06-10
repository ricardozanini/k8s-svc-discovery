package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class KubernetesOfficialClientServiceLocatorTest {

    //TODO: mock k8s server

    @Inject
    public KubernetesOfficialClientServiceLocator serviceLocator;

    @Test
    void whenServiceIsDiscoveredOnAnUnknowEnv() {
        final List<ServiceInfo> services = serviceLocator.discoverService("bsig-cloud", Collections.singletonMap("application", "eap-app"));
        assertNotNull(services);
        assertFalse(services.isEmpty(), "Service list shouldn't be empty");
        assertThat(services.get(0).getUrl(), containsString("8080"));
    }

}
