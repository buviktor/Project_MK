package webtest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.Select;


/**
 * Selenium Teszt a vizsgaremekhez
 * @author Buda Viktor
 */


public class WebTest {
    
    private static WebDriver driver;
    private static List<LogEntry> entries;
    private static String message;
    
    /**
     * pagePath metódus vissza adja a .html fájlok helyét Stringben.
     * 
     * Szöveg típusú paraméterek: page
    **/
    
    private static String pagePath(String page){
        Path sampleFile = Paths.get("../../../../Client/Web/" + page);
        return sampleFile.toUri().toString();
    }
    
    /**
     * login függvény a bejelentkezést biztosítja az oldalra,
     * 1. Rákattint a bejelentkezés fülre
     * 2. Beírja a felhasználónevet
     * 3. Beírja a jelszót
     * 4. Rákattint a bejelentkezés gombra
     * 5. Ellenőrzi, hogy sikerült-e a bejelentkezés
     * 
     * Szöveg típusú paraméter/ek: name, password
    **/
    
    private static void login (String name, String password) {
        try {
            driver.findElement(By.id("home-tab")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("uname")).sendKeys(name);
            driver.findElement(By.id("upassword")).sendKeys(password);
            Thread.sleep(2000);
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("uzenet")).getText();
            if (message.equals("Hibás jelszó!") || message.equals("Hibás felhasználónév!")){
                System.out.println("Login NOK!");
            } else {
                System.out.println("Login OK!");
            }

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
    /**
     * register függvény a regisztrációt biztosítja az oldalra,
     * 1. Rákattint a regisztráció fülre
     * 2. Beírja a felhasználó nevet
     * 3. Beírja a jelszót
     * 4. Beírja az email-t
     * 5. Beírja az irányítószámot
     * 6. Beírja az országot
     * 7. Beírja a megyét
     * 8. Beírja a várost
     * 9. Rákattint a bejelentkezés gombra
     * 10. Ellenőrzi, hogy sikerült-e a regisztáció
     * 
     * Szöveg típusú paraméter/ek: name, password, email, country, county, city
     * Szám típusú paraméter/ek: postcode
    **/
    
    private static void register (String name, String password, String email, int postcode, String country, String county, String city) {
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("runame")).sendKeys(name);
            driver.findElement(By.id("rupassword")).sendKeys(password);
            driver.findElement(By.id("email")).sendKeys(email);
            driver.findElement(By.id("postcode")).sendKeys(Integer.toString(postcode));
            driver.findElement(By.id("country")).sendKeys(country);
            driver.findElement(By.id("county")).sendKeys(county);
            driver.findElement(By.id("city")).sendKeys(city);
            Thread.sleep(2000);
            driver.findElement(By.id("gomb")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("ruzenet")).getText();
            if (!message.equals("Sikeres regisztráció")){
                System.out.println("Register NOK!");
            } else {
                System.out.println("Register OK!");
            }
            
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
    /**
     * newData függvény új adatot vesz fel az adatbázisba,
     * 1. A dátumot megfelől formátummá alakítja
     * 2. Beírja az összeget
     * 3. Beírja a dátumot
     * 4. Kiválasztja a kategóriát
     * 4. Rákattint a hozzáad gombra
     * 5. Ellenőrzi, hogy sikerült-e a bejelentkezés !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 
     * Szöveg típusú paraméter/ek: date
     * Szám típusú paraméter/ek: value, category
    **/
    
    private static void newData(int value, String date, int category) {
        String splitter[] = date.split("-");
        String year = splitter[0];
        String month = splitter[1];
        String day = splitter[2];
        
        try {
            Thread.sleep(1000);
            driver.findElement(By.id("amount")).sendKeys(Integer.toString(value));
            WebElement datePicker = driver.findElement(By.id("dates"));
            new Actions(driver)
                .sendKeys(datePicker, year)
                .sendKeys(Keys.TAB)
                .sendKeys(month)
                .sendKeys(day)
                .perform();
            WebElement selectElement = driver.findElement(By.id("categoriesID"));
            Select selectObject = new Select(selectElement);
            selectObject.selectByIndex(category);
            Thread.sleep(2000);
            driver.findElement(By.id("gomb1")).click();
            
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        
    }
    /**
     * log függvény kiolvassa a státusz választ.
    
    
    private static void log() {
        Boolean status = false;
        String statusText = "\"statusText\":\"OK\"";
        
        entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();      // Lekéri az összes log üzenetett amit generál a driver és listává alakítja.
        for (LogEntry entry : entries) {
            System.out.println(entry.getMessage());
            if (entry.getMessage().contains(statusText)) status = true;      // Megvizsgájla, hogy tartalmazza-e az OK választ üzenet.
        }
        
        if (status) System.out.println("Login OK!");
        else System.out.println("Login NOK!");
    }
    **/
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", 
                "./lib/chromedriver_win32/chromedriver.exe");   // Driver kiválasztása és elérési útja.
        
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
        options.setCapability( "goog:loggingPrefs", logPrefs );
    
        driver = new ChromeDriver(options);  // Új példány a ChromeDriver-ből.

        driver.get(pagePath("index.html"));     // A főoldal betöltése.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);        // Időzítés beállítása
        
        /**
         * Bejelentkezés Teszt Elek fiókkal.
        **/
        
        // login("Teszt Elek", "Elek");
        
        /**
         * Új fiók regisztrálása.
        **/
        
        //register("Édes Anna", "Anna", "anna@gmail.com", 4275, "Magyarország", "Hajdú-Bihar", "Monostorpályi");
        
        /**
         * Új fiók bejelentkezése.
        **/
        
        login("Édes Anna", "Anna");
        
        /**
         * Új fiók adat feltöltése.
        **/
        
        newData(3000, "2022-07-17", 3);
        
        Thread.sleep(4000);
        driver.quit();      // Kilép a böngészőből.
    }
    
}

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
        
        String a = driver.getTitle();
        String  b = "Üdvözlet";
        if (a.equalsIgnoreCase(b)) {
            System.out.println("Test Successful");
        }else{
            System.out.println("Test Failure");
        }
       
        **/

/*
{"message":{
    "method":"Network.responseReceived",
    "params":{
        "frameId":"6B226A5E34F4364EC2E9EFA069C90723",
        "hasExtraInfo":false,
        "loaderId":"A95FBB4D380BD93F6F6B10EA16EF6669",
        "requestId":"A95FBB4D380BD93F6F6B10EA16EF6669",
        "response":{"connectionId":0,
        "connectionReused":false,
        "encodedDataLength":-1,
        "fromDiskCache":false,
        "fromPrefetchCache":false,
        "fromServiceWorker":false,
        "headers":{
            "Content-Type":"text/html",
            "Last-Modified":"Wed, 15 Jun 2022 15:30:03 GMT"},
        "mimeType":"text/html",
        "protocol":"file",
        "remoteIPAddress":"",
        "remotePort":0,
        "securityState":"secure",
        "status":200,
        "statusText":"OK",
        "url":"file:///D:/Documents/Szoftverfejleszto_2021-22/Projects/Project_MK/Client/Web/index.html"},
        "timestamp":2577491.559795,
        "type":"Document"}},
    "webview":"6B226A5E34F4364EC2E9EFA069C90723"}
*/