import models.UserCreateResponse;
import models.UserErrorResponse;
import models.UserUpdateResponse;
import models.UsersListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.UserSpec.*;
import static specs.UserSpec.userResponseSpec;

public class NewUserTests extends TestBase {

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя: ответ содержит поля name, job, id, createdAt")
    void createUserShouldReturnAllRequiredFields() {

        UserCreateResponse response = step("Send POST request to create user with valid data", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.VALID_DATA)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseSpec(201))
                        .extract().as(UserCreateResponse.class));

        step("Verify response contains all required fields: name, job, id and createdAt", () -> {
            assertNotNull(response.getName());
            assertNotNull(response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя: ответ содержит те значения name и job, что были поданы в запросе")
    void createdUserShouldHaveNameAndJobFromRequest() {

        UserCreateResponse response = step("Send request to create user with name and job", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.VALID_DATA)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseSpec(201))
                        .extract().as(UserCreateResponse.class));

        step("Verify response contains correct name and job from request", () -> {
            assertEquals("Karlos", response.getName());
            assertEquals("lawyer", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя без данных в запросе")
    void createUserWithEmptyJsonShouldReturnIdAndTimestamp() {

        UserCreateResponse response = step("Send POST request with empty JSON object", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.EMPTY_DATA)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseSpec(201))
                        .extract().as(UserCreateResponse.class));

        step("Verify user created with empty data has ID and creation timestamp", () -> {
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя с лишними полями в запросе")
    void createUserWithAdditionalFieldsShouldReturnAllFields() {

        UserCreateResponse response = step("Send request with additional age field in body", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.EXTRA_DATA)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseSpec(201))
                        .extract().as(UserCreateResponse.class));

        step("Verify response includes all fields: name, job, age, id and createdAt", () -> {
            assertEquals("Sophi", response.getName());
            assertEquals("devops", response.getJob());
            assertEquals("32", response.getAge());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя с пустым запросом")
    void createUserWithEmptyBodyShouldReturnError() {

        UserErrorResponse response = step("Send POST request with completely empty body", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.EMPTY_BODY)
                        .when()
                        .post("/users")
                        .then()
                        .spec(userResponseSpec(400))
                        .extract().as(UserErrorResponse.class));

        step("Verify appropriate error message for empty request body", () -> {
            assertEquals("Empty request body", response.getError());
            assertEquals("Request body cannot be empty for JSON endpoints", response.getMessage());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Новый пользователь: корректировка данных")
    void updateUserDataShouldReturnUpdatedFields() {

        UserUpdateResponse response = step("Send PUT request to update user data", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.EXTRA_DATA)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(userResponseSpec(200))
                        .extract().as(UserUpdateResponse.class));

        step("Verify user data updated successfully with all fields", () -> {
            assertEquals("Sophi", response.getName());
            assertEquals("devops", response.getJob());
            assertEquals("32", response.getAge());
            assertNotNull(response.getUpdatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Новый пользователь: удаление")
    void deleteUserShouldReturnEmptyResponse() {

        String response = step("Send DELETE request to remove user", () ->
                given(userRequestSpec)
                        .header(TestData.API_KEY)
                        .body(TestData.EXTRA_DATA)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(userResponseSpec(204))
                        .extract().body().asString());

        step("Verify deletion returns empty response body", () ->
                assertEquals("", response));
    }

    @Test
    @DisplayName("В списке пользователей существует хотя бы один пользователь")
    void getUserListShouldReturnAtLeastOneUser() {
        UsersListResponse response = step("Send GET request to retrieve users list", () ->
                given(userRequestSpec)
                        .when()
                        .queryParam("page", "1")
                        .get("/users")
                        .then()
                        .spec(userResponseSpec(200))
                        .extract().as(UsersListResponse.class));

         step("Verify users list contains at least one user with complete data", () -> {
             assertNotNull(response.getData());

             UsersListResponse.User firstUser = response.getData().get(0);
             assertNotNull(firstUser.getId());
             assertNotNull(firstUser.getEmail());
             assertNotNull(firstUser.getFirstName());
             assertNotNull(firstUser.getLastName());
             assertNotNull(firstUser.getAvatar());
         });
    }
}