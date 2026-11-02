import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

@Epic("Аутентификация")
@Feature("Вход в систему")
public class LoginTest extends BaseTest {
    private final ApiClient client = new ApiClient();

    @Test
    @Description("Успешный вход с валидным токеном")
    void successfulLogin() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN")
                .then()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Description("Вход невозможен, внешний сервис недоступен")
    void loginWhenExternalServiceFails() {
        mockService.setupAuthFail(500);
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN")
                .then()
                .statusCode(500)
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Description("Вход без API-ключа отклонён")
    void loginWithoutApiKey() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN", null)
                .then()
                .statusCode(401);
    }

    @Test
    @Description("Вход с неверным API-ключом отклонён")
    void loginWithWrongApiKey() {
        mockService.setupAuthOk();
        String token = ApiClient.generateToken();

        client.sendRequest(token, "LOGIN", "wrongKey")
                .then()
                .statusCode(401);
    }
}