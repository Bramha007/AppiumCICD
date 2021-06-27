package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart drop zone\"]/android.view.ViewGroup/android.widget.TextView")
    private MobileElement productTile;
    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc='test-Item title'])[1]")
    private MobileElement backpackTitle;
    @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc='test-Price'])[1]")
    private MobileElement backpackPrice;
    TestUtils utils = new TestUtils();

    public String getTitle() {
        return getAttribute(productTile, "text","Products Page Header : " );
    }

    public String getBackPackTitle(){
        return  getAttribute(backpackTitle,"text", "Backpack Title from ProductsPage : ");
    }

     public String getBackPackPrice(){
         return getAttribute(backpackPrice, "text", "Backpack Price from ProductsPage : " );
     }

     public ProductDetailsPage clickBackPackTitle(){
        click(backpackTitle, "Navigating to the Products Details Page");
        return new ProductDetailsPage();
     }
}
