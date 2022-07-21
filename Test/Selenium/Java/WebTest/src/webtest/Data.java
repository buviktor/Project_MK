package webtest;

import java.util.Random;

/**
 * Adat feldologása és kezelése
 * @author Buda Viktor
 */

public class Data {
    private String county, city, postcode, country = "Magyarország", firstName, lastName;
    private static String email;
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
}