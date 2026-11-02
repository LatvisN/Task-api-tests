import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@Epic("Управление сессией")
@Feature("Выход из системы")
public class LogoutTest extends BaseTest {
    private final ApiClient client = new ApiClient();

    @Test
    @Description("Успешный выход после входа")
    void successfulLogout() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN");

        client.sendRequest(token, "LOGOUT")
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Description("Выход без предварительного входа")
    void logoutWithoutLogin() {
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGOUT")
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Повторный выход после выхода")
    void doubleLogout() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN");
        client.sendRequest(token, "LOGOUT");

        client.sendRequest(token, "LOGOUT")
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Полный цикл: вход, действие, выход")
    void fullCycle() {
        mockService.setupAuthOk();
        mockService.setupDoActionOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN")
                .then()
                .body("result", equalTo("OK"));

        client.sendRequest(token, "ACTION")
                .then()
                .body("result", equalTo("OK"));

        client.sendRequest(token, "LOGOUT")
                .then()
                .body("result", equalTo("OK"));
    }
}