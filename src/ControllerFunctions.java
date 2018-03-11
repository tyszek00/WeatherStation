import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.aksingh.owmjapis.api.APIException;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;

public class ControllerFunctions {

    private final String DEGREE  = "\u00b0";
    private HashMap<String, String> countryNameCountryCode;
    private Multimap<String, String> cityNameCountryCode;

    private SuggestionProvider<String> citySuggestions;

    private List<City> fullCityList;

    TextField countryLeft;
    TextField countryRight;
    TextField cityLeft;
    TextField cityRight;

    public ControllerFunctions(TextField countryLeft, TextField countryRight, TextField cityLeft, TextField cityRight) {
        String[] locales = Locale.getISOCountries();

        this.countryLeft = countryLeft;
        this.countryRight = countryRight;
        this.cityLeft = cityLeft;
        this.cityRight = cityRight;

        countryNameCountryCode = new HashMap<>();
        for (String countryCode: locales) {
            Locale obj = new Locale("", countryCode);
            countryNameCountryCode.put(obj.getDisplayCountry(), obj.getCountry());
        }

        TextFields.bindAutoCompletion(countryLeft, countryNameCountryCode.keySet());
        TextFields.bindAutoCompletion(countryRight, countryNameCountryCode.keySet());

        try {
            fullCityList = new ListCities().getCityList();

            cityNameCountryCode = ArrayListMultimap.create();

            int iterator = 0;
            for (City i : fullCityList) {
                cityNameCountryCode.put(fullCityList.get(iterator).getCountryCode(), fullCityList.get(iterator).getCityName());
                iterator++;
            }

            citySuggestions = SuggestionProvider.create(cityNameCountryCode.values());
            new AutoCompletionTextFieldBinding<>(cityLeft, citySuggestions);
            new AutoCompletionTextFieldBinding<>(cityRight, citySuggestions);

        } catch(Exception ex) {
            fullCityList = null;

            fileNotFoundError(cityLeft);
            fileNotFoundError(cityRight);
        }
    }

    public void fileNotFoundError(TextField city) {
        city.setPromptText("Nie znaleziono pliku!");
    }

    public void provideSelectedCitySuggestions(String desiredCountryLeft) throws IOException {

        if(fullCityList.isEmpty() == false) {
            if (countryNameCountryCode.containsKey(desiredCountryLeft)) {
                String countryCode = countryNameCountryCode.get(desiredCountryLeft);
                Set<String> filteredAutoCompletions = new HashSet<>(cityNameCountryCode.get(countryCode));

                citySuggestions.clearSuggestions();
                citySuggestions.addPossibleSuggestions(filteredAutoCompletions);
            } else {
                citySuggestions.clearSuggestions();
                citySuggestions.addPossibleSuggestions(cityNameCountryCode.values());
            }
        } else {
            throw new IOException();
        }
    }

    public String getCountryCode(TextField country) {
        String countryName = country.getText();

        if (countryNameCountryCode.containsKey(countryName)) {
            return countryNameCountryCode.get(countryName);
        } else return null;
    }

    public int getCityId(String desiredCountry, String desiredCity) {
        int cityId = -1;

        if (fullCityList != null) {
            int iterator = 0;

            if (countryNameCountryCode.containsKey(desiredCountry)) {
                for (City i : fullCityList) {
                    if (fullCityList.get(iterator).getCountryCode().equals(desiredCountry) &&
                            fullCityList.get(iterator).getCityName().equals(desiredCity)) {

                        cityId = fullCityList.get(iterator).getId();
                    }
                    iterator++;
                }
                return cityId;

            } else {
                for (City i : fullCityList) {
                    if (fullCityList.get(iterator).getCityName().equals(desiredCity)) {

                        cityId = fullCityList.get(iterator).getId();
                    }
                    iterator++;
                }
                return cityId;
            }
        }
        return cityId;
    }

    public void fillWindowWithData(TextField country, TextField city, Label cityName, Label currentDate, Label currentTime, Label currentTemp,
                                    Label currentHumidity, ImageView currentIcon, HBox currentDayNextHours, GridPane container) {

        String desiredCity = city.getText();
        String desiredCountry = getCountryCode(country);

        int cityId = getCityId(desiredCountry, desiredCity);

        try {
            if(cityId < 0) throw new Exception();
            else {
                OWMJapisRequest weatherForCity = new OWMJapisRequest(cityId);

                cityName.setText(weatherForCity.getCityName() + ", " + weatherForCity.getCountryCode());

                String inputDate = DateFormat.getFullDate(weatherForCity.getCurrentDateTime());
                currentDate.setText(inputDate);

                currentTime.setText("Teraz:");
                currentTemp.setText(weatherForCity.getCurrentTemperature() + " " + DEGREE + "C");
                currentHumidity.setText("Wilgotność: " + weatherForCity.getCurrentHumidity() + "%");

                String pathCurrentIconLeft = weatherForCity.getCurrentWeatherIcon();
                currentIcon.setImage(setIcon(pathCurrentIconLeft));

                int[] firstDayIndexes = WeatherDateSegregation.selectWeatherData(weatherForCity);

                printCurrentDayNextHoursData(currentDayNextHours, firstDayIndexes, weatherForCity);

                int startingCounterValue = firstDayIndexes[3] + 2;

                printNextDayHoursData(container, startingCounterValue, weatherForCity);
            }

        } catch (APIException ex) {
            cityName.setText("Niepoprawne dane!");

        } catch (UnknownHostException ex) {
            cityName.setText("Brak połączenia z siecią!");

        } catch (NoRouteToHostException ex) {
            cityName.setText("Przerwano połączenie z siecią!");

        } catch (SocketTimeoutException stex) {
            cityName.setText("Serwer nie odpowiada!");

        } catch (Exception e) {
            cityName.setText("Brak danych!");
        }
    }

    private void printCurrentDayNextHoursData(HBox currentDayNextHours, int[] firstDayIndexes, OWMJapisRequest weatherForCity) {

        List<VBox> currentDayHourColumns = new ArrayList<>();
        List<Label> currentDayHours = new ArrayList<>();;
        List<ImageView> currentDayIconHours = new ArrayList<>();;
        List<Label> currentDayTemp = new ArrayList<>();;
        List<Label> currentDayHumidity = new ArrayList<>();;

        for (int i = 0; i < firstDayIndexes.length; i++) {
            VBox currentColumns = new VBox();
            currentColumns.setAlignment(Pos.CENTER);
            currentColumns.setPrefWidth(83);
            currentDayHourColumns.add(currentColumns);

            Label currentDayHour = new Label();
            currentDayHours.add(currentDayHour);
            ImageView currentDayIcon = new ImageView();
            currentDayIconHours.add(currentDayIcon);
            Label currentDayTempLabel = new Label();
            currentDayTemp.add(currentDayTempLabel);
            Label currentDayHumidityLabel = new Label();
            currentDayHumidity.add(currentDayHumidityLabel);

            currentColumns.getChildren().addAll(currentDayHour, currentDayIcon, currentDayTempLabel, currentDayHumidityLabel);
            currentDayNextHours.getChildren().add(currentColumns);

            if (firstDayIndexes[i] > -1) {
                currentDayHour.setText(weatherForCity.getHourlyDateTime(firstDayIndexes[i]).substring(11, 16));
                String pathFirstDayIcon = weatherForCity.getHourlyWeatherIcon(firstDayIndexes[i]);
                currentDayIcon.setImage(setIcon(pathFirstDayIcon));
                currentDayTempLabel.setText(weatherForCity.getHourlyTemperature(firstDayIndexes[i]) + " " + DEGREE + "C");
                currentDayHumidityLabel.setText(weatherForCity.getHourlyHumidity(firstDayIndexes[i]) + "%");
            }
        }
    }

    private void printNextDayHoursData(GridPane container, int startingCounterValue, OWMJapisRequest weatherForCity) {

        List<VBox> nextDaysList = new ArrayList<>();
        List<Label> dayLabelList = new ArrayList<>();
        List<HBox> dayHourRowsList = new ArrayList<>();
        List<VBox> dayHourColumnsList = new ArrayList<>();
        List<Label> dayHoursList = new ArrayList<>();
        List<ImageView> dayIconHoursList = new ArrayList<>();
        List<Label> dayTempList = new ArrayList<>();
        List<Label> dayHumidityList = new ArrayList<>();

        int rowIndex = 1;

        for (int j = 1; j <= 4; j++) {
            VBox nextDay = new VBox();
            nextDay.setAlignment(Pos.CENTER);
            nextDay.setSpacing(5);
            nextDaysList.add(nextDay);

            Label dayLabel = new Label();
            dayLabelList.add(dayLabel);

            HBox dayRows = new HBox();
            dayRows.setAlignment(Pos.CENTER);
            dayHourRowsList.add(dayRows);

            for (int i = 0; i < 4; i++) {
                VBox dayColumns = new VBox();
                dayColumns.setAlignment(Pos.CENTER);
                dayColumns.setSpacing(5);
                dayColumns.setPrefWidth(83);
                dayHourColumnsList.add(dayColumns);

                Label dayHour = new Label();
                dayHoursList.add(dayHour);
                ImageView dayIcon = new ImageView();
                dayIconHoursList.add(dayIcon);
                Label dayTemp = new Label();
                dayTempList.add(dayTemp);
                Label dayHumidity = new Label();
                dayHumidityList.add(dayHumidity);

                dayColumns.getChildren().addAll(dayHour, dayIcon, dayTemp, dayHumidity);
                dayRows.getChildren().add(dayColumns);

                if (i == 0) {
                    String inputHourlyDate = DateFormat.getFullDate(weatherForCity.getHourlyDateTime(startingCounterValue));
                    dayLabel.setText(inputHourlyDate);
                }
                dayHour.setText(weatherForCity.getHourlyDateTime(startingCounterValue).substring(11, 16));
                String pathDayIcon = weatherForCity.getHourlyWeatherIcon(startingCounterValue);
                dayIcon.setImage(setIcon(pathDayIcon));
                dayTemp.setText(weatherForCity.getHourlyTemperature(startingCounterValue) + " " + DEGREE + "C");
                dayHumidity.setText(weatherForCity.getHourlyHumidity(startingCounterValue) + "%");

                startingCounterValue += 2;
            }
            nextDay.getChildren().add(dayLabel);
            nextDay.getChildren().add(dayRows);
            container.add(nextDay, 0, rowIndex);
            container.setAlignment(Pos.CENTER);

            rowIndex++;
        }
    }

    private static Image setIcon(String path) {
        return new Image(path);
    }
}
