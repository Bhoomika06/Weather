package com.havrylyuk.weather.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.havrylyuk.weather.R;
import com.havrylyuk.weather.dao.OrmCity;
import com.havrylyuk.weather.dao.OrmWeather;
import com.havrylyuk.weather.util.ImageHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by Igor Havrylyuk on 17.02.2017.
 */
public class HoursRecyclerViewAdapter extends RecyclerView.Adapter<HoursRecyclerViewAdapter.ViewHolder> {

    public interface OnIconClickListener{
        void onIconClick(OrmWeather weather, View view);
    }

    private  OnIconClickListener listener;
    private final List<OrmWeather> mHours;
    private SimpleDateFormat mFormatTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private Context context;

    public HoursRecyclerViewAdapter(Context context, List<OrmWeather> hours) {
        mHours = hours;
        this.context= context;
    }

    public void addElement(OrmWeather Weather) {
        mHours.add(mHours.size(), Weather);
        notifyItemInserted(mHours.size());
    }

    public void addElements(List<OrmWeather> weatherList) {
        mHours.addAll(mHours.size(), weatherList);
        notifyDataSetChanged();
    }

    public void clear() {
        mHours.clear();
        notifyDataSetChanged();
    }

    public void setListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Resources res = holder.view.getResources();
        holder.time.setText(mFormatTime.format(mHours.get(position).getDt()));
        String temperatureText = mHours.get(position).getTemp() > 0 ?
                res.getString(R.string.temp_plus, mHours.get(position).getTemp()) :
                res.getString(R.string.temp_minus, mHours.get(position).getTemp());
        holder.temperature.setText(temperatureText);
        String windText = res.getString(R.string.format_wind_hourly, mHours.get(position).getWind_speed(),
                mHours.get(position).getWind_dir());
        holder.wind.setText(windText);
        String humidityText = res.getString(R.string.humidity_hourly, mHours.get(position).getHumidity());
        holder.humidity.setText(humidityText);
        String pressureText = res.getString(R.string.pressure_hourly, mHours.get(position).getPressure());
        holder.pressure.setText(pressureText);
        ImageHelper.load("http:" + mHours.get(position).getIcon(), holder.weatherState);
        final String message = mHours.get(position).getCondition_text();
        holder.weatherState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onIconClick(mHours.get(holder.getAdapterPosition()),v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView time;
        public TextView temperature;
        public TextView wind;
        public TextView humidity;
        public TextView pressure;
        public SimpleDraweeView weatherState;
        public OrmCity city;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.time = (TextView) view.findViewById(R.id.date);
            this.temperature = (TextView) view.findViewById(R.id.temperature);
            this.wind = (TextView) view.findViewById(R.id.wind);
            this.humidity = (TextView) view.findViewById(R.id.humidity);
            this.pressure = (TextView) view.findViewById(R.id.pressure);
            this.weatherState = (SimpleDraweeView) view.findViewById(R.id.weather_state);
        }
    }
}
