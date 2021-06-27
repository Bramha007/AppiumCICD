package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginPage extends BaseTest {

    @AndroidFindBy(accessibility = "test-Username")
    private MobileElement userName;
    @AndroidFindBy(accessibility = "test-Password")
    private MobileElement passWord;
    @AndroidFindBy(accessibility = "test-LOGIN")
    private MobileElement loginButton;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    private MobileElement errorMessage;

    TestUtils utils = new TestUtils();

    //After entering the username(ie this method) we are staying on the LoginPage itself
    //hence returning the Login itself -> return this
    //If after entering the username we navigated to some other page we wld return that page
    public LoginPage enterUserName(String usernameIp) {
        sendKeys(userName, usernameIp, "Username : " + usernameIp);
        return this;
    }

    public LoginPage enterPassword(String passwordIp) {
        sendKeys(passWord, passwordIp, "Password : " + passwordIp);
        return this;
    }

    //After entering the login button we will navigate to Products page
    //Hence returning the new projects page
    public ProductsPage pressLoginButton() {
        click(loginButton, "Press Login button");
        return new ProductsPage();
    }

    public String getErrorText() {
        String errorText = getAttribute(errorMessage, "text", "Error text is : ");
        return errorText;
    }

    public ProductsPage loginUser(String username, String password){
        enterUserName(username);
        enterPassword(password);
        return pressLoginButton();
    }
}
