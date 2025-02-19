package com.seeg2.logicable;

import com.seeg2.logicable.Logger.Logger;
import javafx.fxml.FXML;
import java.net.URI;

public class MainController {
    @FXML
    public void onAboutClicked() {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(Application.GIT_HUB_PAGE_LINK));
        } catch (Exception e) {
            Logger.error("Could not open About-link.");
        }
    }

    @FXML
    public void onCloseClicked() {
        Application.close();
    }
}