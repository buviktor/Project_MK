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
            Thread.sleep(500);
            driver.findElement(By.id("uname")).sendKeys(name);
            driver.findElement(By.id("upassword")).sendKeys(password);
            Thread.sleep(1000);
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("uzenet")).getText();
            minimalLogsAddToList(message);
            
            if (!message.equals("Sikeres bejelentkezés.")){
                minimalLogsAddToList(message + "!");
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
            Thread.sleep(500);
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
            
            List<WebElement> listOfCategory = driver.findElements(By.id("categoriesID"));        // List a kategóriák ArrayList-jéhez.
            ArrayList<String> categories = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            findElementsToArrayList(listOfCategory, categories);
            minimalLogsAddToList("Összeg (Ft): " + amount + ", Dátum: " + date + ", Kategória: " 
                    + categories.get(category) + " " + Integer.toString(category));
            
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
    
    private static void findElementsToArrayList(List<WebElement> table, ArrayList<String> stringList) {         // 192: -1; 
        List<String> strings = new ArrayList<>();
        for(WebElement e : table){
            strings.add(e.getText());
        }
        
        Scanner readList = new Scanner(strings.get(0));
        //readList.nextLine();
        while (readList.hasNextLine()) {
            String nextLine = readList.nextLine().replace(" ", "");
            if (!nextLine.equals("")) stringList.add(nextLine);
        }
    }
    
    private static void tableToArrayList(List<WebElement> table, ArrayList<String> stringList) {
        stringList.clear();
        List<String> strings = new ArrayList<>();
        for(WebElement e : table){
            strings.add(e.getText());
        }
        
        Scanner readList = new Scanner(strings.get(0));
        readList.nextLine();
        while (readList.hasNextLine()) {
            String nextLine = readList.nextLine().replace("...", "");
            if (!nextLine.equals("")) stringList.add(nextLine);
        }
    }
    
    private static void controlForQuery(ArrayList<String> allQuery, int hit, boolean next, String message) {
        if (!message.equals("Nincs ilyen adat!")) {
            minimalLogsAddToList("Talált adat: " + allQuery.size() + ", mért adat: " + hit + ".");
            if (hit != allQuery.size()) {
                next = false;
                start = false;
                minimalLogsAddToList("Nem egyezik a talált és a mért adat!");
            }
        }else minimalLogsAddToList(message);  
    }
        
    private static void queryFromDatabase() {
        int hit = 0;
        boolean next = true;
        String selected = " ", allDay = "Összes nap";
        
        try {
            driver.findElement(By.linkText("Lekérdezés")).click();
            Thread.sleep(500);
            
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            if (!table.get(0).getText().equals("")) start = false;     // Tábla ürességének ellenőrzése.
            else {
                Select selectObject;
                List<WebElement> selectCost = driver.findElements(By.id("cost"));       // Lekérdezés menü: Összegek listázása.
                List<WebElement> selectCategory = driver.findElements(By.id("categoriesID"));       // Lekérdezés menü: Kategóriák listázása.
                List<WebElement> selectYear = driver.findElements(By.id("datesy"));       // Lekérdezés menü: Évek listázása.
                List<WebElement> selectMonth = driver.findElements(By.id("datesm"));       // Lekérdezés menü: Hónapok listázása.
                
                
                ArrayList<String> costArrayList = new ArrayList<>();        // ArrayList a összegek vizsgálatához.
                ArrayList<String> categoryArrayList = new ArrayList<>();        // ArrayList a kategóriák vizsgálatához.
                ArrayList<String> yearArrayList = new ArrayList<>();        // ArrayList a évek vizsgálatához.
                ArrayList<String> monthArrayList = new ArrayList<>();        // ArrayList a hónapok vizsgálatához.
                
                ArrayList<String> allQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
                
                findElementsToArrayList(selectCost, costArrayList);     // Összeg List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectCategory, categoryArrayList);     // Kategória List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectYear, yearArrayList);     // Év List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectMonth, monthArrayList);     // Hónap List-t átalakítjuk ArrayList-é.

                /**
                 * Összes adat lekérdezése.
                **/
                
                driver.findElement(By.id("gomb2")).click();
                Thread.sleep(500);
                
                table = driver.findElements(By.id("lista"));
                tableToArrayList(table, allQuery);
                               
                if (allQuery.size() != uploadData.size()) {
                    next = false;
                    start = false;
                    minimalLogsAddToList("Az összes feltöltött adat és az összes lekérdezett adat nem egyezik meg!");
                }
                else minimalLogsAddToList("Az összes feltöltött adat és az összes lekérdezett adat megegyezik!");
                
                /**
                 * Adatok lekérdezése és ellenőrzése.
                 * 
                 * for(categoryNumber), selectOption(a) : 
                 *      - 0 : Élelmiszer.
                 * 
                 * costNumber :
                 *      - 0 : Kiadás.
                 *      - 1 : Bevétel.
                 *      - 2 : Egyéb összeg.
                **/
                
                if (next) {
                    for (int categoryNumber = 1; categoryNumber < categoryArrayList.size(); categoryNumber++) {
                        if (next) {     // categoryID id mező.
                            hit = 0;
                            
                            selectObject = new Select(selectCategory.get(0));
                            selectObject.selectByIndex(categoryNumber);
                            selectObject = new Select(selectCost.get(0));
                            selectObject.selectByIndex(0);
                            selectObject = new Select(selectYear.get(0));
                            selectObject.selectByIndex(0);
                            selectObject = new Select(selectMonth.get(0));
                            selectObject.selectByIndex(0);
                            if (!driver.findElement(By.id("napok")).isSelected()) {
                                driver.findElement(By.id("napok")).click();
                                Thread.sleep(500);
                                allDay = "Összes nap";
                            }
                            selected = costArrayList.get(0) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                    + yearArrayList.get(0) + ", " + monthArrayList.get(0) + ", " + allDay;
                            
                            driver.findElement(By.id("gomb2")).click();
                            table = driver.findElements(By.id("lista"));
                            tableToArrayList(table, allQuery);
                            message = driver.findElement(By.id("uzenet")).getText();
                            
                            if (!message.equals("Nincs ilyen adat!")) {
                                for (int j = 0; j < allQuery.size(); j++) {
                                    for (int k = 0; k < uploadData.size(); k++) {
                                        String[] s = uploadData.get(k).split(", ");
                                        if (s[2].equals(String.valueOf(categoryNumber)) && allQuery.get(j).contains(s[0])) {
                                            hit++;
                                            minimalLogsAddToList(allQuery.get(j));
                                        }
                                    }
                                }
                            }
                            
                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.

                            if (next) {     // cost id mező.
                                hit = 0;
                                for (int costNumber = 1; costNumber < costArrayList.size(); costNumber++) {
                                    selectObject = new Select(selectCost.get(0));
                                    selectObject.selectByIndex(costNumber);
                                    selectObject = new Select(selectYear.get(0));
                                    selectObject.selectByIndex(0);
                                    selectObject = new Select(selectMonth.get(0));
                                    selectObject.selectByIndex(0);
                                    if (!driver.findElement(By.id("napok")).isSelected()) {
                                        driver.findElement(By.id("napok")).click();
                                        Thread.sleep(500);
                                        allDay = "Összes nap";
                                    }
                                    selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                        + yearArrayList.get(0) + ", " + monthArrayList.get(0) + ", " + allDay;

                                    if (costNumber <= 2) {
                                        driver.findElement(By.id("gomb2")).click();
                                        table = driver.findElements(By.id("lista"));
                                        tableToArrayList(table, allQuery);
                                        message = driver.findElement(By.id("uzenet")).getText();
                                        
                                        if (!message.equals("Nincs ilyen adat!")) {
                                            for (int j = 0; j < allQuery.size(); j++) {
                                                for (int k = 0; k < uploadData.size(); k++) {
                                                    String[] s = uploadData.get(k).split(", ");
                                                    if (allQuery.get(j).contains(s[0])) {
                                                        hit++;
                                                        minimalLogsAddToList(allQuery.get(j));
                                                    }
                                                }
                                            }
                                        }

                                        controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                        
                                        if (next) {     // datesy id mező.
                                            hit = 0;
                                            for (int yearNumber = 1; yearNumber < yearArrayList.size(); yearNumber++) {
                                                selectObject = new Select(selectYear.get(0));
                                                selectObject.selectByIndex(yearNumber);
                                                selectObject = new Select(selectMonth.get(0));
                                                selectObject.selectByIndex(0);
                                                if (!driver.findElement(By.id("napok")).isSelected()) {
                                                    driver.findElement(By.id("napok")).click();
                                                    Thread.sleep(500);
                                                    allDay = "Összes nap";
                                                }
                                                selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                    + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(0) + ", " + allDay;
                                                
                                                driver.findElement(By.id("gomb2")).click();
                                                table = driver.findElements(By.id("lista"));
                                                tableToArrayList(table, allQuery);
                                                message = driver.findElement(By.id("uzenet")).getText();

                                                if (!message.equals("Nincs ilyen adat!")) {
                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                            String[] s = uploadData.get(k).split(", ");
                                                            if (allQuery.get(j).contains(yearArrayList.get(yearNumber)) && allQuery.get(j).contains(s[1])) {
                                                                hit++;
                                                                minimalLogsAddToList(allQuery.get(j));
                                                            }
                                                        }
                                                    }
                                                }
                                                
                                                controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                
                                                if (next) {     // datesm id mező.
                                                    hit = 0;
                                                    for (int monthNumber = 1; monthNumber < monthArrayList.size(); monthNumber++) {
                                                        selectObject = new Select(selectMonth.get(0));
                                                        selectObject.selectByIndex(monthNumber);
                                                        if (!driver.findElement(By.id("napok")).isSelected()) {
                                                            driver.findElement(By.id("napok")).click();
                                                            Thread.sleep(500);
                                                            allDay = "Összes nap";
                                                        }
                                                        selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                            + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                            + allDay;

                                                        driver.findElement(By.id("gomb2")).click();
                                                        table = driver.findElements(By.id("lista"));
                                                        tableToArrayList(table, allQuery);
                                                        message = driver.findElement(By.id("uzenet")).getText();
                                                        
                                                        if (!message.equals("Nincs ilyen adat!")) {
                                                            for (int j = 0; j < allQuery.size(); j++) {
                                                                for (int k = 0; k < uploadData.size(); k++) {
                                                                    String[] s = uploadData.get(k).split(", ");
                                                                    String monthNum = "-" + (monthNumber) + "-";
                                                                    if ((monthNumber) < 10) monthNum = "-0" + (monthNumber) + "-";
                                                                    if (allQuery.get(j).contains(String.valueOf(monthNum)) && allQuery.get(j).contains(s[1])) {
                                                                        hit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                        
                                                        if (next) {     // dated id mező.
                                                            hit = 0;
                                                            driver.findElement(By.id("napok")).click();
                                                            Thread.sleep(500);
                                                            String day = "1";
                                                            if (Integer.parseInt(day) < 10) {
                                                                day = "0" + day;
                                                            }
                                                            selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                            + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                            + day;
                                                            
                                                            driver.findElement(By.id("gomb2")).click();
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                            message = driver.findElement(By.id("uzenet")).getText();
                                                            
                                                            if (!message.equals("Nincs ilyen adat!")) {
                                                                for (int j = 0; j < allQuery.size(); j++) {
                                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                                        String[] s = uploadData.get(k).split(", ");
                                                                        if (allQuery.get(j).contains(String.valueOf("-01")) && allQuery.get(j).contains(s[1])) {
                                                                            hit++;
                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                            
                                                            switch ((monthNumber)) {
                                                                case 1: case 3: case 5: case 7: case 8: case 10: case 12:       // 31 napos hónapok
                                                                    if (next) {
                                                                        for (int dayNumber = 2; dayNumber < 32; dayNumber++) {
                                                                            hit = 0;
                                                                            driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                            if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                            }
                                                                            selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                            + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                            + day;
                                                                            
                                                                            driver.findElement(By.id("gomb2")).click();
                                                                            table = driver.findElements(By.id("lista"));
                                                                            tableToArrayList(table, allQuery);
                                                                            message = driver.findElement(By.id("uzenet")).getText();

                                                                            if (!message.equals("Nincs ilyen adat!")) {
                                                                                for (int j = 0; j < allQuery.size(); j++) {
                                                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                                                        String[] s = uploadData.get(k).split(", ");
                                                                                        String dayNum = "-" + (dayNumber);
                                                                                        if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                        if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                            hit++;
                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                        }
                                                                    }
                                                                    break;
                                                                case 2:        // 28 napos hónapok
                                                                    if (next) {
                                                                        for (int dayNumber = 2; dayNumber < 29; dayNumber++) {
                                                                            hit = 0;
                                                                            driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                            if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                            }
                                                                            selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                            + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                            + day;
                                                                            
                                                                            driver.findElement(By.id("gomb2")).click();
                                                                            table = driver.findElements(By.id("lista"));
                                                                            tableToArrayList(table, allQuery);
                                                                            message = driver.findElement(By.id("uzenet")).getText();

                                                                            if (!message.equals("Nincs ilyen adat!")) {
                                                                                for (int j = 0; j < allQuery.size(); j++) {
                                                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                                                        String[] s = uploadData.get(k).split(", ");
                                                                                        String dayNum = "-" + (dayNumber);
                                                                                        if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                        if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                            hit++;
                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                        }
                                                                    }
                                                                    break;
                                                                case 4: case 6: case 9: case 11:       // 30 napos hónapok
                                                                    if (next) {
                                                                        for (int dayNumber = 2; dayNumber < 31; dayNumber++) {
                                                                            hit = 0;
                                                                            driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                            if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                            }
                                                                            selected = costArrayList.get(costNumber) + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                            + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                            + day;
                                                                            
                                                                            driver.findElement(By.id("gomb2")).click();
                                                                            table = driver.findElements(By.id("lista"));
                                                                            tableToArrayList(table, allQuery);
                                                                            message = driver.findElement(By.id("uzenet")).getText();

                                                                            if (!message.equals("Nincs ilyen adat!")) {
                                                                                for (int j = 0; j < allQuery.size(); j++) {
                                                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                                                        String[] s = uploadData.get(k).split(", ");
                                                                                        String dayNum = "-" + (dayNumber);
                                                                                        if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                        if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                            hit++;
                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                        }
                                                                    }
                                                                    break; 
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                    } else {
                                        for (int i = 0; i < 3; i++) {
                                            String randomData = uploadData.get(rand.nextInt(uploadData.size()));
                                            String[] randomAmount = randomData.split(", ");
                                            driver.findElement(By.id("szam")).clear();
                                            driver.findElement(By.id("szam")).sendKeys(randomAmount[0]);
                                            selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", "+ categoryArrayList.get(categoryNumber) + ", " 
                                                + yearArrayList.get(0) + ", " + monthArrayList.get(0) + ", " + allDay;
                                            
                                            driver.findElement(By.id("gomb2")).click();
                                            table = driver.findElements(By.id("lista"));
                                            tableToArrayList(table, allQuery);
                                            message = driver.findElement(By.id("uzenet")).getText();
                                            
                                            if (!message.equals("Nincs ilyen adat!")) {
                                                for (int j = 0; j < allQuery.size(); j++) {
                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                        String[] s = uploadData.get(k).split(", ");
                                                        if (allQuery.get(j).contains(s[0])) {
                                                            hit++;
                                                            minimalLogsAddToList(allQuery.get(j));
                                                        }
                                                    }
                                                }
                                            }

                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                            
                                            if (next) {     // datesy id mező.
                                                hit = 0;
                                                for (int yearNumber = 1; yearNumber < yearArrayList.size(); yearNumber++) {
                                                    selectObject = new Select(selectYear.get(0));
                                                    selectObject.selectByIndex(yearNumber);
                                                    selectObject = new Select(selectMonth.get(0));
                                                    selectObject.selectByIndex(0);
                                                    if (!driver.findElement(By.id("napok")).isSelected()) {
                                                        driver.findElement(By.id("napok")).click();
                                                        Thread.sleep(500);
                                                        allDay = "Összes nap";
                                                    }
                                                    selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", "+ categoryArrayList.get(categoryNumber) + ", " 
                                                        + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(0) + ", " 
                                                        + allDay;

                                                    driver.findElement(By.id("gomb2")).click();
                                                    table = driver.findElements(By.id("lista"));
                                                    tableToArrayList(table, allQuery);
                                                    message = driver.findElement(By.id("uzenet")).getText();

                                                    if (!message.equals("Nincs ilyen adat!")) {
                                                        for (int j = 0; j < allQuery.size(); j++) {
                                                            for (int k = 0; k < uploadData.size(); k++) {
                                                                String[] s = uploadData.get(k).split(", ");
                                                                if (allQuery.get(j).contains(yearArrayList.get(yearNumber)) && allQuery.get(j).contains(s[1])) {
                                                                    hit++;
                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                }
                                                            }
                                                        }
                                                    }

                                                    controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.

                                                    if (next) {     // datesm id mező.
                                                        hit = 0;
                                                        for (int monthNumber = 1; monthNumber < monthArrayList.size(); monthNumber++) {
                                                            selectObject = new Select(selectMonth.get(0));
                                                            selectObject.selectByIndex(monthNumber);
                                                            if (!driver.findElement(By.id("napok")).isSelected()) {
                                                                driver.findElement(By.id("napok")).click();
                                                                Thread.sleep(500);
                                                                allDay = "Összes nap";
                                                            }
                                                            selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                + allDay;

                                                            driver.findElement(By.id("gomb2")).click();
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                            message = driver.findElement(By.id("uzenet")).getText();

                                                            if (!message.equals("Nincs ilyen adat!")) {
                                                                for (int j = 0; j < allQuery.size(); j++) {
                                                                    for (int k = 0; k < uploadData.size(); k++) {
                                                                        String[] s = uploadData.get(k).split(", ");
                                                                        String monthNum = "-" + (monthNumber) + "-";
                                                                        if ((monthNumber) < 10) monthNum = "-0" + (monthNumber) + "-";
                                                                        if (allQuery.get(j).contains(String.valueOf(monthNum)) && allQuery.get(j).contains(s[1])) {
                                                                            hit++;
                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.

                                                            if (next) {     // dated id mező.
                                                                hit = 0;
                                                                driver.findElement(By.id("napok")).click();
                                                                Thread.sleep(500);
                                                                String day = "1";
                                                                if (Integer.parseInt(day) < 10) {
                                                                    day = "0" + day;
                                                                }
                                                                selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                + day;
                                                            
                                                                driver.findElement(By.id("gomb2")).click();
                                                                table = driver.findElements(By.id("lista"));
                                                                tableToArrayList(table, allQuery);
                                                                message = driver.findElement(By.id("uzenet")).getText();
                                                                
                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                            String[] s = uploadData.get(k).split(", ");
                                                                            if (allQuery.get(j).contains(String.valueOf("-01")) && allQuery.get(j).contains(s[1])) {
                                                                                hit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.

                                                                switch ((monthNumber)) {
                                                                    case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                                                                        if (next) {
                                                                            for (int dayNumber = 2; dayNumber < 32; dayNumber++) {
                                                                                hit = 0;
                                                                                driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                                if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                                }
                                                                                selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                                + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                                + day;
                                                                                
                                                                                driver.findElement(By.id("gomb2")).click();
                                                                                table = driver.findElements(By.id("lista"));
                                                                                tableToArrayList(table, allQuery);
                                                                                message = driver.findElement(By.id("uzenet")).getText();

                                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                                            String[] s = uploadData.get(k).split(", ");
                                                                                            String dayNum = "-" + (dayNumber);
                                                                                            if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                            if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                                hit++;
                                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                            }
                                                                        }
                                                                        break;
                                                                    case 2: 
                                                                        if (next) {
                                                                            for (int dayNumber = 2; dayNumber < 29; dayNumber++) {
                                                                                hit = 0;
                                                                                driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                                if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                                }
                                                                                selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                                + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                                + day;
                                                                                
                                                                                driver.findElement(By.id("gomb2")).click();
                                                                                table = driver.findElements(By.id("lista"));
                                                                                tableToArrayList(table, allQuery);
                                                                                message = driver.findElement(By.id("uzenet")).getText();

                                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                                            String[] s = uploadData.get(k).split(", ");
                                                                                            String dayNum = "-" + (dayNumber);
                                                                                            if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                            if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                                hit++;
                                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                            }
                                                                        }
                                                                        break;
                                                                    case 4: case 6: case 9: case 11:
                                                                        if (next) {
                                                                            for (int dayNumber = 2; dayNumber < 31; dayNumber++) {
                                                                                hit = 0;
                                                                                driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                                if (dayNumber < 10) {
                                                                                day = "0" + dayNumber;
                                                                                }
                                                                                selected = costArrayList.get(costNumber) + ": " +randomAmount[0] + ", " + categoryArrayList.get(categoryNumber) + ", " 
                                                                                + yearArrayList.get(yearNumber) + ", " + monthArrayList.get(monthNumber) + ", " 
                                                                                + day;
                                                                            
                                                                                driver.findElement(By.id("gomb2")).click();
                                                                                table = driver.findElements(By.id("lista"));
                                                                                tableToArrayList(table, allQuery);
                                                                                message = driver.findElement(By.id("uzenet")).getText();

                                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                                            String[] s = uploadData.get(k).split(", ");
                                                                                            String dayNum = "-" + (dayNumber);
                                                                                            if ((dayNumber) < 10) dayNum = "-0" + (dayNumber);
                                                                                            if (allQuery.get(j).contains(String.valueOf(dayNum)) && allQuery.get(j).contains(s[1])) {
                                                                                                hit++;
                                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                controlForQuery(allQuery, hit, next, message);       // Mért adat ellenőrzése.
                                                                            }
                                                                        }
                                                                        break; 
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
 
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
        LocalTime startTime = LocalTime.now();
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
        
        for (int i=0; i<1; i++) {
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
        
        LocalTime endTime = LocalTime.now();
        Duration duration = Duration.between(startTime, endTime);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
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

