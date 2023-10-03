module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires sphinx4.core;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}