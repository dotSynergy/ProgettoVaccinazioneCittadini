module cittadini {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;

    opens cittadini to javafx.fxml;

    exports cittadini;
    exports cittadini.controllers;
    exports cittadini.web;
}
