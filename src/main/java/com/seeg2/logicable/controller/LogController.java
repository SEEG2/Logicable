package com.seeg2.logicable.controller;

import com.seeg2.logicable.logger.LogEntry;
import com.seeg2.logicable.logger.LogEventListener;
import com.seeg2.logicable.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements LogEventListener, Initializable {
    @FXML
    private TextFlow logTextFlow;
    public static Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setOnCloseRequest((action) -> Logger.unsubscribe(this));
        Logger.subscribe(this);
        setLogText();
    }

    @Override
    public void onLogEntryAdded(LogEntry logEntry) {
        addLogEntry(logEntry);
    }

    private void addLogEntry(LogEntry logEntry) {
        Text text = new Text(logEntry.getText() + '\n');
        text.setStyle("-fx-fill:" + Logger.getColorForEntry(logEntry) + ";");
        logTextFlow.getChildren().add(text);
    }

    private void setLogText() {
        for (LogEntry entry : Logger.getLogEntries()) {
            addLogEntry(entry);
        }
    }
}