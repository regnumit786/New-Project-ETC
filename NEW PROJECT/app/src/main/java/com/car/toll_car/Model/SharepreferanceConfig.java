package com.car.toll_car.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.car.toll_car.R;

public class SharepreferanceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    @SuppressLint("StringFormatInvalid")
    public void SharepreferanceConfig (Context context){
        this.context= context;
        sharedPreferences= context.getSharedPreferences(context.getResources().getString(R.string.login_share_prefarance), Context.MODE_PRIVATE);
    }

    public void login_status(boolean status){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_share_prefarance),status);
        editor.apply();
        editor.commit();
    }

    public boolean real_login_status(){
        boolean status= false;
        status= sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_share_prefarance),false);
        return status;
    }

}
