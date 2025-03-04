package com.example.baza;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class tworzenieTabeli extends Application {
    private TextField tableNameField, columnNameField, columnTypeField, columnLengthField;
    private CheckBox autoIncrementCheckBox;
    private Button addColumnButton, createTableButton, removeColumnButton;
    private TableView<ColumnData> columnTable;
    private ObservableList<ColumnData> columnDataList;

    @Override
    public void start(Stage primaryStage) {
        // Pole do wpisania nazwy tabeli
        tableNameField = new TextField();
        tableNameField.setPromptText("Nazwa tabeli");

        // Pola do dodawania kolumn
        columnNameField = new TextField();
        columnNameField.setPromptText("Nazwa kolumny");

        columnTypeField = new TextField();
        columnTypeField.setPromptText("Typ danych (np. INT, VARCHAR)");

        columnLengthField = new TextField();
        columnLengthField.setPromptText("Długość (opcjonalne)");

        autoIncrementCheckBox = new CheckBox("AUTO_INCREMENT");

        addColumnButton = new Button("Dodaj kolumnę");
        createTableButton = new Button("Utwórz tabelę");
        removeColumnButton = new Button("Usuń wybraną kolumnę");

        columnDataList = FXCollections.observableArrayList();
        columnTable = new TableView<>(columnDataList);

        TableColumn<ColumnData, String> nameColumn = new TableColumn<>("Nazwa kolumny");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().columnNameProperty());

        TableColumn<ColumnData, String> typeColumn = new TableColumn<>("Typ danych");
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().columnTypeProperty());

        TableColumn<ColumnData, String> lengthColumn = new TableColumn<>("Długość");
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().columnLengthProperty());

        TableColumn<ColumnData, String> autoIncColumn = new TableColumn<>("AUTO_INCREMENT");
        autoIncColumn.setCellValueFactory(cellData -> cellData.getValue().autoIncrementProperty().asString());

        columnTable.getColumns().addAll(nameColumn, typeColumn, lengthColumn, autoIncColumn);

        addColumnButton.setOnAction(e -> addColumn());
        removeColumnButton.setOnAction(e -> removeSelectedColumn());
        createTableButton.setOnAction(e -> createTable());

        HBox columnInputBox = new HBox(10, columnNameField, columnTypeField, columnLengthField, autoIncrementCheckBox, addColumnButton);
        columnInputBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, tableNameField, columnInputBox, columnTable, removeColumnButton, createTableButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 600, 450);
        primaryStage.setTitle("Tworzenie Tabeli");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addColumn() {
        String columnName = columnNameField.getText().trim();
        String columnType = columnTypeField.getText().trim();
        String columnLength = columnLengthField.getText().trim();
        boolean autoIncrement = autoIncrementCheckBox.isSelected();

        if (!columnName.isEmpty() && !columnType.isEmpty()) {
            columnDataList.add(new ColumnData(columnName, columnType, columnLength, autoIncrement));
            columnNameField.clear();
            columnTypeField.clear();
            columnLengthField.clear();
            autoIncrementCheckBox.setSelected(false);
        } else {
            showAlert("Błąd", "Podaj nazwę i typ kolumny!");
        }
    }

    private void removeSelectedColumn() {
        ColumnData selectedColumn = columnTable.getSelectionModel().getSelectedItem();
        if (selectedColumn != null) {
            columnDataList.remove(selectedColumn);
        } else {
            showAlert("Błąd", "Wybierz kolumnę do usunięcia!");
        }
    }

    private void createTable() {
        String tableName = tableNameField.getText().trim();
        if (tableName.isEmpty() || columnDataList.isEmpty()) {
            showAlert("Błąd", "Podaj nazwę tabeli i dodaj kolumny!");
            return;
        }

        StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (ColumnData column : columnDataList) {
            query.append(column.getColumnName()).append(" ").append(column.getColumnType());

            if (!column.getColumnLength().isEmpty()) {
                query.append("(").append(column.getColumnLength()).append(")");
            }

            if (column.isAutoIncrement()) {
                query.append(" AUTO_INCREMENT PRIMARY KEY");
            }

            query.append(", ");
        }
        query.delete(query.length() - 2, query.length()); // Usunięcie ostatniego przecinka
        query.append(");");

        try (Connection connection = baza.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query.toString());
            showAlert("Sukces", "Tabela '" + tableName + "' została utworzona!");
        } catch (SQLException e) {
            showAlert("Błąd SQL", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
