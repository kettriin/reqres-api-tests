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

public class NewUserTests extends TestBase {

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя: ответ содержит поля name, job, id, createdAt")
    void successfullNewUserTest() {

        UserCreateResponse response = step("newUserWithRequiredBodyReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.validData)
                .when()
                .post("/users")
                .then()
                .spec(newUserResponseSpec)
                .extract().as(UserCreateResponse.class));

        step("checkRequiredFieldsInResp", () -> {
            assertNotNull(response.getName());
            assertNotNull(response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя: ответ содержит те значения name и job, что были поданы в запросе")
    void nameJobFromReqTest() {

        UserCreateResponse response = step("newUserWithNameJobReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.validData)
                .when()
                .post("/users")
                .then()
                .spec(newUserResponseSpec)
                .extract().as(UserCreateResponse.class));

        step("checkNameJobResp", () -> {
            assertEquals("Karlos", response.getName());
            assertEquals("lawyer", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя без данных в запросе")
    void emptyDataTest() {

        UserCreateResponse response = step("emptyDataReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.emptyData)
                .when()
                .post("/users")
                .then()
                .spec(newUserResponseSpec)
                .extract().as(UserCreateResponse.class));

        step("checkEmptyDataResp", () -> {
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Создание нового пользователя с лишними полями в запросе")
    void extraDataTest() {

        UserCreateResponse response = step("extraFieldsNewUserReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.extraData)
                .when()
                .post("/users")
                .then()
                .spec(newUserResponseSpec)
                .extract().as(UserCreateResponse.class));

        step("checkRespWithNewField", () -> {
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
    void emptyBodyReqTest() {

        UserErrorResponse response = step("emptyBodyReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.emptyBody)
                .when()
                .post("/users")
                .then()
                .spec(emptyBodyErrorResponseSpec)
                .extract().as(UserErrorResponse.class));

        step("checkEmptyBodyError", () -> {
            assertEquals("Empty request body", response.getError());
            assertEquals("Request body cannot be empty for JSON endpoints", response.getMessage());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Новый пользователь: корректировка данных")
    void userDataUpdateTest() {

        UserUpdateResponse response = step("updateUserDataReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.extraData)
                .when()
                .put("/users/2")
                .then()
                .spec(userResponseSpec)
                .extract().as(UserUpdateResponse.class));

        step("checkUserDataUpdated", () -> {
            assertEquals("Sophi", response.getName());
            assertEquals("devops", response.getJob());
            assertEquals("32", response.getAge());
            assertNotNull(response.getUpdatedAt());
        });
    }

    @Test
    @Tag("smokeApiUserTests")
    @DisplayName("Новый пользователь: удаление")
    void userDeleteTest() {

        String rsponse = step("deleteUserReq", () -> given(userRequestSpec)
                .header(TestData.apiKey)
                .body(TestData.extraData)
                .when()
                .delete("/users/2")
                .then()
                .spec(deleteUserResponseSpec)
                .extract().body().asString());

        step("checkUserDeleted", () -> assertEquals("", rsponse));
    }

    @Test
    @DisplayName("В списке пользователей существует хотя бы один пользователь")
    void userListNotEmpty() {
        UsersListResponse response = step("getUserListReq", () -> given(userRequestSpec)
                .when()
                .queryParam("page", "2")
                .get("/users")
                .then()
                .spec(userResponseSpec)
                .extract().as(UsersListResponse.class));

        step("checkMoreThanOneUserInList", () -> {
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
