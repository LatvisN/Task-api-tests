import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@Epic("Действия пользователя")
@Feature("Выполнение действия")
public class ActionTest extends BaseTest {
    private final ApiClient client = new ApiClient();

    @Test
    @Description("Успешное действие после входа")
    void successfulActionAfterLogin() {
        mockService.setupAuthOk();
        mockService.setupDoActionOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN");

        client.sendRequest(token, "ACTION")
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Description("Действие без предварительного входа отклонено")
    void actionWithoutLogin() {
        mockService.setupDoActionOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "ACTION")
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Действие после выхода отклонено")
    void actionAfterLogout() {
        mockService.setupAuthOk();
        mockService.setupDoActionOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN");
        client.sendRequest(token, "LOGOUT");

        client.sendRequest(token, "ACTION")
                .then()
                .statusCode(403)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Действие невозможно, внешний сервис недоступен")
    void actionWhenExternalServiceFails() {
        mockService.setupAuthOk();
        mockService.setupDoActionFail(500);
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN");

        client.sendRequest(token, "ACTION")
                .then()
                .statusCode(500)
                .body("result", equalTo("ERROR"));
    }
}