package org.example.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label dashboardLabel;

    @FXML
    public void initialize() {
        dashboardLabel.setText("Welcome to the Dashboard!");
    }
}
