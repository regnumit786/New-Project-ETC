package com.car.toll_car.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.car.toll_car.Model.Car_Detail;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.Model.UpdateModel;
import com.car.toll_car.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private String Car_Registration, Vehicles_Type;
    private static final String TAG= "Dashboard";
    private RequestQueue requestQueue;
    private RecyclerAdapter recyclerAdapter;
    private static final String URL= "http://192.168.50.17/RFIDApicbank/singleall.php?reg_no=Jassore_Metro_LA_1_4896";
    private List<Car_Detail> car_details;
    private RecyclerView recyclerView;

    private TextView carRegistration, vehicleType;

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

        carRegistration= findViewById(R.id.car_registration_title);
        vehicleType= findViewById(R.id.vehicles_types);
        car_details= new ArrayList<>();

        recyclerView= findViewById(R.id.setTollRateAndDateTime);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        requestQueue= Volley.newRequestQueue(this);
        SetData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.nav_profile){
            Intent intent= new Intent(this,Profile.class);
            startActivity(intent);
        } else if (id== R.id.ocr_activity){
            startActivity(new Intent(this, OCRActivityView.class));
        } else if (id== R.id.nav_share){
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent,"Share With"));
        } else if (id== R.id.nav_log_out){
            SharedPreferences sign_in_preferences = getSharedPreferences(getString(R.string.loginStore), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sign_in_preferences.edit();
            editor.clear();
            editor.commit();
            SharedPreferences sign_up_preferences = getSharedPreferences(getString(R.string.signupStore), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sign_up_preferences.edit();
            editor1.clear();
            editor1.commit();
            startActivity(new Intent(this, LoginActivityView.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    private void SetData(){
        Log.e(TAG,"Execute dashboard");
        JsonArrayRequest arrayRequest= new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject= response.getJSONObject(i);
                        Car_Detail car_detail= new Car_Detail();

                        Car_Registration= (jsonObject.getString("reg_no"));
                        Vehicles_Type= (jsonObject.getString("vehicle_class"));

                        car_detail.setToll_rate(jsonObject.getString("toll_rate"));
                        car_detail.setDatetime(jsonObject.getString("datetime"));
                        car_details.add(car_detail);

                        Log.e(TAG,jsonObject.getString("toll_rate"));
                        Log.e(TAG,jsonObject.getString("datetime"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerAdapter= new RecyclerAdapter(car_details, Dashboard.this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

                carRegistration.setText(Car_Registration);
                vehicleType.setText(Vehicles_Type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ResponseError: "+error.getMessage());
            }
        });
        requestQueue.add(arrayRequest);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        List<Car_Detail> car_details;
        Context context;

        public RecyclerAdapter(List<Car_Detail> car_details, Context context) {
            this.car_details = car_details;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item,parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Car_Detail car_detail= car_details.get(position);

            holder.tollRate.setText(car_detail.getToll_rate());
            holder.dateTime.setText(car_detail.getDatetime());
        }

        @Override
        public int getItemCount() {
            //return car_details.size();
            if (car_details != null) {
                return car_details.size();
            }else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tollRate, dateTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tollRate= itemView.findViewById(R.id.toll_rate);
                dateTime= itemView.findViewById(R.id.date_time);
            }
        }
    }
}
