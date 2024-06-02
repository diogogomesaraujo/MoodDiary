package org.example.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/org/example/frontend/sidebar.fxml"));
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/org/example/frontend/dashboard.fxml"));

        BorderPane root = new BorderPane();
        root.setLeft(sidebarLoader.load());
        root.setCenter(dashboardLoader.load());

        Scene scene = new Scene(root, 689, 500); // Adjust dimensions as needed
        scene.getStylesheets().add(getClass().getResource("/org/example/frontend/style.css").toExternalForm());
        stage.setTitle("MoodDiary");
        stage.setScene(scene);

        // Set fixed size and disable resizing
        stage.setWidth(689); // Adjust width as needed
        stage.setHeight(500);
        stage.setResizable(false);

        stage.show();

        // Pass the navigation controller to the sidebar controller
        SidebarController sidebarController = sidebarLoader.getController();
        sidebarController.setStage(stage);
        sidebarController.setRoot(root);

        // Set the dashboard button as selected
        sidebarController.setSelectedButton(sidebarController.getDashboardButton());
    }

    public static void main(String[] args) {
        launch();
    }
}
