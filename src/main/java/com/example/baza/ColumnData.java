package com.example.baza;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

public class ColumnData {
    private final StringProperty columnName;
    private final StringProperty columnType;
    private final StringProperty columnLength;
    private final BooleanProperty autoIncrement;

    public ColumnData(String columnName, String columnType, String columnLength, boolean autoIncrement) {
        this.columnName = new SimpleStringProperty(columnName);
        this.columnType = new SimpleStringProperty(columnType);
        this.columnLength = new SimpleStringProperty(columnLength);
        this.autoIncrement = new SimpleBooleanProperty(autoIncrement);
    }

    public StringProperty columnNameProperty() {
        return columnName;
    }

    public StringProperty columnTypeProperty() {
        return columnType;
    }

    public StringProperty columnLengthProperty() {
        return columnLength;
    }

    public BooleanProperty autoIncrementProperty() {
        return autoIncrement;
    }

    public String getColumnName() {
        return columnName.get();
    }

    public String getColumnType() {
        return columnType.get();
    }

    public String getColumnLength() {
        return columnLength.get();
    }

    public boolean isAutoIncrement() {
        return autoIncrement.get();
    }
}
