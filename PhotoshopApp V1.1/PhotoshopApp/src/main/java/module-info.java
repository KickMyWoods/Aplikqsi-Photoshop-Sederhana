module com.example.photoshopapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.photoshopapp to javafx.fxml;
    exports com.example.photoshopapp;
}