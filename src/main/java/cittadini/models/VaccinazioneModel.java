package cittadini.models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class VaccinazioneModel {

    public int idVaccinazione;

    public int idCittadino;

    public DateTime dataVaccinazione;

    /**
     * model from json
     * @param json the json object
     */
    public VaccinazioneModel(JSONObject json){
        this(
                json.getInt("idVaccinazione"),
                json.getInt("idCittadino"),
                json.getString("dataVaccinazione")
        );
    }

    public VaccinazioneModel(int idVaccinazione, int idCittadino, String dateString){
        this.idCittadino = idCittadino;
        this.idVaccinazione = idVaccinazione;
        this.dataVaccinazione = new DateTime(dateString);
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dataVaccinazione.toDate());
    }

}
