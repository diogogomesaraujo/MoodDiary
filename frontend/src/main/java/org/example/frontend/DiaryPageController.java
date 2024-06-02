package org.example.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryPageController {

    @FXML
    private Label sentimentLabel;

    @FXML
    private TextField textField;

    @FXML
    private ImageView emotionImageView;

    private static final String API_URL = "https://mooddiary.kreativitat.com/emotion";
    private static final String API_KEY = "8a352885-623b-4077-82fb-4d33f7dd8dd0";  // Replace with your actual API key
    private static final String FILE_PATH = "diary_entries.json";

    @FXML
    private void initialize() {
        // Add key event listener to text field to submit on Enter key press
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitText();
            }
        });
    }

    @FXML
    private void analyzeSentiment() {
        String text = textField.getText();
        String sentiment = analyzeTextSentiment(text);
        updateSentimentLabel(sentiment);
        updateEmotionImage(sentiment);
        saveTextAndEmotion(text, sentiment);
    }

    @FXML
    private void submitText() {
        analyzeSentiment();
        textField.clear();
    }

    private String analyzeTextSentiment(String text) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString("{\"text\":\"" + text + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonReader jsonReader = Json.createReader(new java.io.StringReader(response.body()));
            JsonObject responseBody = jsonReader.readObject();
            return responseBody.getString("emotion").toLowerCase();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private void updateSentimentLabel(String emotion) {
        String message;
        switch (emotion) {
            case "joy":
                message = "You seem Happy!";
                break;
            case "sadness":
                message = "You seem Sad.";
                break;
            case "fear":
                message = "You seem Fearful.";
                break;
            case "surprise":
                message = "You seem Surprised!";
                break;
            case "neutral":
                message = "You seem Neutral.";
                break;
            case "disgust":
                message = "You seem Disgusted.";
                break;
            case "anger":
                message = "You seem Angry.";
                break;
            default:
                message = "❗ Error in analyzing sentiment.";
                break;
        }
        sentimentLabel.setText(message);
    }

    private void updateEmotionImage(String emotion) {
        String imagePath = "/images/emotions/" + emotion + ".png";
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl == null) {
            // Handle the case where the image is not found
            System.err.println("Image not found: " + imagePath);
            return;
        }

        Image image = new Image(imageUrl.toExternalForm());

        FadeTransition fadeOutImage = new FadeTransition(Duration.millis(500), emotionImageView);
        fadeOutImage.setFromValue(1.0);
        fadeOutImage.setToValue(0.0);
        fadeOutImage.setOnFinished(event -> {
            emotionImageView.setImage(image);

            FadeTransition fadeInImage = new FadeTransition(Duration.millis(500), emotionImageView);
            fadeInImage.setFromValue(0.0);
            fadeInImage.setToValue(1.0);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), emotionImageView);
            scaleIn.setFromX(0.5);
            scaleIn.setFromY(0.5);
            scaleIn.setToX(1.2);
            scaleIn.setToY(1.2);

            fadeInImage.play();
            scaleIn.play();
        });

        fadeOutImage.play();
    }

    private void saveTextAndEmotion(String text, String emotion) {
        JsonArray entries;
        File file = new File(FILE_PATH);

        // Check if the file exists and read the existing entries
        if (file.exists() && file.length() != 0) {
            try (FileReader reader = new FileReader(file)) {
                JsonReader jsonReader = Json.createReader(reader);
                entries = jsonReader.readArray();
            } catch (IOException | JsonParsingException e) {
                e.printStackTrace();
                entries = Json.createArrayBuilder().build();
            }
        } else {
            entries = Json.createArrayBuilder().build();
        }

        // Get current date
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Create a new entry
        JsonObject data = Json.createObjectBuilder()
                .add("text", text)
                .add("emotion", emotion)
                .add("date", date)
                .build();

        // Add the new entry to the list
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(entries);
        arrayBuilder.add(data);

        // Write the updated list back to the file
        try (FileWriter writer = new FileWriter(FILE_PATH);
             JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeArray(arrayBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
