module com.seeg2.logicable {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.seeg2.logicable to javafx.fxml;
    exports com.seeg2.logicable;
}