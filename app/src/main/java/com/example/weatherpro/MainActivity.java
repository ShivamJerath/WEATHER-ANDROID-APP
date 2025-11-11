package com.example.weatherpro;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TextView tvLocation, tvDate, tvTemperature, tvWeatherDescription;
    private TextView tvHumidity, tvWind, tvPressure;
    private ImageView ivWeatherIcon;
    private CardView currentWeatherCard;
    private RecyclerView rvForecast;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialButton btnSearch, btnLocation, btnViewDetails;

    private FusedLocationProviderClient fusedLocationClient;
    private WeatherApiService apiService;

    // Variables to store current weather data for DetailActivity
    private double currentTemperature = 0;
    private double currentFeelsLike = 0;
    private int currentHumidity = 0;
    private double currentWindSpeed = 0;
    private int currentPressure = 0;
    private String currentDescription = "";
    private long currentSunrise = 0;
    private long currentSunset = 0;
    private int currentVisibility = 0;
    private String currentCityName = "";
    private String currentCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        setupAnimations();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        apiService = ApiClient.getClient();

        // Get weather for default city
        getWeatherData("New York");
    }

    private void initializeViews() {
        tvLocation = findViewById(R.id.tvLocation);
        tvDate = findViewById(R.id.tvDate);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        tvPressure = findViewById(R.id.tvPressure);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        currentWeatherCard = findViewById(R.id.currentWeatherCard);
        rvForecast = findViewById(R.id.rvForecast);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnSearch = findViewById(R.id.btnSearch);
        btnLocation = findViewById(R.id.btnLocation);
        btnViewDetails = findViewById(R.id.btnViewDetails);

        // Setup RecyclerView
        rvForecast.setLayoutManager(new LinearLayoutManager(this));

        // Setup SwipeRefresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            String currentCity = tvLocation.getText().toString().split(",")[0];
            getWeatherData(currentCity);
        });
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnLocation.setOnClickListener(v -> getCurrentLocationWeather());

        btnViewDetails.setOnClickListener(v -> {
            // Pass current weather data to DetailActivity
            if (currentCityName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Weather data not available yet", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("city", currentCityName + ", " + currentCountry);
            intent.putExtra("temperature", currentTemperature);
            intent.putExtra("feels_like", currentFeelsLike);
            intent.putExtra("humidity", currentHumidity);
            intent.putExtra("wind_speed", currentWindSpeed);
            intent.putExtra("pressure", currentPressure);
            intent.putExtra("description", currentDescription);
            intent.putExtra("sunrise", currentSunrise);
            intent.putExtra("sunset", currentSunset);
            intent.putExtra("visibility", currentVisibility);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        });
    }

    private void setupAnimations() {
        // Card entrance animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(currentWeatherCard, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(currentWeatherCard, "scaleY", 0.8f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(currentWeatherCard, "alpha", 0f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void getWeatherData(String city) {
        showLoading();
        Call<WeatherData> call = apiService.getCurrentWeather(city, "metric", WeatherApiService.API_KEY);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    getForecastData(city);
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                hideLoading();
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getForecastData(String city) {
        Call<ForecastData> call = apiService.getForecast(city, "metric", WeatherApiService.API_KEY);
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateForecastUI(response.body().getList());
                } else {
                    // Silently handle forecast failure
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                // Silently handle forecast failure
            }
        });
    }

    private void updateUI(WeatherData data) {
        // Store data for DetailActivity
        currentTemperature = data.getMain().getTemperature();
        currentFeelsLike = data.getMain().getFeelsLike();
        currentHumidity = data.getMain().getHumidity();
        currentWindSpeed = data.getWind().getSpeed();
        currentPressure = data.getMain().getPressure();
        currentDescription = data.getWeather()[0].getDescription();
        currentSunrise = data.getSys().getSunrise();
        currentSunset = data.getSys().getSunset();
        currentVisibility = data.getVisibility();
        currentCityName = data.getCityName();
        currentCountry = data.getSys().getCountry();

        // Update UI
        tvLocation.setText(currentCityName + ", " + currentCountry);

        // Update date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        tvDate.setText(dateFormat.format(new Date()));

        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", currentTemperature));
        tvWeatherDescription.setText(currentDescription);
        tvHumidity.setText(currentHumidity + "%");
        tvWind.setText(String.format(Locale.getDefault(), "%.1f km/h", currentWindSpeed));
        tvPressure.setText(currentPressure + " hPa");

        // Load weather icon
        String iconUrl = "https://openweathermap.org/img/wn/" +
                data.getWeather()[0].getIcon() +
                "@2x.png";

        com.bumptech.glide.Glide.with(this)
                .load(iconUrl)
                .into(ivWeatherIcon);

        // Enable view details button
        btnViewDetails.setEnabled(true);
        btnViewDetails.setAlpha(1f);
    }

    private void updateForecastUI(List<ForecastData.ForecastItem> forecastItems) {
        // Show only 5 days forecast (every 8th item for daily forecast)
        if (forecastItems != null && forecastItems.size() > 5) {
            // Get forecast for next 5 days at 12:00 PM
            List<ForecastData.ForecastItem> dailyForecasts = forecastItems.subList(0, Math.min(forecastItems.size(), 5));
            ForecastAdapter adapter = new ForecastAdapter(dailyForecasts);
            rvForecast.setAdapter(adapter);
        } else if (forecastItems != null) {
            ForecastAdapter adapter = new ForecastAdapter(forecastItems);
            rvForecast.setAdapter(adapter);
        }
    }

    private void getCurrentLocationWeather() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        showLoading();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            getWeatherByLocation(location.getLatitude(), location.getLongitude());
                        } else {
                            hideLoading();
                            Toast.makeText(MainActivity.this, "Unable to get location. Please ensure location is enabled.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getWeatherByLocation(double lat, double lon) {
        showLoading();
        Call<WeatherData> call = apiService.getCurrentWeatherByLocation(lat, lon, "metric", WeatherApiService.API_KEY);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    getForecastData(response.body().getCityName());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get weather for current location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                hideLoading();
                Toast.makeText(MainActivity.this, "Location weather fetch failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
        // Disable buttons during loading
        btnViewDetails.setEnabled(false);
        btnViewDetails.setAlpha(0.5f);
    }

    private void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        // Enable buttons after loading
        btnViewDetails.setEnabled(true);
        btnViewDetails.setAlpha(1f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather();
            } else {
                Toast.makeText(this, "Location permission denied. You can still search for cities manually.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String city = data.getStringExtra("city");
            if (city != null && !city.trim().isEmpty()) {
                getWeatherData(city.trim());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to app
        if (!currentCityName.isEmpty()) {
            getWeatherData(currentCityName);
        }
    }
}