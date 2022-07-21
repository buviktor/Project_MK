package webtest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.Select;


/**
 * Selenium Teszt a vizsgaremekhez
 * @author Buda Viktor
 */


public class WebTest {
    
    private static WebDriver driver;
    
    private static String message, randomLocation, randomName, randomEmail, password;
    private static Boolean start;
    
    static Random rand = new Random();
    
    static ArrayList<Data> locations = new ArrayList<>();
    static ArrayList<Data> names = new ArrayList<>();
    
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
                start = false;
            } else {
                System.out.println("Login OK!");
            }
            
        } catch (Exception e) {
            System.out.println(e);
            start = false;
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
    
    private static void register (String randName, String randLocation, String randEmail, String randPassword) {
        String postcode, country, county, city;
        
        randomEmail = randEmail;
        password = randPassword;
        
        String[] l = randLocation.split(",");
        postcode = l[0]; country = l[1]; county = l[2]; city = l[3];
                        
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("runame")).sendKeys(randName);
            driver.findElement(By.id("rupassword")).sendKeys(password);
            driver.findElement(By.id("email")).sendKeys(randomEmail);
            driver.findElement(By.id("postcode")).sendKeys(postcode);
            driver.findElement(By.id("country")).sendKeys(country);
            driver.findElement(By.id("county")).sendKeys(county);
            driver.findElement(By.id("city")).sendKeys(city);
            Thread.sleep(2000);
            driver.findElement(By.id("gomb")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("ruzenet")).getText();
            if (!message.equals("Sikeres regisztráció")){
                System.out.println("Register NOK!");
                start = false;
            } else {
                System.out.println("Register OK!");
            }
            
        } catch (Exception e) {
            System.out.println(e);
            start = false;
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
        String dateList[] = date.split("-");
        String year = dateList[0];
        String month = dateList[1];
        String day = dateList[2];
        
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
            Thread.sleep(1000);
            
        } catch (Exception e) {
            System.out.println(e);
            start = false;
        } 
    }
    
    /**
     * randomLocationAndName függvény kiválaszt véletlen szerűen egy nnevet és egy helységet
    **/
    
    private static void randomLocationAndName() {
        randomName = names.get(rand.nextInt(names.size())).getName();
        randomLocation = locations.get(rand.nextInt(locations.size())).getLocation();
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
     * @param args
     * @throws java.lang.InterruptedException
        */
    
    public static void main(String[] args) throws Exception{

        try (Scanner scan = new Scanner(new File("./lib/locations.csv"))){      // locations.csv állomány betöltése és mentése listában.
            while (scan.hasNextLine()) {
                locations.add(new Data(0, scan.nextLine()));
            }
        } catch (IOException l) {
            System.out.println(l);
        }
        
        try (Scanner scan = new Scanner(new File("./lib/names.csv"))){      // names.csv állomány betöltése és mentése listában.
            while (scan.hasNextLine()) {
                names.add(new Data(1, scan.nextLine()));
            }
        } catch (IOException n) {
            System.out.println(n);
        }

        System.setProperty("webdriver.chrome.driver", 
                "./lib/chromedriver_win32/chromedriver.exe");   // Driver kiválasztása és elérési útja.

        ChromeOptions options = new ChromeOptions();        // Böngésző főbb beállításai.
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
        options.setCapability( "goog:loggingPrefs", logPrefs );

        driver = new ChromeDriver(options);  // Új példány a ChromeDriver-ből az előző beállításokkal.

        driver.get(pagePath("index.html"));     // A főoldal betöltése.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);        // Időzítés beállítása.
        
        start = true;       // Indítási érték.
        
        /**
        * Első teszt: Automata teszt 5x.
        **/
        
        randomLocationAndName();        // Első fiók paraméterei.
        for (int i=0; i<5; i++) {
            if (start) register(randomName, randomLocation, Data.getEmail(), Data.getPassword());       // Új fiók regisztrálása.
            if (start) login(randomName, password);     // Új fiók bejelentkezése.
            if (start) newData(3000, "2022-07-17", 3);      // Új fiók adatainak feltöltése.
            if (start) driver.findElement(By.linkText("Kijelentkezés")).click();
            if (start) randomLocationAndName();         // Következő fiók paraméterei.
        }
        
        Thread.sleep(2000);
        driver.quit();      // Kilép a böngészőből.
        System.out.println("Test end");
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

