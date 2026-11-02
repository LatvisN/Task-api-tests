import io.restassured.response.Response;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private static final String API_KEY = "qazWSXedc";
    private static final String CHARS = "0123456789ABCDEF";

    public Response sendRequest(String token, String action, String apiKey) {
        var spec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .formParam("token", token)
                .formParam("action", action);

        if (apiKey != null) {
            spec.header("X-Api-Key", apiKey);
        }

        return spec.post("/endpoint");
    }

    public Response sendRequest(String token, String action) {
        return sendRequest(token, action, API_KEY);
    }

    public static String generateToken() {
        var sb = new StringBuilder(32);
        var random = new Random();
        for (int i = 0; i < 32; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}