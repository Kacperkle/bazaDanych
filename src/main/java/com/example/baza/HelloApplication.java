package com.example.baza;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button btnWindowOne = new Button("Obsługa tablicy");
        Button btnWindowTwo = new Button("Tworzenie tablicy");

        btnWindowOne.setOnAction(e -> new obslugaTabeli().start(new Stage()));
        btnWindowTwo.setOnAction(e -> new tworzenieTabeli().start(new Stage()));

        VBox layout = new VBox(10, btnWindowOne, btnWindowTwo);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Strona główna");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
