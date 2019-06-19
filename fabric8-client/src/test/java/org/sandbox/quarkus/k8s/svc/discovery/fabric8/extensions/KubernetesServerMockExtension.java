package org.sandbox.quarkus.k8s.svc.discovery.fabric8.extensions;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class KubernetesServerMockExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    public static final String MOCK_NAMESPACE = "mock-namespace";
    public KubernetesServer server = new KubernetesServer(false, true);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        server.before();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        server.after();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(KubernetesClient.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return server.getClient().inNamespace(MOCK_NAMESPACE);
    }

}
