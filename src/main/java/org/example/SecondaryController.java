package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

/*
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue -
 *       Andrea Pini - mat.740675
 */

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}