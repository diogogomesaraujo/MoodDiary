package org.example.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    private Button dashboardButton;
    @FXML
    private Button journalButton;
    @FXML
    private Button statisticsButton;
    @FXML
    private Button archiveButton;
    @FXML
    private Button settingsButton;

    private BorderPane root;

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public void setStage(Stage stage) {
        // Your code for setting the stage
    }

    @FXML
    private void handleHomeAction() throws IOException {
        loadPage("dashboard.fxml");
        setSelectedButton(dashboardButton);
    }

    @FXML
    private void handleJournalAction() throws IOException {
        loadPage("diary-page.fxml");
        setSelectedButton(journalButton);
    }

    @FXML
    private void handleStatisticsAction() throws IOException {
        loadPage("statistics.fxml");
        setSelectedButton(statisticsButton);
    }

    @FXML
    private void handleArchiveAction() throws IOException {
        loadPage("archive.fxml");
        setSelectedButton(archiveButton);
    }

    @FXML
    private void handleSettingsAction() throws IOException {
        loadPage("settings.fxml");
        setSelectedButton(settingsButton);
    }

    @FXML
    private void handleButtonAction() throws IOException {
        loadPage("diary-page.fxml");
        setSelectedButton(journalButton); // Select the journal button when the bottom button is clicked
    }

    private void loadPage(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root.setCenter(loader.load());
    }

    public void setSelectedButton(Button selectedButton) {
        dashboardButton.getStyleClass().remove("selected");
        journalButton.getStyleClass().remove("selected");
        statisticsButton.getStyleClass().remove("selected");
        archiveButton.getStyleClass().remove("selected");
        settingsButton.getStyleClass().remove("selected");

        selectedButton.getStyleClass().add("selected");
    }

    public Button getDashboardButton() {
        return dashboardButton;
    }
}
