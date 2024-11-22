package uz.fluxCrm.fluxCrm.crm.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import uz.fluxCrm.fluxCrm.crm.util.Postgres;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration (initializers = Postgres.Initializer.class)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void initAll() {
        Postgres.postgres.start();
        KeycloakContainer keycloakContainer = new KeycloakContainer();
        keycloakContainer.start();
    }

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
