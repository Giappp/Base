package ui.dashboard;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.xml.transform.Transformer;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    @FXML
    private ChoiceBox<String> EarningChoiceBox;

    private String[] earningDate = {"Today, Yesterday, Last 7 day, Last week, Last month, Last year"};

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private ChoiceBox<String> ProductChoiceBox;

    @FXML
    private ChoiceBox<String> deliveryChoiceBox;

    @FXML
    private VBox main_side;

    @FXML
    private AnchorPane slider;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        slider.setTranslateX(-176);

        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });


    }
}
