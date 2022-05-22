package admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Admin felülethet a controller osztály
 * @author Buda Viktor
 */

public class FXMLDocumentController implements Initializable {
    
    String[] data = {"Admin", "admin"};
    Data d = new Data();
    
    Dotenv dotenv = Dotenv.load();
    String dPassword = dotenv.get("ADMINPASSWORD");
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private Label lblUsername;

    @FXML
    private TextField txtPassword;
    
    @FXML
    private Label lblPassword;

    @FXML
    private Label lblLogged;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnLogout;

    @FXML
    void Login() {
        if (BCrypt.checkpw(txtPassword.getText(), dPassword.substring(1, dPassword.length()-1))) {
            lblLogged.setText("Bejelentkezve: " + txtUsername.getText());
            
            txtUsername.setVisible(false);
            lblUsername.setVisible(false);
            txtPassword.setVisible(false);
            lblPassword.setVisible(false);
            btnLogin.setVisible(false);
            btnLogout.setVisible(true);
            
            txtUsername.setText("");
            txtPassword.setText("");
        }
    }

    @FXML
    void Logout() {
        lblLogged.setText("Kijelentkezve!");
        txtUsername.setVisible(true);
        lblUsername.setVisible(true);
        txtPassword.setVisible(true);
        lblPassword.setVisible(true);
        btnLogin.setVisible(true);
        btnLogout.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
