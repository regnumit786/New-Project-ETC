package com.car.toll_car.Model;

public class Car_Detail {
    private String reg_no, vehicle_class, toll_rate, datetime;

    public Car_Detail() {
    }

    public Car_Detail(String reg_no, String vehicle_class, String toll_rate, String datetime) {
        this.reg_no = reg_no;
        this.vehicle_class = vehicle_class;
        this.toll_rate = toll_rate;
        this.datetime = datetime;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getVehicle_class() {
        return vehicle_class;
    }

    public void setVehicle_class(String vehicle_class) {
        this.vehicle_class = vehicle_class;
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
