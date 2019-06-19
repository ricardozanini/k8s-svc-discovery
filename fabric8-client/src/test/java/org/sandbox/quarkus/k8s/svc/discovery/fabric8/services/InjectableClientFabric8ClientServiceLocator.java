package org.sandbox.quarkus.k8s.svc.discovery.fabric8.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.sandbox.quarkus.k8s.svc.discovery.fabric8.services.Fabric8ClientServiceLocator;

@ApplicationScoped
@Named("integrationTest")
public class InjectableClientFabric8ClientServiceLocator extends Fabric8ClientServiceLocator {

    @Override
    public void setupClient() throws Exception {
        //skip :D   
    }

    @Override
    protected void verifyClientConnection() {
        //skip :D
    }

}
