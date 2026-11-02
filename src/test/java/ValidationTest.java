import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Валидация")
@Feature("Проверка входных данных")
public class ValidationTest extends BaseTest {
    private final ApiClient client = new ApiClient();

    @Test
    @Description("Токен короче 32 символов отклонён")
    void tokenTooShort() {
        mockService.setupAuthOk();

        client.sendRequest("ABC", "LOGIN")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Токен длиннее 32 символов отклонён")
    void tokenTooLong() {
        mockService.setupAuthOk();

        client.sendRequest("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "LOGIN")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Токен со строчными буквами отклонён")
    void tokenWithLowercase() {
        mockService.setupAuthOk();

        client.sendRequest("abcdef0123456789abcdef0123456789", "LOGIN")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Токен с недопустимыми символами отклонён")
    void tokenWithInvalidChars() {
        mockService.setupAuthOk();

        client.sendRequest("GHIJKLMNOPQRSTUVWXYZ012345678901", "LOGIN")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Неизвестное действие отклонено")
    void unknownAction() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "DELETE")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Запрос без параметра token отклонён")
    void missingToken() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .header("X-Api-Key", "qazWSXedc")
                .formParam("action", "LOGIN")
                .post("/endpoint")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Запрос без параметра action отклонён")
    void missingAction() {
        String token = ApiClient.generateToken();

        given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .header("X-Api-Key", "qazWSXedc")
                .formParam("token", token)
                .post("/endpoint")
                .then()
                .statusCode(400)
                .body("result", equalTo("ERROR"));
    }
}