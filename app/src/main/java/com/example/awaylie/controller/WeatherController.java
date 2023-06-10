package com.example.awaylie.controller;

import com.example.awaylie.bean.CityAllBean;
import com.example.awaylie.bean.CityBean;
import com.example.awaylie.bean.WeatherAllBean;
import com.example.awaylie.bean.WeatherBean;
import com.example.awaylie.bean.WeatherResultBean;
import com.example.awaylie.utils.CityCodeApiUtil;
import com.example.awaylie.utils.WeatherApiUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 这里主要是获取天气
 * */
public class WeatherController {

    private String cityCodeJson;

    Gson gson = new Gson();

    //通过城市名获取到城市编码
    public String getCityCodeByName(String name){
        //获取城市编码的json数据
        cityCodeJson = CityCodeApiUtil.getCityData(name);
        //通过Gson对获取到的json进行解析
        CityAllBean cityAllBean = gson.fromJson(cityCodeJson,CityAllBean.class);
        List<CityBean> areaList = cityAllBean.getAreaList();
        for (CityBean cityBean : areaList) {
            String cityCode = cityBean.getAreacode();
            return cityCode;
        }
        return null;
    }

    //通过城市编码获取到城市的天气和气温
    public List<WeatherBean> getWeatherByCityCode(String code ){
        List<WeatherBean> list = new ArrayList<>();
        String weatherAllJson = WeatherApiUtil.getWeatherData(code);
        WeatherAllBean weatherAllBean = gson.fromJson(weatherAllJson , WeatherAllBean.class);//获取到WeatherAll类的对象
        WeatherResultBean weatherResultBean = weatherAllBean.getResult();//获取到WeatherResultBean对象
        WeatherBean weatherBean = weatherResultBean.getRealtime();
        list.add(weatherBean);
        return list;
    }

}
