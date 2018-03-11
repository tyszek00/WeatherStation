import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TemplateController extends WeatherDateSegregation implements Initializable {

    @FXML private TextField countryLeft;

    @FXML private TextField cityLeft;
    @FXML private Label cityNameLeft;
    @FXML private Label currentTimeLeft;
    @FXML private Label currentTempLeft;
    @FXML private Label currentHumidityLeft;
    @FXML private ImageView currentIconLeft;
    @FXML private Label currentDateLeft;

    @FXML private HBox currentDayNextHoursLeft;

    @FXML private GridPane containerLeft;

    @FXML private TextField countryRight;

    @FXML private TextField cityRight;
    @FXML private Label cityNameRight;
    @FXML private Label currentTimeRight;
    @FXML private Label currentTempRight;
    @FXML private Label currentHumidityRight;
    @FXML private ImageView currentIconRight;
    @FXML private Label currentDateRight;

    @FXML private HBox currentDayNextHoursRight;

    @FXML private GridPane containerRight;

    ControllerFunctions functions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        functions = new ControllerFunctions(countryLeft, countryRight, cityLeft, cityRight);

        Platform.runLater(() -> countryLeft.requestFocus());
    }

    @FXML
    public void getCitiesListLeft(ActionEvent event) {
        String desiredCountryLeft = countryLeft.getText();

        try {
            functions.provideSelectedCitySuggestions(desiredCountryLeft);
            cityLeft.requestFocus();
        } catch (IOException ioex) {
            functions.fileNotFoundError(cityLeft);
        }
    }

    @FXML
    public void getCitiesListRight(ActionEvent event) {
        String desiredCountryRight = countryRight.getText();

        try {
            functions.provideSelectedCitySuggestions(desiredCountryRight);
            cityRight.requestFocus();
        } catch (IOException ioex) {
            functions.fileNotFoundError(cityRight);
        }
    }

    @FXML
    public void printDataLeftSide(ActionEvent event) {

        clearScreen(cityNameLeft, currentTimeLeft, currentTempLeft, currentHumidityLeft,
                currentIconLeft, currentDateLeft, currentDayNextHoursLeft, containerLeft);

        functions.fillWindowWithData(countryLeft, cityLeft, cityNameLeft, currentDateLeft, currentTimeLeft, currentTempLeft,
                currentHumidityLeft, currentIconLeft, currentDayNextHoursLeft, containerLeft);

        countryRight.requestFocus();
    }

    @FXML
    public void printDataRightSide(ActionEvent event) {

        clearScreen(cityNameRight, currentTimeRight, currentTempRight, currentHumidityRight,
                currentIconRight, currentDateRight, currentDayNextHoursRight, containerRight);

        functions.fillWindowWithData(countryRight, cityRight, cityNameRight, currentDateRight, currentTimeRight, currentTempRight,
                currentHumidityRight, currentIconRight, currentDayNextHoursRight, containerRight);

        countryLeft.requestFocus();
    }

    public void clearScreen(Label cityName, Label currentTime, Label currentTemp, Label currentHumidity,
                            ImageView currentIcon, Label currentDate, HBox currentDayNextHours, GridPane container) {

        cityName.setText(null);
        currentTime.setText(null);;
        currentTemp.setText(null);;
        currentHumidity.setText(null);;
        currentIcon.setImage(null);
        currentDate.setText(null);;

        currentDayNextHours.getChildren().clear();

        container.getChildren().clear();
    }
}