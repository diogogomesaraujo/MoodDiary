package com.example.mooddiary;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HelloController {

    @FXML
    private TextArea inputTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private ImageView emotionImageView;

    @FXML
    private VBox rightContainer; // Add this line to your controller

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleTextSubmit());
        inputTextArea.setOnKeyPressed(this::handleKeyPress);

        // Set the initial transparent background color for the text area
        inputTextArea.setBackground(new Background(new BackgroundFill(Color.rgb(128, 128, 128, 0.3), new CornerRadii(15), null)));
    }

    private void handleTextSubmit() {
        String inputText = inputTextArea.getText();
        String emotion = detectEmotion(inputText);
        updateEmotionImage(emotion);
        updateRightContainerColor(emotion); // Add this line to update the container color
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleTextSubmit();
        }
    }

    private String detectEmotion(String text) {
        // Simple keyword-based detection logic for demonstration purposes
        if (text.contains("happy") || text.contains("joy")) {
            return "Happiness";
        } else if (text.contains("sad") || text.contains("grief")) {
            return "Sadness";
        } else if (text.contains("angry") || text.contains("frustrated")) {
            return "Anger";
        } else if (text.contains("surprised") || text.contains("astonished")) {
            return "Surprise";
        } else if (text.contains("love") || text.contains("affection")) {
            return "Love/Affection";
        }
        return "Neutral"; // Default or unknown emotion
    }

    private void updateEmotionImage(String emotion) {
        String imagePath = "/images/";
        switch (emotion) {
            case "Happiness":
                imagePath += "happiness.png";
                break;
            case "Sadness":
                imagePath += "sadness.png";
                break;
            case "Anger":
                imagePath += "anger.png";
                break;
            case "Surprise":
                imagePath += "surprise.png";
                break;
            case "Love/Affection":
                imagePath += "love_affection.png";
                break;
            default:
                imagePath += "neutral.png";
                break;
        }
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        emotionImageView.setImage(image);
    }

    private void updateRightContainerColor(String emotion) {
        Color color;
        switch (emotion) {
            case "Happiness":
                color = Color.web("#FFEBB5"); // Pastel Yellow
                break;
            case "Sadness":
                color = Color.web("#A7C7E7"); // Pastel Blue
                break;
            case "Anger":
                color = Color.web("#FFB5B5"); // Pastel Red
                break;
            case "Surprise":
                color = Color.web("#FFD1A7"); // Pastel Orange
                break;
            case "Love/Affection":
                color = Color.web("#FFCCE5"); // Pastel Pink
                break;
            default:
                color = Color.web("#D3D3D3"); // Pastel Gray for Neutral
                break;
        }
        rightContainer.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, null)));
    }
}
