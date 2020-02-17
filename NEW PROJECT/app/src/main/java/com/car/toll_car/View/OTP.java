package com.car.toll_car.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.car.toll_car.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    private EditText editTextOTP;
    private Button sendOTP;
    private ProgressBar progressBar;
    String verificationId;
    int otp;
    int receiveOTP;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setTitle("OTP Verification");
        InitialView();
        mAuth= FirebaseAuth.getInstance();

        String receiveNumber= getIntent().getStringExtra("phoneNumber");
        Log.e("receiveVerifyNumber", receiveNumber);
        sendVerificationCode(receiveNumber);

    }

    public void SendOTP(View view){
        String code = editTextOTP.getText().toString().trim();
        if ((code.isEmpty() || code.length() < 6)){
            editTextOTP.setError("Enter code...");
            editTextOTP.requestFocus();
            return;
        }
        verifyCode(code);
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(OTP.this, "Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(OTP.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(intent);
                } else {
                    Toast.makeText(OTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String sms_code = phoneAuthCredential.getSmsCode();
                    if (sms_code != null){
                        editTextOTP.setText(sms_code);
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(sms_code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(OTP.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            };

    private void InitialView() {
        editTextOTP= findViewById(R.id.receiveOTP);
        progressBar= findViewById(R.id.progress_bar);
    }
}
