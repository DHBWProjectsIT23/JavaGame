package org.itdhbw.futurewars.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SelectedTileService {
    // Singleton instance
    private static SelectedTileService instance;
    private final StringProperty selectedTileMessage = new SimpleStringProperty();

    // Private constructor
    private SelectedTileService() {
    }

    // Static method to get the singleton instance
    public static SelectedTileService getInstance() {
        if (instance == null) {
            instance = new SelectedTileService();
        }
        return instance;
    }

    public void setSelectedTileMessage(String message) {
        selectedTileMessage.set(message);
    }

    public StringProperty selectedTileMessageProperty() {
        return selectedTileMessage;
    }
}
