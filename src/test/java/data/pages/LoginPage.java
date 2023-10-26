package data.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.PagesLinks.LOGIN;

public class LoginPage {

    private final SelenideElement usernameInput = $("#userName");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login");

    public LoginPage openPage() {
        open(LOGIN.getLink());
        return this;
    }

    public LoginPage enterUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public BookStorePage clickLogin() {
        loginButton.click();
        return new BookStorePage();
    }
}
