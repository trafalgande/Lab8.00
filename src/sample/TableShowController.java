package sample;

import client.Client;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sun.security.util.Resources;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TableShowController implements Initializable {
    TreeMap<SimpleStringProperty, Creature> treeMap = new TreeMap<>(new Comparator<SimpleStringProperty>() {
        @Override
        public int compare(SimpleStringProperty o1, SimpleStringProperty o2) {
            int a;
            if (o1.equals(o2)) a = 1;
            else a = -1;
            return a;
        }
    });
    ObservableList<Map.Entry<SimpleStringProperty, Creature>> listData;

    @FXML
    private ImageView srbBut;

    @FXML
    private ImageView spaBut;

    @FXML
    private ImageView enBut;

    @FXML
    private ImageView ruBut;

    @FXML
    private TableView<Map.Entry<SimpleStringProperty, Creature>> tableCollections;

    @FXML
    private TableColumn<Map.Entry<SimpleStringProperty, Creature>, String> key;

    @FXML
    private TableColumn<Map.Entry<SimpleStringProperty, Creature>, String> name;

    @FXML
    private TableColumn<Map.Entry<SimpleStringProperty, Creature>, String> status;

    @FXML
    private TableColumn<Map.Entry<SimpleStringProperty, Creature>, String> loc;

    @FXML
    private TextField keyInTable;

    @FXML
    private TextField nameInTable;

    @FXML
    private TextField statusInTable;

    @FXML
    private TextField locInTable;

    @FXML
    private Button addInTable;

    @FXML
    private Button removeFromTable;

    @FXML
    private TextField userLogin;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane pane;

    @FXML
    private TextField coordinates;

    @FXML
    private TextField time;

    @FXML
    private TextField keyIfClick;

    static final String DB_URL = "jdbc:postgresql://localhost:5432/studs"; //localhost на pg
    static final String USER = "s264476";
    static final String PASS = "sny683";

    private static Connection connection;
    private static Statement statement;

    private String id_hostCurr = null;

    String pass;
    String login;
    String priznak;

    Client client = new Client();

    ObservableMap<String, Creature> observableMap;

    private int[] keys = new int[150];
    AtomicInteger t = new AtomicInteger(0);
    RotateTransition rotateTransition;

    URL location;
    ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resourceBundle = resources;

        login = CurrentUser.login;
        client = CurrentUser.client;
        userLogin.setEditable(false);
        userLogin.setText(login);
        tableCollections.setEditable(true);

        connect();
        getCollections();

        key.setCellValueFactory(param -> param.getValue().getKey()); // заполнили столбец ключей
        name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName())); //заполнили столбец имён
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getStatus())); // заполниил столбец статусов
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        loc.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getLocation())); //заполнили столбец локaций
        loc.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCollections.setItems(listData);

        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        drawer(pane);

    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void getCollections() {
        treeMap = new TreeMap<>(new Comparator<SimpleStringProperty>() { //обнуляем карту
            @Override
            public int compare(SimpleStringProperty o1, SimpleStringProperty o2) {
                int a;
                if (o1.equals(o2)) a = 1;
                else a = -1;
                return a;
            }
        });
        int i = 0;
        ResultSet r = null;
        try {
            r = statement.executeQuery("SELECT * FROM creature");
            while (r.next()) {
                String id_host = r.getString("id_host");
                id_hostCurr = id_host;
                String id_key1 = r.getString("id_key");
                String n_ame = r.getString("n_ame");
                String status = r.getString("status");
                String locate = r.getString("locate");
                Creature creature = new Creature(n_ame, status, locate);
                SimpleStringProperty id_key = new SimpleStringProperty(id_key1);
                treeMap.put(id_key, creature);
                keys[i] = Integer.parseInt(id_host);
                i++;
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listData = FXCollections.observableArrayList(treeMap.entrySet());
    }

    //insert{key}{"name" : "1111","status": "EAT","location": "ROOF"}
    public void addInTableActive() {
        if (keyInTable.getText().isEmpty() | nameInTable.getText().isEmpty() | statusInTable.getText().isEmpty() | locInTable.getText().isEmpty()) {
            new AlertInfo().showAlert(Alert.AlertType.ERROR, keyInTable.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("emptyFields.alert"));
        } else {
            String com = "insert " + keyInTable.getText().trim() + " " + nameInTable.getText().trim() + " " + statusInTable.getText().trim() + " " + locInTable.getText().trim();
            client.doCom(com, login, pass);
            getCollections();
            tableCollections.setItems(listData);
            drawer(pane);
        }
    }

    //remove{key}
    public void removeFromTableActive() {
        String try_id_host = null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM authuser WHERE login like '" + login + "'");
            while (resultSet.next()) {
                try_id_host = resultSet.getString("id");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (keyInTable.getText().isEmpty()) {
            new AlertInfo().showAlert(Alert.AlertType.ERROR, keyInTable.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("populateKey.alert"));
        } else {
            if (try_id_host.equals(id_hostCurr)|| try_id_host.equals("3")) {
                String com = "remove " + keyInTable.getText().trim();
                client.doCom(com, login, pass);
                getCollections();
                tableCollections.setItems(listData);
                drawer(pane);
            } else new AlertInfo().showAlert(Alert.AlertType.ERROR, tableCollections.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("denied.alert"));
        }
    }

    public void drawer(Pane pane) {
        pane.getChildren().clear();

        getCollections();
        int i = 0;
        for (Map.Entry<SimpleStringProperty, Creature> entry : treeMap.entrySet()) {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth((entry.getValue().nameProperty().toString().length() + entry.getValue().getStatus().length()) / 3);
            rectangle.setHeight((entry.getValue().getName().length() + entry.getValue().statusProperty().toString().length()) / 3);
            rectangle.setX(Math.random() * 190);
            rectangle.setArcHeight(5);
            rectangle.setArcWidth(5);
            rectangle.setY(Math.random() * 190);
            rectangle.setFill(Color.TRANSPARENT);
            System.out.println(keys[i]);
            String hexColor = String.format("#%06X", (0xFFFFFF & keys[i]*10000+12313));
            //rectangle.setStroke(Color.color(keys[i] / (double) amount * 0.01, keys[i] / (double) amount, keys[i] / (double) amount * 0.3));
            rectangle.setStroke(Color.web(hexColor));
            pane.getChildren().add(rectangle);

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), rectangle);
            fadeTransition.setFromValue(0.3);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();

            rectangle.setOnMouseClicked(event -> {
                RotateTransition rotateTransitionNew = new RotateTransition(Duration.seconds(10), rectangle);
                if (t.get() > 0) {
                    rotateTransition.stop();
                }
                rotateTransitionNew.setByAngle(6000);
                rotateTransitionNew.play();
                t.getAndIncrement();
                this.rotateTransition = rotateTransitionNew;
                coordinates.setText("X:" + Math.round(rectangle.getX()) + " " + "Y:" + Math.round(rectangle.getY()));
                time.setText(entry.getValue().getCrearedTime());
                keyIfClick.setText(entry.getKey().get());
            });
            i++;
        }
    }

    public void chaneOnEn(){

        try {
            pane.getScene().setRoot(FXMLLoader.load(getClass().getResource("tableShow.fxml"), ResourceBundle.getBundle("bundles.locale", new Locale("en"))));
            userLogin.setText(login);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void chaneOnRu(){
        try {
            pane.getScene().setRoot(FXMLLoader.load(getClass().getResource("tableShow.fxml"), ResourceBundle.getBundle("bundles.locale", new Locale("ru"))));
            userLogin.setText(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void chaneOnSrp(){
        try {
            pane.getScene().setRoot(FXMLLoader.load(getClass().getResource("tableShow.fxml"), ResourceBundle.getBundle("bundles.locale", new Locale("srp"))));
            userLogin.setText(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void chaneOnSpa(){
        try {
            pane.getScene().setRoot(FXMLLoader.load(getClass().getResource("tableShow.fxml"), ResourceBundle.getBundle("bundles.locale", new Locale("spa"))));
            userLogin.setText(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditName(TableColumn.CellEditEvent<Map.Entry<SimpleStringProperty, Creature>, String> event) {
        String try_id_host = null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM authuser WHERE login like '" + login + "'");
            while (resultSet.next()) {
                try_id_host = resultSet.getString("id");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        TablePosition<Map.Entry<SimpleStringProperty, Creature>, String> pos = event.getTablePosition();
        String newFullName = event.getNewValue();
        int row = pos.getRow();
        Map.Entry<SimpleStringProperty, Creature> entry = event.getTableView().getItems().get(row);
        if (try_id_host.equals(id_hostCurr) | login.equals("ad")) {
            Creature creature = entry.getValue();
            creature.setName(String.valueOf(newFullName));
            getCollections();
            updateCollection(entry);
        } else {
            initialize(location, resourceBundle);
            new AlertInfo().showAlert(Alert.AlertType.ERROR, tableCollections.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("denied.alert"));
        }
    }

    public void onEditStatus(TableColumn.CellEditEvent<Map.Entry<SimpleStringProperty, Creature>, String> event) {
        String try_id_host = null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM authuser WHERE login like '" + login + "'");
            while (resultSet.next()) {
                try_id_host = resultSet.getString("id");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (try_id_host.equals(id_hostCurr) | login.equals("ad")) {
            TablePosition<Map.Entry<SimpleStringProperty, Creature>, String> pos = event.getTablePosition();

            String statusFullName = event.getNewValue();

            int row = pos.getRow();
            Map.Entry<SimpleStringProperty, Creature> entry = event.getTableView().getItems().get(row);
            Creature creature = entry.getValue();

            creature.setStatus(String.valueOf(statusFullName));
            getCollections();
            updateCollection(entry);
        } else {
            initialize(location, resourceBundle);
            new AlertInfo().showAlert(Alert.AlertType.ERROR, tableCollections.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("denied.alert"));
        }

    }

    public void onEditLocation(TableColumn.CellEditEvent<Map.Entry<SimpleStringProperty, Creature>, String> event) {

        String try_id_host = null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM authuser WHERE login like '" + login + "'");
            while (resultSet.next()) {
                try_id_host = resultSet.getString("id");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (try_id_host.equals(id_hostCurr) | login.equals("ad")) {
            TablePosition<Map.Entry<SimpleStringProperty, Creature>, String> pos = event.getTablePosition();

            String locFullName = event.getNewValue();

            int row = pos.getRow();
            Map.Entry<SimpleStringProperty, Creature> entry = event.getTableView().getItems().get(row);
            System.out.println(entry);
            Creature creature = entry.getValue();

            creature.setLocation(String.valueOf(locFullName));
            getCollections();
            updateCollection(entry);
        } else {
            initialize(location, resourceBundle);
            new AlertInfo().showAlert(Alert.AlertType.ERROR, tableCollections.getScene().getWindow(), resourceBundle.getString("oh.alert"), resourceBundle.getString("denied.alert"));
        }
    }

    private void updateCollection(Map.Entry<SimpleStringProperty, Creature> entryIn) {
        Map.Entry<SimpleStringProperty, Creature> entry = entryIn;
        Creature creature = entry.getValue();
        try {
            statement.execute("UPDATE creature SET n_ame = '" + creature.getName().trim() + "', status = '" + creature.getStatus().trim() + "', locate = '" + creature.getLocation().trim() + "' WHERE id_key like '" + entry.getKey().get().trim() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        drawer(pane);
    }
}
