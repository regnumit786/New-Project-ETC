package com.car.toll_car.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.car.toll_car.Model.PostRequestModel;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.R;
import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView profileName, profileEmail, profilePhone, profileAccount;
    private DrawerLayout drawer;
    private ApiClint apiClint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("User Profile");

        drawer= findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        InitialView();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        profileName.setText(preferences.getString("name","default_value"));
        profileEmail.setText(preferences.getString("email","default_value"));
        profilePhone.setText(preferences.getString("mobile","default_value"));
    }

    private void InitialView() {
        profileName= findViewById(R.id.profile_name);
        profileEmail= findViewById(R.id.profile_email);
        profilePhone= findViewById(R.id.profile_phone);
        profileAccount= findViewById(R.id.profile_account);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.update){
            DialogBuilder();
        } else if (id== R.id.ocr_activity){
            startActivity(new Intent(this, OCRActivityView.class));
        } else if (id== R.id.nav_share){
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"Share With"));
        } else if (id== R.id.nav_logout){
            startActivity(new Intent(this, SignupActivityView.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void DialogBuilder(){

        apiClint= RetrofitClint.getRetrifitClint().create(ApiClint.class);

        //Call<PostRequestModel> call= apiClint.update(profileName.getText().toString(),)

        final Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.updateitem);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final EditText name, phone, email;
        Button btnUpdate;
        name= dialog.findViewById(R.id.update_name);
        phone= dialog.findViewById(R.id.update_number);
        email= dialog.findViewById(R.id.update_email);
        btnUpdate= dialog.findViewById(R.id.btn_update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName.setText(name.getText().toString());
                profilePhone.setText(phone.getText().toString());
                profileEmail.setText(email.getText().toString());
                dialog.dismiss();;
            }
        });
        dialog.create();
        dialog.show();
    }
}
