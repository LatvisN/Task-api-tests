import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockService {
    private final WireMockServer wireMock;

    public MockService(WireMockServer wireMock) {
        this.wireMock = wireMock;
    }

    public void setupAuthOk() {
        wireMock.stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse().withStatus(200)));
    }

    public void setupAuthFail(int status) {
        wireMock.stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse().withStatus(status)));
    }

    public void setupDoActionOk() {
        wireMock.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(200)));
    }

    public void setupDoActionFail(int status) {
        wireMock.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(status)));
    }
}