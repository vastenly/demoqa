package tests.web;

import data.ScenarioContext;
import data.pages.BookStorePage;
import data.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import tests.TestBase;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.*;

@Owner("vastenly")
@Epic(value = "Book Store Application")
@Feature(value = "Book Store Page")
@Story(value = "Book Store Search")
@Tag("ui")
class BookStoreTests extends TestBase {

    private final BookStorePage bookStorePage = new BookStorePage();

    @Severity(CRITICAL)
    @DisplayName("User can login and add a book to collection")
    @Test()
    void userCanLoginAndAddRandomBookToCollection() {
        // Create a user using the shared method from TestBase
        createUser();

        // Fetch credentials from ScenarioContext
        String userName = (String) scenarioContext.getContext(ScenarioContext.Context.USER_NAME);
        String password = (String) scenarioContext.getContext(ScenarioContext.Context.PASSWORD);

        LoginPage loginPage = new LoginPage();
        BookStorePage bookStorePage = new BookStorePage();

        step("Open Login Page", loginPage::openPage);

        step("Enter credentials and Login", () -> {
            loginPage.enterUsername(userName)
                    .enterPassword(password)
                    .clickLogin();
        });

        // Selecting a random book title
        String selectedBook = bookStorePage.getRandomBookTitle();

        step("Search for the selected book", () -> bookStorePage.search(selectedBook));
        step("Add the selected book to collection", () -> bookStorePage.addBookToCollection(selectedBook));
        step("Verify the selected book is added to collection", () -> bookStorePage.verifyBookInCollection(selectedBook));

        // Logging out and logging in again
        step("Log out from the application", bookStorePage::logout);
        step("Open Login Page again", loginPage::openPage);
        step("Log in back to the application", () -> {
            loginPage.enterUsername(userName)
                    .enterPassword(password)
                    .clickLogin();
        });

        // Verifying the book is still present in the collection after re-login
        step("Verify the selected book is still in the collection after re-login", () -> bookStorePage.verifyBookInCollection(selectedBook));
    }
}
