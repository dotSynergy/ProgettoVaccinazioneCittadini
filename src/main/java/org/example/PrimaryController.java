package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

/*
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue -
 *       Andrea Pini - mat.740675
 */


public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
