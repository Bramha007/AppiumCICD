package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuOptionsPage extends BaseTest {
    TestUtils utils = new TestUtils();
    @AndroidFindBy(accessibility = "test-LOGOUT")
    private MobileElement logoutButton;

    public LoginPage pressLogoutButton(){
        click(logoutButton, "Click Logout Button");
        return new LoginPage();
    }
}
