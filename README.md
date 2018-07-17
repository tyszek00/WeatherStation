# WeatherStation
5-day weather forecast for two cities.

This is a JavaFX weather station based on the data received from OpenWeatherMap site.
It uses OWMJapis-2.5.2.1 for retreiving the data and ControlsFX 9.0.0 autocompletion text fields for selecting desired city.
The application requires city.list.min.json.gz file from OpenWeatherMap: http://bulk.openweathermap.org/sample/
Also you need your own API key obtained after making account on OpenWeatherMap.

The source code is written using JDK 9 libraries, but you can replace them with JDK 8. If so, you should also change ControlsFX repository to version 8, for example ControlsFX 8.40.14.

The list of all external libraries can be found in the WeatherStation.iml file.
