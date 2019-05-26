package sample;


import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    static String log;

    @FXML
    private PasswordField passwordAuth;

    @FXML
    private TextField loginAuth;

    @FXML
    private Button startButtonAuth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButtonAuth.setOnAction(event -> {
            String login = loginAuth.getText().trim();
            String pass = passwordAuth.getText().trim();

            if (login.isEmpty() || pass.isEmpty()) {
                new AlertInfo().showAlert(Alert.AlertType.WARNING, startButtonAuth.getScene().getWindow(), resources.getString("oh.alert"),resources.getString("emptyFields.alert"));
            } else {
                //startButtReg.getScene().getWindow().hide();
                this.log = login;
                Client client = new Client();
                client.connect();
                String mess = client.doCom("2", log, pass);
                if (mess.contains("your data")){
                    new AlertInfo().showAlert(Alert.AlertType.ERROR, startButtonAuth.getScene().getWindow(), resources.getString("sorry.alert") , resources.getString("checkData.alert"));
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
                    tableShowController.initialize(location, resources);
                    stage.setTitle("App you");
                    stage.getIcons().add(new Image("/image/image.png"));
                    stage.setResizable(false);
                    stage.showAndWait();
                    startButtonAuth.getScene().getWindow().hide();
                }
            }
        });
    }
}
