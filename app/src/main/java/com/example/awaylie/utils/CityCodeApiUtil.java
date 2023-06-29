package com.example.awaylie.utils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityCodeApiUtil {
    private static final String BASE_URL = "https://eolink.o.apispace.com/456456/function/v001/city";
    private static final String TOKEN = "nq4hwle8rzxfuai8vv72gw3uxhr3a2l9";
    private static final OkHttpClient clien = new OkHttpClient();

    public static String getCityData( String location){
        HttpUrl url = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addQueryParameter("location",location)
                .addQueryParameter("items","10")
                .addQueryParameter("area","china")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APISpace-Token",TOKEN)
                .addHeader("Authorization-Type","apikey")
                .build();

        try {
            Response response = clien.newCall(request).execute();
            if (response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
