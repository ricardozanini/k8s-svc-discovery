package org.sandbox.quarkus.k8s.svc.discovery.services;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseServiceLocator implements ServiceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceLocator.class);

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

    public abstract void setupClient() throws Exception;

    protected abstract void verifyClientConnection() throws Exception;

    protected abstract String getMasterURL();

    /**
     * Initializes the bean with the common algorithm.
     * 
     * @throws Exception
     */
    @PostConstruct
    public void doSetupClient() throws Exception {
        setupClient();
        try {
            verifyClientConnection();
            LOGGER.info("Successfully connected to remote kubernetes cluster at {}", this.getMasterURL());
        } catch (Exception e) {
            LOGGER.debug("Failed to connect to Kubernetes cluster", e);
            LOGGER.warn("Client is invalid, tried to connect to {}, got \"{}\"", this.getMasterURL(), e.getMessage());
        }
    }

}
