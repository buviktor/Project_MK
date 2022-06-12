package admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Label lblLogged;

    @FXML
    private Button btnLogin;

    @FXML
    void Login() throws Exception{
        window();
    }

    private void window() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        
        LoginController lc = loader.getController();
                
        Scene scene = new Scene(root);
        Stage loginWindows = new Stage();
        loginWindows.setTitle("Bejelentkezés");
        loginWindows.initModality(Modality.APPLICATION_MODAL);
        loginWindows.setResizable(false);
        loginWindows.setScene(scene);
        loginWindows.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
