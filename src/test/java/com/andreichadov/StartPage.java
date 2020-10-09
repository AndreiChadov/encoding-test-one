package com.andreichadov;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class StartPage {
    public WebDriver driver;
    public StartPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver; }

    @FindBy(xpath = "//*[@id=\"hub-search\"]/button")
    private WebElement searchBtn;

    @FindBy(xpath = "//*[contains(@class, 'Input Input_md _3Ew3MsI12QPjxH_YVws8-j')]")
    private WebElement searchField;

    @FindBy(xpath = "//*[contains(@href, '/reference-link/responses-getstatus-extended')]")
    private WebElement getStatusExtendedBtn;

    public void clickSearchBtn(){
        WebDriverWait waitBtn = new WebDriverWait(driver, 20);
        waitBtn.until(visibilityOfElementLocated(By.xpath("//*[@id=\"hub-search\"]/button")));
        searchBtn.click();
    }

    public void search(String request){
        WebDriverWait waitField = new WebDriverWait(driver, 20);
        waitField.until(visibilityOfElementLocated(By.xpath("//*[contains(@class, 'Input Input_md _3Ew3MsI12QPjxH_YVws8-j')]")));
        searchField.sendKeys(request);
    }

    public void clickGetStatusExtendedBtn(){
        getStatusExtendedBtn.click();
    }
}
