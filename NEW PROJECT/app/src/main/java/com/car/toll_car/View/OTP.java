package com.car.toll_car.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.car.toll_car.R;

public class OTP extends AppCompatActivity {

    private EditText editTextOTP;
    private Button sendOTP;
    int otp;
    int receiveOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("OTP Verification");
        InitialView();
    }

    public void SendOTP(View view){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        otp= settings.getInt("OTP_PIN",0);
        Log.e("show_otp",String.valueOf(otp));
        receiveOTP= Integer.parseInt(editTextOTP.getText().toString());
        Log.e("receive_otp",String.valueOf(receiveOTP));

        if (receiveOTP == otp){
            Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OTP.this, Dashboard.class));
        }else {
            Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void InitialView() {
        editTextOTP= findViewById(R.id.receiveOTP);
        sendOTP= findViewById(R.id.receiveOTPBtn);
    }
}
