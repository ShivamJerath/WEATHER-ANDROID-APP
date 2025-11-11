package com.example.weatherpro;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastData {
    @SerializedName("list")
    private List<ForecastItem> list;

    @SerializedName("city")
    private City city;

    public List<ForecastItem> getList() { return list; }
    public void setList(List<ForecastItem> list) { this.list = list; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public static class ForecastItem {
        @SerializedName("dt")
        private long date;

        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("dt_txt")
        private String dateText;

        public long getDate() { return date; }
        public void setDate(long date) { this.date = date; }

        public Main getMain() { return main; }
        public void setMain(Main main) { this.main = main; }

        public List<Weather> getWeather() { return weather; }
        public void setWeather(List<Weather> weather) { this.weather = weather; }

        public String getDateText() { return dateText; }
        public void setDateText(String dateText) { this.dateText = dateText; }
    }

    public static class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("feels_like")
        private double feelsLike;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("pressure")
        private int pressure;

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }

        public double getFeelsLike() { return feelsLike; }
        public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }

        public int getPressure() { return pressure; }
        public void setPressure(int pressure) { this.pressure = pressure; }
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        public String getMain() { return main; }
        public void setMain(String main) { this.main = main; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }

    public static class City {
        @SerializedName("name")
        private String name;

        @SerializedName("country")
        private String country;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }
}