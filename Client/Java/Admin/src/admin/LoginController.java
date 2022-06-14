/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import com.sun.net.httpserver.HttpsConfigurator;
import io.github.cdimascio.dotenv.Dotenv;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
            
            conn.setRequestMethod("POST");
            conn.connect();
            
            int responsecode = conn.getResponseCode();
            
        } catch (Exception e) {
            System.out.println(e);
        }
        */
        
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
              .url("localhost:5000/login")
              .method("POST", body)
              .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
