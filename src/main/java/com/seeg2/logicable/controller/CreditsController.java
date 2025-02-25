package com.seeg2.logicable.controller;

import com.seeg2.logicable.Application;
import com.seeg2.logicable.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CreditsController implements Initializable {

    @FXML
    private TextFlow creditsTextFlow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text versionInfo = new Text(Application.APPLICATION_NAME + " " + Application.VERSION + '\n');
        versionInfo.setStyle("-fx-font-style: italic;");
        creditsTextFlow.getChildren().add(versionInfo);

        ArrayList<String> lines = getCreditsText();

        if (lines.isEmpty()) {
            return;
        }

        Text headline = new Text(lines.removeFirst());

        headline.setStyle("-fx-font-weight: bold;");
        creditsTextFlow.getChildren().add(headline);

        for (String line : lines) {
            creditsTextFlow.getChildren().add(new Text(line));
        }
    }

    private ArrayList<String> getCreditsText() {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/text/credits.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line + '\n');
            }
        } catch (Exception e) {
            Logger.error("Failed to load credits text");
        }

        return lines;
    }
}