package org.example.frontend;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardController {
    @FXML
    private Label mostFeltEmotionLabel;
    @FXML
    private Label pagesWrittenLabel;
    @FXML
    private LineChart<String, Number> pagesWrittenChart;
    @FXML
    private StackPane emotionCard;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BorderPane root;

    @FXML
    public void initialize() {
        String jsonFilePath = "diary_entries.json"; // Update this path
        File jsonFile = new File(jsonFilePath);

        if (!jsonFile.exists() || jsonFile.length() == 0) {
            showJournalButton();
            return;
        }

        try (JsonReader reader = Json.createReader(new FileReader(jsonFile))) {
            JsonArray entries = reader.readArray();

            if (entries.isEmpty()) {
                showJournalButton();
                return;
            }

            // Calculate statistics
            Map<String, Long> emotionCount = new HashMap<>();
            for (JsonObject entry : entries.getValuesAs(JsonObject.class)) {
                String emotion = entry.getString("emotion");
                emotionCount.put(emotion, emotionCount.getOrDefault(emotion, 0L) + 1);
            }

            String mostFeltEmotion = emotionCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("None");

            mostFeltEmotionLabel.setText(capitalize(mostFeltEmotion));

            // Set emotion card background based on most felt emotion
            emotionCard.getStyleClass().removeIf(style -> style.startsWith("emotion-card"));
            emotionCard.getStyleClass().add("emotion-card");
            emotionCard.getStyleClass().add(mostFeltEmotion);

            long pagesWritten = entries.size();
            pagesWrittenLabel.setText(String.valueOf(pagesWritten));

            // Plot chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Pages Written");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Integer> dateCounts = new HashMap<>();
            List<String> last7Days = getLast7Days();
            for (String day : last7Days) {
                dateCounts.put(day, 0); // Initialize counts for each day
            }

            for (JsonObject entry : entries.getValuesAs(JsonObject.class)) {
                Date date = sdf.parse(entry.getString("date"));
                String dayOfWeek = new SimpleDateFormat("E", Locale.ENGLISH).format(date); // Abbreviated day name in English
                dateCounts.put(dayOfWeek, dateCounts.getOrDefault(dayOfWeek, 0) + 1);
            }

            for (String day : last7Days) {
                series.getData().add(new XYChart.Data<>(day, dateCounts.get(day)));
            }

            xAxis.setCategories(FXCollections.observableArrayList(last7Days));
            xAxis.setLabel("");

            yAxis.setLabel(""); // Remove the y-axis label

            pagesWrittenChart.getData().add(series);
            pagesWrittenChart.setLegendVisible(false); // Remove the legend

        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
            showJournalButton();
        }
    }

    private List<String> getLast7Days() {
        List<String> days = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.ENGLISH); // Abbreviated day names in English
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            days.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_WEEK, -1);
        }
        Collections.reverse(days); // To keep the order from oldest to latest
        return days;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showJournalButton() {
        Button journalButton = new Button("Go to Journal");
        journalButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("diary-page.fxml"));
                root.setCenter(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox vbox = new VBox(journalButton);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        root.setCenter(vbox);
    }
}
