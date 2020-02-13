package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.car.toll_car.R;
import com.car.toll_car.ViewModel.LoginViewModel;

public class LoginActivityView extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText mobileText, passwordText;
    private Button signIn;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        InitialView();
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);

        setTitle("ETC Toll Plaza");
    }

    public void LoginBtn(View view){
        LoginValidityCheck();
        if (count>0){
            startActivity(new Intent(this, OCRActivityView.class));
        }
    }

    public void LoginValidityCheck(){
        int checkLogin= loginViewModel.CheckLoginValidity(mobileText.getText().toString(), passwordText.getText().toString());
        if (checkLogin == 0){
            mobileText.setError("Must enter mobile");
        } else if (checkLogin == 1){
            passwordText.setError("Password must at least 6 char long");
        } else if (checkLogin == 3){
            count++;
        }
    }

    public void InitialView(){
        mobileText= findViewById(R.id.login_mobile);
        passwordText= findViewById(R.id.password);
        signIn= findViewById(R.id.login);
    }
}
