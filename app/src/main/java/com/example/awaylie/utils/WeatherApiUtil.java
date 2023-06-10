package com.example.awaylie.utils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApiUtil {
    private static final String BASE_URL = "https://eolink.o.apispace.com/456456/weather/v001/now";
    private static final String TOKEN = "nq4hwle8rzxfuai8vv72gw3uxhr3a2l9";
    private static final OkHttpClient client = new OkHttpClient();

    public static String getWeatherData(String areacode) {
        HttpUrl url = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addQueryParameter("areacode", areacode)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APISpace-Token", TOKEN)
                .addHeader("Authorization-Type", "apikey")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
