package org.sandbox.quarkus.k8s.svc.discovery.tests;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class TestSupport {

    private TestSupport() {

    }

    public static String readResourceFromClasspath(final String classpath) {
        BufferedInputStream bis = new BufferedInputStream(TestSupport.class.getResourceAsStream(classpath));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        try {
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            return buf.toString("UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading classpath resource", e);
        }

    }

}
