package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.LoginResponse;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.R;
import com.car.toll_car.ViewModel.LoginViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityView extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText mobileText, passwordText;
    private Button signIn;
    private TextView noSignUp;
    private int count=0;
    private long backPressTime;
    private String mobile, password;
    private ApiClint apiClint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        InitialView();
        setTextData();
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        setTitle("ETC Toll Plaza");

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
            UserLogin();
            startActivity(new Intent(this, Dashboard.class));
        }
    }

    public void LoginValidityCheck(){
        int checkLogin= loginViewModel.CheckLoginValidity(mobileText.getText().toString(), passwordText.getText().toString());
        if (checkLogin== 0){
            mobileText.setError("Must enter mobile");
        } else if (checkLogin== 1){
            passwordText.setError("Password must at least 6 char long");
        } else if (checkLogin== 3){
            count++;
        }
    }

    private void UserLogin(){
        apiClint= RetrofitClint.getRetrofitClint().create(ApiClint.class);
        Call<LoginResponse> call= apiClint.user_LogIn(mobileText.getText().toString().trim(), passwordText.getText().toString().trim());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200){
                    LoginResponse loginResponse= response.body();
                    if (loginResponse.isSuccess()){
                        if (!mobile.equals(loginResponse.getMobile())){
                            mobileText.setError("Mobile number not match");
                        } else if (!password.equals(loginResponse.getPassword())){
                            passwordText.setError("Password not match");
                        }else {
                            Toast.makeText(LoginActivityView.this, "Login successfully " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivityView.this, "Login in Error "+response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void InitialView(){
        mobileText= findViewById(R.id.login_mobile);
        passwordText= findViewById(R.id.password);
        signIn= findViewById(R.id.login);
        noSignUp= findViewById(R.id.no_sing_up);
    }

    private void setTextData(){
        mobile= mobileText.getText().toString().trim();
        password= passwordText.getText().toString().trim();
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
