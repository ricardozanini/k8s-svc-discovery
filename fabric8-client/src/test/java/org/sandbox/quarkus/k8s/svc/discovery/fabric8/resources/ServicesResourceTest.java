package org.sandbox.quarkus.k8s.svc.discovery.fabric8.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class ServicesResourceTest {

    @Test
    public void testQuery() {
        given()
               .when().get("/services/mynamespace")
               .then()
               .statusCode(200)
               .body(containsString("8080"));
    }

}
