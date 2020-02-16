package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.car.toll_car.Model.Example;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private TextView profileName, profileEmail, profilePhone, profileAccount;
    ApiClint apiClint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitialView();
        getSharepreferance();
    }

    private void InitialView() {
        profileName= findViewById(R.id.profile_name);
        profileEmail= findViewById(R.id.profile_email);
        profilePhone= findViewById(R.id.profile_phone);
        profileAccount= findViewById(R.id.profile_account);
    }

    private void getSharepreferance(){
        SharedPreferences preferences= getSharedPreferences("LogedDataStore", Context.MODE_PRIVATE);
        if (preferences.contains("LogedName") && preferences.contains("LogedEmail") &&
                preferences.contains("LogedMobile") && preferences.contains("LogedPassword")) {

            String name = preferences.getString("LogedName", "default");
            String email = preferences.getString("LogedEmail", "default");
            String mobile = preferences.getString("LogedMobile", "default");
            profileName.setText(name);
            profileEmail.setText(email);
            profilePhone.setText(mobile);
            profileAccount.setText(R.string.account);
        }
    }

    private void getPost(){
        apiClint= RetrofitClint.getRetrofitClint().create(ApiClint.class);
        Call<List<Example>> call= apiClint.getPostData();
        call.enqueue(new Callback<List<Example>>() {
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response) {
                if (response.code() == 200){
                    Log.e("response_successfully",String.valueOf(response.code()));

                    /*List<Example> example= response.body();
                    String nn= null, ee= null, pp= null;

                    for (Example example1: example){
                        nn= example1.getName();
                        ee= example1.getEmail();
                        pp= example1.getMobile();
                    }
                    profileName.setText(nn);
                    profileEmail.setText(ee);
                    profilePhone.setText(pp);*/

                } else {
                    Toast.makeText(Profile.this, "Server request failed", Toast.LENGTH_SHORT).show();
                }

                Log.e("Working_process_are_ok", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<List<Example>> call, Throwable t) {
                Log.e("Error_is:",t.getMessage());
            }
        });
    }

}
