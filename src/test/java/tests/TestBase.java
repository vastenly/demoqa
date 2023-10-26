package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.ProjectConfiguration;
import config.web.LaunchConfig;
import config.web.LaunchConfigReader;
import data.RegistrationData;
import data.ScenarioContext;
import data.api.AuthorizationApi;
import data.models.Credentials;
import data.models.UserNew;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.util.Objects;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static config.user.UserProperties.PASSWORD;

public class TestBase {

    private static final LaunchConfig CONFIG = LaunchConfigReader.Instance.read();
    protected ScenarioContext scenarioContext;
    private final AuthorizationApi authorizationApi = new AuthorizationApi();
    private final RegistrationData data = new RegistrationData();

    public TestBase() {
        scenarioContext = new ScenarioContext();
    }

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        ProjectConfiguration projectConfiguration = new ProjectConfiguration(CONFIG);
        projectConfiguration.webConfig();
        projectConfiguration.apiConfig();
    }

    @AfterEach
    void tearDown() {
        Attach.screenshotAs("Screenshot");
        Attach.pageSource();
        if (Objects.equals(System.getProperty("browserName"), "chrome")) {
            Attach.browserConsoleLogs();
        }
        Attach.addVideo();

        getWebDriver().manage().deleteAllCookies();
    }

    protected void createUser() {
        Credentials credentials = new Credentials();
        credentials.setUserName(data.firstName + data.lastName);
        credentials.setPassword(PASSWORD);
        authorizationApi.createUser(credentials);

        scenarioContext.setContext(ScenarioContext.Context.USER_NAME, credentials.getUserName());
        scenarioContext.setContext(ScenarioContext.Context.PASSWORD, PASSWORD);
    }
}
