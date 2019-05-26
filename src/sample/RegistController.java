package sample;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistController implements Initializable {

    static String log;

    @FXML
    private TextField loginReg;

    @FXML
    private Button startButtReg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButtReg.setOnAction(event -> {
            String login = loginReg.getText();

            if (login.isEmpty()) {
                new AlertInfo().showAlert(Alert.AlertType.WARNING, startButtReg.getScene().getWindow(), resources.getString("oh.alert"),resources.getString("emptyFields.alert"));
            } else {
                //startButtReg.getScene().getWindow().hide();
                this.log = login;
                Client client = new Client();
                client.connect();
                String mess = client.doCom("1", log, "");
                if (mess.contains("a new")){
                    new AlertInfo().showAlert(Alert.AlertType.ERROR, startButtReg.getScene().getWindow(), resources.getString("sorry.alert"), resources.getString("alreadyExist.alert"));
                } else{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/sample/tableShow.fxml"));
                    loader.setResources(resources);
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    TableShowController tableShowController = loader.getController();
                    CurrentUser.login = log;
                    CurrentUser.client = client;
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("App you");
                    stage.getIcons().add(new Image("/image/image.png"));
                    stage.setResizable(false);
                    tableShowController.initialize(location, resources);
                    stage.showAndWait();
                    startButtReg.getScene().getWindow().hide();
                }
            }
        });
    }
}
