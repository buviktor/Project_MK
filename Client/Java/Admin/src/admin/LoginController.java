/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import io.github.cdimascio.dotenv.Dotenv;
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
        if (BCrypt.checkpw(txtPassword.getText(), dPassword.substring(1, dPassword.length()-1))) {
                        
            txtUserName.setText("");
            txtPassword.setText("");
            
            lblMessage.setText("Sikeres!");
            Thread.sleep(1000);
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
