package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void testTittleValid() {
        LoginPage loginPage = new LoginPage(driver);
        String pageTitle = driver.getTitle();
        Assert.assertEquals(pageTitle, "Swag Labs", "The title of this page does not match the expected value.");
    }

    @Test
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("standard_user","secret_sauce");

        String currrentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currrentUrl);

        Assert.assertTrue(currrentUrl.contains("/inventory.html"), "The URL does not contain the expected path ");
        //Assert basic the TestNG to check if navigation worked
        //Assert.assertEquals(driver.getTitle(), "WRONG TITLE HERE", "Force failure for screenshot test.");
    }

    // Each row is one scenario: username, password, expected error message.
    // Adding a scenario means adding a row, not a new test method.
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
            {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out."},
            {"standard_user", "wrong_password", "Username and password do not match any user in this service"},
            {"", "secret_sauce", "Username is required"}
        };
    }

    @Test(dataProvider = "invalidLogins")
    public void testInvalidLoginShowsError(String username, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Unexpected error message for user '" + username + "': " + actualMessage);
    }

}