package cittadini.controllers;

import cittadini.web.ServerJSONHandler;
import cittadini.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue -
 * @author Andrea Pini - mat.740675
 */

public class HomeController implements Initializable {
    private int centerID;

    private String name;
    private String surname;
    private String cf;

    ServerJSONHandler s;

    @FXML
    private ListView<String> vaccineCenterList;

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
        JSONArray j = json.join();

        if(j != null && j.length() > 0){
            for(int i = 0; i < j.length(); i++){
                JSONObject tmp = j.getJSONObject(i);
                obs.add(tmp.getString("nomeCentro"));
            }
        }



    }
}
