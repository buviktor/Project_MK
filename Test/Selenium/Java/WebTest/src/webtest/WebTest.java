package webtest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    
    private static String message, randomLocation, randomName, password;
    private static LocalTime startTime;
    private static Boolean start;
    
    static Random rand = new Random();
    
    static ArrayList<Data> locations = new ArrayList<>();
    static ArrayList<Data> names = new ArrayList<>();
    static ArrayList<String> uploadData = new ArrayList<>();
    static ArrayList<String> minimalLogs = new ArrayList<>();
    
    
    
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
            minimalLogsAddToList(message);
            
            if (message.equals("Hibás jelszó!") || message.equals("Hibás felhasználónév!")){
                start = false;
            }
            
        } catch (Exception e) {
            minimalLogsAddToList("Sikertelen belépési adat kitöltés!");
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
    
    private static void register (String randLocation, String randEmail, String randPassword) {
        String postcode, country, county, city;
        
        password = randPassword;
        
        String[] l = randLocation.split(",");
        postcode = l[0]; country = l[1]; county = l[2]; city = l[3];
        
        minimalLogsAddToList("Felhasználónév: " + randomName + "; Jelszó: " + password + "; Email: " 
                + randEmail + "; Irányítószám: " + postcode + "; Ország: " + country + "; Megye: " 
                + county + "; Város: " + city);
                        
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("runame")).sendKeys(randomName);
            driver.findElement(By.id("rupassword")).sendKeys(password);
            driver.findElement(By.id("email")).sendKeys(randEmail);
            driver.findElement(By.id("postcode")).sendKeys(postcode);
            driver.findElement(By.id("country")).sendKeys(country);
            driver.findElement(By.id("county")).sendKeys(county);
            driver.findElement(By.id("city")).sendKeys(city);
            Thread.sleep(1000);
            driver.findElement(By.id("gomb")).click();
            Thread.sleep(500);
            
            message = driver.findElement(By.id("ruzenet")).getText();
            minimalLogsAddToList(message + "!");
            
            if (!message.equals("Sikeres regisztráció")){
                start = false;
            }
            
        } catch (Exception e) {
            minimalLogsAddToList("Sikertelen fiók adat kitöltés!");
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
        try {       
            String dateList[] = date.split("-");
            String year = dateList[0], month = dateList[1], day = dateList[2], amount;
            
            if (category == 3 || category == 7) amount = Integer.toString(value);
            else if (value >= 100000) amount = "-" + (value / 50);
            else amount = "-" + (value / 5);
            
            uploadData.add(amount + ", " + date + ", " + Integer.toString(category));     // uploadData lista feltöltése a generált adatokkal, log fájlhoz szükséges.
                        
            Thread.sleep(500);
            driver.findElement(By.id("amount")).sendKeys(amount);
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
            
            minimalLogsAddToList("Összeg (Ft): " + amount + ", Dátum: " + date + ", Kategória: " 
                    + Integer.toString(category));
            
            Thread.sleep(500);
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(500);
            
            message = driver.findElement(By.id("uzenet")).getText();
            
            if (!message.equals("Sikeres hozzáadás")){
                minimalLogsAddToList(message + "!");
                start = false;
            }
            
        } catch (Exception e) {
            minimalLogsAddToList("Sikertelen adat feltöltés!");
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
    
    private static void readFile(String path, ArrayList list, int which) {
        try (Scanner scan = new Scanner(new File(path))){
            while (scan.hasNextLine()) {
                list.add(new Data(which, scan.nextLine()));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void writeFile(ArrayList list, String log) {
        try (PrintWriter writer = new PrintWriter(new File("./logs/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HHmmss")) + log))){
            for (int i=0; i<list.size(); i++) {
                writer.println(list.get(i));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private static void minimalLogsAddToList(String prompt) {
        minimalLogs.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\t" + prompt);
    }
    
    private static void queryFromDatabase() {
        try {
            driver.findElement(By.linkText("Lekérdezés")).click();
            
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            List<WebElement> selectCategory = driver.findElements(By.id("categoriesID"));
            // if (!table.isEmpty()) start = false;     // Tábla ürességének ellenőrzése.
            
            driver.findElement(By.id("gomb2")).click();
            
            Thread.sleep(500);
            table = driver.findElements(By.id("lista"));
            
            
            for (int i = 0; i < table.size(); i++) {
               // System.out.println(table.get(i).getText());
               // System.out.println(uploadData.get(0));    
                
            }
            
            for (int i = 0; i < selectCategory.size(); i++) {
                System.out.println(selectCategory.get(i).getText());
            }
            Thread.sleep(2000);
            
            start = false;
            
            
        } catch (Exception e) {
            System.out.println(e);
            start = false;
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
     * @param args
     * @throws java.lang.InterruptedException
        */
    
    public static void main(String[] args) throws Exception{
        startTime = LocalTime.now();
        minimalLogsAddToList("Teszt indítása");

        minimalLogsAddToList("Szükséges forrás fájlok betöltése....");
        readFile("./lib/locations.csv", locations, 0);      // locations.csv állomány betöltése és mentése listában.
        readFile("./lib/names.csv", names, 1);      // names.csv állomány betöltése és mentése listában.
        minimalLogsAddToList("Forrás fájlok betöltése kész!");
                
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
        * Első teszt: Automata teszt 3x.
        **/
        
        for (int i=0; i<3; i++) {
            minimalLogs.add(" ");       // Üres sor beszúrása.
            /*
            if (start) {
                minimalLogsAddToList((i+1) + " fiók adatainak létrehozása");
                randomLocationAndName();         // A fiók paraméterei.
            }   
            
            if (start) register(randomLocation, Data.getEmail(), Data.getPassword());       // Új fiók regisztrálása.
            if (start) login(randomName, password);     // Új fiók bejelentkezése.
            if (start) minimalLogsAddToList("Adatok feltöltése....");
            
            for (int j=0; j < 50; j++) {
                if (start) {
                    newData(Data.getMoney(), Data.getDate(), Data.getCategory());        // Új fiók adatainak feltöltése.
                    driver.navigate().refresh();
                } 
            }
            */
            if (start) {
                login("Ferencsik Délia", "77Gqh84175");
                for (int j=0; j < 50; j++) {
                    if (start) {
                        newData(Data.getMoney(), Data.getDate(), Data.getCategory());        // Új fiók adatainak feltöltése.
                        driver.navigate().refresh();
                    } 
                }
                queryFromDatabase();
            }
            
            if (start) {
                minimalLogsAddToList("Adatok sikeresen feltöltve!");
                driver.findElement(By.linkText("Kijelentkezés")).click();
                uploadData.clear();
            }
        }
               
        Thread.sleep(2000);
        driver.quit();      // Kilép a böngészőből.
        
        Duration duration = Duration.between(startTime, LocalTime.now());       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        minimalLogsAddToList("A teszt befejeződött! Teljes ideje: " + duration.getSeconds()/60 + " perc " + duration.getSeconds()% 60 + " másodperc.");
        
        writeFile(minimalLogs, "_testlog.txt");       // Ki írja fájlba a minimalis logokat.
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

