package org.sandbox.quarkus.k8s.svc.discovery.mock.server;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockserver.integration.ClientAndServer;
import org.sandbox.quarkus.k8s.svc.discovery.services.ServiceLocatorProperties;

public class KubernetesServerMockExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    public static final String MOCK_NAMESPACE = "mock-namespace";
    public static final int DEFAULT_PORT = 8282;

    private ClientAndServer mockServer;

    public KubernetesServerMockExtension() {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        mockServer = ClientAndServer.startClientAndServer(DEFAULT_PORT);
        System.setProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_URL,
                           String.format("http://%s:%d", "localhost", mockServer.getLocalPort()));
        System.setProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_VALIDATE_CERT, "false");
        System.setProperty(ServiceLocatorProperties.K8S_SVC_CLUSTER_TOKEN, "SyG22ufSqgRzYSaIIKpW011qsdy_-inbE4G9aw7OoN4");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (mockServer != null) {
            mockServer.stop();
        }
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return this.mockServer;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(ClientAndServer.class);
    }

}
