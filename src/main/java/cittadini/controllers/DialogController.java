package cittadini.controllers;

import cittadini.web.ServerJSONHandler;
import cittadini.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    @FXML
    public TextField descriptionText;

    @FXML
    public ChoiceBox<Integer> gravitySelect;

    @FXML
    public TextArea noteTextArea;

    @FXML
    public Button cancelButton, sendButton;

    public int idVaccinazione;
    public int idCentro;

    /**
     * Server connector
     */
    ServerJSONHandler s;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        s = new ServerJSONHandler();

        ObservableList<Integer> choices = FXCollections.observableArrayList();
        choices.addAll(1, 2, 3, 4, 5);

        gravitySelect.setItems(choices);
        gravitySelect.getSelectionModel().selectFirst();

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        sendButton.setOnAction(event -> {
            JSONObject json = new JSONObject();

            if(descriptionText.getText().length() == 0)
                return;

            json.put("descrizione", descriptionText.getText());
            json.put("note", noteTextArea.getText());
            json.put("gravita", gravitySelect.getValue());
            json.put("idCentro", idCentro);
            json.put("idVaccinazione", idVaccinazione);

            try {
                s.setMethod(WebMethods.POST).setEndpoint("EventiAvversi").setData(json).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        });
    }


}
