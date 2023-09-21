package logSign;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button btn_signup;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirm_pass;
    @FXML
    private TextField tf_email;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBController.changeScene(event, "log-in.fxml", "Log in!", null);
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().trim().isEmpty() && !pf_password.getText().trim().isEmpty()
                        && !pf_confirm_pass.getText().trim().isEmpty() && !tf_email.getText().trim().isEmpty()) {
                    DBController.changeScene(event, tf_username.getText(), pf_password.getText(), tf_email.getText());
                } else {
                    System.out.println("Please fill all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill all information to sign up.");
                    alert.show();
                }
            }
        });
    }
}
