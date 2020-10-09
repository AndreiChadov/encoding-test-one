package com.andreichadov;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GetStatusExtendedPage {
    public WebDriver driver;
    public GetStatusExtendedPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver; }

    @FindBy (xpath = "//*[@id=\"page-responses-getstatus-extended\"]/div[2]/div[1]/div/div[2]/div[1]/button[2]")
    private WebElement jsonResponseBtn;

    @FindBy(xpath = "//*[@id=\"page-responses-getstatus-extended\"]/div[2]/div[1]/div/div[2]/div[2]/pre[2]/code/button")
    private WebElement copyJsonResponseBtn;

    public void clickOnJsonResponseBtn(){
        jsonResponseBtn.click();
    }

    public void copyJsonResponseToClipboard(){
        copyJsonResponseBtn.click();
    }
}
