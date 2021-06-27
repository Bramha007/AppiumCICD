package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    InputStream dataIs;
    JSONObject loginUsers;
    TestUtils utils;

    @BeforeClass
    public void beforeClass() throws IOException {
        try{
            String dataFileName = "Data/loginUser.json";
            dataIs = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(dataIs);
            loginUsers = new JSONObject(tokener);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(dataIs != null){
                dataIs.close();
            }

        }
        utils = new TestUtils();
        closeApp();
        launchApp();

    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        utils.log().debug("\n-----Starting Test-----\n" + method.getName() + "\n");
        loginPage = new LoginPage();
    }

    @AfterMethod
    public void afterMethod() {
    }

    @Test
    public void invalidUsername() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginButton();

        String actualErrText = loginPage.getErrorText();
        String expectedErrText = strings.get("errorTextForLoginPage");
        utils.log().debug("Expected Error msg : " + expectedErrText);

        Assert.assertEquals(actualErrText, expectedErrText);
//        try{
//            //Put the above test code here
//        }catch (Exception e){
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            System.out.println(sw.toString()); //printing the stacktrace on console
//            Assert.fail(sw.toString()); //sending the stacktrace to testNg
//        }
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginButton();

        String actualErrText = loginPage.getErrorText();
        String expectedErrText = strings.get("errorTextForLoginPage");
        utils.log().debug("Expected Error msg : " + expectedErrText);
        Assert.assertEquals(actualErrText, expectedErrText);
    }

    @Test
    public void successfulLogin() {
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginButton();

        String actualPageTitle = productsPage.getTitle();
        String expectedPageTitle = strings.get("productsPageTitle");
        utils.log().debug("Actual Products page header : " + actualPageTitle);
        utils.log().debug("Expected Products page header : " + actualPageTitle);
        Assert.assertEquals(actualPageTitle, expectedPageTitle);

    }
}
