package com.example.demoandroidnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends android.widget.ArrayAdapter<WeatherForecastModel> {

    private Context context;
    private List<WeatherForecastModel> weatherForecastModelList;

    public WeatherAdapter(@NonNull Context context, @NonNull List<WeatherForecastModel> weatherForecastModelList) {
        super(context, 0, weatherForecastModelList);
        this.context = context;
        this.weatherForecastModelList = weatherForecastModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_weather_items, parent, false);
        }

        ImageView imgForecast = view.findViewById(R.id.imgForecast);
        TextView txtTimeForecast = view.findViewById(R.id.txtTimeForecast);
        TextView txtTemperatureForecast = view.findViewById(R.id.txtTemperatureForecast);
        TextView txtWindForecast = view.findViewById(R.id.txtWindForecast);

        WeatherForecastModel weatherForecastModel = getItem(position);

        if (weatherForecastModel != null) {
            txtTimeForecast.setText(weatherForecastModel.getTime());
            txtTemperatureForecast.setText(weatherForecastModel.getTemperature() + "°C");
            txtWindForecast.setText(weatherForecastModel.getWindSpeed() + " km/h");
            Picasso.get().load("https:" + weatherForecastModel.getIcon()).into(imgForecast);
        }

        return view;
    }
}
