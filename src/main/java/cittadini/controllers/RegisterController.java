package cittadini.controllers;
/*
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue -
 *       Andrea Pini - mat.740675
 */

import cittadini.web.ServerJSONHandler;
import cittadini.web.ServerStatusException;
import cittadini.web.WebMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class RegisterController implements Initializable {


    @FXML
    public TextField nameText;

    @FXML
    public TextField surnameText;

    @FXML
    public TextField cfText;

    @FXML
    public TextField usernameText;

    @FXML
    public TextField passwordText;

    @FXML
    public TextField nationText;

    @FXML
    public TextField regionText;

    @FXML
    public TextField provinceText;

    @FXML
    public TextField cityText;

    @FXML
    public TextField streetText;

    @FXML
    public Button registrationButton;

    @FXML
    public Button backButton;

    @FXML
    public Label error;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public boolean register(ServerJSONHandler s) {

        boolean returnVal = false;

        JSONObject jsonobj = new JSONObject();

        jsonobj.put("nome", nameText.getText().toString());
        jsonobj.put("cognome", surnameText.getText().toString());
        jsonobj.put("codiceFiscale", cfText.getText().toString());
        jsonobj.put("userName", usernameText.getText().toString());
        jsonobj.put("pass", passwordText.getText().toString());
        jsonobj.put("nazione", nationText.getText().toString());
        jsonobj.put("regione", regionText.getText().toString());
        jsonobj.put("provincia", provinceText.getText().toString());
        jsonobj.put("comune", cityText.getText().toString());
        jsonobj.put("via", streetText.getText().toString());

        try {
            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/signup_cittadino")
                    .setData(jsonobj)
                    .makeRequest();

            System.out.println(json.join());

            returnVal = true;

        } catch (IOException | InterruptedException | ServerStatusException e) {
            e.printStackTrace();
        }
            return returnVal;


    }

}
