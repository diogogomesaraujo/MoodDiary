package org.example.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArchiveController {

    @FXML
    private TableView<Entry> tableView;

    @FXML
    private TableColumn<Entry, String> dateColumn;

    @FXML
    private TableColumn<Entry, String> messageColumn;

    @FXML
    private TableColumn<Entry, String> emotionColumn;

    private static final String FILE_PATH = "diary_entries.json";

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        emotionColumn.setCellValueFactory(new PropertyValueFactory<>("emotion"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Enable constrained resize policy

        dateColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3)); // 30% of the table width
        messageColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.5)); // 50% of the table width
        emotionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2)); // 20% of the table width

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Enable constrained resize policy
        tableView.getItems().setAll(loadEntries());
    }


    private List<Entry> loadEntries() {
        List<Entry> entries = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (file.exists() && file.length() != 0) {
            try (FileReader reader = new FileReader(file)) {
                JsonReader jsonReader = Json.createReader(reader);
                JsonArray jsonArray = jsonReader.readArray();

                for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                    String date = jsonObject.getString("date");
                    String message = jsonObject.getString("text");
                    String emotion = jsonObject.getString("emotion");
                    entries.add(new Entry(date, message, emotion));
                }
            } catch (IOException | JsonParsingException e) {
                e.printStackTrace();
            }
        }

        return entries;
    }

    public static class Entry {
        private final String date;
        private final String message;
        private final String emotion;

        public Entry(String date, String message, String emotion) {
            this.date = date;
            this.message = message;
            this.emotion = emotion;
        }

        public String getDate() {
            return date;
        }

        public String getMessage() {
            return message;
        }

        public String getEmotion() {
            return emotion;
        }
    }
}
