package main.api;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.Weather;
import net.aksingh.owmjapis.model.param.WeatherData;

import java.net.SocketTimeoutException;
import java.util.List;

public class OWMJapisRequest {

    private OWM openWeatherRequest = new OWM("14de59c7740e93f8e7cd1b4f9934145c");
    private CurrentWeather currentWeather;
    private HourlyWeatherForecast hourlyWeatherForecast;

    public OWMJapisRequest(int id) throws APIException, SocketTimeoutException {
        openWeatherRequest.setUnit(OWM.Unit.METRIC);
        openWeatherRequest.setLanguage(OWM.Language.POLISH);

        CurrentWeather tempCurrentWeather = getCurrentWeather(id);
        HourlyWeatherForecast tempHourlyWeatherForecast = getHourlyWeather(id);

        if(tempCurrentWeather == null || tempHourlyWeatherForecast == null) {
            throw new SocketTimeoutException();
        } else {
            currentWeather = tempCurrentWeather;
            hourlyWeatherForecast = tempHourlyWeatherForecast;
        }
    }

    private CurrentWeather getCurrentWeather(int cityId) throws APIException {
        CurrentWeather currentWeatherRequest = openWeatherRequest.currentWeatherByCityId(cityId);
        if (currentWeatherRequest.hasRespCode() && currentWeatherRequest.getRespCode() == 200) {
            return currentWeatherRequest;
        } else return null;
    }

    private HourlyWeatherForecast getHourlyWeather(int cityId) throws APIException {
        return openWeatherRequest.hourlyWeatherForecastByCityId(cityId);
    }

    public String getCityName() {
        if (currentWeather.hasCityName()) {
            return currentWeather.getCityName();
        } else return null;
    }

    public String getCountryCode() {
        if (hourlyWeatherForecast.hasCityData()) {
            return hourlyWeatherForecast.getCityData().getCountryCode();
        } else return null;
    }

    public String getCurrentDateTime() {
        if (currentWeather.hasDateTime()) {
            return currentWeather.getDateTime().toString();
        } else return null;
    }

    public String getCurrentTemperature() {
        if (currentWeather.getMainData().hasTemp()) {
            double temperature = currentWeather.getMainData().getTemp();
            Double roundedTemperature = Math.round(temperature * 10.0) / 10.0;
            return roundedTemperature.toString();
        } else return null;
    }

    public String getCurrentHumidity() {
        if (currentWeather.getMainData().hasHumidity()) {
            return currentWeather.getMainData().getHumidity().toString();
        } else return null;
    }

    public List<Weather> getCurrentWeatherList() {
        if (currentWeather.hasWeatherList())
            return currentWeather.getWeatherList();
        else return null;
    }

    public String getCurrentWeatherIcon() {
        Weather currentWeatherData = getCurrentWeatherList().get(0);
        if (currentWeatherData.hasIconLink())
            return currentWeatherData.getIconLink();
        else return null;
    }

    public List<WeatherData> getHourlyWeatherDataList() {
        if (hourlyWeatherForecast.hasDataList())
            return hourlyWeatherForecast.getDataList();
        else return null;
    }

    public WeatherData getHourlyWeatherData(int hourIndex) {
        return getHourlyWeatherDataList().get(hourIndex);
    }

    public String getHourlyDateTime(int hourIndex) {
        if (getHourlyWeatherData(hourIndex).hasDateTime()) {
            return getHourlyWeatherData(hourIndex).getDateTime().toString();
        } else return null;
    }

    public String getHourlyTemperature(int hourIndex) {
        if (getHourlyWeatherData(hourIndex).getMainData().hasTemp()) {
            double temperature = getHourlyWeatherData(hourIndex).getMainData().getTemp();
            Double roundedTemperature = Math.round(temperature * 10.0) / 10.0;
            return roundedTemperature.toString();
        } else return null;
    }

    public String getHourlyHumidity(int hourIndex) {
        if (getHourlyWeatherData(hourIndex).getMainData().hasHumidity()) {
            return getHourlyWeatherData(hourIndex).getMainData().getHumidity().toString();
        } else return null;
    }

    public List<Weather> getHourlyWeatherList(int hourIndex) {
        if (getHourlyWeatherData(hourIndex).hasWeatherList())
            return getHourlyWeatherData(hourIndex).getWeatherList();
        else return null;
    }

    public String getHourlyWeatherIcon(int hourIndex) {
        Weather hourlyWeatherData = getHourlyWeatherList(hourIndex).get(0);
        if (hourlyWeatherData.hasIconLink())
            return hourlyWeatherData.getIconLink();
        else return null;
    }
}
