package com.example.weatherpro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    String API_KEY = "a214f1b2dfdad1a35049679719c0204c"; // Replace with your OpenWeatherMap API key

    @GET("weather")
    Call<WeatherData> getCurrentWeather(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("weather")
    Call<WeatherData> getCurrentWeatherByLocation(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("forecast")
    Call<ForecastData> getForecast(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}