/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Viktor
 */

public class LoginController implements Initializable {

    Dotenv dotenv = Dotenv.load();
    String dPassword = dotenv.get("ADMINPASSWORD");
       
        
    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;
    
    @FXML
    private Label lblMessage;

    @FXML
    void cancel() {
        Window window = lblMessage.getScene().getWindow();
        window.hide();
    }

    @FXML
    void login() throws InterruptedException{
        /*
        if (BCrypt.checkpw(txtPassword.getText(), dPassword.substring(1, dPassword.length()-1))) {
                        
            txtUserName.setText("");
            txtPassword.setText("");
            
            lblMessage.setText("Sikeres!");
            Thread.sleep(1000);
            
        }
        */
        
        /*
        try {
            URL loginUrl = new URL("localhost:5000/login");
            
            
            
            HttpURLConnection conn = (HttpURLConnection)loginUrl.openConnection();
            
            conn.setRequestMethod("GET");
            conn.connect();
            
            int responsecode = conn.getResponseCode();
            String inline = "";
            
            Scanner sc = new Scanner(loginUrl.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            System.out.println(inline);
            sc.close();
            
            
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
        */
        
        /*
        try {
            HttpResponse<String> response = Unirest.post("localhost:5000/login")
                .header("Content-Type", "application/json")
                .body("{\r\n\"uname\" : \"Admin\",\r\n\"upassword\" : \"admin\"\r\n}")
                .asString();
            
            UnirestParsingException ex = response.getParsingError().get();
            lblMessage.setText(ex.getMessage());
            
        } catch (Exception e) {
        }
        */
        String name = txtUserName.getText();
        String password = txtPassword.getText();
        
        try {
            String rawData = "{\r\n\"uname\" : \"" + name +
                    "\",\r\n\"" + password + "\" : \"admin\"\r\n}";
            String type = "application/json";
            String encodedData = URLEncoder.encode( rawData, "UTF-8" ); 
            URL u = new URL("localhost:5000/login");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-Type", type );
            conn.setRequestProperty( "Content-Length", String.valueOf(encodedData.length()));
            OutputStream os = conn.getOutputStream();
            os.write(encodedData.getBytes());
            
            lblMessage.setText(conn.getResponseMessage());
        } catch (Exception e) {
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
