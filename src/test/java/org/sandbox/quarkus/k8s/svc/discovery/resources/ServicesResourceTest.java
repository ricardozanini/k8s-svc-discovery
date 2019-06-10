package org.sandbox.quarkus.k8s.svc.discovery.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ServicesResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
               .when().get("/services")
               .then()
               .statusCode(200)
               .body(is("hello"));
    }

}
