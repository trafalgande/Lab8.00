package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static jdk.nashorn.internal.objects.ArrayBufferView.length;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Creature implements Serializable, Comparable<Creature> {
    public void setName(String name) {
        this.name.set(name);
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public Creature(String name, String status, String location) {
        this.name = new SimpleStringProperty(name);
        this.location = new SimpleStringProperty(location);
        this.status = new SimpleStringProperty(status);
        this.createdTime = LocalDateTime.now();
    }
    public Creature(){
        this.name = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    SimpleStringProperty name;
    LocalDateTime createdTime;

    public String getStatus() {
        return status.getValue();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getLocation() {
        return location.getValue();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public String getCrearedTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm dd.MM");
        Random random = new Random();
        return createdTime.minusHours((long) (Math.random()*10)).minusDays((long) random.nextInt(4)).format(dateTimeFormatter);
    }

    private final StringProperty status;
    private final StringProperty location;

    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

//    @JsonCreator
//    public Creature(@JsonProperty("name") StringProperty name,
//                    @JsonProperty("location") Location location,
//                    @JsonProperty("status") StringProperty status) {
//        this.name = name;
//        this.location = Location.valueOf(location);
//        this.status = Status.valueOf(status);
//    }

//    @Override
//    public int compareTo(Creature creature) {
//        int nameComparing = name.compareTo(creature.name);
//        if (nameComparing != 0) {
//            return nameComparing;
//        }
//
//        int statusComparing = status.compareTo(creature.status);
//        if (statusComparing != 0) {
//            return statusComparing;
//        }
//
//        return location.compareTo(creature.location);
//    }

    @Override
    public String toString() {
        return "Creature{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", location=" + location +
                '}';
    }

    @Override
    public int compareTo(Creature creature) {
        int one = length(this.name);
        int two = length(creature.name);
        return (one - two);
    }
}