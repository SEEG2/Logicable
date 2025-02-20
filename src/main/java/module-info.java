module com.seeg2.logicable {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.seeg2.logicable to javafx.fxml;
    exports com.seeg2.logicable;
    exports com.seeg2.logicable.controller;
    exports com.seeg2.logicable.logger;
    exports com.seeg2.logicable.simulationElement;
    exports com.seeg2.logicable.logicGate;
    opens com.seeg2.logicable.controller to javafx.fxml;
}