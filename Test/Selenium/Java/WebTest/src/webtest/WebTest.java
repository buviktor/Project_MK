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
            Thread.sleep(500);
            driver.findElement(By.id("uname")).sendKeys(name);
            driver.findElement(By.id("upassword")).sendKeys(password);
            Thread.sleep(1000);
            driver.findElement(By.id("gomb1")).click();
            Thread.sleep(1000);
            
            message = driver.findElement(By.id("uzenet")).getText();
                        
            if (!message.equals("Sikeres bejelentkezés.")){
                minimalLogsAddToList(message);
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
        
        minimalLogsAddToList("Felhasználónév: " + randomName + "; Jelszó: " + password + "; Email: " 
                + randEmail + "; Irányítószám: " + postcode + "; Ország: " + country + "; Megye: " 
                + county + "; Város: " + city);
                        
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
            
            Thread.sleep(250);
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
    
    private static void controlForQuery(ArrayList<String> allQuery, int hit, String message) {
        if (!message.equals("Nincs ilyen adat!")) {
            minimalLogsAddToList("Talált adat: " + allQuery.size() + ", mért adat: " + hit + ".");
            if (hit != allQuery.size()) {
                //next = false;
                //start = false;
                minimalLogsAddToList("Nem egyezik a talált és a mért adat!");
            }
        }else minimalLogsAddToList(message);  
    }
    
    private static void sorting(int value, boolean sort, ArrayList category) {
        uploadDataSorting.clear();
        uploadDataSortingInt.clear();
        ArrayList<String> supportCategoryList = new ArrayList<>();
        supportCategoryList.clear();
        String replaced;
        
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
                replaced = String.valueOf(category.get(Integer.parseInt(s[2])));
                if (categoryName.equals("Egyébjutatások")) {
                    categoryName = replaced.replace("Egyébjutatások", "Egyéb jutatások");
                }
                if (categoryName.equals("Egyébkiadások")) {
                    categoryName = replaced.replace("Egyébkiadások", "Egyéb kiadások");
                }
                if (categoryName.equals("Élelmiszer")) {
                    categoryName = replaced.replace("Élelmiszer", "Elelmiszer");
                }
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
                        replaced = l;
                        if (l.equals("Elelmiszer")) {
                            l = replaced.replace("Elelmiszer", "Élelmiszer");
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
                        replaced = l;
                        if (l.equals("Elelmiszer")) {
                            l = replaced.replace("Elelmiszer", "Élelmiszer");
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
        
    private static void queryFromDatabase() {
        int hit = 0, dayHit = 0, monthHit = 0, yearHit = 0, allHit = 0;;
        next = true;
        String selected = " ", allDay = "Összes nap";
                
        try {
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            if (!table.get(0).getText().equals("")) start = false;     // Tábla ürességének ellenőrzése.
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
                
                selectObject = new Select(selectSort.get(0));       // Listázás szerint Dátumra állítva.
                selectObject.selectByIndex(0);
                selectObject = new Select(selectSorting.get(0));       // Sorrend szerint csökkenőre állítva.
                selectObject.selectByIndex(0);
                
                /**
                 * Adatok lekérdezése és ellenőrzése.
                 * 
                 * categoryNumber: 
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
                            
                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
                            
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

                                    switch (categoryNumber) {
                                        case 1: case 2: case 4: case 5: case 6: 
                                            if (costNumber == 1) {
                                                dayHit = 0; monthHit = 0; yearHit = 0; allHit = 0;
                                                
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
                                                                allHit++;
                                                                minimalLogsAddToList(allQuery.get(j));
                                                            }
                                                        }
                                                    }
                                                }

                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                        yearHit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                monthHit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                    dayHit++;
                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                            
                                                if (costNumber == 3) {
                                                    dayHit = 0; monthHit = 0; yearHit = 0; allHit = 0;
                                                    ArrayList<String> supportList = new ArrayList<>();
                                                    
                                                    for (int i = 0; i < uploadData.size(); i++) {
                                                        String[] e = uploadData.get(i).split(", ");
                                                        if(e[0].contains("-") && e[2].contains(String.valueOf(categoryNumber))) supportList.add(e[0]);
                                                    };
                                                    
                                                    for (int i = 0; i < 2; i++) {
                                                        String randomData = supportList.get(rand.nextInt(supportList.size()));
                                                        if(supportList.isEmpty()) {
                                                            randomData = "0";
                                                            next = false;
                                                        }
                                                        driver.findElement(By.id("szam")).clear();
                                                        driver.findElement(By.id("szam")).sendKeys(randomData);
                                                        selected = costArrayList.get(costNumber) + ": " +randomData + ", "+ categoryArrayList.get(categoryNumber) + ", " 
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
                                                                        allHit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                selected = costArrayList.get(costNumber) + ": " +randomData + ", "+ categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                yearHit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                        selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                        monthHit++;
                                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                        if (next) {     // dated id mező.
                                                                            hit = 0;
                                                                            driver.findElement(By.id("napok")).click();
                                                                            Thread.sleep(500);
                                                                            String day = "1";
                                                                            if (Integer.parseInt(day) < 10) {
                                                                                day = "0" + day;
                                                                            }
                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                            dayHit++;
                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                            switch ((monthNumber)) {
                                                                                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                                                                                    if (next) {
                                                                                        for (int dayNumber = 2; dayNumber < 32; dayNumber++) {
                                                                                            hit = 0;
                                                                                            driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                                            if (dayNumber < 10) {
                                                                                            day = "0" + dayNumber;
                                                                                            }
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                                            dayHit++;
                                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                                            dayHit++;
                                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                
                                            break;
                                        case 3 : case 7:
                                            if (costNumber == 2) {
                                                dayHit = 0; monthHit = 0; yearHit = 0; allHit = 0;
                                                
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
                                                                allHit++;
                                                                minimalLogsAddToList(allQuery.get(j));
                                                            }
                                                        }
                                                    }
                                                }

                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                        yearHit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                monthHit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                    dayHit++;
                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                                    dayHit++;
                                                                                                    minimalLogsAddToList(allQuery.get(j));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                            
                                                if (costNumber == 3) {
                                                    dayHit = 0; monthHit = 0; yearHit = 0; allHit = 0;
                                                    ArrayList<String> supportList = new ArrayList<>();
                                                    
                                                    for (int i = 0; i < uploadData.size(); i++) {
                                                        String[] e = uploadData.get(i).split(", ");
                                                        if(!e[0].contains("-") && e[2].contains(String.valueOf(categoryNumber))) supportList.add(e[0]);
                                                    };
                                                    
                                                    for (int i = 0; i < 2; i++) {
                                                        String randomData = supportList.get(rand.nextInt(supportList.size()));
                                                        if(supportList.isEmpty()) {
                                                            randomData = "0";
                                                            next = false;
                                                        }
                                                        driver.findElement(By.id("szam")).clear();
                                                        driver.findElement(By.id("szam")).sendKeys(randomData);
                                                        selected = costArrayList.get(costNumber) + ": " +randomData + ", "+ categoryArrayList.get(categoryNumber) + ", " 
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
                                                                        allHit++;
                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                selected = costArrayList.get(costNumber) + ": " +randomData + ", "+ categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                yearHit++;
                                                                                minimalLogsAddToList(allQuery.get(j));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

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
                                                                        selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                        monthHit++;
                                                                                        minimalLogsAddToList(allQuery.get(j));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                        if (next) {     // dated id mező.
                                                                            hit = 0;
                                                                            driver.findElement(By.id("napok")).click();
                                                                            Thread.sleep(500);
                                                                            String day = "1";
                                                                            if (Integer.parseInt(day) < 10) {
                                                                                day = "0" + day;
                                                                            }
                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                            dayHit++;
                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.

                                                                            switch ((monthNumber)) {
                                                                                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                                                                                    if (next) {
                                                                                        for (int dayNumber = 2; dayNumber < 32; dayNumber++) {
                                                                                            hit = 0;
                                                                                            driver.findElement(By.id("dated")).sendKeys(String.valueOf(Keys.UP));
                                                                                            if (dayNumber < 10) {
                                                                                            day = "0" + dayNumber;
                                                                                            }
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                                            dayHit++;
                                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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
                                                                                                            dayHit++;
                                                                                                            minimalLogsAddToList(allQuery.get(j));
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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
                                                                                            selected = costArrayList.get(costNumber) + ": " +randomData + ", " + categoryArrayList.get(categoryNumber) + ", " 
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

                                                                                            controlForQuery(allQuery, hit, message);       // Mért adat ellenőrzése.
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

                if (!p[0].equals(email)) minimalLogsAddToList("Email cím nem egyezik meg!");
                if (!p[1].equals(d[0])) minimalLogsAddToList("Irányítószám nem egyezik meg!");
                if (!p[2].equals(d[1])) minimalLogsAddToList("Ország nem egyezik meg!");
                if (!p[3].equals(d[2])) minimalLogsAddToList("Megye nem egyezik meg!");
                if (!p[4].equals(d[3])) minimalLogsAddToList("Város nem egyezik meg!");

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
                Thread.sleep(250);
                message = driver.findElement(By.id("uzenet")).getText();
                
                if (!message.equals("Módosítás sikeres")) {
                    minimalLogsAddToList("Adat módosítás nem sikerült. " + message);
                    start = false;
                    next = false;
                }
            }
            
            if (next) {
                minimalLogsAddToList(message);
                password = newPassword;
                driver.findElement(By.linkText("Kijelentkezés")).click();
                Thread.sleep(250);
                
                login(randomName,newPassword);

                if (!message.equals("Sikeres bejelentkezés.")){
                    next = false;
                }
            }
            
            if (next) {
                Thread.sleep(250);
                driver.findElement(By.linkText("Adataim")).click();
                Thread.sleep(250);
                personalDataList.clear();
                table = driver.findElements(By.id("adatok"));
                tableToArrayList(table, personalDataList);
                p = personalDataList.get(0).split(" ");
                
                if (!p[0].equals(newEmail)) minimalLogsAddToList("Módosított email cím nem egyezik meg!");
                if (!p[1].equals(newPostcode)) minimalLogsAddToList("Módosított irányítószám nem egyezik meg!");
                if (!p[2].equals(newCountry)) minimalLogsAddToList("Módosított ország nem egyezik meg!");
                if (!p[3].equals(newCounty)) minimalLogsAddToList("Módosított megye nem egyezik meg!");
                if (!p[4].equals(newCity)) minimalLogsAddToList("Módosított város nem egyezik meg!");
                
                
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
                    start = true;
                    
                    driver.navigate().refresh();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void dataEditAndDelete() {
        try {
            int hit = 0;
            String amount = "1111", dateY = "2022", dateM = "05", dateD = "05", category = "Fizetés";
            String editedData = amount + " " + dateY + "-" + dateM + "-" + dateD + " " + category;
            driver.findElement(By.id("gomb2")).click();
            Thread.sleep(250);  
            
            List<WebElement> table = driver.findElements(By.id("lista"));       // Lekérdezés táblázatának inicializálása.
            ArrayList<String> originalQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            ArrayList<String> editedQuery = new ArrayList<>();         // ArrayList a kilistázott összes adathoz.
            tableToArrayList(table, originalQuery);
            
            driver.findElement(By.name("0")).click();
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
            Thread.sleep(250);
            
            message = driver.findElement(By.id("uzenet")).getText();
            minimalLogsAddToList(message);
            
            if (!message.equals("Módosítás sikeres")) {
                minimalLogsAddToList("Adat módosítása sikertelen. " + message);
                next = false;
                start = false;
            }
            
            if (next) {
                if (!editedQuery.get(0).equals(editedData)){
                    minimalLogsAddToList("Adat módosítása sikeres!");
                    
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
                    
                    Thread.sleep(500);
                    message = driver.findElement(By.id("uzenet")).getText();
                    minimalLogsAddToList(message);
                }
            }
            
            if (next) {
                driver.findElement(By.id("gomb2")).click();
                Thread.sleep(250); 
                
                table = driver.findElements(By.id("lista"));
                tableToArrayList(table, editedQuery);
                
                if (originalQuery.size()==editedQuery.size()) {
                    minimalLogsAddToList("Adat eltávolítása sikertelen!");
                    start = false;
                    next = false;
                }
                
                minimalLogsAddToList("Adat eltávolítása sikeres!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void functionTest() {
        //Kérem minden mezőt töltsön ki!
        try {
            driver.findElement(By.id("profile-tab")).click();
            Thread.sleep(250);
            driver.findElement(By.id("gomb")).click();
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            Thread.sleep(250);
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
                driver.findElement(By.id("runame")).sendKeys("Test");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(250);
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
                driver.findElement(By.id("rupassword")).sendKeys("Test");
                driver.findElement(By.id("gomb")).click();
                alert = wait.until(ExpectedConditions.alertIsPresent());
                Thread.sleep(250);
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
            
            
/*
            driver.findElement(By.id("email")).sendKeys(email);
            driver.findElement(By.id("postcode")).sendKeys(postcode);
            driver.findElement(By.id("country")).sendKeys(country);
            driver.findElement(By.id("county")).sendKeys(county);
            driver.findElement(By.id("city")).sendKeys(city);
            */
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
        LocalTime startFunctionTest = LocalTime.now(), endFunctionTest = LocalTime.now();
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
        
        wait = new FluentWait<WebDriver>(driver)
            .withTimeout(Duration.ofSeconds(5))
            .pollingEvery(Duration.ofSeconds(5))
            .ignoring(NoSuchElementException.class);

        driver.get(pagePath("index.html"));     // A főoldal betöltése.
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);        // Időzítés beállítása.
        
        start = true;       // Indítási érték.
        
        /**
        * Első teszt: Funcionalitás teszt.
        **/
        /*
        if (start) {
            startFunctionTest = LocalTime.now();
            functionTest();
            endFunctionTest = LocalTime.now();
        }
        */
        /**
        * Második teszt: Automata teszt 3x.
        **/
        login("valaki", "valaki");
        for (int i=0; i<1; i++) {
            minimalLogs.add(" ");       // Üres sor beszúrása.
            /*
            if (start) {
                startUserDataInit = LocalTime.now();
                minimalLogsAddToList((i+1) + " fiók adatainak létrehozása");
                randomLocationAndName();         // A fiók paraméterei.
                endUserDataInit = LocalTime.now();
            }   
            
            if (start) {       // Új fiók regisztrálása.
                startUserRegister = LocalTime.now();
                register(randomLocation, Data.getEmail(), Data.getPassword());
                endUserRegister = LocalTime.now();
            }
            
            if (start) {     // Új fiók bejelentkezése.
                startUserLogin = LocalTime.now();
                login(randomName, password);
                endUserLogin = LocalTime.now();
            }
            */
            if (start) {        // Új fiók adatainak feltöltése.
                minimalLogsAddToList("Adatok feltöltése....");
                startUserDataAdd = LocalTime.now();
            
                for (int j=0; j < 50; j++) {
                    if (start) {
                        newData(Data.getMoney(), Data.getDate(), Data.getCategory());

                    } 
                }
                
                endUserDataAdd = LocalTime.now();
            }
            
            if (start) {        // Feltöltött adatok lekérdezése.
                startUserDataQuery = LocalTime.now();
                minimalLogsAddToList("Adatok sikeresen feltöltve!");
                driver.findElement(By.linkText("Lekérdezés")).click();
                Thread.sleep(250);
                queryFromDatabase();
                uploadData.clear();
                driver.navigate().refresh();
                endUserDataQuery = LocalTime.now();
            }
            
            for (int j = 0; j < 40; j++) {
                
            
            if (start) {        // Feltöltött adat módosítása és törlése.
                startUserDataEditAndDelete = LocalTime.now();
                driver.findElement(By.linkText("Lekérdezés")).click();
                Thread.sleep(250);
                dataEditAndDelete();
                endUserDataEditAndDelete = LocalTime.now();
            }
            
            }
            /*
            if (start) {        // Felhasználó adatainak módosítása és a felhasználó törlése.
                startUserEditAndDelete = LocalTime.now();
                driver.findElement(By.linkText("Adataim")).click();
                Thread.sleep(250);
                personalDataEditAndDelete();
                endUserEditAndDelete = LocalTime.now();
            }
            */
        }
               
        Thread.sleep(2000);
        driver.quit();      // Kilép a böngészőből.
        
        LocalTime endTime = LocalTime.now();
        Duration durationAll = Duration.between(startTime, endTime);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserDataInit = Duration.between(startUserDataInit, endUserDataInit);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserRegister = Duration.between(startUserRegister, endUserRegister);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserLogin = Duration.between(startUserLogin, endUserLogin);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserDataAdd = Duration.between(startUserDataAdd, endUserDataAdd);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserDataQuery = Duration.between(startUserDataQuery, endUserDataQuery);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserDataEditAndDelete = Duration.between(startUserDataEditAndDelete, endUserDataEditAndDelete);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        Duration durationUserEditAndDelete = Duration.between(startUserEditAndDelete, endUserEditAndDelete);       // Kiszámolja menniy idő telt el a kezdéstől a befejezésig.
        
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
        minimalLogsAddToList("Egy adat módosításának és törlésének az ideje: " + durationUserDataEditAndDelete.getSeconds()/60 
                + " perc " + durationUserDataEditAndDelete.getSeconds()% 60 + " másodperc.");
        minimalLogsAddToList("Felhasználó adatának módosítása és felhasználó törlésének az ideje: " + durationUserEditAndDelete.getSeconds()/60 
                + " perc " + durationUserEditAndDelete.getSeconds()% 60 + " másodperc.");
        minimalLogsAddToList("A teszt befejeződött! Teljes ideje: " + durationAll.getSeconds()/60 
                + " perc " + durationAll.getSeconds()% 60 + " másodperc.");
        
        writeFile(minimalLogs, "_testlog.txt");       // Ki írja fájlba a minimalis logokat.
    } 
}