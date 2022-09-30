module com.example.scrumfaster {
    requires javafx.controls;
    requires javafx.fxml;


    opens ScrumFaster to javafx.fxml;
    exports ScrumFaster;
}