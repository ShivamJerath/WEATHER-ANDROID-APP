package com.example.weatherpro;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    private TextView tvDetailLocation, tvDetailInfo, tvSunrise, tvSunset, tvFeelsLike, tvHumidity, tvWind, tvPressure, tvVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        setupClickListeners();
        displayWeatherData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailInfo = findViewById(R.id.tvDetailInfo);
        tvSunrise = findViewById(R.id.tvSunrise);
        tvSunset = findViewById(R.id.tvSunset);
        tvFeelsLike = findViewById(R.id.tvFeelsLike);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        tvPressure = findViewById(R.id.tvPressure);
        tvVisibility = findViewById(R.id.tvVisibility);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void displayWeatherData() {
        // Get data passed from MainActivity
        String cityName = getIntent().getStringExtra("city");
        double temperature = getIntent().getDoubleExtra("temperature", 0);
        double feelsLike = getIntent().getDoubleExtra("feels_like", 0);
        int humidity = getIntent().getIntExtra("humidity", 0);
        double windSpeed = getIntent().getDoubleExtra("wind_speed", 0);
        int pressure = getIntent().getIntExtra("pressure", 0);
        String description = getIntent().getStringExtra("description");
        long sunrise = getIntent().getLongExtra("sunrise", 0);
        long sunset = getIntent().getLongExtra("sunset", 0);
        int visibility = getIntent().getIntExtra("visibility", 0);

        // Update UI with actual data
        if (cityName != null) {
            tvDetailLocation.setText("Detailed Forecast for " + cityName);

            String detailedInfo = "Current Conditions:\n\n" +
                    "• Temperature: " + String.format(Locale.getDefault(), "%.1f°C", temperature) + "\n" +
                    "• Weather: " + (description != null ? description : "N/A") + "\n" +
                    "• Feels Like: " + String.format(Locale.getDefault(), "%.1f°C", feelsLike) + "\n" +
                    "• Humidity: " + humidity + "%\n" +
                    "• Wind Speed: " + String.format(Locale.getDefault(), "%.1f km/h", windSpeed) + "\n" +
                    "• Pressure: " + pressure + " hPa\n" +
                    "• Visibility: " + (visibility / 1000) + " km";

            tvDetailInfo.setText(detailedInfo);

            // Set detailed information in individual TextViews
            tvFeelsLike.setText(String.format(Locale.getDefault(), "%.1f°C", feelsLike));
            tvHumidity.setText(humidity + "%");
            tvWind.setText(String.format(Locale.getDefault(), "%.1f km/h", windSpeed));
            tvPressure.setText(pressure + " hPa");
            tvVisibility.setText((visibility / 1000) + " km");

            // Format and display sunrise/sunset times
            if (sunrise > 0) {
                String sunriseTime = formatTime(sunrise);
                tvSunrise.setText(sunriseTime);
            }

            if (sunset > 0) {
                String sunsetTime = formatTime(sunset);
                tvSunset.setText(sunsetTime);
            }
        } else {
            tvDetailLocation.setText("Detailed Weather Information");
            tvDetailInfo.setText("No weather data available. Please check your connection and try again.");
        }
    }

    private String formatTime(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp * 1000));
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}