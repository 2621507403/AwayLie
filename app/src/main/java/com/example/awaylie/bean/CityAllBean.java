package com.example.awaylie.bean;

import java.util.List;

public class CityAllBean {
    private int status;
    private List<CityBean> areaList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CityBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<CityBean> cityList) {
        this.areaList = cityList;
    }


}
