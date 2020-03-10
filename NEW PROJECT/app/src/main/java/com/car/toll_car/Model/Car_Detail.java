package com.car.toll_car.Model;

public class Car_Detail {
    private String toll_rate, datetime;

    public Car_Detail() {
    }

    public Car_Detail(String toll_rate, String datetime) {
        this.toll_rate = toll_rate;
        this.datetime = datetime;
    }

    public String getToll_rate() {
        return toll_rate;
    }

    public void setToll_rate(String toll_rate) {
        this.toll_rate = toll_rate;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
