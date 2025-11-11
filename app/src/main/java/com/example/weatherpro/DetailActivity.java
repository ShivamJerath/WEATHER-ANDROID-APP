package com.example.weatherpro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DetailActivity extends AppCompatActivity {

    private MaterialButton btnBack;
    private TextView tvDetailLocation, tvDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        setupClickListeners();

        String city = getIntent().getStringExtra("city");
        if (city != null) {
            tvDetailLocation.setText("Detailed forecast for " + city);
            tvDetailInfo.setText("This screen would show more detailed weather information including:\n\n" +
                    "• Hourly forecast for the next 24 hours\n" +
                    "• Extended 7-day forecast\n" +
                    "• Sunrise and sunset times\n" +
                    "• UV index\n" +
                    "• Visibility\n" +
                    "• Precipitation probability\n" +
                    "• Wind direction and gusts\n" +
                    "• Atmospheric pressure trends\n\n" +
                    "This feature can be expanded with more detailed weather data from the API.");
        }
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailInfo = findViewById(R.id.tvDetailInfo);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}