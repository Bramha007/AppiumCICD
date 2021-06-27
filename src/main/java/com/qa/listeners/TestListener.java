package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {

    public void onTestFailure(ITestResult result) {
        if (result.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            System.out.println(sw.toString());
        }
        BaseTest base = new BaseTest();
        File file = base.getDriver().getScreenshotAs(OutputType.FILE);
        //Converting File Object to base64 String
        byte[] encoded = null;
        try {
            encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        }catch (IOException e){
            e.printStackTrace();
        }

        Map<String, String> parameters = new HashMap<String, String>();
        parameters = result.getTestContext().getCurrentXmlTest().getAllParameters();
        String imgPath = "Screenshots" + File.separator + parameters.get("platformName") + "_" + parameters.get("platformVersion") + "_" + parameters.get("deviceName") +
                File.separator + base.getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName() + File.separator + result.getName() + ".png";
        String completeImgPath = System.getProperty("user.dir") + File.separator + imgPath; //Adding the SS to the emailable report
        try {
            FileUtils.copyFile(file, new File(imgPath));
            Reporter.log("This is the sample ScreenShot");
            Reporter.log("<a href'"+completeImgPath+"'><img src='"+completeImgPath+"'height='200' width='150'/></a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExtentReport.getTest().fail("Test Failed",
                MediaEntityBuilder.createScreenCaptureFromPath(completeImgPath).build());
        ExtentReport.getTest().fail("Test Failed",
                MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
        ExtentReport.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestStart(ITestResult result) {
        BaseTest base = new BaseTest();
        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
        .assignCategory(base.getPlatform()+ "_" + base.getDeviceName())
        .assignAuthor("Nitish Choudhary");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReport.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
//        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
//        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReport.getExtentReport().flush();
    }
}
