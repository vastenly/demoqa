package tests.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.api.AuthorizationApi;
import data.models.*;
import io.qameta.allure.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static data.specs.RequestSpecs.jsonRequestSpec;
import static data.specs.RequestSpecs.requestSpec;
import static data.specs.ResponseSpecs.responseSpec;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
@Owner("vastenly")
@Epic(value = "Book Store Application")
@Feature(value = "API")
@Story(value = "Book Store API")
@Tag("api")
class BookStoreApiTests extends TestBaseApi {

    private final AuthorizationApi authorizationApi = new AuthorizationApi();

    @SneakyThrows
    @Severity(CRITICAL)
    @DisplayName("Add a random book to the collection and verify")
    @Description("Add a random book to the collection using API and verify it's added")
    @Test
    void addAndVerifyRandomBookAddedToCollectionTest() {
        // Step 1: Access bookstore app is done in the setup

        // Step 2: Add one random book to the collection
        String responseJson = given(requestSpec)
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().body().asString();

        ObjectMapper objectMapper = new ObjectMapper();

// Deserialize the books list inside the root "books" key
        List<Books> allBooks = objectMapper.readValue(responseJson, new TypeReference<Map<String, List<Books>>>() {})
                .get("books");

        Random random = new Random();
        Books randomBook = allBooks.get(random.nextInt(allBooks.size()));

        UserNew user = authorizationApi.createUser(credentials);
        Token token = authorizationApi.getToken(credentials);

        Map<String, Object> requestBody = getBody (randomBook.getIsbn(), user);

        given(jsonRequestSpec)
                .header("Authorization", "Bearer " + token.getToken())
                .body(requestBody)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec)
                .statusCode(201);

        // Step 3: Verify the book is added to the collection
        User userInfo = given(requestSpec)
                .header("Authorization", "Bearer " + token.getToken())
                .when()
                .get("/Account/v1/User/" + user.getUserID())
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(User.class);

        assertThat(userInfo.getBooks()).hasSize(1);
        assertThat(userInfo.getBooks().get(0).getIsbn()).isEqualTo(randomBook.getIsbn());
    }

    @SneakyThrows
    @Severity(CRITICAL)
    @DisplayName("Add specified book to the collection and verify")
    @Description("Add specified book to the collection using API and verify it's added")
    @Test
    void addAndVerifySpecifiedBookAddedToCollectionTest() {
        // Step 1: Access bookstore app is done in the setup

        // Step 2: Add one book to the collection
        String randomBookISBN = "9781593277574";

        UserNew user = authorizationApi.createUser(credentials);
        Token token = authorizationApi.getToken(credentials);

        Map<String, Object> requestBody = getBody (randomBookISBN, user);

        given(jsonRequestSpec)
                .header("Authorization", "Bearer " + token.getToken())
                .body(requestBody)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec)
                .statusCode(201);

        // Step 3: Verify the book is added to the collection
        User userInfo = given(requestSpec)
                .header("Authorization", "Bearer " + token.getToken())
                .when()
                .get("/Account/v1/User/" + user.getUserID())
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(User.class);

        assertThat(userInfo.getBooks()).hasSize(1);
        assertThat(userInfo.getBooks().get(0).getIsbn()).isEqualTo(randomBookISBN);
    }

    private Map<String, Object>  getBody (String ISBN, UserNew user) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", user.getUserID());

        List<Map<String, String>> isbnList = new ArrayList<>();
        Map<String, String> isbnMap = new HashMap<>();
        isbnMap.put("isbn", ISBN);
        isbnList.add(isbnMap);

        requestBody.put("collectionOfIsbns", isbnList);
        return requestBody;
    }
}
