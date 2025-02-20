package com.seeg2.logicable.controller;

import com.seeg2.logicable.logger.LogEntry;
import com.seeg2.logicable.logger.LogEventListener;
import com.seeg2.logicable.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements LogEventListener, Initializable {
    @FXML
    private TextArea logTextArea;
    public static Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setOnCloseRequest((action) -> Logger.unsubscribe(this));
        Logger.subscribe(this);
        logTextArea.setText(Logger.getLogAsText());
    }

    @Override
    public void onLogEntryAdded(LogEntry logEntry) {
        logTextArea.appendText(logEntry.getText());
        logTextArea.appendText("\n");
    }
}