package com.qa;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;


import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    protected static AppiumDriver driver;
    protected static Properties props;
    protected static HashMap<String, String> strings = new HashMap<String, String>();
    protected static String dateTime;
    private static AppiumDriverLocalService server;
    protected static ThreadLocal <String> platform = new ThreadLocal<String>();
    protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();

    InputStream ipStream;
    InputStream stringIs;
    TestUtils utils = new TestUtils();



    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getPlatform() {
        return platform.get();
    }

    public void setPlatform(String platform2) {
        platform.set(platform2);
    }
    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    @BeforeMethod()
    public void beforeMethod(){
        ((CanRecordScreen) driver).startRecordingScreen();
    }

    //Recording the testcases
    @AfterMethod
    public  void afterMethod(ITestResult result) {
        String media = ((CanRecordScreen) driver).stopRecordingScreen();

        //Will capture video only when the test fails
        if(result.getStatus() == 2){
            Map<String, String> parameters = new HashMap<String, String>();
            parameters = result.getTestContext().getCurrentXmlTest().getAllParameters();
            String dir = "Videos" + File.separator + parameters.get("platformName") + "_" + parameters.get("platformVersion") + "_" + parameters.get("deviceName")
                    + File.separator + dateTime + File.separator + result.getTestClass().getRealClass().getSimpleName();
            File videoDir = new File(dir);
            if(!videoDir.exists()){
                videoDir.mkdirs();
            }
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                try {
                    stream.write(Base64.decodeBase64(media));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Parameters({"platformName", "platformVersion", "deviceName"})
    @BeforeTest
    public void beforeTest(String platformName, String platformVersion, String deviceName) throws Exception {
        utils = new TestUtils();
        URL url;
        dateTime = utils.getDateTime();
        String strFile = "Logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY", strFile);
        try {
            // Loading the properties file
            props = new Properties();
            String propFileName = "config.properties";
            String xmlFileName = "Strings/strings.xml";

            ipStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(ipStream);

            stringIs = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            strings = utils.parseStringXML(stringIs);

            // Setting capabilites for android device
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
            caps.setCapability(MobileCapabilityType.UDID, "emulator-5554");

            // Remotely Launching an App using AppPackage and AppActivity
            caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
            caps.setCapability("appActivity", props.getProperty("androidAppActivity"));

            //Installing the app
            String appUrl = System.getProperty("user.dir") + props.getProperty("androidAppLocation");
            utils.log().info("App url is : " + appUrl);
            caps.setCapability(MobileCapabilityType.APP, appUrl);


            url = new URL("http://localhost:4723/wd/hub");

            driver = new AndroidDriver(url, caps);
            String sessionId = driver.getSessionId().toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ipStream != null) {
                ipStream.close();
            }
            if (stringIs != null) {
                stringIs.close();
            }
        }

    }

    public void waitForVisibility(MobileElement e) {
        WebDriverWait wait = new WebDriverWait(driver, utils.wait);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void click(MobileElement e) {
        waitForVisibility(e);
        e.click();
    }

    public void click(MobileElement e, String message) {
        waitForVisibility(e);
        utils.log().debug(message);
        ExtentReport.getTest().log(Status.INFO, message);
        e.click();
    }

    public void sendKeys(MobileElement e, String text) {
        waitForVisibility(e);
        e.sendKeys(text);
    }
    public void sendKeys(MobileElement e, String text, String message) {
        waitForVisibility(e);
        utils.log().info(message);
        ExtentReport.getTest().log(Status.INFO, message);
        e.sendKeys(text);
    }

    public String getAttribute(MobileElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getAttribute(MobileElement e, String attribute, String message) {
        waitForVisibility(e);
        String text = e.getAttribute("text");
        String returnText = message + text;
        utils.log().debug(returnText);
        ExtentReport.getTest().log(Status.INFO, returnText);
        return text;
    }


    public MobileElement scrollToElement() {
        return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));");
    }
    public MobileElement scrollToElement(String message) {
        utils.log().debug(message);
        return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));");
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    public void closeApp() {
        ((InteractsWithApps) driver).closeApp();
    }

    public void launchApp() {
        ((InteractsWithApps) driver).launchApp();
    }

    public String getDateTime(){
        return dateTime;
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("Starting Server");
//        ThreadContext.put("ROUTINGKEY", "ServerLogs");
        server = getAppiumServerDefault();
//        server = getAppiumService();
            server.clearOutPutStreams();
            server.start();
//            utils.log().info("Start Server");

    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("Stop Server");
        server.stop();
    }

    public AppiumDriverLocalService getAppiumService(){
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
        .withLogFile(new File("ServerLogs/server.log"))
        );
    }

    public AppiumDriverLocalService getAppiumServerDefault(){
        return  AppiumDriverLocalService.buildDefaultService();
    }
}
