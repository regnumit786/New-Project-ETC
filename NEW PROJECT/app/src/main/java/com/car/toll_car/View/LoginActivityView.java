package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.car.toll_car.R;
import com.car.toll_car.ViewModel.LoginViewModel;

public class LoginActivityView extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText mobileText, passwordText;
    private Button signIn;
    private TextView noSignUp;
    private int count=0;
    private SharedPreferences preferences;
    private long backPressTime;
    private int logCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        InitialView();
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);

        setTitle("ETC Toll Plaza");

        preferences = getSharedPreferences("LogedDataStore", Context.MODE_PRIVATE);

        noSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivityView.this,SignupActivityView.class));
            }
        });
    }

    public void LoginBtn(View view){
        LoginValidityCheck();
        if (count>0){
            startActivity(new Intent(this, OCRActivityView.class));
        }
    }

    public void LoginValidityCheck(){
        String mobile = preferences.getString("LogedMobile", "default");
        String password = preferences.getString("LogedPassword", "default");
        Log.e("preference_mobile", mobile);
        Log.e("preference_password", password);
        int checkLogin= loginViewModel.CheckLoginValidity(mobileText.getText().toString(), passwordText.getText().toString());
        if (checkLogin == 0){
            mobileText.setError("Must enter mobile");
        } else if (checkLogin == 1){
            passwordText.setError("Password must at least 6 char long");
        } else if (!mobileText.getText().toString().trim().equals(mobile)){
            mobileText.setError("Mobile number not match");
        } else if (!passwordText.getText().toString().trim().equals(password)){
            passwordText.setError("Password not match");
        } else if (checkLogin == 3){
            count++;
        }
    }

    public void InitialView(){
        mobileText= findViewById(R.id.login_mobile);
        passwordText= findViewById(R.id.password);
        signIn= findViewById(R.id.login);
        noSignUp= findViewById(R.id.no_sing_up);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (logCount == 0) {
            String status = preferences.getString("LogedMobile", "");
            Log.e("status", status);
            if (!status.isEmpty()) {
                Toast.makeText(this, "You are already loged", Toast.LENGTH_SHORT).show();
                logCount++;
                startActivity(new Intent(this, Dashboard.class));
                Log.e("logCount",String.valueOf(logCount));
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "press again for exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
}
