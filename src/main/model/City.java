package main.model;

public class City {

    private int id;
    private String name;
    private String country;

    public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return name;
    }

    public String getCountryCode() {
        return country;
    }

}
