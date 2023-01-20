import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.org.apache.bcel.internal.generic.DREM;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FreeCRMTestExtentDemo {

    public WebDriver driver;
    public ExtentTest extentTest;
    public ExtentReports extentReports;


    @BeforeTest
    public void SetExtent() {
        extentReports = new ExtentReports(System.getProperty("user.dir") + "/test-output/ExtentReport.html",true);
        extentReports.addSystemInfo("Username", "Archana");
        extentReports.addSystemInfo("Environment", "QA");
        extentReports.addSystemInfo("Hostname", "archana's mac");
    }

    @AfterTest
    public void endReport() {
        extentReports.flush();
        extentReports.close();
    }

    public static String GetScreenshot(WebDriver driver, String screenshotname) throws IOException {
        String datename = new SimpleDateFormat("yymmddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String Destination = System.getProperty("user.dir") + "/FailedTestCasesScreenshot/" + datename + screenshotname;
        File FinalDestination = new File(Destination);
        FileUtils.copyFile(src, FinalDestination);
        return Destination;
    }

    @BeforeMethod
    public void Setup() {
        System.setProperty("webDriver.chrome.driver", "/Users/archanamudlapur/Documents/ExtentReportDemo/src/test/resources/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://freecrm.com/");
    }

    @Test(priority = 1)
    public void GetTitleTest() {
        extentTest = extentReports.startTest("GetTitleTest");
        String Title = driver.getTitle();
        Assert.assertEquals(Title, "Free CM Software for every business");
        System.out.println(Title);
    }

    @Test(priority = 2)
    public void VerifyLinkTest() {
        extentTest = extentReports.startTest("VerifyLinkTest");
        boolean B = driver.findElement(By.xpath("//a[contains(txt(),'Sign Up')]")).isDisplayed();
        Assert.assertTrue(B);
    }

    @AfterMethod
    public void TearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(LogStatus.FAIL, "Testcase failed is" + result.getName());
            extentTest.log(LogStatus.FAIL, "Testcase failed is" + result.getThrowable());
            String Screenshotpath = FreeCRMTestExtentDemo.GetScreenshot(driver, result.getName());
            extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(Screenshotpath));
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(LogStatus.SKIP, "Testcase skipped is" + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(LogStatus.PASS, "Testcase passed is " + result.getName());
        }
        extentReports.endTest(extentTest);
        driver.quit();
    }
}

