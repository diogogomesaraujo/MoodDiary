package org.example.frontend;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class StatisticsController {
    @FXML private PieChart emotionChart;
    @FXML private Label thoughtsShared;
    @FXML private Label mostFeltEmotion;

    @FXML
    public void initialize() {
        setupCharts();
        updateStatistics();
    }

    private void setupCharts() {
        // Set up data for the charts
        emotionChart.getData().addAll(
                new PieChart.Data("Joy", 50),
                new PieChart.Data("Fear", 50)
        );
    }

    private void updateStatistics() {
        thoughtsShared.setText("150 thoughts");
        mostFeltEmotion.setText("Joy");
    }
}
