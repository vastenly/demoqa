package data.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static data.PagesLinks.BOOK_STORE;

import java.util.logging.Logger;
import java.util.logging.Level;

public class BookStorePage {

    private static final Logger LOG = Logger.getLogger(BookStorePage.class.getName());

    private final ElementsCollection booksCollection = $$(".action-buttons");
    private final ElementsCollection submitButtons = $$("#submit");
    private final ElementsCollection booksTitles = $$("a[href^='/books?book=']");
    private final SelenideElement bookTitle = $("div.action-buttons span a[href^='/profile?book=']");
    private final SelenideElement goToBookStoreButton = $("#gotoStore");
    private final SelenideElement profileLink = $$("ul.menu-list li .text").findBy(text("Profile"));
    private final SelenideElement addToYourCollectionButton = $$("button.btn.btn-primary")
            .filterBy(text("Add To Your Collection")).first();
    private final SelenideElement logoutButton = $("#submit.btn.btn-primary");
    private final SelenideElement searchInput = $("#searchBox"),
            header = $(".main-header"),
            noDataDiv = $(".rt-noData");

    public void goToBookStoreIfPresent() {
        if(goToBookStoreButton.is(Condition.visible)) {
            goToBookStoreButton.click();
            goToBookStoreButton.shouldNotBe(visible);
        }
    }
    public String getRandomBookTitle() {
        goToBookStoreIfPresent(); // Ensure to bee in the book store page
        if (booksTitles.isEmpty()) {
            //TODO check if page is refreshed. Probably bug.
            LOG.log(Level.SEVERE, "Book titles list is still empty after refresh. Possible bug.");
            openPage();
            goToBookStoreIfPresent();
        }
        booksTitles.shouldHave(sizeGreaterThan(0));

        int randomIndex = new Random().nextInt(booksTitles.size());
        return booksTitles.get(randomIndex).getText();
    }
    public void search(String value) {
        searchInput.setValue(value).pressEnter();
    }

    public void checkFoundBook(String value) {
        booksCollection.shouldHave(exactTexts(value));
    }

    public void checkFoundBooks(List<String> values) {
        booksCollection.shouldHave(exactTexts(values));
    }

    public void checkNoDataMessage(String value) {
        noDataDiv.shouldHave(exactText(value));
    }

    public void addBookToCollection(String title) {
        booksTitles.findBy(text(title)).click();
        //TODO check if user still authorized. Probably bug.
        if (!logoutButton.isDisplayed()) {
            LOG.log(Level.SEVERE, "Check if user still authorized. Possible bug.");
        }
        logoutButton.shouldHave(Condition.text("Log out"));
        // Wait for the book details to load and then click on the add-to-collection button
        addToYourCollectionButton.shouldBe(Condition.visible, Duration.ofSeconds(2)).click();
    }

    public void verifyBookInCollection(String title) {
        // Click on the Profile link to open the collection of books
        profileLink.shouldBe(Condition.visible, Duration.ofSeconds(2));
        if (!profileLink.isSelected()) {
            Selenide.executeJavaScript("window.scrollBy(0,500);");
            profileLink.click();
        }
        // Verify the specific book is in the collection by checking its title
        bookTitle.shouldHave(text(title));
    }

    public void openPage() {
        open(BOOK_STORE.getLink());
        header.shouldHave(text(BOOK_STORE.getHeader()));
        executeJavaScript("$('footer').remove()");
        executeJavaScript("$('#fixedban').remove()");
    }

    public void checkLogoutButtonIsVisible() {
        submitButtons.shouldHave(itemWithText("Log out"));
    }

    public void logout() {
        logoutButton.shouldHave(Condition.text("Log out")).click();
    }
}
