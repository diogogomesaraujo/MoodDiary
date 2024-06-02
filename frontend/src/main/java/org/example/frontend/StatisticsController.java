package org.example.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsController {

    @FXML
    private PieChart emotionChart;
    @FXML
    private Label topEmotion1;
    @FXML
    private Label topEmotion2;
    @FXML
    private Label topEmotion3;

    @FXML
    public void initialize() {
        updateStatistics();
    }

    private void updateStatistics() {
        Map<String, Integer> emotionCounts = readEmotionCounts();
        if (emotionCounts.isEmpty()) {
            topEmotion1.setText("No data available.");
            topEmotion2.setText("");
            topEmotion3.setText("");
        } else {
            updatePieChart(emotionCounts);
            updateTopEmotions(emotionCounts);
        }
    }

    private Map<String, Integer> readEmotionCounts() {
        Map<String, Integer> counts = new HashMap<>();
        try (FileReader reader = new FileReader("diary_entries.json");
             JsonReader jsonReader = Json.createReader(reader)) {
            JsonArray entries = jsonReader.readArray();
            for (JsonObject entry : entries.getValuesAs(JsonObject.class)) {
                String emotion = entry.getString("emotion");
                counts.put(emotion, counts.getOrDefault(emotion, 0) + 1);
            }
        } catch (IOException e) {
            System.err.println("Error reading diary entries: " + e.getMessage());
        }
        return counts;
    }

    private void updatePieChart(Map<String, Integer> emotionCounts) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        emotionCounts.forEach((emotion, count) -> pieData.add(new PieChart.Data(emotion, count)));
        emotionChart.setData(pieData);
        applyChartColors();
    }

    private void applyChartColors() {
        emotionChart.getData().forEach(data -> {
            String color;
            switch (data.getName().toLowerCase()) {
                case "joy":
                    color = "#C0C0C0"; // Light gray
                    break;
                case "sadness":
                    color = "#808080"; // Medium gray
                    break;
                case "anger":
                    color = "#404040"; // Dark gray
                    break;
                case "fear":
                    color = "#A9A9A9"; // Darker gray
                    break;
                case "surprise":
                    color = "#E0E0E0"; // Lighter gray
                    break;
                case "disgust":
                    color = "#696969"; // Another shade of gray
                    break;
                default:
                    color = "#000000"; // Black
                    break;
            }
            data.getNode().setStyle("-fx-pie-color: " + color + ";");
        });
    }

    private void updateTopEmotions(Map<String, Integer> emotionCounts) {
        List<Map.Entry<String, Integer>> sortedEmotions = emotionCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        topEmotion1.setText(sortedEmotions.size() > 0 ? "Most frequent: " + sortedEmotions.get(0).getKey() + " (" + sortedEmotions.get(0).getValue() + ")" : "Most frequent: N/A");
        topEmotion2.setText(sortedEmotions.size() > 1 ? "Second most frequent: " + sortedEmotions.get(1).getKey() + " (" + sortedEmotions.get(1).getValue() + ")" : "Second most frequent: N/A");
        topEmotion3.setText(sortedEmotions.size() > 2 ? "Third most frequent: " + sortedEmotions.get(2).getKey() + " (" + sortedEmotions.get(2).getValue() + ")" : "Third most frequent: N/A");
    }
}
