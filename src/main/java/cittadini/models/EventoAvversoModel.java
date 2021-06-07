package cittadini.models;

import java.util.Date;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class EventoAvversoModel {
    public int idEvento;
    public String descrizione;
    public int gravita;
    public String note;
    public int idVaccinazione;
    public int idCentro;
    public Date inseritoIl;

    public EventoAvversoModel(int idEvento, String descrizione, int gravita, String note, int idVaccinazione, int idCentro){

        this.idEvento = idEvento;
        this.descrizione = descrizione;
        this.gravita = gravita;
        this.note = note;
        this.idVaccinazione = idVaccinazione;
        this.idCentro = idCentro;

    }

    @Override
    public String toString() {
        return descrizione;
    }
}
