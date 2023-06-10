package com.example.awaylie;

import java.util.List;

public class CityAll {
    private int status;
    private List<City> areaList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<City> getCityList() {
        return areaList;
    }

    public void setCityList(List<City> cityList) {
        this.areaList = cityList;
    }
}
