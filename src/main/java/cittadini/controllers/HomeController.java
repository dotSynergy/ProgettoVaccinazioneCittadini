package cittadini.controllers;

import cittadini.App;
import cittadini.models.*;
import cittadini.web.ServerJSONHandler;
import cittadini.web.WebMethods;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
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

    /**
     * the current data
     */
    private CentroVaccinaleModel selectedCentre;
    private CittadinoModel cittadino;
    private VaccinazioneModel selectedVaccination;


    /**
     * the observable lists for ListViews
     */
    ObservableList<VaccinazioneModel> vaccinationsObservable = FXCollections.observableArrayList();
    ObservableList<CentroVaccinaleModel> centersObservable = FXCollections.observableArrayList();
    ObservableList<EventoAvversoModel> eventsObservable = FXCollections.observableArrayList();


    /**
     * Server connector
     */
    ServerJSONHandler s;

    @FXML
    private TextField nameSearchText, citySearchText;

    @FXML
    private ListView<CentroVaccinaleModel> vaccineCenterList;
    @FXML
    private ListView<VaccinazioneModel> vaccinationsList;
    @FXML
    private ListView<EventoAvversoModel> eventsList;

    @FXML
    private Label nameLabel, surnameLabel, cfLabel, nameLabel2, typeLabel, addressLabel, vaccinationWarningLabel, registeredLabel;

    @FXML
    private ChoiceBox<String> checkType;

    @FXML
    private Button regButton, insertEvent;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupStaticElements();

        setupDynamicElements();

    }


    /**
     * setup for static elements in the scene
     */
    private void setupStaticElements(){

        //static ui elements
        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.addAll("hub", "ospedaliero","aziendale");

        checkType.setItems(choices);
        checkType.getSelectionModel().selectFirst();

    }

    /**
     * setup for dynamic elements in the scene
     */
    private void setupDynamicElements(){

        //dynamic ui elements

        s = new ServerJSONHandler();

        CompletableFuture<JSONArray> centersJson = null;
        try {
            centersJson = s.setEndpoint("CentriVaccinali").setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //observable list di stringhe perchè i nomi dei centri vaccinali sono univoci nel database, quindi è uno spreco inserire dei modelli di centro vaccinale

        vaccineCenterList.setItems(centersObservable);
        vaccinationsList.setItems(vaccinationsObservable);
        eventsList.setItems(eventsObservable);

        assert centersJson != null;
        JSONArray centersJsonArray = centersJson.join();


        //costruisco la query per prendere gli indirizzi
        CompletableFuture<JSONArray> addressesJson = null;
        StringBuilder endpointBuilder = new StringBuilder("Indirizzi?or=(");

        for(int i = 0; i < centersJsonArray.length(); i++) {
            endpointBuilder.append("idIndirizzo.eq.").append(centersJsonArray.getJSONObject(i).getInt("idIndirizzo"));
            if(!(i == centersJsonArray.length()-1))
                endpointBuilder.append(",");
        }
        endpointBuilder.append(")");

        String endpoint = endpointBuilder.toString();
        try {
            addressesJson = s.setEndpoint(endpoint).setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        fillCentersList(centersObservable, centersJsonArray, "");

        //prendo i dati del cittadino
        CompletableFuture<JSONArray> citizenJson = null;
        try {
            citizenJson = s.setEndpoint("Cittadini").makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert citizenJson != null;
        JSONArray citizenJsonArray = citizenJson.join();

        if(citizenJsonArray.length() > 0) {
            cittadino = new CittadinoModel(citizenJsonArray.getJSONObject(0));

            nameLabel.setText(cittadino.nome);
            surnameLabel.setText(cittadino.cognome);
            cfLabel.setText(cittadino.codiceFiscale);

        }


        //  listener ricerca per nome

        CompletableFuture<JSONArray> finalCentersJson = centersJson;
        nameSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaCentroVaccinale(centersObservable, finalCentersJson, null, newValue, true);
        });

        // listener ricerca per comune e tipologia

        CompletableFuture<JSONArray> finalAddressesJson = addressesJson;
        citySearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaCentroVaccinale(centersObservable, finalCentersJson, finalAddressesJson, newValue, false);
        });


        //listener su listview

        vaccineCenterList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<CentroVaccinaleModel>() {
                    public void changed(ObservableValue<? extends CentroVaccinaleModel> observableValue, CentroVaccinaleModel centroVaccinaleModel, CentroVaccinaleModel t1) {

                        if(t1 == null)
                            return;

                        JSONArray centreList = finalCentersJson.join();
                        JSONArray addressesList = finalAddressesJson.join();
                        for (int i = 0; i < centreList.length(); i++){
                            if (centreList.getJSONObject(i).getString("nomeCentro").toLowerCase(Locale.ROOT).equals(t1.nomeCentro.toLowerCase(Locale.ROOT))) {
                                selectedCentre = new CentroVaccinaleModel(centreList.getJSONObject(i));

                                for (int j = 0; j < addressesList.length(); j++)
                                    if(selectedCentre.idIndirizzo == addressesList.getJSONObject(j).getInt("idIndirizzo"))
                                        selectedCentre.setAddress(new IndirizzoModel(addressesList.getJSONObject(j)));

                                nameLabel2.setText(selectedCentre.nomeCentro);
                                typeLabel.setText(selectedCentre.tipologiaCentro);

                                addressLabel.setText(selectedCentre.indirizzo.toString());

                                try {
                                    checkRegistration(cittadino.idCittadino, selectedCentre.idCentro);
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * search function to filter results
     *  @param obs observable list of components
     * @param futureJson expected center json data
     * @param addressesJson expected addresses json data
     * @param filter string to filter with
     * @param nameOrCity bool to choose which search type to use
     */
    private void cercaCentroVaccinale(ObservableList<CentroVaccinaleModel> obs,
                                      CompletableFuture<JSONArray> futureJson,
                                      CompletableFuture<JSONArray> addressesJson,
                                      String filter,
                                      boolean nameOrCity)
    {
        JSONArray centersJson = futureJson.join();
        if(nameOrCity) {

            vaccineCenterList.getItems().clear();
            fillCentersList(obs, centersJson, filter);
        }
        else
        {

            JSONArray addressesArray = addressesJson.join();

            vaccineCenterList.getItems().clear();
            String tipologia = checkType.getValue();
            JSONArray filteredCentersJson = new JSONArray();
            JSONArray finalCentersJson = new JSONArray();

            //filtro per tipologia
            for (int i = 0; i < centersJson.length(); i++)
                if(centersJson.getJSONObject(i).getString("tipologiaCentro").toLowerCase(Locale.ROOT).equals(tipologia.toLowerCase()))
                    filteredCentersJson.put(centersJson.getJSONObject(i));

            //filtro per comune
            for (int i = 0; i < filteredCentersJson.length(); i++){
                int idIndirizzo = centersJson.getJSONObject(i).getInt("idIndirizzo");
                for (int j = 0; j < addressesArray.length(); j++){
                    if(addressesArray.getJSONObject(j).getInt("idIndirizzo") == idIndirizzo)
                        if(addressesArray.getJSONObject(j).getString("comune")
                                .toLowerCase(Locale.ROOT)
                                .contains(filter.toLowerCase(Locale.ROOT)))
                            finalCentersJson.put(centersJson.getJSONObject(i));
                }
            }

            fillCentersList(obs, finalCentersJson, "");
        }
    }

    /**
     * @param obs observable list of components
     * @param centersJson json data to fill the listview with
     * @param filter string to filter out certain names
     */
    private void fillCentersList(ObservableList<CentroVaccinaleModel> obs, JSONArray centersJson, String filter)
    {
        obs.removeAll();
        if(centersJson != null && centersJson.length() > 0){
            for(int i = 0; i < centersJson.length(); i++){

                JSONObject json = centersJson.getJSONObject(i);

                CentroVaccinaleModel tmp = new CentroVaccinaleModel(json);
                if(filter.length() == 0)
                    obs.add(tmp);
                else {
                    if (tmp.nomeCentro.toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)))
                        obs.add(tmp);
                }
            }
        }
    }

    /** the function to check if a user is registered to a center
     * @param idCittadino the citizen id
     * @param idCentro the center id
     * @throws IOException exception for the web call
     * @throws InterruptedException exception for the web call
     */
    private void checkRegistration(int idCittadino, int idCentro) throws IOException, InterruptedException {
        new Thread(() -> {
            CompletableFuture<JSONArray> returnJson = null;
            try {
                 returnJson = s.setEndpoint("RegistrazioniCentriVaccinali?and=(idCittadino.eq." + idCittadino + ",idCentro.eq." + idCentro + ")").setMethod(WebMethods.GET).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert returnJson != null;

            boolean visible = true;


            if(returnJson.join().length() == 0) {
                regButton.setVisible(visible);
                registeredLabel.setVisible(!visible);
                regButton.setOnAction(new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        JSONObject json = new JSONObject();
                        json.put("idCittadino", idCittadino).put("idCentro", idCentro);
                        try {
                            s.setMethod(WebMethods.POST).setEndpoint("RegistrazioniCentriVaccinali").setData(json).makeRequest();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        registeredLabel.setVisible(visible);
                        regButton.setVisible(!visible);
                        insertEvent.setVisible(!visible);
                    }

                });
            } else {
                regButton.setVisible(!visible);
                registeredLabel.setVisible(visible);
            }

            loadVaccinations();

        }).start();
    }

    /**
     * the function to load vaccinations into the ListView
     */
    private void loadVaccinations() {
        new Thread(() -> {
            CompletableFuture<JSONArray> j = null;
            try {
                j = s.setMethod(WebMethods.GET).setEndpoint("Vaccinati_"+selectedCentre.nomeCentro+"?idCittadino=eq."+cittadino.idCittadino).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert j != null;
            JSONArray jsonArray = j.join();

            //previene problemi di Not on FX application thread
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    vaccinationsObservable.clear();

                    if(jsonArray != null && jsonArray.length() > 0){
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            VaccinazioneModel tmp = new VaccinazioneModel(json);
                            vaccinationsObservable.add(tmp);
                        }
                    } else {
                        regButton.setVisible(false);
                        registeredLabel.setVisible(false);
                    }
                }
            });

        }).start();

        vaccinationsList.getSelectionModel().selectedItemProperty()
            .addListener(new ChangeListener<VaccinazioneModel>() {
                @Override
                public void changed(ObservableValue<? extends VaccinazioneModel> observableValue, VaccinazioneModel vaccinazioneModel, VaccinazioneModel t1) {
                    if(t1 != null) {
                        insertEvent.setVisible(true);
                        vaccinationWarningLabel.setVisible(false);
                        selectedVaccination = t1;

                        insertEvent.setOnAction(new EventHandler() {

                            @Override
                            public void handle(Event event) {

                                //inserisciEventiAvversi

                                Stage dialog = new Stage();
                                Parent root = null;
                                FXMLLoader loader = new FXMLLoader(App.class.getResource( "dialog.fxml" ));
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                DialogController d = loader.getController();
                                d.idCentro = selectedCentre.idCentro;
                                d.idVaccinazione = selectedVaccination.idVaccinazione;
                                dialog.setScene(new Scene(root));
                                dialog.initOwner(insertEvent.getContextMenu());
                                dialog.initModality(Modality.APPLICATION_MODAL);
                                dialog.showAndWait();

                                loadEvents();
                            }
                        });
                        loadEvents();
                    } else {

                        eventsObservable.clear();

                        insertEvent.setVisible(false);
                        vaccinationWarningLabel.setVisible(true);
                    }
                }
            });

    }

    /**
     * the function to load events into the ListView
     */
    private void loadEvents()
    {

        new Thread(() -> {
            CompletableFuture<JSONArray> j = null;
            try {
                j = s.setMethod(WebMethods.GET).setEndpoint("EventiAvversi?idVaccinazione=eq."+selectedVaccination.idVaccinazione).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert j != null;
            JSONArray jsonArray = j.join();

            //previene problemi di Not on FX application thread
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    eventsObservable.clear();

                    if(jsonArray != null && jsonArray.length() > 0){
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            EventoAvversoModel tmp = new EventoAvversoModel(json);
                            eventsObservable.add(tmp);
                        }
                    }
                }
            });

        }).start();
    }
}
