package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductDetailsPage extends MenuPage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private MobileElement productTitle;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    private MobileElement productDesc;
    @AndroidFindBy (accessibility = "test-BACK TO PRODUCTS")
    private MobileElement backToProductsPageButton;
    @AndroidFindBy(accessibility = "test-Price")
    private MobileElement productPrice;

    TestUtils utils = new TestUtils();

    public ProductsPage clickBackToProductsPageButton(){
        click(backToProductsPageButton,"Click \"Back to Products Button\" and navigate to Products Page");
        return new ProductsPage();
    }

    public String getProductTitle(){
        return getAttribute(productTitle,"text", "Product Title from ProductDetailsPage : ");
    }

    public  String getProductPrice(){
        return getAttribute(productPrice, "text", "Product Price from ProductsDetailsPage : ");
    }
    public String getProductDescription(){
        return getAttribute(productDesc, "text", "Product Description from ProductsDetailsPage : ");
    }

    public ProductDetailsPage scrollToPrice(){
        scrollToElement("Scroll to prize");
        return this;
    }

}
