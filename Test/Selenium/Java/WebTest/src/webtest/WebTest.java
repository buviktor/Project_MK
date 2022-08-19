package webtest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;


/**
 * Selenium Teszt a vizsgaremekhez
 * @author Buda Viktor
 */


public class WebTest {
    
    private static WebDriver driver;
    
    private static String message, randomLocation, randomName, password, email;
    private static Boolean start, next = true;
    
    static Random rand = new Random();
    
    static ArrayList<Data> locations = new ArrayList<>();
    static ArrayList<Data> names = new ArrayList<>();
    static ArrayList<String> uploadData = new ArrayList<>();
    static ArrayList<String> minimalLogs = new ArrayList<>();
    static ArrayList<String> uploadDataSorting = new ArrayList<>();       // ArrayList a sorrend megállapításához.
    static ArrayList<Integer> uploadDataSortingInt = new ArrayList<>();       // ArrayList a sorrend megállapításához.
    
    static Wait<WebDriver> wait;
    
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
            Thread.sleep(250);
            driver.findElement(By.id("uname")).sendKeys(name);
            driver.findElement(By.id("upassword")).sendKeys(password);
            Thread.sleep(250);
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(250);
            
            message = driver.findElement(By.id("uzenet")).getText();
                        
            if (!message.equals("Sikeres bejelentkezés.")){
                minimalLogsAddToList("Sikertelen bejelentkezés! Rendszer üzenet: " + message);
                start = false;
            }
            else minimalLogsAddToList(message);
            
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
        email = randEmail;
        
        String[] l = randLocation.split(",");
        postcode = l[0]; country = l[1]; county = l[2]; city = l[3];
        
        minimalLogsAddToList("Felhasználónév: " + randomName);
        minimalLogsAddToList("Jelszó: " + password);
        minimalLogsAddToList("Email: " + randEmail);
        minimalLogsAddToList("Irányítószám: " + postcode);
        minimalLogsAddToList("Ország: " + country);
        minimalLogsAddToList("Megye: " + county);
        minimalLogsAddToList("Város: " + city);
                        
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(500);
            driver.findElement(By.id("runame")).sendKeys(randomName);
            driver.findElement(By.id("rupassword")).sendKeys(password);
            driver.findElement(By.id("email")).sendKeys(email);
            driver.findElement(By.id("postcode")).sendKeys(postcode);
            driver.findElement(By.id("country")).sendKeys(country);
            driver.findElement(By.id("county")).sendKeys(county);
            driver.findElement(By.id("city")).sendKeys(city);
            Thread.sleep(1000);
            driver.findElement(By.id("gomb")).click();
            Thread.sleep(500);
            
            message = driver.findElement(By.id("ruzenet")).getText();
                        
            if (!message.equals("Sikeres regisztráció")){
                minimalLogsAddToList("Sikertelen regisztráció! Rendszer üzenet: " + message);
                start = false;
            } else minimalLogsAddToList(message + "!");
            
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
                    
            Thread.sleep(250);
            driver.findElement(By.id("amount")).sendKeys(amount);
            Thread.sleep(100);
            WebElement datePicker = driver.findElement(By.id("dates"));
            new Actions(driver)
                .sendKeys(datePicker, year)
                .sendKeys(Keys.TAB)
                .sendKeys(month)
                .sendKeys(day)
                .perform();
            Thread.sleep(100);
            WebElement selectElement = driver.findElement(By.id("categoriesID"));
            Select selectObject = new Select(selectElement);
            selectObject.selectByIndex(category);
            
            Thread.sleep(250);
            List<WebElement> listOfCategory = driver.findElements(By.id("categoriesID"));        // List a kategóriák ArrayList-jéhez.
            ArrayList<String> categories = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            findElementsToArrayList(listOfCategory, categories);
            minimalLogsAddToList("Összeg (Ft): " + String.join("", Collections.nCopies(6-amount.length(), " ")) + amount + ",  Dátum: " + date 
                    + ",  Kategória: " + categories.get(category) + " (" + Integer.toString(category) + ")");
            
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(250);
            
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
        
        if(randomName.length() != 0 && randomLocation.length() != 0) minimalLogsAddToList("Fiók adatainak létrehozása kész!");
        else {
            minimalLogsAddToList("Fiók adatainak létrehozása sikertelen!");
            start = false;
        }
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
        minimalLogs.add(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\t" + prompt);
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
    
    private static void controlForQuery(ArrayList<String> allQuery, int hit, String message) {
        if (!message.equals("Nincs ilyen adat!")) {
            if (hit != allQuery.size()) {
                List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
                tableToArrayList(table, allQuery);
                
                if (hit != allQuery.size()) {
                    next = false;
                    start = false;
                    minimalLogsAddToList("Talált adat: " + allQuery.size() + ", mért adat: " + hit + ".");
                    minimalLogsAddToList("Nem egyezik a talált és a mért adat!"); 
                } else minimalLogsAddToList("Talált adat: " + allQuery.size() + ", mért adat: " + hit + ".");
            } else minimalLogsAddToList("Talált adat: " + allQuery.size() + ", mért adat: " + hit + ".");
        }else minimalLogsAddToList(message);  
    }
    
    private static void sorting(int value, boolean sort, ArrayList category) {
        uploadDataSorting.clear();
        uploadDataSortingInt.clear();
        ArrayList<String> supportCategoryList = new ArrayList<>();
        supportCategoryList.clear();
        
        for (int i = 0; i < uploadData.size(); i++) {
            String[] s = uploadData.get(i).split(", ");
            if (value == 0) {
                uploadDataSorting.add(s[1]);
            }
            if (value == 1) {
                uploadDataSortingInt.add(Integer.parseInt(s[0]));
            }
            if (value == 2) {
                String categoryName = String.valueOf(category.get(Integer.parseInt(s[2])));
                if (categoryName.equals("Egyébjuttatások")) categoryName = categoryName.replace("Egyébjuttatások", "Egyéb juttatások");
                if (categoryName.equals("Egyébkiadások")) categoryName = categoryName.replace("Egyébkiadások", "Egyéb kiadások");
                if (categoryName.equals("Élelmiszer")) categoryName = categoryName.replace("Élelmiszer", "Elelmiszer");
                supportCategoryList.add(categoryName);
            }
        }
        
        if (sort) {
            switch (value) {
                case 1:
                    Collections.sort(uploadDataSortingInt);
                    break;
                case 2:
                    Collections.sort(supportCategoryList);
                    for (String l : supportCategoryList) {
                        if (l.equals("Elelmiszer")) {
                            l = l.replace("Elelmiszer", "Élelmiszer");
                        }
                        uploadDataSorting.add(l);
                    }
                    break;
                default:
                    Collections.sort(uploadDataSorting);
                    break;
            }
        }
        else {
            switch (value) {
                case 1:
                    Collections.sort(uploadDataSortingInt, Collections.reverseOrder());
                    break;
                case 2:
                    Collections.sort(supportCategoryList, Collections.reverseOrder());
                    for (String l : supportCategoryList) {
                        if (l.equals("Elelmiszer")) {
                            l = l.replace("Elelmiszer", "Élelmiszer");
                        }
                        uploadDataSorting.add(l);
                    }
                    break;
                default:
                    Collections.sort(uploadDataSorting, Collections.reverseOrder());
                    break;
            }
        }
    }
    
    private static void querySelected(String cost, String category, String year, String month, String day) {
        String date = "";
        if (cost.equals("Mindenösszeg")) cost = cost.replace("Mindenösszeg", "Minden összeg");
        if (cost.equals("Egyébösszeg")) cost = cost.replace("Egyébösszeg", "Egyéb összeg");
        if (category.equals("Egyébjuttatások")) category = category.replace("Egyébjuttatások", "Egyéb juttatások");
        if (category.equals("Egyébkiadások")) category = category.replace("Egyébkiadások", "Egyéb kiadások");
        if (year.equals("Összesév")) {
            year = year.replace("Összesév", "Összes év");
            date = year;
        }
        if (month.equals("Összeshónap")) {
            month = month.replace("Összeshónap", "Összes hónap");
            date = year;
        }
        if (!month.equals("Összes hónap") && day.equals("Összes nap")) {
            date = year + "-" + month;
        } else if (!day.equals("Összes nap") && !month.equals("Összes hónap")) {
            date = year + "-" + month + "-" + day;
        } else date = year;
        
        minimalLogs.add(" ");
        minimalLogsAddToList("Kiválasztva: " + cost + ", " + category + ", " + date);
    }
        
    private static void queryFromDatabase() {
        int hit = 0;
        next = true;
        String allDay = "Összes nap";
                
        try {
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            if (!table.get(0).getText().equals("")) {     // Tábla ürességének ellenőrzése.
                next = false;
                start = false;
            }
            else {
                Select selectObject;
                List<WebElement> selectCost = driver.findElements(By.id("cost"));       // Lekérdezés menü: Összegek listázása.
                List<WebElement> selectCategory = driver.findElements(By.id("categoriesID"));       // Lekérdezés menü: Kategóriák listázása.
                List<WebElement> selectYear = driver.findElements(By.id("datesy"));       // Lekérdezés menü: Évek listázása.
                List<WebElement> selectMonth = driver.findElements(By.id("datesm"));       // Lekérdezés menü: Hónapok listázása.
                List<WebElement> selectSort = driver.findElements(By.id("order"));       // Lekérdezés menü: Listázás szerint listázása.
                List<WebElement> selectSorting = driver.findElements(By.id("desc"));       // Lekérdezés menü: Sorrend listázása.
                
                ArrayList<String> costArrayList = new ArrayList<>();        // ArrayList a összegek vizsgálatához.
                ArrayList<String> categoryArrayList = new ArrayList<>();        // ArrayList a kategóriák vizsgálatához.
                ArrayList<String> yearArrayList = new ArrayList<>();        // ArrayList a évek vizsgálatához.
                ArrayList<String> monthArrayList = new ArrayList<>();        // ArrayList a hónapok vizsgálatához.
                ArrayList<String> sortArrayList = new ArrayList<>();        // ArrayList a listázás szerint vizsgálatához.
                ArrayList<String> sortingArrayList = new ArrayList<>();        // ArrayList a sorrend vizsgálatához.
                
                ArrayList<String> allQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
                
                findElementsToArrayList(selectCost, costArrayList);     // Összeg List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectCategory, categoryArrayList);     // Kategória List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectYear, yearArrayList);     // Év List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectMonth, monthArrayList);     // Hónap List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectSort, sortArrayList);     // Listázás szerint List-t átalakítjuk ArrayList-é.
                findElementsToArrayList(selectSorting, sortingArrayList);     // Sorrend List-t átalakítjuk ArrayList-é.

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
                 * Sorrend teszt.
                **/
                
                if (next) {
                    selectObject = new Select(selectSort.get(0));       // Dátum szerint csökkenő.
                    selectObject.selectByIndex(0);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(0, false, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(uploadDataSorting.get(i))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(0) + " szerinti " + sortingArrayList.get(0) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(0) + " szerinti " + sortingArrayList.get(0) + " sorrend megegyezett!");
                    }
                }
                
                if (next) {
                    selectObject = new Select(selectSorting.get(0));       // Dátum szerint növekvő.
                    selectObject.selectByIndex(1);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(0, true, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(uploadDataSorting.get(i))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(0) + " szerinti " + sortingArrayList.get(1) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(0) + " szerinti " + sortingArrayList.get(1) + " sorrend megegyezett!");
                    }
                }
                
                if (next) {
                    selectObject = new Select(selectSort.get(0));       // Összeg szerint növekvő.
                    selectObject.selectByIndex(1);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(1, true, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(String.valueOf(uploadDataSortingInt.get(i)))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(1) + " szerinti " + sortingArrayList.get(1) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(1) + " szerinti " + sortingArrayList.get(1) + " sorrend megegyezett!");
                    }
                }
                
                if (next) {
                    selectObject = new Select(selectSorting.get(0));       // Összeg szerint csökkenő.
                    selectObject.selectByIndex(0);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(1, false, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(String.valueOf(uploadDataSortingInt.get(i)))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(1) + " szerinti " + sortingArrayList.get(0) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(1) + " szerinti " + sortingArrayList.get(0) + " sorrend megegyezett!");
                    }
                }
                
                if (next) {
                    selectObject = new Select(selectSort.get(0));       // kategória szerint csökkenő.
                    selectObject.selectByIndex(2);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(2, false, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(uploadDataSorting.get(i))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(2) + " szerinti " + sortingArrayList.get(0) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(2) + " szerinti " + sortingArrayList.get(0) + " sorrend megegyezett!");
                    }
                }
                
                if (next) {
                    selectObject = new Select(selectSorting.get(0));       // kategória szerint növekvő.
                    selectObject.selectByIndex(1);
                    driver.findElement(By.id("gomb2")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, allQuery);
                    sorting(2, true, categoryArrayList);
                    for (int i = 0; i < allQuery.size(); i++) {
                        if (!allQuery.get(i).contains(uploadDataSorting.get(i))) {
                            start = false;
                            next = false;
                            minimalLogsAddToList(i + " :" + sortArrayList.get(2) + " szerinti " + sortingArrayList.get(1) + " sorrend nem egyezik meg!");
                        }
                    }
                    if (next) {
                        minimalLogsAddToList(sortArrayList.get(2) + " szerinti " + sortingArrayList.get(1) + " sorrend megegyezett!");
                    }
                }
                
                /**
                 * Összegzés teszt.
                **/
                
                if (next) {
                    int plus = 0, minus = 0;
                    ArrayList<String> sumQuery = new ArrayList<>();
                    
                    for (int i = 0; i < allQuery.size(); i++) {
                        String[] n = allQuery.get(i).split(" ");
                        
                        if (n[0].contains("-")){
                            String nReplace = n[0].replace("-", "");
                            minus += Integer.parseInt(nReplace);
                        }
                        else plus += Integer.parseInt(n[0]);
                    }
                    
                    table = driver.findElements(By.id("lista2"));
                    tableToArrayList(table, sumQuery);
                    
                    String[] n = sumQuery.get(0).split(" ");
                    
                    if(Integer.parseInt(n[0]) != (plus-minus)){
                        minimalLogsAddToList("Az összegzet összeg nem egyezik meg!");
                        start = false;
                        next = false;
                    }
                    
                    if(Integer.parseInt(n[1]) != plus){
                        minimalLogsAddToList("A bevétel összeg nem egyezik meg!");
                        start = false;
                        next = false;
                    }
                    
                    if(Integer.parseInt(n[2]) != minus){
                        minimalLogsAddToList("A kiadás összeg nem egyezik meg!");
                        start = false;
                        next = false;
                    }
                    
                    if (next) {
                        minimalLogsAddToList("Az összegzés rész adatai valósak!");
                    }
                }
                
                selectObject = new Select(selectSort.get(0));       // Listázás szerint Dátumra állítva.
                selectObject.selectByIndex(0);
                selectObject = new Select(selectSorting.get(0));       // Sorrend szerint növekvőre állítva.
                selectObject.selectByIndex(1);
                
                /**
                 * Adatok lekérdezése és ellenőrzése.
                 * 
                 * categoryNumber: 
                 *      - 0 : Összes kategória.
                 *      - 1 : Élelmiszer.
                 *      - 2 : Élelmiszer.
                 *      - 3 : Élelmiszer.
                 *      - 4 : Élelmiszer.
                 *      - 5 : Élelmiszer.
                 *      - 6 : Élelmiszer.
                 *      - 7 : Élelmiszer.
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
                                Thread.sleep(250);
                                allDay = "Összes nap";
                            }
                            
                            if (next) {     // cost id mező.
                                for (int costNumber = 1; costNumber < costArrayList.size(); costNumber++) {
                                    selectObject = new Select(selectCost.get(0));
                                    selectObject.selectByIndex(costNumber);
                                    selectObject = new Select(selectYear.get(0));
                                    selectObject.selectByIndex(0);
                                    selectObject = new Select(selectMonth.get(0));
                                    selectObject.selectByIndex(0);
                                    if (!driver.findElement(By.id("napok")).isSelected()) {
                                        driver.findElement(By.id("napok")).click();
                                        Thread.sleep(250);
                                        allDay = "Összes nap";
                                    }
                                    
                                    switch (categoryNumber) {
                                        case 1: case 2: case 4: case 5: case 6: 
                                            if (costNumber == 1) {
                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(0), monthArrayList.get(0), allDay);
                                                
                                                driver.findElement(By.id("gomb2")).click();
                                                Thread.sleep(100);
                                                message = driver.findElement(By.id("uzenet")).getText();
                                                
                                                hit = 0;
                                                if (!message.equals("Nincs ilyen adat!")) {
                                                    table = driver.findElements(By.id("lista"));
                                                    tableToArrayList(table, allQuery);
                                                    
                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                            String[] s = uploadData.get(k).split(", ");
                                                            String[] q = allQuery.get(j).split(" ");
                                                            String qDate;
                                                            if (categoryNumber==6) {
                                                                qDate = q[3];
                                                            }else{
                                                                qDate = q[2];
                                                            }
                                                            if (q[0].equals(s[0]) && qDate.equals(s[1])) {
                                                                hit++;
                                                                minimalLogsAddToList(allQuery.get(j));
                                                            }
                                                        }
                                                    }
                                                } else allQuery.clear();

                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                if (next) {     // datesy id mező.
                                                    for (int yearNumber = 1; yearNumber < yearArrayList.size(); yearNumber++) {
                                                        selectObject = new Select(selectYear.get(0));
                                                        selectObject.selectByIndex(yearNumber);
                                                        selectObject = new Select(selectMonth.get(0));
                                                        selectObject.selectByIndex(0);
                                                        if (!driver.findElement(By.id("napok")).isSelected()) {
                                                            driver.findElement(By.id("napok")).click();
                                                            Thread.sleep(250);
                                                            allDay = "Összes nap";
                                                        }
                                                        
                                                        querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(0), allDay);
                                                                                                                
                                                        driver.findElement(By.id("gomb2")).click();
                                                        Thread.sleep(100);
                                                        message = driver.findElement(By.id("uzenet")).getText();

                                                        hit = 0;
                                                        if (!message.equals("Nincs ilyen adat!")) {
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                            
                                                            for (int j = 0; j < allQuery.size(); j++) {
                                                                for (int k = 0; k < uploadData.size(); k++) {
                                                                    String[] s = uploadData.get(k).split(", ");
                                                                    String[] q = allQuery.get(j).split(" ");
                                                                    if (allQuery.get(j).contains(yearArrayList.get(yearNumber)) && q[0].equals(s[0]) && allQuery.get(j).contains(s[1])) {
                                                                        hit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        } else allQuery.clear();

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                        if (next) {     // datesm id mező.
                                                            for (int monthNumber = 1; monthNumber < monthArrayList.size(); monthNumber++) {
                                                                selectObject = new Select(selectMonth.get(0));
                                                                selectObject.selectByIndex(monthNumber);
                                                                if (!driver.findElement(By.id("napok")).isSelected()) {
                                                                    driver.findElement(By.id("napok")).click();
                                                                    Thread.sleep(250);
                                                                    allDay = "Összes nap";
                                                                }
                                                                
                                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(monthNumber), allDay);
                                                                                                                              
                                                                driver.findElement(By.id("gomb2")).click();
                                                                Thread.sleep(100);
                                                                message = driver.findElement(By.id("uzenet")).getText();

                                                                hit = 0;
                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                    table = driver.findElements(By.id("lista"));
                                                                    tableToArrayList(table, allQuery);
                                                                
                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                            String[] s = uploadData.get(k).split(", ");
                                                                            String[] q = allQuery.get(j).split(" ");
                                                                            String monthNum = "-" + (monthNumber) + "-";
                                                                            if ((monthNumber) < 10) monthNum = "-0" + (monthNumber) + "-";
                                                                            if (allQuery.get(j).contains(String.valueOf(monthNum)) && q[0].equals(s[0]) && allQuery.get(j).contains(s[1])) {
                                                                                hit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                } else allQuery.clear();

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                if (next) {     // dated id mező.
                                                                    ArrayList<String> dayQuery = new ArrayList<>();
                                                                    
                                                                    if (!allQuery.isEmpty()) {
                                                                        driver.findElement(By.id("napok")).click();
                                                                        Thread.sleep(250);                                                                    
                                                                                                                                                
                                                                        for (int e = 0; e < allQuery.size(); e++) {
                                                                            String[] d, dd;
                                                                            if (categoryNumber==6) {
                                                                                d = allQuery.get(e).split(" ");
                                                                                dd = d[3].split("-");
                                                                            }else{
                                                                                d = allQuery.get(e).split(" ");
                                                                                dd = d[2].split("-");
                                                                            }
                                                                            
                                                                            querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(monthNumber), dd[2]);
                                                                            
                                                                            driver.findElement(By.id("dated")).clear();
                                                                            driver.findElement(By.id("dated")).sendKeys(dd[2]);
                                                                            driver.findElement(By.id("gomb2")).click();
                                                                            Thread.sleep(100);
                                                                            table = driver.findElements(By.id("lista"));
                                                                            tableToArrayList(table, dayQuery);
                                                                            
                                                                            hit = 0;
                                                                            for (int j = 0; j < dayQuery.size(); j++) {
                                                                                for (int o = 0; o < allQuery.size(); o++) {
                                                                                    if (allQuery.get(o).equals(dayQuery.get(j))) {
                                                                                        hit++;
                                                                                        minimalLogsAddToList(allQuery.get(o));
                                                                                    }
                                                                                }
                                                                            }
                                                                            controlForQuery(dayQuery, hit, message);       // Mért adat ellenőrzése.
                                                                        }
                                                                    }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            
                                                if (costNumber == 3) {
                                                    ArrayList<String> supportList = new ArrayList<>();
                                                    
                                                    for (int i = 0; i < uploadData.size(); i++) {
                                                        String[] e = uploadData.get(i).split(", ");
                                                        if(e[0].contains("-") && e[2].contains(String.valueOf(categoryNumber))) supportList.add(e[0]);
                                                    };
                                                    
                                                    String randomData = supportList.get(rand.nextInt(supportList.size()));
                                                    
                                                    if(supportList.isEmpty()) {
                                                        randomData = "0";
                                                    } else {
                                                        driver.findElement(By.id("szam")).clear();
                                                        Thread.sleep(100);
                                                        driver.findElement(By.id("szam")).sendKeys(randomData);

                                                        querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(0), monthArrayList.get(0), allDay);

                                                        driver.findElement(By.id("gomb2")).click();
                                                        Thread.sleep(250);
                                                        message = driver.findElement(By.id("uzenet")).getText();

                                                        hit = 0;
                                                        if (!message.equals("Nincs ilyen adat!")) {
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                        
                                                            for (int j = 0; j < allQuery.size(); j++) {
                                                                for (int k = 0; k < uploadData.size(); k++) {
                                                                    String[] s = uploadData.get(k).split(", ");
                                                                    String[] q = allQuery.get(j).split(" ");
                                                                    String qDate;
                                                                    if (categoryNumber==6) {
                                                                        qDate = q[3];
                                                                    }else{
                                                                        qDate = q[2];
                                                                    }
                                                                    if (q[0].equals(s[0]) && qDate.equals(s[1])) {
                                                                        hit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        } else allQuery.clear();

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                        if (!supportList.isEmpty()) {     // datesy, datesm, dated id mező.
                                                            hit = 0;
                                                            ArrayList<String> dayQuery = new ArrayList<>();

                                                            if (!allQuery.isEmpty()) {
                                                                if (driver.findElement(By.id("napok")).isSelected()) {
                                                                    driver.findElement(By.id("napok")).click();
                                                                    Thread.sleep(250);
                                                                }

                                                                String[] d, dd;
                                                                d = allQuery.get(0).split(" ");

                                                                if (categoryNumber==6) {
                                                                    dd = d[3].split("-");
                                                                }else{
                                                                    dd = d[2].split("-");
                                                                }

                                                                selectObject = new Select(selectYear.get(0));
                                                                selectObject.selectByVisibleText(dd[0]);
                                                                selectObject = new Select(selectMonth.get(0));
                                                                selectObject.selectByIndex(Integer.parseInt(dd[1]));

                                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), dd[0], dd[1], dd[2]);

                                                                driver.findElement(By.id("dated")).clear();
                                                                driver.findElement(By.id("dated")).sendKeys(dd[2]);
                                                                driver.findElement(By.id("gomb2")).click();
                                                                Thread.sleep(100);
                                                                table = driver.findElements(By.id("lista"));
                                                                tableToArrayList(table, dayQuery);

                                                                if (allQuery.get(0).equals(dayQuery.get(0))) {
                                                                    hit++;
                                                                    minimalLogsAddToList(allQuery.get(0));
                                                                }

                                                                controlForQuery(dayQuery, hit, message);       // Mért adat ellenőrzése.
                                                            }
                                                        }
                                                    }
                                                }
                                                
                                            break;
                                        case 3 : case 7:
                                            if (costNumber == 2) {
                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(0), monthArrayList.get(0), allDay);
                                                
                                                driver.findElement(By.id("gomb2")).click();
                                                Thread.sleep(100);
                                                message = driver.findElement(By.id("uzenet")).getText();

                                                hit = 0;
                                                if (!message.equals("Nincs ilyen adat!")) {
                                                    table = driver.findElements(By.id("lista"));
                                                    tableToArrayList(table, allQuery);
                                                
                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                            String[] s = uploadData.get(k).split(", ");
                                                            String[] q = allQuery.get(j).split(" ");
                                                            String qDate;
                                                            if (categoryNumber==7) {
                                                                qDate = q[3];
                                                            }else{
                                                                qDate = q[2];
                                                            }
                                                            if (q[0].equals(s[0]) && qDate.equals(s[1])) {
                                                                hit++;
                                                                minimalLogsAddToList(allQuery.get(j));
                                                            }
                                                        }
                                                    }
                                                } else allQuery.clear();

                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                if (next) {     // datesy id mező.
                                                    for (int yearNumber = 1; yearNumber < yearArrayList.size(); yearNumber++) {
                                                        selectObject = new Select(selectYear.get(0));
                                                        selectObject.selectByIndex(yearNumber);
                                                        selectObject = new Select(selectMonth.get(0));
                                                        selectObject.selectByIndex(0);
                                                        if (!driver.findElement(By.id("napok")).isSelected()) {
                                                            driver.findElement(By.id("napok")).click();
                                                            Thread.sleep(250);
                                                            allDay = "Összes nap";
                                                        }
                                                        
                                                        querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(0), allDay);
                                                        
                                                        driver.findElement(By.id("gomb2")).click();
                                                        Thread.sleep(100);
                                                        message = driver.findElement(By.id("uzenet")).getText();

                                                        hit = 0;
                                                        if (!message.equals("Nincs ilyen adat!")) {
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                        
                                                            for (int j = 0; j < allQuery.size(); j++) {
                                                                for (int k = 0; k < uploadData.size(); k++) {
                                                                    String[] s = uploadData.get(k).split(", ");
                                                                    String[] q = allQuery.get(j).split(" ");
                                                                    if (allQuery.get(j).contains(yearArrayList.get(yearNumber)) && q[0].equals(s[0]) && allQuery.get(j).contains(s[1])) {
                                                                        hit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        } else allQuery.clear();

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                        if (next) {     // datesm id mező.
                                                            for (int monthNumber = 1; monthNumber < monthArrayList.size(); monthNumber++) {
                                                                selectObject = new Select(selectMonth.get(0));
                                                                selectObject.selectByIndex(monthNumber);
                                                                if (!driver.findElement(By.id("napok")).isSelected()) {
                                                                    driver.findElement(By.id("napok")).click();
                                                                    Thread.sleep(250);
                                                                    allDay = "Összes nap";
                                                                }
                                                                
                                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(monthNumber), allDay);
                                                                
                                                                driver.findElement(By.id("gomb2")).click();
                                                                Thread.sleep(100);
                                                                message = driver.findElement(By.id("uzenet")).getText();

                                                                hit = 0;
                                                                if (!message.equals("Nincs ilyen adat!")) {
                                                                    table = driver.findElements(By.id("lista"));
                                                                    tableToArrayList(table, allQuery);
                                                                
                                                                    for (int j = 0; j < allQuery.size(); j++) {
                                                                        for (int k = 0; k < uploadData.size(); k++) {
                                                                            String[] s = uploadData.get(k).split(", ");
                                                                            String[] q = allQuery.get(j).split(" ");
                                                                            String monthNum = "-" + (monthNumber) + "-";
                                                                            if ((monthNumber) < 10) monthNum = "-0" + (monthNumber) + "-";
                                                                            if (allQuery.get(j).contains(String.valueOf(monthNum)) && q[0].equals(s[0]) && allQuery.get(j).contains(s[1])) {
                                                                                hit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                } else allQuery.clear();

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                if (next) {     // dated id mező.
                                                                    ArrayList<String> dayQuery = new ArrayList<>();
                                                                    
                                                                    if (!allQuery.isEmpty()) {
                                                                        driver.findElement(By.id("napok")).click();
                                                                        Thread.sleep(250);
                                                                        
                                                                        for (int e = 0; e < allQuery.size(); e++) {
                                                                            String[] d, dd;
                                                                            if (categoryNumber==7) {
                                                                                d = allQuery.get(e).split(" ");
                                                                                dd = d[3].split("-");
                                                                            }else{
                                                                                d = allQuery.get(e).split(" ");
                                                                                dd = d[2].split("-");
                                                                            }
                                                                            
                                                                            querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(yearNumber), monthArrayList.get(monthNumber), dd[2]);

                                                                            driver.findElement(By.id("dated")).clear();
                                                                            driver.findElement(By.id("dated")).sendKeys(dd[2]);
                                                                            driver.findElement(By.id("gomb2")).click();
                                                                            Thread.sleep(100);
                                                                            table = driver.findElements(By.id("lista"));
                                                                            tableToArrayList(table, dayQuery);
                                                                            
                                                                            hit = 0;
                                                                            for (int j = 0; j < dayQuery.size(); j++) {
                                                                                for (int o = 0; o < allQuery.size(); o++) {
                                                                                    if (allQuery.get(o).equals(dayQuery.get(j))) {
                                                                                        hit++;
                                                                                        minimalLogsAddToList(allQuery.get(o));
                                                                                    }
                                                                                }
                                                                            }  
                                                                            controlForQuery(dayQuery, hit, message);       // Mért adat ellenőrzése.
                                                                        }
                                                                    }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            
                                                if (costNumber == 3) {
                                                    ArrayList<String> supportList = new ArrayList<>();
                                                    
                                                    for (int i = 0; i < uploadData.size(); i++) {
                                                        String[] e = uploadData.get(i).split(", ");
                                                        if(!e[0].contains("-") && e[2].contains(String.valueOf(categoryNumber))) supportList.add(e[0]);
                                                    };
                                                    
                                                    String randomData = supportList.get(rand.nextInt(supportList.size()));
                                                    if(supportList.isEmpty()) {
                                                        randomData = "0";
                                                    } else {
                                                        driver.findElement(By.id("szam")).clear();
                                                        Thread.sleep(100);
                                                        driver.findElement(By.id("szam")).sendKeys(randomData);

                                                        querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), yearArrayList.get(0), monthArrayList.get(0), allDay);

                                                        driver.findElement(By.id("gomb2")).click();
                                                        Thread.sleep(250);
                                                        message = driver.findElement(By.id("uzenet")).getText();

                                                        hit = 0;
                                                        if (!message.equals("Nincs ilyen adat!")) {
                                                            table = driver.findElements(By.id("lista"));
                                                            tableToArrayList(table, allQuery);
                                                        
                                                            for (int j = 0; j < allQuery.size(); j++) {
                                                                for (int k = 0; k < uploadData.size(); k++) {
                                                                    String[] s = uploadData.get(k).split(", ");
                                                                    String[] q = allQuery.get(j).split(" ");
                                                                    String qDate;
                                                                        if (categoryNumber==7) {
                                                                            qDate = q[3];
                                                                        }else{
                                                                            qDate = q[2];
                                                                        }
                                                                    if (q[0].equals(s[0]) && qDate.equals(s[1])) {
                                                                        hit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        } else allQuery.clear();

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                        if (!allQuery.isEmpty()) {     // datesy, datesm, dated id mező.
                                                            hit = 0;
                                                            ArrayList<String> dayQuery = new ArrayList<>();

                                                            if (!allQuery.isEmpty()) {
                                                                if (driver.findElement(By.id("napok")).isSelected()) {
                                                                    driver.findElement(By.id("napok")).click();
                                                                    Thread.sleep(250);
                                                                }

                                                                String[] d, dd;
                                                                d = allQuery.get(0).split(" ");

                                                                if (categoryNumber==7) {
                                                                    dd = d[3].split("-");
                                                                }else{
                                                                    dd = d[2].split("-");
                                                                }

                                                                selectObject = new Select(selectYear.get(0));
                                                                selectObject.selectByVisibleText(dd[0]);
                                                                selectObject = new Select(selectMonth.get(0));
                                                                selectObject.selectByIndex(Integer.parseInt(dd[1]));

                                                                querySelected(costArrayList.get(costNumber), categoryArrayList.get(categoryNumber), dd[0], dd[1], dd[2]);

                                                                driver.findElement(By.id("dated")).clear();
                                                                driver.findElement(By.id("dated")).sendKeys(dd[2]);
                                                                driver.findElement(By.id("gomb2")).click();
                                                                Thread.sleep(100);
                                                                table = driver.findElements(By.id("lista"));
                                                                tableToArrayList(table, dayQuery);

                                                                if (allQuery.get(0).equals(dayQuery.get(0))) {
                                                                    hit++;
                                                                    minimalLogsAddToList(allQuery.get(0));
                                                                }

                                                                controlForQuery(dayQuery, hit, message);       // Mért adat ellenőrzése.
                                                            }
                                                        }
                                                    }
                                                }
                                            break;
                                        default:
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList(categoryArrayList.get(categoryNumber));
                                            minimalLogsAddToList("Ilyen kategóriában nincs feltöltött adat!");
                                            break;
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
    
    private static void personalDataEditAndDelete() {
        try {
            String newPassword = "", newEmail = "", newPostcode = "", newCountry = "", newCounty = "", newCity = "";
            String[] p;
            List<WebElement> table = driver.findElements(By.id("adatok"));       // Adatok táblázatának inicializálása.
            ArrayList<String> personalDataList = new ArrayList<>();         // ArrayList a kilistázott adathoz.
            tableToArrayList(table, personalDataList);
            
            
            if (personalDataList.get(0).isEmpty()) {        // Tábla ürességének ellenőrzése.
                minimalLogsAddToList("Felhasználói adat ellenőrzése sikertelen. Nincs adat!");
                start = false;
                next = false;
            }     
            
            if (next) {
                p = personalDataList.get(0).split(" ");
                String[] d = randomLocation.split(",");
                
                if (!p[0].equals(email)) {
                    minimalLogsAddToList("Email cím nem egyezik meg!");
                    next = false;
                }
                if (!p[1].equals(d[0])) {
                    minimalLogsAddToList("Irányítószám nem egyezik meg!");
                    next = false;
                }
                if (!p[2].equals(d[1])) {
                    minimalLogsAddToList("Ország nem egyezik meg!");
                    next = false;
                }
                if (!p[3].equals(d[2])) {
                    minimalLogsAddToList("Megye nem egyezik meg!");
                    next = false;
                }
                if (!p[4].equals(d[3])) {
                    minimalLogsAddToList("Város nem egyezik meg!");
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("gomb5")).click();
                Thread.sleep(250);

                newPassword = Data.getNewPassword();
                newEmail = "editEmail@mail.com";
                newPostcode = "1111";
                newCountry = "Asguard";
                newCounty = "Mordor";
                newCity = "LegoCity";
                
                driver.findElement(By.id("email")).clear();
                driver.findElement(By.id("postcode")).clear();
                driver.findElement(By.id("country")).clear();
                driver.findElement(By.id("county")).clear();
                driver.findElement(By.id("city")).clear();
                
                driver.findElement(By.id("newpassword")).sendKeys(newPassword);
                driver.findElement(By.id("email")).sendKeys(newEmail);
                driver.findElement(By.id("postcode")).sendKeys(newPostcode);
                driver.findElement(By.id("country")).sendKeys(newCountry);
                driver.findElement(By.id("county")).sendKeys(newCounty);
                driver.findElement(By.id("city")).sendKeys(newCity);
                driver.findElement(By.id("password")).sendKeys(password);
                Thread.sleep(250);

                driver.findElement(By.id("gomb4")).click();
                Thread.sleep(1000);
                message = driver.findElement(By.id("uzenet")).getText();
                
                if (!message.equals("Módosítás sikeres")) {
                    minimalLogsAddToList("Felhasználó adatainak módosítás nem sikerült. Rendszer üzenet:" + message);
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                
                password = newPassword;
                driver.findElement(By.linkText("Kijelentkezés")).click();
                Thread.sleep(250);
                
                login(randomName,newPassword);

                if (!message.equals("Sikeres bejelentkezés.")){
                    next = false;
                } else {
                    Thread.sleep(250);
                    driver.findElement(By.linkText("Adataim")).click();
                    Thread.sleep(250);
                    personalDataList.clear();
                    table = driver.findElements(By.id("adatok"));
                    tableToArrayList(table, personalDataList);
                    p = personalDataList.get(0).split(" ");

                    if (!p[0].equals(newEmail)) {
                        minimalLogsAddToList("Módosított email cím nem egyezik meg!");
                        next = false;
                    }
                    if (!p[1].equals(newPostcode)) {
                        minimalLogsAddToList("Módosított irányítószám nem egyezik meg!");
                        next = false;
                    }
                    if (!p[2].equals(newCountry)) {
                        minimalLogsAddToList("Módosított ország nem egyezik meg!");
                        next = false;
                    }
                    if (!p[3].equals(newCounty)) {
                        minimalLogsAddToList("Módosított megye nem egyezik meg!");
                        next = false;
                    }
                    if (!p[4].equals(newCity)) {
                        minimalLogsAddToList("Módosított város nem egyezik meg!");
                        next = false;
                    }
                }
            }
                
            if (next) {
                minimalLogsAddToList("Felhasználó adatainak módosítása sikeres!");
                
                driver.findElement(By.id("gomb6")).click();
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(250);
                String text = alert.getText();
                if (text.equals("Biztosan törölni szeretné?")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.get(pagePath("index.html"));
                login(randomName, newPassword);
                if (!start) {
                    minimalLogsAddToList("Felhasználó törlése sikeres!");
                    minimalLogsAddToList("Felhasználó adatainak módosítása és törlése sikeres!");
                    start = true;
                    
                    driver.navigate().refresh();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void dataEditAndDelete(int value) {
        try {
            String amount = "1111", dateY = "2022", dateM = "05", dateD = "05", category = "Fizetés";
            String editedData = amount + " " + dateY + "-" + dateM + "-" + dateD + " " + category;
            driver.findElement(By.id("gomb2")).click();
            Thread.sleep(500);  
            
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            ArrayList<String> originalQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            ArrayList<String> editedQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            tableToArrayList(table, originalQuery);
            
            driver.findElement(By.name("0")).click();
            Thread.sleep(250);
            driver.findElement(By.id("amount")).clear();
            driver.findElement(By.id("regAt")).clear();
            
            table = driver.findElements(By.id("lista"));
            tableToArrayList(table, editedQuery);
            
            driver.findElement(By.id("amount")).sendKeys(amount);
            WebElement datePicker = driver.findElement(By.id("regAt"));
            new Actions(driver)
                .sendKeys(datePicker, dateY)
                .sendKeys(Keys.TAB)
                .sendKeys(dateM)
                .sendKeys(dateD)
                .perform();
            WebElement selectElement = driver.findElement(By.id("menu"));
            Select selectObject = new Select(selectElement);
            selectObject.selectByVisibleText(category);
            driver.findElement(By.id("gomb")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("uzenet")).getText();
            
            if (!message.equals("Módosítás sikeres")) {
                minimalLogsAddToList(value + ". adat módosítása sikertelen. Rendszer üzenet: " + message);
                next = false;
                start = false;
            }else {
                minimalLogsAddToList(value + ". adat módosítása sikeres!");
            
                if (!editedQuery.get(0).equals(editedData)){                    
                    driver.findElement(By.id("gomb1")).click();
                    Thread.sleep(250);
                    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                    Thread.sleep(250);
                    String text = alert.getText();
                    if (text.equals("Biztosan törölni szeretné?")) alert.accept();
                    else {
                        minimalLogsAddToList("Hibás alert!");
                        alert = driver.switchTo().alert();
                        alert.dismiss();
                        start = false;
                        next = false;
                    }
                }
            }
            
            if (next) {
                driver.findElement(By.id("gomb2")).click();
                Thread.sleep(250); 
                table = driver.findElements(By.id("lista"));
                tableToArrayList(table, editedQuery);
                
                if (originalQuery.size()==editedQuery.size()) {
                    minimalLogsAddToList(value + ". adat eltávolítása sikertelen!");
                    start = false;
                    next = false;
                }else minimalLogsAddToList(value + ". adat eltávolítása sikeres!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void functionTest() {
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(250);
            driver.findElement(By.id("gomb")).click();
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            Thread.sleep(100);
            String text = alert.getText();
            if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
            else {
                minimalLogsAddToList("Hibás alert!");
                alert = driver.switchTo().alert();
                alert.dismiss();
                start = false;
                next = false;
            }
                
            if (next) {
                driver.findElement(By.id("runame")).sendKeys("Function");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("rupassword")).sendKeys("Function");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("email")).sendKeys("function@test.hu");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("postcode")).sendKeys("1111");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("country")).sendKeys("TestLand");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("county")).sendKeys("Testas");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                text = alert.getText();
                if (text.equals("Kérem minden mezőt töltsön ki!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("city")).sendKeys("TestCity");
                driver.findElement(By.id("gomb")).click();
                Thread.sleep(250);
                message = driver.findElement(By.id("ruzenet")).getText();
                if (message.equals("Van már ilyen nevű felhasználó!!") || message.equals("Sikeres regisztráció")) minimalLogsAddToList("Regisztrációs funkció teszt OK!");
                else {
                    minimalLogsAddToList("Regisztrációs funkció teszt hibás!");
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("home-tab")).click();
                Thread.sleep(250);
                driver.findElement(By.id("gomb1")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(250);
                text = alert.getText();
                if (text.equals("Kérem adja meg a felhasználó nevet és a jelszót!")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                driver.findElement(By.id("uname")).sendKeys("Function");
                driver.findElement(By.id("gomb1")).click();
                Thread.sleep(250);
                message = driver.findElement(By.id("uzenet")).getText();
                if (message.equals("Hibás jelszó!")) minimalLogsAddToList("Bejelentkezős funkció teszt OK!");
                else {
                    minimalLogsAddToList("Regisztrációs funkció teszt hibás!");
                    start = false;
                    next = false;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void adminSorting(int order, ArrayList<String> query, ArrayList<String> sort) {
        ArrayList<String> supportSort = new ArrayList<>();
        ArrayList<Integer> supportSortInt = new ArrayList<>();
        ArrayList<String> supportQuery = new ArrayList<>();
        int hit = 0;
        boolean number = true;
        
        for (String e : sort) {
            try {
                supportSortInt.add(Integer.parseInt(replaceLetter(e.toLowerCase())));
            } catch (Exception a) {
                number = false;
            }
            if (!number) {
                supportSort.add(replaceLetter(e.toLowerCase()));
            }
        }
        for (String e : query) {
            supportQuery.add(replaceLetter(e.toLowerCase()));
        }
        
        if (number) {
            if (order == 0) Collections.sort(supportSortInt, Collections.reverseOrder());
            else Collections.sort(supportSortInt);
        } else {
            if (order == 0) Collections.sort(supportSort, Collections.reverseOrder());
            else Collections.sort(supportSort);
        }
       
        for (int i = 0; i < supportQuery.size(); i++) {
            if (number) {
                if(supportQuery.get(i).contains(String.valueOf(supportSortInt.get(i)))) {
                    hit++;
                }
            } else {
                if(supportQuery.get(i).contains(supportSort.get(i))) {
                    hit++;
                }
            }
        }
        
        if (supportQuery.size() == hit) {
            minimalLogsAddToList("Sorrend egyezik!");
        } else {
            start = false;
            next = false;
            minimalLogsAddToList("Sorrend nem egyezik!");
        }
    }
    
    private static String replaceLetter(String name) {
        String replacedLetters = "";
        String ABC = "abcdefghijklmnopqrstuvwxyz ,-0123456789";
        char c;
        for (int i=0; i<name.length(); i++) {
            c = name.charAt(i);
            switch(c) {
                case 'á':
                   c = 'a'; break;
                case 'é':
                   c = 'e'; break;
                case 'í':
                   c = 'i'; break;
                case 'ó': case 'ö': case 'ő':
                   c = 'o'; break;
                case 'ú': case 'ü': case 'ű':
                   c = 'u'; break;
            }
            if (ABC.indexOf(c) > -1) replacedLetters += c;
        }
        return replacedLetters;
    }
    
    private static void adminTest() {
        int hit = 0, desc = 0;
        next = true;
        Select selectObject;
        String categoryName = "", find = "";
        
        try {
            Thread.sleep(250);
            List<WebElement> table = driver.findElements(By.id("lista"));       // Oldalon lévő táblázatának inicializálása.
            List<WebElement> selectLocation = driver.findElements(By.id("what"));       // Lekérdezés menü: Lokáció listázása.
            List<WebElement> selectOrder = driver.findElements(By.id("order1"));       // Lekérdezés menü: Sorrend listázása.
            List<WebElement> selectUser;                                            // Lekérdezés menü: Felhasználók csoportosítás listázása.
                
            ArrayList<String> adminQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            ArrayList<String> locationArrayList = new ArrayList<>();        // ArrayList a lokációk vizsgálatához.
            ArrayList<String> orderArrayList = new ArrayList<>();        // ArrayList a sorrend vizsgálatához.
            ArrayList<String> sort = new ArrayList<>();
            ArrayList<String> supportQuery = new ArrayList<>();
            ArrayList<String> userArrayList = new ArrayList<>();
            ArrayList<String> deletedQuery = new ArrayList<>();
                
            tableToArrayList(table, adminQuery);
            findElementsToArrayList(selectLocation, locationArrayList);
            findElementsToArrayList(selectOrder, orderArrayList);
            
            if (table.get(0).getText().equals("")) {     // Tábla ürességének ellenőrzése.
                next = false;
                start = false;
                minimalLogsAddToList("Nincsenek regisztrált felhasználók!");
            }
            else {
                if (next) {
                    for (int location = 0; location < locationArrayList.size(); location++) {
                        for (int order = 0; order < orderArrayList.size(); order++) {
                            sort.clear();
                            selectObject = new Select(selectLocation.get(0));       // Lokáció kiválasztása.
                            selectObject.selectByIndex(location);
                            selectObject = new Select(selectOrder.get(0));       // Dátum szerint csökkenő.
                            selectObject.selectByIndex(order);
                            
                            driver.findElement(By.id("gomb2")).click();
                            Thread.sleep(250);
                            table = driver.findElements(By.id("lista"));
                            tableToArrayList(table, adminQuery);
                            
                            switch (location) {
                                case 0:         // Ország alapján keres.
                                    if (next) {
                                        minimalLogs.add(" ");
                                        minimalLogsAddToList("Kiválasztva: " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                        for (int i = 0; i < adminQuery.size(); i++) {
                                            find = driver.findElement(By.xpath("/html/body/div[2]/table/tbody[" + (i+2) + "]/tr/td[2]")).getText();
                                            sort.add(find);
                                            minimalLogsAddToList(adminQuery.get(i));
                                        }
                                        
                                        adminSorting(order, adminQuery, sort);
                                    }
                                    break;
                                case 1:         // Irányítószám alapján keres.
                                    if (next) {
                                        minimalLogs.add(" ");
                                        minimalLogsAddToList("Kiválasztva: " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                        for (int i = 0; i < adminQuery.size(); i++) {
                                            find = driver.findElement(By.xpath("/html/body/div[2]/table/tbody[" + (i+2) + "]/tr/td[5]")).getText();
                                            sort.add(find);
                                            minimalLogsAddToList(adminQuery.get(i));
                                        }
                                       
                                        adminSorting(order, adminQuery, sort);
                                    }
                                    break;
                                case 2:         // Város alapján keres.
                                    if (next) {
                                        minimalLogs.add(" ");
                                        minimalLogsAddToList("Kiválasztva: " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                        for (int i = 0; i < adminQuery.size(); i++) {
                                            find = driver.findElement(By.xpath("/html/body/div[2]/table/tbody[" + (i+2) + "]/tr/td[4]")).getText();
                                            sort.add(find);
                                            minimalLogsAddToList(adminQuery.get(i));
                                        }
                                        
                                        adminSorting(order, adminQuery, sort);
                                    }
                                    break;
                                case 3:         // Megye alapján keres.
                                    if (next) {
                                        minimalLogs.add(" ");
                                        minimalLogsAddToList("Kiválasztva: " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                        for (int i = 0; i < adminQuery.size(); i++) {
                                            find = driver.findElement(By.xpath("/html/body/div[2]/table/tbody[" + (i+2) + "]/tr/td[3]")).getText();
                                            sort.add(find);
                                            minimalLogsAddToList(adminQuery.get(i));
                                        }
                                        
                                        adminSorting(order, adminQuery, sort);
                                    }
                                    break;
                            }
                        }
                    }
                } 
            }
            
            if (next) {
                categoryName = "Drogéria";
                driver.findElement(By.linkText("Kategóriák")).click();
                Thread.sleep(100);
                table = driver.findElements(By.id("menu"));
                tableToArrayList(table, adminQuery);
                
                minimalLogs.add(" ");
                minimalLogsAddToList(categoryName + " kategória hozzáadása...");
                
                driver.findElement(By.id("categori")).sendKeys(categoryName);
                driver.findElement(By.id("gomb1")).click();
                Thread.sleep(100);
                message = driver.findElement(By.id("uzenet")).getText();
                
                if (message.equals("Hozzáadás sikeres!")) {
                    minimalLogsAddToList("Sikeres hozzáadás!");
                }
            }
            
            if (next) {
                Thread.sleep(3000);
                String newCategoryName = "Egészség";
                
                minimalLogs.add(" ");
                minimalLogsAddToList(categoryName + " kategória módosítása " + newCategoryName + " -ra/-re..." );
                
                table = driver.findElements(By.id("menu"));
                tableToArrayList(table, supportQuery);
                
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
                Thread.sleep(500);
                driver.findElement(By.id(String.valueOf(supportQuery.size()))).click();
                Thread.sleep(100);
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
                Thread.sleep(500);
                driver.findElement(By.id("newcategori")).sendKeys(newCategoryName);
                driver.findElement(By.id("gomb2")).click();
                Thread.sleep(3000);
                
                table = driver.findElements(By.id("menu"));
                tableToArrayList(table, adminQuery);
                
                for (int i = 0; i < adminQuery.size(); i++) {
                    if (adminQuery.get(i).contains(newCategoryName)) {
                        hit++;
                    }
                }
                
                if (hit != 0) minimalLogsAddToList("Sikeres módosítás!");
                else {
                    start = false;
                    next = false;
                    minimalLogsAddToList("Sikertelen módosítás!");
                }
            }
            
            if (next) {
                driver.findElement(By.linkText("Felhasználók")).click();
                Thread.sleep(100);
                selectUser = driver.findElements(By.id("active")); 
                findElementsToArrayList(selectUser, userArrayList);
                locationArrayList.clear();
                selectLocation = driver.findElements(By.id("order"));
                findElementsToArrayList(selectLocation, locationArrayList);
                orderArrayList.clear();
                selectOrder = driver.findElements(By.id("desc")); 
                findElementsToArrayList(selectOrder, orderArrayList);
                
                for (int user = 0; user < userArrayList.size(); user++) {
                    for (int location = 0; location < locationArrayList.size(); location++) {
                        for (int order = 0; order < orderArrayList.size(); order++) {
                            sort.clear();
                            selectObject = new Select(selectUser.get(0));       // User kiválasztása.
                            selectObject.selectByIndex(user);
                            selectObject = new Select(selectLocation.get(0));       // Lokáció kiválasztása.
                            selectObject.selectByIndex(location);
                            selectObject = new Select(selectOrder.get(0));       // növekvő.
                            selectObject.selectByIndex(order);

                            driver.findElement(By.id("gomb")).click();
                            Thread.sleep(250);
                            table = driver.findElements(By.id("lista"));
                            tableToArrayList(table, adminQuery);
                            message = driver.findElement(By.id("össz")).getText();
                            
                            if (order == 0) desc = 1;
                            else desc = 0;
                            
                            if (!message.equals("Nincs felhasználó!")) {
                                switch (location) {
                                    case 0:         // Név alapján keres.
                                        if (next) {
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList("Kiválasztva: " + userArrayList.get(user) + ", " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                            for (int i = 0; i < adminQuery.size(); i++) {
                                                find = driver.findElement(By.xpath("/html/body/div[3]/table/tbody[" + (i+2) + "]/tr/td[1]")).getText();
                                                sort.add(find);
                                                minimalLogsAddToList(adminQuery.get(i));
                                            }
                                            
                                            adminSorting(desc, adminQuery, sort);
                                        }
                                        break;
                                    case 1:         // Irányítószám alapján keres.
                                        if (next) {
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList("Kiválasztva: " + userArrayList.get(user) + ", " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                            for (int i = 0; i < adminQuery.size(); i++) {
                                                find = driver.findElement(By.xpath("/html/body/div[3]/table/tbody[" + (i+2) + "]/tr/td[6]")).getText();
                                                sort.add(find);
                                                minimalLogsAddToList(adminQuery.get(i));
                                            }
                                            
                                            adminSorting(desc, adminQuery, sort);
                                        }
                                        break;
                                    case 2:         // Megye alapján keres.
                                        if (next) {
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList("Kiválasztva: " + userArrayList.get(user) + ", " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                            for (int i = 0; i < adminQuery.size(); i++) {
                                                find = driver.findElement(By.xpath("/html/body/div[3]/table/tbody[" + (i+2) + "]/tr/td[4]")).getText();
                                                sort.add(find);
                                                minimalLogsAddToList(adminQuery.get(i));
                                            }
                                            
                                            adminSorting(desc, adminQuery, sort);
                                        }
                                        break;
                                    case 3:         // Ország alapján keres.
                                        if (next) {
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList("Kiválasztva: " + userArrayList.get(user) + ", " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                            for (int i = 0; i < adminQuery.size(); i++) {
                                                find = driver.findElement(By.xpath("/html/body/div[3]/table/tbody[" + (i+2) + "]/tr/td[3]")).getText();
                                                sort.add(find);
                                                minimalLogsAddToList(adminQuery.get(i));
                                            }
                                            
                                            adminSorting(desc, adminQuery, sort);
                                        }
                                        break;
                                    case 4:         // Város alapján keres.
                                        if (next) {
                                            minimalLogs.add(" ");
                                            minimalLogsAddToList("Kiválasztva: " + userArrayList.get(user) + ", " + locationArrayList.get(location) + ", " + orderArrayList.get(order));
                                            
                                            for (int i = 0; i < adminQuery.size(); i++) {
                                                find = driver.findElement(By.xpath("/html/body/div[3]/table/tbody[" + (i+2) + "]/tr/td[5]")).getText();
                                                sort.add(find);
                                                minimalLogsAddToList(adminQuery.get(i));
                                            }
                                            
                                            adminSorting(desc, adminQuery, sort);
                                        }
                                        break;
                                }
                            }
                            else minimalLogsAddToList(message);
                        }
                    }
                }
            }
            
            if (next) {
                minimalLogs.add(" ");
                minimalLogsAddToList("Felhasználó törlése...");
                selectObject = new Select(selectOrder.get(0));       // növekvő.
                selectObject.selectByIndex(0);
                selectUser = driver.findElements(By.id("active"));
                driver.findElement(By.id("gomb")).click();
                Thread.sleep(250);
                driver.findElement(By.xpath("/html/body/div[3]/table/tbody[2]/tr/td[8]/button")).click();
                Thread.sleep(100);
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(100);
                String text = alert.getText();
                if (text.equals("Biztosan törölni szeretné?")) alert.accept();
                else {
                    minimalLogsAddToList("Hibás alert!");
                    alert = driver.switchTo().alert();
                    alert.dismiss();
                    start = false;
                    next = false;
                }
                
                selectObject = new Select(selectUser.get(0));       // User kiválasztása.
                selectObject.selectByIndex(2);
                selectObject = new Select(selectLocation.get(0));       // Lokáció kiválasztása.
                selectObject.selectByIndex(4);
                selectObject = new Select(selectOrder.get(0));       // növekvő.
                selectObject.selectByIndex(0);
                driver.findElement(By.id("gomb")).click();
                Thread.sleep(250);
                table = driver.findElements(By.id("lista"));
                tableToArrayList(table, deletedQuery);
                
                if (!adminQuery.get(0).equals(deletedQuery.get(0))) minimalLogsAddToList("Felhasználó törlése sikeres!");
                else {
                    next = false;
                    start = false;
                    minimalLogsAddToList("Sikertelen felhasználó törlés!");
                }
            }
            
            if (next) {
                minimalLogs.add(" ");
                minimalLogsAddToList("Passzív felhasználók törlése...");
                selectUser = driver.findElements(By.id("active"));
                selectObject = new Select(selectUser.get(0));       // User kiválasztása.
                selectObject.selectByIndex(0);
                selectObject = new Select(selectLocation.get(0));       // Lokáció kiválasztása.
                selectObject.selectByIndex(0);
                selectObject = new Select(selectOrder.get(0));       // növekvő.
                selectObject.selectByIndex(0);
                
                driver.findElement(By.id("gomb")).click();
                Thread.sleep(250);
                table = driver.findElements(By.id("lista"));
                tableToArrayList(table, deletedQuery);
                message = driver.findElement(By.id("össz")).getText();
                
                if (!message.equals("Nincs felhasználó!")) {
                    driver.findElement(By.id("delete")).click();
                    Thread.sleep(100);
                    Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                    Thread.sleep(100);
                    String text = alert.getText();
                    if (text.equals("Biztosan törölni szeretné?")) alert.accept();
                    else {
                        minimalLogsAddToList("Hibás alert!");
                        alert = driver.switchTo().alert();
                        alert.dismiss();
                        start = false;
                        next = false;
                    }
                    
                    Thread.sleep(1000);
                    driver.findElement(By.id("gomb")).click();
                    Thread.sleep(250);
                    table = driver.findElements(By.id("lista"));
                    tableToArrayList(table, deletedQuery);
                
                    if (!deletedQuery.get(0).isEmpty()) {
                        minimalLogsAddToList("Passzív felhasználók törlése sikeres!");
                        driver.findElement(By.linkText("Kijelentkezés")).click();
                    }
                    else {
                        next = false;
                        start = false;
                        minimalLogsAddToList("Sikertelen a passzív felhasználók törlése!");
                    }
                } else {
                    minimalLogsAddToList(message);
                    minimalLogsAddToList("Sikertelen a passzív felhasználók törlése!");
                }
            }
        } catch (Exception e) {
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
     * @param args
     * @throws java.lang.InterruptedException
        */
    
    public static void main(String[] args) throws Exception{
        LocalTime startUserDataInit = LocalTime.now(), endUserDataInit = LocalTime.now(), startUserRegister = LocalTime.now();
        LocalTime endUserRegister = LocalTime.now(), startUserLogin = LocalTime.now(), endUserLogin = LocalTime.now();
        LocalTime startUserDataAdd = LocalTime.now(), endUserDataAdd = LocalTime.now(), startUserDataQuery = LocalTime.now();
        LocalTime endUserDataQuery = LocalTime.now(), startUserDataEditAndDelete = LocalTime.now(), endUserDataEditAndDelete = LocalTime.now();
        LocalTime startUserEditAndDelete = LocalTime.now(), endUserEditAndDelete = LocalTime.now();
        LocalTime startFunctionTest = LocalTime.now(), endFunctionTest = LocalTime.now(), startAdminTest = LocalTime.now(), endAdminTest = LocalTime.now();
        LocalTime startTime = LocalTime.now();
        int AllTimeUserDataInit = 0, AllTimeUserRegister = 0, AllTimeUserLogin = 0, AllTimeUserDataAdd = 0, AllTimeUserDataQuery = 0;
        int AllTimeUserDataEditAndDelete = 0, AllTimeUserEditAndDelete = 0;
        
        minimalLogs.add(String.join("", Collections.nCopies(100, "#")));
        minimalLogs.add("#" + String.join("", Collections.nCopies(32, " ")) + LocalDate.now() + " Automata Selenium Teszt" 
                + String.join("", Collections.nCopies(32, " ")) + "#");
        minimalLogs.add(String.join("", Collections.nCopies(100, "#")));
        minimalLogs.add(" ");
        minimalLogs.add(String.join("", Collections.nCopies(100, "/")));
        minimalLogsAddToList("Teszt indítása!");
        minimalLogs.add(String.join("", Collections.nCopies(100, "/")));
        
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
        
        wait = new FluentWait<WebDriver>(driver)
            .withTimeout(Duration.ofSeconds(5))
            .pollingEvery(Duration.ofSeconds(5))
            .ignoring(NoSuchElementException.class);

        driver.get(pagePath("index.html"));     // A főoldal betöltése.
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);        // Időzítés beállítása.
        
        start = true;       // Indítási érték.
        
        /**
        * Első teszt: Funcionalitás teszt.
        **/
        
        if (start) {
            minimalLogs.add(" ");
            minimalLogs.add(String.join("", Collections.nCopies(100, ">")));
            minimalLogsAddToList("Funkciónális teszt indítása!");
            minimalLogs.add(String.join("", Collections.nCopies(100, "<")));
            
            startFunctionTest = LocalTime.now();
            functionTest();
            driver.navigate().refresh();
            endFunctionTest = LocalTime.now();
        }
        
        /**
        * Második teszt: Automata teszt 3x.
        **/
        
        if (start) {
            minimalLogs.add(" ");       // Üres sor beszúrása a log file-ba.
            minimalLogs.add(String.join("", Collections.nCopies(100, ">")));
            minimalLogsAddToList("Automata teszt indítása!");
            minimalLogs.add(String.join("", Collections.nCopies(100, "<")));

            for (int i=0; i<3; i++) {
                if (start) {
                    startUserDataInit = LocalTime.now();
                    minimalLogsAddToList((i+1) + ". felhasználó adatainak létrehozása...");
                    randomLocationAndName();         // A fiók paraméterei.
                    endUserDataInit = LocalTime.now();
                }

                if (start) {       // Új fiók regisztrálása.
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    
                    startUserRegister = LocalTime.now();
                    minimalLogsAddToList((i+1) + ". felhasználó regisztrálása...");
                    register(randomLocation, Data.getEmail(), Data.getPassword());
                    endUserRegister = LocalTime.now();
                }

                if (start) {     // Új fiók bejelentkezése.
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    
                    startUserLogin = LocalTime.now();
                    minimalLogsAddToList((i+1) + ". felhasználó bejelentkezése...");
                    login(randomName, password);
                    endUserLogin = LocalTime.now();
                }

                if (start) {        // Új fiók adatainak feltöltése.
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    minimalLogsAddToList("Adatok feltöltése...");
                    
                    startUserDataAdd = LocalTime.now();

                    for (int j=0; j < 50; j++) {
                        if (start) {
                            newData(Data.getMoney(), Data.getDate(), Data.getCategory());
                        } 
                    }
                    
                    if (start) minimalLogsAddToList("Adatok feltöltése kész!");
                    else minimalLogsAddToList("Sikertelen adat feltöltés!");
                    
                    endUserDataAdd = LocalTime.now();
                }

                if (start) {        // Feltöltött adatok lekérdezése.
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    minimalLogsAddToList("Adatok lekérdezése...");
                    
                    startUserDataQuery = LocalTime.now();
                    driver.findElement(By.linkText("Lekérdezés")).click();
                    Thread.sleep(250);
                    queryFromDatabase();
                    driver.navigate().refresh();
                    endUserDataQuery = LocalTime.now();
                    
                    if(!start) writeFile(uploadData, "_" + (i+1) + "_user_uploadData.txt");
                    uploadData.clear();
                }

                if (start) {
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    minimalLogsAddToList("Adatok módosítása és törlése...");
                    
                    startUserDataEditAndDelete = LocalTime.now();
                    driver.findElement(By.linkText("Lekérdezés")).click();
                    Thread.sleep(250);
                            
                    for (int j = 0; j < 5; j++) {
                        if (start) {        // Feltöltött adat módosítása és törlése.
                            Thread.sleep(250);
                            dataEditAndDelete(j+1);
                        }
                    }
                    
                    if (start) minimalLogsAddToList("Sikeres adat módosítás és törlés!");
                    endUserDataEditAndDelete = LocalTime.now();
                }

                if (start) {        // Felhasználó adatainak módosítása és a felhasználó törlése.
                    minimalLogs.add(String.join("", Collections.nCopies(100, "-")));
                    minimalLogsAddToList("Felhasználó adatainak módosítása és törlése...");
                    
                    startUserEditAndDelete = LocalTime.now();
                    driver.findElement(By.linkText("Adataim")).click();
                    Thread.sleep(250);
                    personalDataEditAndDelete();
                    endUserEditAndDelete = LocalTime.now();
                }
                
                Duration durationUserDataInit = Duration.between(startUserDataInit, endUserDataInit);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserDataInit += Integer.parseInt(String.valueOf(durationUserDataInit.getSeconds()));
                Duration durationUserRegister = Duration.between(startUserRegister, endUserRegister);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserRegister += Integer.parseInt(String.valueOf(durationUserRegister.getSeconds()));
                Duration durationUserLogin = Duration.between(startUserLogin, endUserLogin);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserLogin += Integer.parseInt(String.valueOf(durationUserLogin.getSeconds()));
                Duration durationUserDataAdd = Duration.between(startUserDataAdd, endUserDataAdd);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserDataAdd += Integer.parseInt(String.valueOf(durationUserDataAdd.getSeconds()));
                Duration durationUserDataQuery = Duration.between(startUserDataQuery, endUserDataQuery);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserDataQuery += Integer.parseInt(String.valueOf(durationUserDataQuery.getSeconds()));
                Duration durationUserDataEditAndDelete = Duration.between(startUserDataEditAndDelete, endUserDataEditAndDelete);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserDataEditAndDelete += Integer.parseInt(String.valueOf(durationUserDataEditAndDelete.getSeconds()));
                Duration durationUserEditAndDelete = Duration.between(startUserEditAndDelete, endUserEditAndDelete);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
                AllTimeUserEditAndDelete += Integer.parseInt(String.valueOf(durationUserEditAndDelete.getSeconds()));

                minimalLogs.add(" ");
                minimalLogs.add(String.join("", Collections.nCopies(100, "=")));
                minimalLogsAddToList((i+1) + ". felhasználó tesztjeinek részideje:");
                minimalLogs.add(" ");
                minimalLogsAddToList("Felhasználói adat létrehozásának az ideje: " + durationUserDataInit.getSeconds()/60 
                    + " perc " + durationUserDataInit.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Felhasználó regisztrálásának az ideje: " + durationUserRegister.getSeconds()/60 
                    + " perc " + durationUserRegister.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Felhasználó belépésének az ideje: " + durationUserLogin.getSeconds()/60 
                    + " perc " + durationUserLogin.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Adatok feltöltésének az ideje: " + durationUserDataAdd.getSeconds()/60 
                    + " perc " + durationUserDataAdd.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Adatok lekérdezésének az ideje: " + durationUserDataQuery.getSeconds()/60 
                    + " perc " + durationUserDataQuery.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Adatok módosításának és törlésének az ideje: " + durationUserDataEditAndDelete.getSeconds()/60 
                    + " perc " + durationUserDataEditAndDelete.getSeconds()% 60 + " másodperc.");
                minimalLogsAddToList("Felhasználó adatának módosítása és felhasználó törlésének az ideje: " + durationUserEditAndDelete.getSeconds()/60 
                    + " perc " + durationUserEditAndDelete.getSeconds()% 60 + " másodperc.");
                minimalLogs.add(String.join("", Collections.nCopies(100, "=")));
                minimalLogs.add(" ");
                
                if (!start) i = 3;
            }
        }
        
        /**
        * Harmadik teszt: Admin teszt.
        **/
        
        if (start) {
            minimalLogs.add(" ");
            minimalLogs.add(String.join("", Collections.nCopies(100, ">")));
            minimalLogsAddToList("Admin teszt indítása!");
            minimalLogs.add(String.join("", Collections.nCopies(100, "<")));
            
            startAdminTest = LocalTime.now(); 
            login("Admin", "admin");
            adminTest();
            endAdminTest = LocalTime.now();
        }
        
        Thread.sleep(1000);
        driver.quit();      // Kilép a böngészőből.
        
        LocalTime endTime = LocalTime.now();
        Duration durationAll = Duration.between(startTime, endTime);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationFunctionTest = Duration.between(startFunctionTest, endFunctionTest);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationAdminTest = Duration.between(startAdminTest, endAdminTest);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.

        minimalLogs.add(String.join("", Collections.nCopies(100, "\\")));
        minimalLogsAddToList("Tesztek összideje:");
        minimalLogs.add("");
        minimalLogsAddToList("Regisztráció és bejelentkező oldal funkcionális tesztjének az ideje: " + durationFunctionTest.getSeconds()/60 
                    + " perc " + durationFunctionTest.getSeconds()% 60 + " másodperc.");
        minimalLogsAddToList("Felhasználói adat létrehozásának az összideje: " + AllTimeUserDataInit /60 
                    + " perc " + AllTimeUserDataInit % 60 + " másodperc.");
        minimalLogsAddToList("Felhasználó regisztrálásának az összideje: " + AllTimeUserRegister /60 
                    + " perc " + AllTimeUserRegister % 60 + " másodperc.");
        minimalLogsAddToList("Felhasználó belépésének az összideje: " + AllTimeUserLogin /60 
                    + " perc " + AllTimeUserLogin % 60 + " másodperc.");
        minimalLogsAddToList("Adatok feltöltésének az összideje: " + AllTimeUserDataAdd /60 
                    + " perc " + AllTimeUserDataAdd % 60 + " másodperc.");
        minimalLogsAddToList("Adatok lekérdezésének az összideje: " + AllTimeUserDataQuery /60 
                    + " perc " + AllTimeUserDataQuery % 60 + " másodperc.");
        minimalLogsAddToList("Adatok módosításának és törlésének az összideje: " + AllTimeUserDataEditAndDelete /60 
                    + " perc " + AllTimeUserDataEditAndDelete % 60 + " másodperc.");
        minimalLogsAddToList("Felhasználó adatának módosítása és felhasználó törlésének az összideje: " + AllTimeUserEditAndDelete /60 
                    + " perc " + AllTimeUserEditAndDelete % 60 + " másodperc.");
        minimalLogsAddToList("Admin felület tesztjének az ideje: " + durationAdminTest.getSeconds()/60 
                    + " perc " + durationAdminTest.getSeconds()% 60 + " másodperc.");
        minimalLogs.add(String.join("", Collections.nCopies(100, "\\")));
        minimalLogs.add(" ");
        minimalLogsAddToList("A teszt befejeződött! Teljes ideje: " + durationAll.getSeconds()/60 
                    + " perc " + durationAll.getSeconds()% 60 + " másodperc.");
        
        writeFile(minimalLogs, "_testlog.txt");       // Ki írja fájlba a minimalis logokat.
    } 
}