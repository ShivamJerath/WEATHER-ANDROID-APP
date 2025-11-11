package com.example.weatherpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<ForecastData.ForecastItem> forecastItems;

    public ForecastAdapter(List<ForecastData.ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastData.ForecastItem item = forecastItems.get(position);

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        String date = dateFormat.format(new Date(item.getDate() * 1000));

        holder.tvDate.setText(date);
        holder.tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", item.getMain().getTemp()));

        if (item.getWeather() != null && !item.getWeather().isEmpty()) {
            holder.tvDescription.setText(item.getWeather().get(0).getDescription());

            // Load weather icon
            String iconUrl = "https://openweathermap.org/img/wn/" +
                    item.getWeather().get(0).getIcon() +
                    "@2x.png";

            Glide.with(holder.itemView.getContext())
                    .load(iconUrl)
                    .into(holder.ivWeatherIcon);
        }
    }

    @Override
    public int getItemCount() {
        return forecastItems != null ? forecastItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTemperature, tvDescription;
        ImageView ivWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
        }
    }
}