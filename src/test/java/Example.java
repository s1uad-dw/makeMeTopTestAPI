import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class Example {
    @Test(description = "Проверка работоспособности allure и Jenkins")
    public void testAllureAndJenkins() {
        assertEquals(1, 1);
    }

    @Test(description = "Авторизация не зарегестрированного пользователя")
    public void testUnregisteredUserAuthorization() {
        baseURI = "http://localhost:4401/api/v1";
        JSONObject requestParams = new JSONObject();
        requestParams.put("login", "login");
        requestParams.put("password", "password");
        requestParams.put("role", "EXPLORER");

        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
        .when()
                .post("/auth/login/")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/not-found-scheme.json"))
                .body("errorCode", equalTo("Not Found"))
                .body("errorMessage", equalTo("Бортовой компьютер не смог вас идентифицировать"))
                .statusCode(404).log().all();
    }

    @Test(description = "Авторизация пользователя с несуществующей ролью")
    public void testNonExistentRoleUserAuthorization() {
        baseURI = "http://localhost:4401/api/v1";
        JSONObject requestParams = new JSONObject();
        requestParams.put("login", "user");
        requestParams.put("password", "user");
        requestParams.put("role", "Hitler");

        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
        .when()
                .post("/auth/login/")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/bad-request-schema.json"))
                .body("errorCode", equalTo("Bad Request"))
                .body("errorMessage", equalTo("Данная роль недоступна"))
                .statusCode(400).log().all();
    }

    @Test(description = "Получение всех галактик")
    public void testGetAllGalaxies() {
        baseURI = "http://localhost:4401/api/v1";

        given()
                .param("detailed","true")
        .when()
                .get("/galaxy-app/galaxies/")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/all-galaxies-schema.json"))
                .body("galaxyId", hasItems(1, 2, 3))
                .body("galaxyName", hasItems("Автоматизация тестирования", "UI Дизайн", "Frontend Разработка"))
                .statusCode(200).log().all();
    }

    @Test(description = "Выход авторизированного пользователя")
    public void testAuthorizedUserExit() {
        baseURI = "http://localhost:4401/api/v1";
        JSONObject requestParams = new JSONObject();
        requestParams.put("login", "user");
        requestParams.put("password", "user");
        requestParams.put("role", "EXPLORER");

        given()
                .body(requestParams.toJSONString())
        .when()
                .post("/auth/logout/")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/logout-success-schema.json"))
                .body("message", equalTo("Выход успешный"))
                .statusCode(200).log().all();
    }

    @Test(description = "Получение систем галактики")
    public void testGetAllSystemsOfGalaxy() {
        baseURI = "http://localhost:4401/api/v1";

        get("/galaxy-app/galaxies/1")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/systems-of-galaxy-schema.json"))
                .body("galaxyId", equalTo(1))
                .body("galaxyName", equalTo("Автоматизация тестирования"))
                .body("orbitList.orbitId", hasItems(1, 2, 3, 4))
                .statusCode(200).log().all();
    }
}
