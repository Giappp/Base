package ui.logSign;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Platform;

import java.net.URL;
import java.util.Objects;
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
    private PasswordField pf_confirm_password;
    @FXML
    private TextField tf_email;
    @FXML
    private  Label lbl_error_pass;

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
                if (!tf_username.getText().trim().isEmpty()
                        && validatePasswords(pf_password.getText(), pf_confirm_password.getText(),lbl_error_pass)
                        && !tf_email.getText().trim().isEmpty()) {
                    DBController.signUpUser(event, tf_username.getText(), pf_password.getText(), pf_confirm_password.getText(),tf_email.getText());
                } else {
                    System.out.println("Please fill all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill all information to sign up.");
                    alert.show();
                }
            }
        });
        // Add listener on focus action
        pf_confirm_password.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    validatePasswords(pf_password.getText(),pf_confirm_password.getText(),lbl_error_pass);
                }
            }
        });
        pf_password.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    validatePasswords(Objects.requireNonNull(pf_password.getText()),Objects.requireNonNull(pf_confirm_password.getText()),lbl_error_pass);
                }
            }
        });
    }

    //
    private boolean validatePasswords(String password, String confirmPassword, Label lbl_error_pass) {
        // Check if confirmed password matches with password
        if (!password.isBlank() && !confirmPassword.isBlank() && !password.equals(confirmPassword)) {
            Platform.runLater(() -> {
                lbl_error_pass.setText("Confirm password does not match");
            });
            return false;
        }
        // Check if password is blank
        else if(password.isBlank() || confirmPassword.isBlank()){
            return false;
        }
        //
        else{
            lbl_error_pass.setText("");
            return true;
        }
    }
}
