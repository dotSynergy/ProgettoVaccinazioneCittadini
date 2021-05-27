package cittadini.controllers;

/**
 * The Login controller for the Login view.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue -
 *       @author Andrea Pini - mat.740675
 *
 */

import cittadini.web.ServerJSONHandler;
import cittadini.web.ServerStatusException;
import cittadini.web.WebMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;


public class LoginController implements Initializable {

    @FXML
    public TextField usernameText;

    @FXML
    public TextField passwordText;

    @FXML
    public Label errorLabel;

    @FXML
    public Button loginButton;

    @FXML
    public Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


    public boolean login(ServerJSONHandler s, String username, String password) {

        boolean returnVal = false;

        try {
            JSONObject jsonobj = new JSONObject();

            jsonobj.put("userName", username);
            jsonobj.put("password", password);

            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/login_cittadino")
                    .setData(jsonobj)
                    .makeRequest();

            String token = (String) json.join().getJSONObject(0).get("token");

            if(token.length() > 0){

                s.setJWT(token);

                returnVal = true;

            }

        } catch (IOException | InterruptedException | ServerStatusException e) {
            e.printStackTrace();
        } finally {
            return returnVal;
        }


    }

}
