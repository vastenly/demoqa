package tests;

import config.ProjectConfiguration;
import config.web.LaunchConfig;
import config.web.LaunchConfigReader;
import data.RegistrationData;
import data.models.Credentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBaseApi {
    private static final LaunchConfig CONFIG = LaunchConfigReader.Instance.read();
    protected final Credentials credentials = new Credentials();
    protected final RegistrationData data = new RegistrationData();

    @BeforeAll
    static void setUp() {
        ProjectConfiguration projectConfiguration = new ProjectConfiguration(CONFIG);
        projectConfiguration.apiConfig();
    }

    @BeforeEach
    void createUserForTest() {
        credentials.setUserName(data.firstName + data.lastName);
        credentials.setPassword(config.user.UserProperties.PASSWORD);
    }
}
