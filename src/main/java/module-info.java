module cittadini {
    requires javafx.controls;
    requires javafx.fxml;

    opens cittadini to javafx.fxml;
    exports cittadini;
    exports cittadini.controllers;
}
