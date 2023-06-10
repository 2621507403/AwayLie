package com.example.awaylie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
    private String weatherText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topBarWeatherTV = findViewById(R.id.main_Topbar_Weather);
        //topBarWeatherTV.setText(getWeather("合肥"));
        getWeather("合肥");
        //Log.d("MainActivity", "onCreate: "+weatherText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        topBarWeatherTV.setText(weatherText);
    }

    //这个函数用来获取到天气信息,位置信息从sharedPreference中获取，这里模拟一下
    //这个函数用于给变量weatherText进行初始化操作
    private String getWeather(String cityName){
        //创建子线程，实现对天气的获取
        HandlerThread weatherThread = new HandlerThread("weatherThread");
        weatherThread.start();
        Handler handler = new Handler(weatherThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    weatherText = (String) msg.obj;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //在主线程中更新ui
                        Log.d("MainActivity", "onCreate: "+weatherText);
                        topBarWeatherTV.setText(weatherText);
                    }
                });
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
//                String text = getWeather("合肥");//这里的合肥是模拟数据
                WeatherController weatherController = new WeatherController();
                String cityCode = weatherController.getCityCodeByName(cityName);
                List<WeatherBean> weatherBeanList = weatherController.getWeatherByCityCode(cityCode);
                Message m = Message.obtain();
                m.obj = weatherBeanList.get(0).getText()+","+weatherBeanList.get(0).getTemp()+"℃";;
                m.what = 1;
                handler.sendMessage(m);
            }
        });
        return weatherText;
    }

}