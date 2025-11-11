package com.example.weatherpro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimation;
    private CircularProgressIndicator progressBar;
    private TextView tvAppName, tvTagline, tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeViews();
        startAnimations();
    }

    private void initializeViews() {
        lottieAnimation = findViewById(R.id.lottieAnimation);
        progressBar = findViewById(R.id.progressBar);
        tvAppName = findViewById(R.id.tvAppName);
        tvTagline = findViewById(R.id.tvTagline);
        tvVersion = findViewById(R.id.tvVersion);

        // Set initial states for animation
        tvAppName.setAlpha(0f);
        tvTagline.setAlpha(0f);
        tvVersion.setAlpha(0f);
        progressBar.setAlpha(0f);
    }

    private void startAnimations() {
        // Start Lottie animation
        lottieAnimation.playAnimation();

        // Animate app name with delay
        new Handler().postDelayed(() -> {
            tvAppName.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Animate tagline after app name
                            tvTagline.animate()
                                    .alpha(1f)
                                    .setDuration(800)
                                    .start();

                            // Animate version and progress bar
                            tvVersion.animate()
                                    .alpha(1f)
                                    .setDuration(800)
                                    .start();

                            progressBar.animate()
                                    .alpha(1f)
                                    .setDuration(800)
                                    .start();
                        }
                    })
                    .start();
        }, 500);

        // Navigate to MainActivity after 3 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lottieAnimation != null) {
            lottieAnimation.cancelAnimation();
        }
    }
}