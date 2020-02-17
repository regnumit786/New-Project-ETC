package com.car.toll_car.Model.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginResponse(boolean success, String mobile, String password) {
        this.success = success;
        this.mobile = mobile;
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
