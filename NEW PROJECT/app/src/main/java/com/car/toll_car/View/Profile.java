package com.car.toll_car.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.car.toll_car.Model.Retrofit.ApiClint;
import com.car.toll_car.Model.Retrofit.RetrofitClint;
import com.car.toll_car.R;
import com.google.android.gms.dynamic.IFragmentWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private TextView profileName, profileEmail, profilePhone, profileAccount;
    private RequestQueue mRequestQueue;
    private ApiClint apiClint;
    private EditText name, email;
    private String mobile;
    Button btnUpdate;
    private String REQUEST_URL;
    private String login_mobile, sign_up_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        InitialView();
        mRequestQueue= Volley.newRequestQueue(this);
        GetAllData();
    }

    private void InitialView() {
        profileName= findViewById(R.id.profile_name);
        profileEmail= findViewById(R.id.profile_email);
        profilePhone= findViewById(R.id.profile_phone);
        profileAccount= findViewById(R.id.profile_account);
    }

    private void getSharepreferance(){
        SharedPreferences preferences= getSharedPreferences("LogedDataStore", Context.MODE_PRIVATE);
        if (preferences.contains("LogedName") && preferences.contains("LogedEmail") &&
                preferences.contains("LogedMobile") && preferences.contains("LogedPassword")) {

            String name = preferences.getString("LogedName", "default");
            String email = preferences.getString("LogedEmail", "default");
            String mobile = preferences.getString("LogedMobile", "default");
            profileName.setText(name);
            profileEmail.setText(email);
            profilePhone.setText(mobile);
            profileAccount.setText(R.string.account);
        }
    }

    /*

    private void getPostUsingRetrofit(){
        apiClint= RetrofitClint.getRetrofitClint().create(ApiClint.class);
        Call<SignUpPostModel> call= apiClint.getPostData();
        call.enqueue(new Callback<SignUpPostModel>() {
            @Override
            public void onResponse(Call<SignUpPostModel> call, Response<SignUpPostModel> response) {
                if (response.code() == 200){
                    Log.e("response_successfully",String.valueOf(response.code()));
                } else {
                    Toast.makeText(Profile.this, "Server request failed", Toast.LENGTH_SHORT).show();
                }
                Log.e("Working_process_are_ok", String.valueOf(response.code()));
                SignUpPostModel signUpPostModel= response.body();
                profileName.setText(signUpPostModel.getName());
                profileEmail.setText(signUpPostModel.getEmail());
            }

            @Override
            public void onFailure(Call<SignUpPostModel> call, Throwable t) {
                Log.e("Error_is:",t.getMessage());
            }
        });
    }

    */

    private void GetAllData(){
        /**
         * get log in data
         */
        SharedPreferences login_preferences = getSharedPreferences("Login_DataDemoStore", Context.MODE_PRIVATE);
        login_mobile= login_preferences.getString("Login_Mobile","");
        int value= login_preferences.getInt("Check_login_value",0);
        ///log
        Log.e("LOGIN_REQUEST_MOBILE", login_mobile);
        /**
         * get sign up data
         */
        SharedPreferences sign_up_preferences = getSharedPreferences("Sign_up_DatabaseStore", Context.MODE_PRIVATE);
        sign_up_mobile= sign_up_preferences.getString("Sign_in_Mobile","");
        ///log
        Log.e("SIGN_UP_REQUEST_MOBILE", sign_up_mobile);
        if (value == 1){
            Log.e("Execute_login_blocks", login_mobile);
            GetLoginData();
        } else{
            Log.e("Execute_Sign_up_blocks", sign_up_mobile);
            GetSignUpData();
        }
    }

    private void GetSignUpData(){

        REQUEST_URL= "http://192.168.50.10/RFIDApicbank/updated.php?mobile="+sign_up_mobile;

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, REQUEST_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i= 0; i<= response.length(); i++) {
                            try {
                                JSONObject object= response.getJSONObject(i);
                                profileName.setText(object.getString("name"));
                                profileEmail.setText(object.getString("email"));
                                profilePhone.setText(sign_up_mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse",error.getMessage());
            }
        });

        mRequestQueue.add(jsonArrayRequest);
    }

    private void GetLoginData(){

        /*JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.GET, REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int i= 0; i<= response.length(); i++) {
                            try {
                                //JSONObject object= response.getJSONObject(i);
                                profileName.setText(response.getString("name"));
                                profileEmail.setText(response.getString("email"));
                                profilePhone.setText(mobile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_Response",error.getMessage());
            }
        });*/

        try {
            REQUEST_URL= "http://192.168.50.10/RFIDApicbank/updated.php?mobile="+login_mobile;

            JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, REQUEST_URL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i= 0; i<= response.length(); i++) {
                                try {
                                    JSONObject object= response.getJSONObject(i);
                                    profileName.setText(object.getString("name"));
                                    profileEmail.setText(object.getString("email"));
                                    profilePhone.setText(login_mobile);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ErrorResponse","error.getMessage()");
                }
            });
            mRequestQueue.add(jsonArrayRequest);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.action_update){
            DialogBuilder();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogBuilder(){
        final Dialog dialog= new Dialog(this);
        dialog.setContentView(R.layout.updateitem);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        name= dialog.findViewById(R.id.update_name);
        email= dialog.findViewById(R.id.update_email);
        btnUpdate= dialog.findViewById(R.id.btn_update);

        name.setText(profileName.getText().toString());
        email.setText(profileEmail.getText().toString());
        mobile= profilePhone.getText().toString();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePost();
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void UpdatePost() {
        String UPDATE_URL = "http://192.168.50.10/RFIDApicbank/updated.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, UPDATE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("updatednull")) {
                    Toast.makeText(Profile.this, "Update successfully: " + response, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Profile.this, "Update Error: "+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "ErrorResponse: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String put_name = name.getText().toString().trim();
                String put_email = email.getText().toString().trim();
                String put_mobile = mobile;

                params.put("mobile", put_mobile);
                params.put("name", put_name);
                params.put("email", put_email);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
