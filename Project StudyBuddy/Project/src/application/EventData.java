package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventData {
    private final StringProperty date;
    private final StringProperty description;
    private final SimpleIntegerProperty eventId;

    public EventData(String date, String description, int eventId) {
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
        this.eventId = new SimpleIntegerProperty(eventId);
    }

    public String getDate() {
        return date.get();
    }

    public String getDescription() {
        return description.get();
    }

    public int getEventId() {
        return eventId.get();
    }
}