package webtest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    
    public static String getNewPassword() {
        password(10);
        return new String(password);
    }
        
    public static int getMoney(){
        money = rand.nextInt(500000)+1000;
        
        return (int)(Math.ceil(money / 100) * 100);
    }
    
    public static String getDate() {
        String[] localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).split("-");
        String year = "", month = "", day = "", yearNow = localDate[0];
        int randomNumberForMonth = 0, monthNow = Integer.parseInt(localDate[1]);
        int randomNumberForDay = 0, dayNow = Integer.parseInt(localDate[2]);
        int randYear = (int)(Math.random() * 2 + 1);
        
        if (1 == randYear) year = yearNow;
        else year = "2021";
        
        randomNumberForMonth = rand.nextInt(12)+1;
        if (year.equals(yearNow)) {
            month = String.valueOf(rand.nextInt(monthNow)+1);
            if (Integer.parseInt(month) < 10) month = "0" + month;
        } else {
            if (randomNumberForMonth < 10) {
                month = "0" + String.valueOf(randomNumberForMonth);
            } else month = String.valueOf(randomNumberForMonth);
        }
        
        randomNumberForDay = rand.nextInt(31)+1;
        
        if (year.equals(yearNow) && Integer.parseInt(month) == monthNow) {
            day = String.valueOf(rand.nextInt(dayNow)+1);
            if (Integer.parseInt(day) < 10) day = "0" + day;
        } else {
            switch (Integer.parseInt(month)) {
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay);
                    else day = "0" + randomNumberForDay;
                    break;
                case 2: 
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay-3);
                    else day = "0" + randomNumberForDay;
                    break;
                case 4: case 6: case 9: case 11:
                    if (randomNumberForDay >= 10) day = String.valueOf(randomNumberForDay-1);
                    else day = "0" + randomNumberForDay;
                    break;
            }
        }
        
        if (day.length() != 2) day = "0" + day;
        return year + "-" + month + "-" + day;
    }
    
    public static int getCategory() {
        return rand.nextInt(7)+1;
    }
}