package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.TextView;

import com.example.awaylie.bean.WeatherBean;
import com.example.awaylie.controller.WeatherController;

import java.util.List;

/**
     * MainActivity中主要用于承载fragment
     *
     *
     * */
public class MainActivity extends AppCompatActivity {
    private TextView topBarWeatherTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topBarWeatherTV = findViewById(R.id.main_Topbar_Weather);
        //topBarWeatherTV.setText(getWeather("合肥"));

        //创建子线程，实现对天气的获取
        HandlerThread weatherThread = new HandlerThread("weatherThread");
        weatherThread.start();
        Handler handler = new Handler(weatherThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String text = getWeather("合肥");//这里的合肥是模拟数据
                //在线程中更新UI操作
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        topBarWeatherTV.setText(text);
                    }
                });
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    //这个函数用来获取到天气信息,位置信息从sharedPreference中获取，这里模拟一下
    private String getWeather(String cityName){
        WeatherController weatherController = new WeatherController();
        String cityCode = weatherController.getCityCodeByName(cityName);
        List<WeatherBean> weatherBeanList = weatherController.getWeatherByCityCode(cityCode);
        String text = weatherBeanList.get(0).getText()+","+weatherBeanList.get(0).getTemp()+"℃";
        return text;
    }

}