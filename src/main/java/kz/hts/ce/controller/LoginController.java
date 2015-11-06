package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogin;
    @FXML
    public Label lblMessage;

    @FXML
    private void btnLoginAction(ActionEvent event) throws IOException {
        if (txtUsername.getText().equals("test") && txtPassword.getText().equals("test")) {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent parent = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Main");
            stage.show();
        } else {
            lblMessage.setText("Username or Password invalid");
        }
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
