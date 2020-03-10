package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.car.toll_car.Model.LocalDB.SQLiteHelper;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.LoginResponse;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.Model.SharepreferanceConfig;
import com.car.toll_car.R;
import com.car.toll_car.ViewModel.LoginViewModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityView extends AppCompatActivity {
    private static final int LOGIN_STATE=1;
    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private EditText mobileText, passwordText;
    private Button signIn;
    private TextView noSignUp;
    private int count=0;
    private long backPressTime;
    private ApiClint apiClint;
    private String POST_URL= "http://192.168.50.17/RFIDApicbank/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        InitialView();
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
            PostLoginUsingVolley();
            ///Store sharepreference data
            SharedPreferences sign_in_preferences = getSharedPreferences(getString(R.string.loginStore), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sign_in_preferences.edit();
            editor.putString("Login_Mobile", mobileText.getText().toString());
            editor.putInt("Check_login_value",1);
            editor.apply();
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

    /**

    private void UserLoginUsingRetrofit(){
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

    */

    public void InitialView(){
        mobileText= findViewById(R.id.login_mobile);
        passwordText= findViewById(R.id.password);
        signIn= findViewById(R.id.login);
        noSignUp= findViewById(R.id.no_sing_up);
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

    private void PostLoginUsingVolley(){
        try {
            StringRequest stringRequest= new StringRequest(Request.Method.POST, POST_URL, new
                    com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Intent intent= new Intent(LoginActivityView.this, Dashboard.class);
                                Log.e("Login_mobile: ",mobileText.getText().toString().trim());
                                startActivity(intent);
                                Toast.makeText(LoginActivityView.this, "Login successfully: " + response, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivityView.this, "Login Error: "+response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivityView.this, "Server Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params= new HashMap<String, String>();
                    String number= mobileText.getText().toString();
                    String password= passwordText.getText().toString();

                    params.put("mobile",number);
                    params.put("password",password);
                    Log.e("VolleyNumber", number);
                    Log.e("VolleyPassword", password);

                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sign_in_preferences = getSharedPreferences(getString(R.string.loginStore), Context.MODE_PRIVATE);
        SharedPreferences sign_up_preferences = getSharedPreferences(getString(R.string.signupStore), Context.MODE_PRIVATE);
        int sign_in_value= sign_in_preferences.getInt("Check_login_value",0);
        int sign_up_value= sign_up_preferences.getInt("Check_signup_value",0);
        if (sign_in_value == 1 || sign_up_value== 2){
            startActivity(new Intent(this, Dashboard.class));
        }else {
            Toast.makeText(this, "Please Log in", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "OnRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
    }*/
}
