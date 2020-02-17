package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.car.toll_car.Model.Example;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.R;
import com.car.toll_car.ViewModel.LoginViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivityView extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private Button btnSignUp;
    private EditText inputName, inputNumber, inputEmail, inputPassword, inputRePassword;
    private ApiClint apiClint;
    private int count=0;
    int randomOTPnumber;
    private SharedPreferences preferences;
    private long backPressTime;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupview);

        setTitle("ETC Toll Plaza");

        InitialView();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        //PostApi();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void SignUpBtn(View view) {
        Validity();
        if (count>0){
            Toast.makeText(SignupActivityView.this, "All are OK", Toast.LENGTH_SHORT).show();
            //SendOTP();
            Intent intent= new Intent(SignupActivityView.this, OTP.class);
            DataStore();
            NotificationGenerate();
            PostApi(inputName.getText().toString(), inputEmail.getText().toString(),
                    inputPassword.getText().toString(), inputNumber.getText().toString());

            number= inputNumber.getText().toString().trim();
            if (number.isEmpty() || number.length() < 11) {
                inputNumber.setError("Valid number is required");
                inputNumber.requestFocus();
            }
            String phoneNumber= "+88" + number;
            intent.putExtra("phoneNumber",phoneNumber);
            Log.e("Verification_Number",phoneNumber);
            startActivity(intent);
            setNullEditText();
        }
    }

    private void NotificationGenerate() {
        Intent intent = new Intent(this, SignupActivityView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Regnum Toll OTP")
                .setContentText(inputNumber.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                //.setOnlyAlertOnce(true)
                .addAction(R.drawable.logo, "Verify This", pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, notification.build());
    }

    // OTP method are skip
    private void SendOTP() {

        try {
            String phonenumber= "0088"+inputNumber.getText().toString();
            Random random= new Random();
            randomOTPnumber= random.nextInt(999999);
            // Construct data
            String key="s7uxL5lA+Vo-rHXE3P7p3edDGVJz1RvlZnCto3ZrwJ";
            String apiKey = "apikey=" +key;
            String message = "&message=" + "Your OTP is: "+randomOTPnumber;
            String sender = "&sender=" + "Regnum Toll Plaza";
            String numbers = "&numbers=" + phonenumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Toast.makeText(this, "send OTP successfully", Toast.LENGTH_SHORT).show();
            //return stringBuffer.toString();
        } catch (Exception e) {
            //Log.d("Error SMS:",e.getMessage());
            Toast.makeText(this, "Error send OTP"+e.getMessage(), Toast.LENGTH_SHORT).show();
            //System.out.println("Error SMS "+e);
            //return "Error "+e;
        }
    }

    private void Validity (){

        int checkValid= loginViewModel.CheckValidity(inputName.getText().toString(), inputNumber.getText().toString(),
                inputEmail.getText().toString(), inputPassword.getText().toString(), inputRePassword.getText().toString());
        Log.d("checkValid",String.valueOf(checkValid));

        if (checkValid == 1){
            inputName.setError("Must enter name");
        } else if (checkValid == 2){
            inputNumber.setError("Must enter mobile");
        } else if (checkValid == 3){
            inputPassword.setError("Password at least 6 char long");
        } else if (checkValid == 4){
            inputRePassword.setError("password must similar");
        } else if (checkValid == 6){
            count++;
        }
    }

    private void InitialView() {
        inputName= findViewById(R.id.input_name);
        inputNumber= findViewById(R.id.input_number);
        inputEmail= findViewById(R.id.input_email);
        inputPassword= findViewById(R.id.input_password);
        inputRePassword= findViewById(R.id.input_re_password);
        btnSignUp= findViewById(R.id.btn_signup);
    }

    private void PostApi(final String name, final String email, final String password, final String mobile){

        apiClint= RetrofitClint.getRetrofitClint().create(ApiClint.class);
        Call<Example> call= apiClint.post(name, email, password, mobile);
        Log.e("signing_test_name", name);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()){
                    Log.e("request_code_match",String.valueOf(response.code()));
                    Toast.makeText(SignupActivityView.this, "Data Post successful", Toast.LENGTH_SHORT).show();
                }
                Log.e("Working_process_are_ok", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e("server_failed",t.getMessage());
            }
        });
    }

    private void setNullEditText(){
        inputName.setText("");
        inputNumber.setText("");
        inputEmail.setText("");
        inputPassword.setText("");
        inputRePassword.setText("");
    }

    private void DataStore(){
        preferences = getSharedPreferences("LogedDataStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LogedName",inputName.getText().toString());
        editor.putString("LogedEmail",inputEmail.getText().toString());
        editor.putString("LogedMobile",inputNumber.getText().toString());
        editor.putString("LogedPassword",inputPassword.getText().toString());
        editor.putString("LogedrePassword",inputRePassword.getText().toString());
        editor.putInt("OTP_PIN", randomOTPnumber);
        editor.apply();
        editor.commit();
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
