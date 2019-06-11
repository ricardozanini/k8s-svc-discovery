package org.sandbox.quarkus.k8s.svc.discovery.services;

public final class ServiceLocatorProperties {

    /**
     * The Kubernetes Cluster URL to connect 
     */
    public static final String K8S_SVC_CLUSTER_URL = "K8S_SVC_CLUSTER_URL";

    /**
     * User used to authenticate to the Kubernetes Cluster 
     */
    public static final String K8S_SVC_CLUSTER_USER = "K8S_SVC_CLUSTER_USER";

    /**
     * User password used to authenticate to the Kubernetes Cluster 
     */
    public static final String K8S_SVC_CLUSTER_PWD = "K8S_SVC_CLUSTER_PWD";

    /**
     * Should the k8s client validate the SSL certificate? Set to false when using self signed certificates  
     */
    public static final String K8S_SVC_CLUSTER_VALIDATE_CERT = "K8S_SVC_CLUSTER_VALIDATE_CERT";

    /**
     * Used instead of user and password authentication
     */
    public static final String K8S_SVC_CLUSTER_TOKEN = "K8S_SVC_CLUSTER_TOKEN";

    private ServiceLocatorProperties() {

    }
}
