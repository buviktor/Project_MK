package webtest;

import java.util.Random;

/**
 * Adat feldologás
 * @author Buda Viktor
 */

public class Data {
    private static String county, city, postcode, country = "Magyarország", firstName, lastName, email;
    static Random rand = new Random();
    static String[] emailDomain = {"gmail.com", "freemail.hu", "outlook.hu", "citromail.hu", "sulinet.hu"};

    public Data(int number, String row) {
        if (number == 0) location(row) ;
        else name(row) ;
    }
    
    private static void location (String loc) {
        String[] l = loc.split(",");
        county = l[0];
        city = l[1];
        postcode = l[2];
    }
    
    public String getLocation() {
        return postcode +","+ country +","+ county +","+ city;
    }
        
    private static void name (String nam) {
        String[] n = nam.split(" ");
        lastName = n[0];
        firstName = n[1];
        email();
    }
    
    public String getName() {
        return lastName + " " + firstName;
    }
    
    private static void email () {
        String randomNumber = Integer.toString(rand.nextInt(9));
        for (int i=0; i<3; i++) {
            randomNumber += Integer.toString(rand.nextInt(9));
        }
        email = lastName + "." + firstName + randomNumber + "@" + emailDomain[rand.nextInt(4)];
    }
    
    public static String getEmail() {
        return email;
    }
}


