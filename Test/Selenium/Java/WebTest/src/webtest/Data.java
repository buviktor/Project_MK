package webtest;

/**
 * Adat feldologás
 * @author Buda Viktor
 */
public class Data {
    private static String county, city, postcode, country = "Magyarország";

    public Data(String row) {
       location(row) ;
    }
    
    private static void location (String loc) {
        String[] s = loc.split(",");
        county = s[0];
        city = s[1];
        postcode = s[2];
        country = country;
    }
    
    public String getLocation() {
        return postcode +","+ country +","+ county +","+ city;
    }
        
    
}


