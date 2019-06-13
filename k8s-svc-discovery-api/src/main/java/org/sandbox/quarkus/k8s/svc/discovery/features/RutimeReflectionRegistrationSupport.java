package org.sandbox.quarkus.k8s.svc.discovery.features;

import org.graalvm.nativeimage.hosted.RuntimeReflection;

public abstract class RutimeReflectionRegistrationSupport {

    protected void registerAll(Class<?> clazz) {
        RuntimeReflection.register(clazz.getDeclaredFields());
        RuntimeReflection.register(clazz.getDeclaredConstructors());
        RuntimeReflection.register(clazz.getDeclaredMethods());
        RuntimeReflection.registerForReflectiveInstantiation(clazz);
    }
}
