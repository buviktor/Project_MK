package webtest;


import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Selenium Teszt a vizsgaremekhez
 * @author Buda Viktor
 */


public class WebTest {

    /**
    * Új példány a ChromeDriver-ből.
    **/
        
    static WebDriver driver = new ChromeDriver();
    
    static void pageLoading(String page){
        Path sampleFile = Paths.get("../../../../Client/Web/" + page);
        driver.get(sampleFile.toUri().toString());
    }
    
    public static void main(String[] args) throws InterruptedException {
        /**
        * Driver kiválasztása és elérési útja.
        **/
        
        System.setProperty("webdriver.chrome.driver", 
                "./lib/chromedriver_win32/chromedriver.exe");
        
        /**
        * Új példány a ChromeDriver-ből.
        **/
        
        //WebDriver driver = new ChromeDriver();
        
        /**
        * A főoldal betöltése.
        **/
        
        pageLoading("index.html");
        
          
                /**
                * Teljes ablakra vált
                **/
                //driver.manage().window().maximize();
        
        /**
        * Console-ra kiírja az oldal címét(title),
        * beírja a felhasználó nevet és jelszavát,
        * rá nyom a bejelentkezés gombra.
        **/
        
        System.out.println("Page title is: " + driver.getTitle());
        Thread.sleep(1000);
        
        /*
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Poco F1");
        Thread.sleep(2000);
        driver.findElement(By.className("nav-search-submit")).click();
        //driver.findElement(By.linkText("Xiaomi")).click();
        driver.navigate().to("http://google.com");
        Thread.sleep(2000);
        driver.navigate().back();
        Thread.sleep(2000);
        
        /**
        *  
        **/
        String a = driver.getTitle();
        String  b = "Üdvözlet";
        if (a.equalsIgnoreCase(b)) {
            System.out.println("Test Successful");
        }else{
            System.out.println("Test Failure");
        }
        
        /**
        * Kilép a böngészőből
        **/
        driver.quit();
    }
    
}
