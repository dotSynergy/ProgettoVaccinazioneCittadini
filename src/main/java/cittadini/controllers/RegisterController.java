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

    private boolean validation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        validation = false;
        cfText.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!cfText.getText().matches("(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$")){
                    cfText.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                    validation = false;
                } else {
                    cfText.setStyle("-fx-border-color: none ; -fx-border-width: none ;");
                    validation = true;
                }
            }

        });
    }


    public boolean register(ServerJSONHandler s) {

        boolean returnVal = false;

        if(!validation)
            return false;

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

            json.join();

            if(s.getResponseCode() == 200)
                returnVal = true;
            else returnVal = false;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return returnVal;

    }

}
