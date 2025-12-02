import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class NewUserTests extends TestBase {

    @Test
    @DisplayName("Создание нового пользователя: ответ содержит поля name, job, id, createdAt")
    void successfullNewUserTest() {

        given()
                .header(TestData.apiKey)
                .body(TestData.validData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post()

                .then()
                .log().status()
                .log().body()
                .statusCode(201)

                .body("$", allOf(
                        hasKey("name"),
                        hasKey("job"),
                        hasKey("id"),
                        hasKey("createdAt")
                ));
    }

    @Test
    @DisplayName("Создание нового пользователя: ответ содержит те значения name и job, что были поданы в запросе")
    void nameJobFromReqTest() {

        given()
                .header(TestData.apiKey)
                .body(TestData.validData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post()

                .then()
                .log().status()
                .log().body()
                .statusCode(201)

                .body("name", is("Karlos"))
                .body("job", is("lawyer"))
                .body("$", allOf(
                        hasKey("id"),
                        hasKey("createdAt")
                ));
    }

    @Test
    @DisplayName("Создание нового пользователя без данных в запросе")
    void emptyDataTest() {

        given()
                .header(TestData.apiKey)
                .body(TestData.emptyData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post()

                .then()
                .log().status()
                .log().body()
                .statusCode(201)

                .body("$", allOf(
                        hasKey("id"),
                        hasKey("createdAt")
                ));
    }

    @Test
    @DisplayName("Создание нового пользователя с лишними полями в запросе")
    void extraDataTest() {

        given()
                .header(TestData.apiKey)
                .body(TestData.extraData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post()

                .then()
                .log().status()
                .log().body()
                .statusCode(201)

                .body("name", is("Sophi"))
                .body("job", is("devops"))
                .body("age", is("32"))
                .body("$", allOf(
                        hasKey("id"),
                        hasKey("createdAt")
                ));
    }

    @Test
    @DisplayName("Создание нового пользователя с пустым запросом")
    void emptyBodyReqTest() {

        given()
                .header(TestData.apiKey)
                .body(TestData.emptyBody)
                .contentType(JSON)
                .log().uri()
                .when()
                .post()

                .then()
                .log().status()
                .log().body()
                .statusCode(400)

                .body("error", is("Empty request body"))
                .body("message", is("Request body cannot be empty for JSON endpoints"));
    }
}
