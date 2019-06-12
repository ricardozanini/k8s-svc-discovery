package org.sandbox.quarkus.k8s.svc.discovery.official.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.services.ServiceLocator;

@Path("/services")
public class ServicesResource {

    @Inject
    @Named("kubernetesLocator")
    ServiceLocator officialClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{namespace}")
    public List<ServiceInfo> queryWithOfficialK8sClient(@PathParam("namespace") String namespace, @QueryParam("labels") String labelSelector) {
        return officialClient.discoverService(namespace, this.transformLabelsStringToMap(labelSelector));
    }

    private Map<String, String> transformLabelsStringToMap(String labels) {
        final Map<String, String> labelsMap = new HashMap<>();
        if (labels != null && !labels.isEmpty()) {
            labelsMap.putAll(Stream.of(labels.split(","))
                                   .collect(
                                            Collectors.toMap(l -> l.toString().split("=")[0],
                                                             l -> l.toString().split("=")[1])));
        }
        return labelsMap;
    }
}
