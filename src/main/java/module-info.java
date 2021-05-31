/**
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue - mat.740727
 *       @author Andrea Pini - mat.740675
 */

module cittadini {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;

    opens cittadini to javafx.fxml;
    opens cittadini.controllers to javafx.fxml;

    exports cittadini;
    exports cittadini.controllers;
    exports cittadini.web;
}
