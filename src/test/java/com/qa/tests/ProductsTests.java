package com.qa.tests;

import com.qa.BaseTest;
import com.qa.MenuPage;
import com.qa.pages.LoginPage;
import com.qa.pages.MenuOptionsPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductsTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    InputStream dataIs;
    JSONObject loginUsers;
    MenuPage menuPage; // = new MenuPage();
    MenuOptionsPage menuOptionsPage;
    ProductDetailsPage productDetailsPage;
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
            if(dataIs != null) {
                dataIs.close();
            }
        }
        closeApp();
        launchApp();
        utils = new TestUtils();

    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        utils.log().debug("\n-----Starting Test-----\n" + method.getName() + "\n");
        loginPage = new LoginPage();
        //Login Process
        String username = loginUsers.getJSONObject("validUser").getString("username");
        String password = loginUsers.getJSONObject("validUser").getString("password");
        productsPage = loginPage.loginUser(username, password);
    }

    @AfterMethod
    public void afterMethod() {
        //Logging out
        menuOptionsPage = productsPage.pressMenuButton();
        loginPage = menuOptionsPage.pressLogoutButton();

    }

    @Test
    public void validateProductOnProductsPageAndBackToProductPageButton(){
        SoftAssert sa = new SoftAssert();
        //Products Page Testing
        String backpackTitle =  productsPage.getBackPackTitle();
        String backpackPrice = productsPage.getBackPackPrice();
        utils.log().debug(backpackTitle + " |" + strings.get("backpackTitle"));
        utils.log().debug(backpackPrice + " | " + strings.get("backpackPrice"));
        sa.assertEquals(backpackTitle, strings.get("backpackTitle"));
        sa.assertEquals(backpackPrice, strings.get("backpackPrice"));
        //Testing back to Products Page button
        productDetailsPage = productsPage.clickBackPackTitle();
        String backpackTitleFromDetailsPage = productDetailsPage.getProductTitle();
        utils.log().debug(backpackTitleFromDetailsPage + " | " + strings.get("backpackTitleOnProductDetailsPage")); //Asserting the Details Page
        productsPage = productDetailsPage.clickBackToProductsPageButton();

        sa.assertAll();


    }

    @Test
    public void validateProductOnProductsDetailsPage(){
        SoftAssert sa = new SoftAssert();
        //Go to ProductDetails Page
        productDetailsPage =  productsPage.clickBackPackTitle();
        String productTitle= productDetailsPage.getProductTitle();
        String productDesc = productDetailsPage.getProductDescription();
        utils.log().debug(productTitle + " | " + strings.get("backpackTitleOnProductDetailsPage"));
        utils.log().debug(productDesc + "\n | \n" + strings.get("backpackDescription"));
        sa.assertEquals(productTitle, strings.get("backpackTitleOnProductDetailsPage"));
        sa.assertEquals(productDesc, strings.get("backpackDescription"));
        productDetailsPage.scrollToPrice();
        String productPrize = productDetailsPage.getProductPrice();
        utils.log().debug(productPrize + " | " + strings.get("backpackPrizeOnProductDetailsPage"));
        //Testing back to Products Page button
        productsPage = productDetailsPage.clickBackToProductsPageButton();
        String backpackTitleOnProductsPage =  productsPage.getBackPackTitle();
        sa.assertEquals(backpackTitleOnProductsPage, strings.get("backpackTitle"));

        sa.assertAll();
    }


}
