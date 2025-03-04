package com.example.baza;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class obslugaTabeli extends Application {
    private TextField nameField, ageField, gradeField, idField, tableField;
    private Button addButton, updateButton, deleteButton, tableButton;
    private TableView<Student> studentTable;
    private String currentTableName = null; // Przechowuje nazwę aktywnej tabeli

    public static void obsluga(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        tableField = new TextField();
        tableField.setPromptText("Podaj Nazwę Tablicy");

        tableButton = new Button("Włącz");

        nameField = new TextField();
        nameField.setPromptText("Podaj Imię");

        ageField = new TextField();
        ageField.setPromptText("Podaj Wiek");

        gradeField = new TextField();
        gradeField.setPromptText("Podaj Klasę");

        idField = new TextField();
        idField.setPromptText("Podaj ID");

        addButton = new Button("Dodaj");
        updateButton = new Button("Zaktualizuj");
        deleteButton = new Button("Usuń");

        studentTable = new TableView<>();

        addButton.setOnAction(e -> addStudent());
        updateButton.setOnAction(e -> updateStudent());
        deleteButton.setOnAction(e -> deleteStudent());

        tableButton.setOnAction(e -> connectToTable());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        HBox tableBox = new HBox(10, tableField, tableButton);
        tableBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, tableBox, nameField, ageField, gradeField, idField, buttonBox, studentTable);
        Scene scene = new Scene(layout, 500, 500);

        primaryStage.setTitle("Obsługa Bazy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToTable() {
        String tableName = tableField.getText().trim();
        if (tableName.isEmpty()) {
            showAlert("Błąd", "Podaj nazwę tabeli!");
            return;
        }

        currentTableName = tableName; // Ustawiamy aktywną tabelę
        studentTable.getColumns().clear();
        studentTable.getColumns().addAll(createColumns());

        refreshTable();
    }

    private void refreshTable() {
        if (currentTableName == null) {
            showAlert("Błąd", "Najpierw podaj nazwę tabeli!");
            return;
        }

        ObservableList<Student> students = baza.getAllStudents(currentTableName);
        studentTable.setItems(students);
    }

    private void addStudent() {
        if (currentTableName == null) {
            showAlert("Błąd", "Najpierw wybierz tabelę!");
            return;
        }

        try {
            baza.addStudent(currentTableName, nameField.getText(), Integer.parseInt(ageField.getText()), gradeField.getText());
            refreshTable();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "Wiek musi być liczbą!");
        }
    }

    private void updateStudent() {
        if (currentTableName == null) {
            showAlert("Błąd", "Najpierw wybierz tabelę!");
            return;
        }

        try {
            baza.updateStudent(currentTableName, nameField.getText(), Integer.parseInt(ageField.getText()), gradeField.getText(), Integer.parseInt(idField.getText()));
            refreshTable();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "ID i wiek muszą być liczbami!");
        }
    }

    private void deleteStudent() {
        if (currentTableName == null) {
            showAlert("Błąd", "Najpierw wybierz tabelę!");
            return;
        }

        try {
            baza.deleteStudent(currentTableName, Integer.parseInt(idField.getText()));
            refreshTable();
        } catch (NumberFormatException e) {
            showAlert("Błąd", "ID musi być liczbą!");
        }
    }

    private List<TableColumn<Student, ?>> createColumns() {
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());

        TableColumn<Student, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());

        TableColumn<Student, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asString());

        List<TableColumn<Student, ?>> columns = new ArrayList<>();
        columns.add(idColumn);
        columns.add(nameColumn);
        columns.add(ageColumn);
        columns.add(gradeColumn);

        return columns;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
