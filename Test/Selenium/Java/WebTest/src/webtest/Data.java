package webtest;

import java.time.LocalDate;
import java.util.Random;

/**
 * Adat feldologása és kezelése
 * @author Buda Viktor
 */

public class Data {
    private String county, city, postcode, country = "Magyarország", firstName, lastName;
    private static String email;
    private static int money;
    private static final Random rand = new Random();
    private static StringBuilder password;
    private static final String[] emailDomain = {"gmail.com", "freemail.hu", "outlook.hu", "citromail.hu", "sulinet.hu"};
    private static final String[] charCategories = new String[] {
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789"};

    public Data(int n, String row) {
        if (n == 0) location(row);
        else name(row);
    }
    
    private void location (String loc) {
        String[] l = loc.split(",");
        county = l[0];
        city = l[1];
        postcode = l[2];
    }
    
    public String getLocation() {
        return postcode +","+ country +","+ county +","+ city;
    }
        
    private void name (String nam) {
        String[] n = nam.split(" ");
        lastName = n[0];
        firstName = n[1];
    }
    
    public String getName() {
        email();
        password(10);
        return lastName + " " + firstName;
    }
    
    private void email () {
        String randomNumber = Integer.toString(rand.nextInt(9));
        for (int i=0; i<3; i++) {
            randomNumber += Integer.toString(rand.nextInt(9));
        }
        email = replaceLetter(lastName.toLowerCase()) + "." + replaceLetter(firstName.toLowerCase()) + randomNumber + "@" + emailDomain[rand.nextInt(4)];
    }
    
    private String replaceLetter(String name) {
        String replacedLetters = "";
        String ABC = "abcdefghijklmnopqrstuvwxyz";
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
    
    public static String getEmail() {
        return email;
    }
    
    private static void password(int length) {
        password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories[rand.nextInt(charCategories.length)];
            int position = rand.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }           
    }
    
    public static String getPassword() {
        return new String(password);
    }
        
    public static int getMoney(){
        money = rand.nextInt(500000)+1000;
        
        return (int)(Math.ceil(money / 100) * 100);
    }
    
    public static String getDate() {
        String[] localDate = LocalDate.now().toString().split("-");
        String year, month, day = "", yearNow = localDate[0];
        int randomNumberForMonth, monthNow = Integer.parseInt(localDate[1]);
        int randomNumberForDay, dayNow = Integer.parseInt(localDate[2]);
        
        if (0 < rand.nextInt(2)) year = yearNow;
        else year = "2021";
        
        randomNumberForMonth = rand.nextInt(12)+1;
        if (year.equals(yearNow)) month = "0" + (rand.nextInt(monthNow)+1);
        else if (randomNumberForMonth >= 10) month = String.valueOf(randomNumberForMonth);
        else month = "0" + randomNumberForMonth;
        
        randomNumberForDay = rand.nextInt(31)+1;
        if (year.equals(yearNow) && month.equals(monthNow)) {
            if (randomNumberForDay >= 10 && randomNumberForDay <= dayNow) day = String.valueOf(randomNumberForDay);
            else if (randomNumberForDay <= dayNow) day = "0" + randomNumberForDay;
        } else {
            switch (randomNumberForMonth-1) {
                case 0: case 2: case 4: case 6: case 7: case 9: case 11:
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay);
                    else day = "0" + randomNumberForDay;
                    break;
                case 1: 
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay-3);
                    else day = "0" + randomNumberForDay;
                    break;
                case 3: case 5: case 8: case 10:
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay-1);
                    else day = "0" + randomNumberForDay;
                    break;
            }
        }
        
        return year + "-" + month + "-" + day;
    }
    
    public static int getCategory() {
        return rand.nextInt(7)+1;
    }
}