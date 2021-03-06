package main.files;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import main.model.City;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ListCities {

    private List<City> cityList;

    public ListCities() throws Exception {
        loadJSONList();
    }

    private void loadJSONList() throws Exception {

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("main/resources/city.list.min.json.gz");

            GZIPInputStream gzis = new GZIPInputStream(in);

            JsonArray jsonArray = new JsonParser().parse(new InputStreamReader(gzis)).getAsJsonArray();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<City>>(){}.getType();
            List<City> cityList = gson.fromJson(jsonArray, listType);

            this.cityList = cityList;

        } catch(Exception ex) {
            throw new Exception();
        }
    }

    public List<City> getCityList() {
        return cityList;
    }
}
