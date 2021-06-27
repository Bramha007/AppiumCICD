package com.qa.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentReport {
    static ExtentReports extentReport;
    final static String filePath = "Extent.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    public synchronized static ExtentReports getExtentReport(){
        if(extentReport == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ExtentSpark.html");
            sparkReporter.config().setDocumentTitle("Appium TDD FrameWork");
            sparkReporter.config().setReportName("Sauce Labs App");
            sparkReporter.config().setTheme(Theme.DARK);
            extentReport = new ExtentReports();
            extentReport.attachReporter(sparkReporter);
        }
        return extentReport;
    }
    public static  synchronized ExtentTest getTest() {
        return  (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }
//    public static  synchronized ExtentTest endTest() {
//        extentReport.removeTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
//    }
    public static synchronized ExtentTest startTest(String testName, String desc){
        ExtentTest test = getExtentReport().createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
}
