package com.car.toll_car.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.car.toll_car.Model.Example;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.Model.UpdateModel;
import com.car.toll_car.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ApiClint apiClint;
    private EditText name, phone, email;
    private Button btnUpdate;
    private List<Example> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("User Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer= findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.nav_profile){
            startActivity(new Intent(this,Profile.class));
        } else if (id== R.id.update){
            DialogBuilder();
        } else if (id== R.id.ocr_activity){
            startActivity(new Intent(this, OCRActivityView.class));
        } else if (id== R.id.nav_share){
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"Share With"));
        } else if (id== R.id.nav_log_out){
            startActivity(new Intent(this, LoginActivityView.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void DialogBuilder(){

        apiClint= RetrofitClint.getRetrofitClint().create(ApiClint.class);

        final Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.updateitem);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        name= dialog.findViewById(R.id.update_name);
        phone= dialog.findViewById(R.id.update_number);
        email= dialog.findViewById(R.id.update_email);
        btnUpdate= dialog.findViewById(R.id.btn_update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPost();
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
    /**
    private void UpdatePost (){
        UpdateModel updateModel= new UpdateModel(name.getText().toString(), email.getText().toString());
        Call<UpdateModel> call= apiClint.update(phone.getText().toString(), updateModel);
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful()){
                    Log.e("response_successfully",String.valueOf(response.code()));
                }

                //UpdateModel updateDate= response.body();
                profileName.setText(name.getText().toString());
                Log.e("profileName",profileName.getText().toString());
                profileEmail.setText(email.getText().toString());
                Log.e("profileEmail",profileEmail.getText().toString());
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                Log.e("response_failed",t.getMessage());
            }
        });
    }
    */

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("You Want Exit !");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onYesClick();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    }

    private void onYesClick() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        this.finish();
    }
}
