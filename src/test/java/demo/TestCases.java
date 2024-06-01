package demo;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;


public class TestCases {
    private WebDriverWrapper webDriverWrapper;
    ChromeDriver driver;

    @BeforeClass
    public void setUp(){
        webDriverWrapper = new WebDriverWrapper();
        
    }

    @AfterClass
    public void tearDown(){
        webDriverWrapper.quit();
    }

    
    @Test(priority = 1)
    public  void testCase01() throws StreamWriteException, DatabindException, IOException{
        System.out.println("Start Test case: testCase01");
        webDriverWrapper.getDriver().get("https://www.scrapethissite.com/pages/");
      
        WebDriverWait wait= new WebDriverWait(webDriverWrapper.getDriver(), Duration.ofSeconds(10));
        WebElement Hockey= webDriverWrapper.findElement(By.xpath("(//h3[@class='page-title'])[2]"));
        webDriverWrapper.clickelement(Hockey);

        List<HashMap<String,Object>> data= new ArrayList<>();

       

        for(int page=1; page <=4; page++){
            String url= "https://www.scrapethissite.com/pages/forms/?page=" + page;
            webDriverWrapper.getDriver().get(url);

           
            WebElement table=webDriverWrapper.findElement(By.xpath("//table[@class='table']"));
            System.out.println("table");

            List<WebElement> rows= table.findElements(By.tagName("tr"));
            for(WebElement row: rows){
                List<WebElement> cells=row.findElements(By.tagName("td"));
                if(cells.size() < 3)
                continue;
                String teamName= cells.get(0).getText();
                String year= cells.get(1).getText();
                String winpercent= cells.get(2).getText().replace("%", "").trim();
                double winpercentage= Double.parseDouble(winpercent.replace("%", ""))/100;
                if(winpercentage < 0.40){
                    HashMap<String,Object> rowdata= new HashMap<>();
                    rowdata.put("Epoch Time of Scrape", Instant.now().getEpochSecond());
                    rowdata.put("teamname", teamName);
                    rowdata.put("yearr", year);
                    rowdata.put("win%" , winpercent);
                    data.add(rowdata);

                    
                }
            }
        }

        ObjectMapper mapper= new ObjectMapper();
        File outputfile= new File("output/hockey-team-data.json");
        outputfile.getParentFile().mkdirs();
         mapper.writeValue(outputfile, data);

        Assert.assertTrue(outputfile.exists(), "File does not exist");
        Assert.assertTrue(outputfile.length()> 0 , "File is empty");

        System.out.println("End test case: testCase01");

      }

      @Test(priority = 2)
      public  void testCase02() throws StreamWriteException, DatabindException, IOException, InterruptedException {
          System.out.println("Start Test case02");
          webDriverWrapper.getDriver().get("https://www.scrapethissite.com/pages/");
          
          WebElement oscarLink = webDriverWrapper.findElement(By.xpath("(//h3[@class='page-title'])[3]"));
          webDriverWrapper.clickelement(oscarLink);
  
          List<HashMap<String, Object>> data = new ArrayList<>();
  
        
          List<WebElement> yearsElements = webDriverWrapper.findElements(By.xpath("//a[@class='year-link']"));
          for (WebElement yearElement : yearsElements) {
            String year= yearElement.getText();
            System.out.println(year);
            yearElement.click();
            Thread.sleep(5000);
           
              List<WebElement> rows = webDriverWrapper.findElements(By.xpath("//table[@class='table']//tr[td]"));
           
              int count = 0;
  
              for (WebElement row : rows) {
                  if (count >= 5) 
                  break;
                  List<WebElement> cells = row.findElements(By.tagName("td"));
  
                  if(cells.size() <3 )
                  continue;
                  String title = cells.get(0).getText();
                  String nominations = cells.get(1).getText();
                  String awards = cells.get(2).getText();
                  boolean isWinner = row.getAttribute("class").contains("film-best-picture");
  
                  HashMap<String, Object> rowData = new HashMap<>();
                  rowData.put("Epoch Time of Scrape", Instant.now().getEpochSecond());
                  rowData.put("Year", year);
                  rowData.put("Title", title);
                  rowData.put("Nominations", nominations);
                  rowData.put("Awards", awards);
                  rowData.put("isWinner", isWinner);
  
                  data.add(rowData);
                  count++;
              }

              webDriverWrapper.getDriver().navigate().back();
            yearsElements= webDriverWrapper.findElements(By.xpath("//a[@class='year-link']"));
        }
          ObjectMapper mapper = new ObjectMapper();
      
  
          File outputFile = new File("output/oscar-winner-data.json");
          outputFile.getParentFile().mkdirs();
          mapper.writeValue(outputFile, data);
  
          Assert.assertTrue(outputFile.exists(), "File does not exist");
          Assert.assertTrue(outputFile.length() > 0, "File is empty");
  
          System.out.println("End test case: scrapeOscarWinningFilms");
      }
    }


    