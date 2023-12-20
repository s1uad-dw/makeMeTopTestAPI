import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class Example {
    @Test(description = "Проверка работоспособности allure и Jenkins")
    public void testAllureAndJenkins() {
        assertEquals(1, 1);
    }

    @Test(description = "Получение списка пользователей")
    public void testListUsers() {
        baseURI = "https://reqres.in/api";
        given()
                .param("page", 2)
        .when()
                .get("/users")
                .then()
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data.id", hasItems(7, 8, 9, 10, 11, 12))
                .statusCode(200).log().all();
    }

    @Test(description = "Создание пользователя")
    public void testCreateUser() {
        baseURI = "https://reqres.in/api";

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "leader");

        given()
                .header("Content-Type", "application/json")
                .body(requestParams.toJSONString())
        .when()
                .post("/users")
        .then()
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .statusCode(201).log().all();
    }

    @Test(description = "Обновление пользователя методом PUT")
    public void testUpdateUserPUT() {
        baseURI = "https://reqres.in/api";

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");

        given()
                .header("Content-Type", "application/json")
                .body(requestParams.toJSONString())
        .when()
                .put("/users/2")
        .then()
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .statusCode(200).log().all();
    }

    @Test(description = "Обновление пользователя методом PATCH")
    public void testUpdateUserPATCH() {
        baseURI = "https://reqres.in/api";

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");

        given()
                .header("Content-Type", "application/json")
                .body(requestParams.toJSONString())
        .when()
                .patch("/users/2")
        .then()
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .statusCode(200).log().all();
    }

    @Test(description = "Обновление пользователя методом PATCH")
    public void testDeleteUser() {
        baseURI = "https://reqres.in/api";

        delete("/users/2")
                .then()
                .statusCode(204).log().all();
    }

}
