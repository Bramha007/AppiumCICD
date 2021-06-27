package com.qa;

import com.qa.pages.MenuOptionsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuPage extends BaseTest {
    @AndroidFindBy (xpath="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView\n" +
            "")
//    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    private MobileElement menuButton;
    TestUtils utils = new TestUtils();

    public MenuOptionsPage pressMenuButton(){
        utils.log().debug("Click Menu Button");
        click(menuButton);
        return new MenuOptionsPage();
    }
}
