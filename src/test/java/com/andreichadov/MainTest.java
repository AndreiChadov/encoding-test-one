package com.andreichadov;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class MainTest {
    public static WebDriver driver;
    public static StartPage startPage;
    public static GetStatusExtendedPage getStatusExtendedPage;

    public static String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                ;
        if (hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException | IOException ex){
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void testResponseJason(){
     

    }

    @BeforeTest
    public static void setup() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        startPage = new StartPage(driver);
        getStatusExtendedPage = new GetStatusExtendedPage(driver);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://api.encoding.com/");
    }

    @Test
    public void test(){
        startPage.clickSearchBtn();
        startPage.search("getStatus");
        startPage.clickGetStatusExtendedBtn();

        WebDriverWait waitField = new WebDriverWait(driver, 20);
        waitField.until(visibilityOfElementLocated(By.xpath("//*[contains(@class, 'CodeTabs-toolbar')]")));

        Assert.assertEquals(driver.getCurrentUrl(),"https://api.encoding.com/reference/responses-getstatus-extended");

        getStatusExtendedPage.clickOnJsonResponseBtn();
        getStatusExtendedPage.copyJsonResponseToClipboard();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test-response.json"))){
            writer.write(getClipboardContents());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public static void close(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }


}
