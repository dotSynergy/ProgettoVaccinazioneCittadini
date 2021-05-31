package cittadini.controllers;

import cittadini.web.ServerJSONHandler;
import cittadini.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Home controller for the "home" view.
 *
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */
public class HomeController implements Initializable {
    private int centerID;

    private String name;
    private String surname;
    private String cf;

    /**
     * Server connector
     */
    ServerJSONHandler s;

    @FXML
    private ListView<String> vaccineCenterList;

    @FXML
    private Label nameLabel, surnameLabel, cfLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        s = new ServerJSONHandler();

        CompletableFuture<JSONArray> json = null;
        try {
            json = s.setEndpoint("CentriVaccinali").setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ObservableList<String> obs = FXCollections.observableArrayList();
        vaccineCenterList.setItems(obs);

        assert json != null;
        JSONArray centersJson = json.join();

        if(centersJson != null && centersJson.length() > 0){
            for(int i = 0; i < centersJson.length(); i++){
                JSONObject tmp = centersJson.getJSONObject(i);
                obs.add(tmp.getString("nomeCentro"));
            }
        }

        try {
            json = s.setEndpoint("Cittadini").makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert json != null;
        JSONArray citizenJson = json.join();

        if(citizenJson != null && citizenJson.length() > 0) {
            JSONObject o = citizenJson.getJSONObject(0);

            nameLabel.setText(o.getString("nome"));
            surnameLabel.setText(o.getString("cognome"));
            cfLabel.setText(o.getString("codiceFiscale"));


        }
    }
}
