package org.sandbox.quarkus.k8s.svc.discovery.kogito.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.sandbox.quarkus.k8s.svc.discovery.domain.ServiceInfo;
import org.sandbox.quarkus.k8s.svc.discovery.resources.BaseServicesResource;

//TODO: investigate
//we have to inherit everything since the annotations in the base class are getting ignored during unit tests in the IDE in this Quarkus version.

@Path("/services")
public class ServicesResource extends BaseServicesResource {

    @Override
    @GET
    @Path("/{namespace}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceInfo> queryServices(@PathParam("namespace") String namespace, @QueryParam("labels") String labelSelector) {
        return super.queryServices(namespace, labelSelector);
    }

}
