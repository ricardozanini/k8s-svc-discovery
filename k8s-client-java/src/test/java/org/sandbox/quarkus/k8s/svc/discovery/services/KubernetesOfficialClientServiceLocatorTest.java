package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.mock.server.KubernetesServerMockExtension;
import org.sandbox.quarkus.k8s.svc.discovery.official.services.KubernetesClientServiceLocator;
import org.sandbox.quarkus.k8s.svc.discovery.tests.TestSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(KubernetesServerMockExtension.class)
@QuarkusTest
public class KubernetesOfficialClientServiceLocatorTest {

    @Inject
    KubernetesClientServiceLocator serviceLocator;

    @Test
    void whenServiceIsDiscoveredOnAnUnknowEnv(ClientAndServer client) {
        final Map<String, String> labels = Collections.singletonMap("application", "eap-app");
        client.when(request(String.format("/api/v1/namespaces/%s/services", KubernetesServerMockExtension.MOCK_NAMESPACE)))
              .respond(response(TestSupport.readResourceFromClasspath("/mock/responses/ocp311/serviceList.json")));

        final List<ServiceInfo> services = serviceLocator.discoverService(KubernetesServerMockExtension.MOCK_NAMESPACE, labels);
        assertNotNull(services);
        assertFalse(services.isEmpty(), "Service list shouldn't be empty");
        assertThat(services.get(0).getUrl(), is("http://172.30.66.58:8080"));
        assertThat(services.get(0).getName(), is("eap-app"));
    }

}
