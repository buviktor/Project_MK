package admin;

/**
 * Admin felülethet a bejelentkező adat osztály
 * @author Buda Viktor
 */

import org.springframework.security.crypto.bcrypt.BCrypt;


public class Data {
            
    String accept(String dataName, String dataPassword, String username, String password){
        if (dataName.equals(username)) {
            if (BCrypt.checkpw(password, dataPassword.substring(1, dataPassword.length()-1))){
                System.out.println("valami");
                return "OK";
            }
            else return "NOK";
        }
        return "NOK";
    }
    
}
