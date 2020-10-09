package com.andreichadov;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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

    /*Метод для получения буфера обмена в виде строки*/
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


    @BeforeTest
    /*Задаем параметры для Selenium Grid, создаем экземпляры страниц и веб драйвера*/
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
        /*Осуществляем поиск*/
        startPage.clickSearchBtn();
        startPage.search("getStatus");
        startPage.clickGetStatusExtendedBtn();

        /*Ожидаем прогрузки страницы*/
        WebDriverWait waitField = new WebDriverWait(driver, 20);
        waitField.until(visibilityOfElementLocated(By.xpath("//*[contains(@class, 'CodeTabs-toolbar')]")));

        /*Проверяем соответствие URL*/
        Assert.assertEquals(driver.getCurrentUrl(),"https://api.encoding.com/reference/responses-getstatus-extended");

        /*Копируем JSON в буфер обмена*/
        getStatusExtendedPage.clickOnJsonResponseBtn();
        getStatusExtendedPage.copyJsonResponseToClipboard();


        /*Создаем JSONObject с помощью данных из буфера обмена и получем нужные поля*/
        JSONObject userJson = new JSONObject(getClipboardContents());
        JSONArray job = userJson.getJSONObject("response").getJSONArray("job");
        JSONArray format = null;
        String processor = "";
        String status = "";
        for (int i = 0; i < job.length(); ++i) {
            JSONObject rec = job.getJSONObject(i);
            processor = rec.getString("processor");
            format = rec.getJSONArray("format");
        }
        for (int i = 0; i < format.length(); ++i) {
            JSONObject rec = format.getJSONObject(i);
            status = rec.getString("status");
        }

        /*Проверяем соответствия полей*/
        Assert.assertTrue(processor.contains("AMAZON") && processor.contains("RACKSPACE"));
        Assert.assertEquals(status,"[Status]");
    }

    @AfterTest
    public static void close() {
        driver.quit();
    }
}
