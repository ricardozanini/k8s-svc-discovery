package org.sandbox.quarkus.k8s.svc.discovery.kogito.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.LoadBalancerStatus;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.cloud.kubernetes.client.DefaultKogitoKubeClient;
import org.kie.kogito.cloud.kubernetes.client.KogitoKubeConfig;
import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.kogito.extensions.KubernetesServerMockExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@ExtendWith(KubernetesServerMockExtension.class)
class KogitoClientServiceLocatorTest {

    @Inject
    KogitoClientServiceLocator serviceLocator;

    @Test
    void whenServiceIsDiscovered(KubernetesClient client) throws Exception {
        final DefaultKogitoKubeClient kogitoClient = new DefaultKogitoKubeClient();
        serviceLocator.setClient(kogitoClient.withConfig(new KogitoKubeConfig(client)));

        final Map<String, String> labels = Collections.singletonMap("service", "test-kieserver");
        final ServiceSpec serviceSpec = new ServiceSpec();
        serviceSpec.setPorts(Collections.singletonList(new ServicePort("http", 0, 8080, "http", new IntOrString(8080))));
        serviceSpec.setClusterIP("172.30.158.31");
        serviceSpec.setType("ClusterIP");
        serviceSpec.setSessionAffinity("ClientIP");

        final ObjectMeta metadata = new ObjectMeta();
        metadata.setName("eap-app");
        metadata.setNamespace(KubernetesServerMockExtension.MOCK_NAMESPACE);
        metadata.setLabels(labels);

        final Service service = new Service("v1", "Service", metadata, serviceSpec, new ServiceStatus(new LoadBalancerStatus()));
        client.services().create(service);

        final List<ServiceInfo> services = serviceLocator.discoverService(KubernetesServerMockExtension.MOCK_NAMESPACE, labels);
        assertNotNull(services);
        assertFalse(services.isEmpty(), "Service list shouldn't be empty");
        assertThat(services.get(0).getUrl(), is("http://172.30.158.31:8080"));
        assertThat(services.get(0).getName(), is("eap-app"));

    }

}
