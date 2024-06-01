package demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverWrapper {
    private WebDriver driver;
    private WebDriverWait wait;

    public WebDriverWrapper(){

    WebDriverManager.chromedriver().timeout(30).setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait=new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    public WebDriver getDriver(){
        return driver;
    }

    public void quit(){
        driver.quit();
    }

    public String getCuurentUrl(){
        return driver.getCurrentUrl();
    }

    public WebElement findElement(By locator){
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void clickelement(WebElement element){
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public List<WebElement> findElements(By locator){
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
}
