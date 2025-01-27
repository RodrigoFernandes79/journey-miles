package com.br.rodrigo.jornadamilhas.integrationTests.testContainer.swagger;

import com.br.rodrigo.jornadamilhas.configs.TestConfigs;
import com.br.rodrigo.jornadamilhas.integrationTests.testContainer.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Test Should show Swagger UI page")
    void testShouldShowSwaggerUIPage() {
        //Arrange / Given
        String content = given()
                .basePath("swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
                .when()   //Act / When
                .get()
                .then()      //assert / Then
                .statusCode(200)
                .extract()
                .body()
                .asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
