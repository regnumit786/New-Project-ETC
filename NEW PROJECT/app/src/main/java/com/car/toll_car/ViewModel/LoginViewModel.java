package com.car.toll_car.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.car.toll_car.Model.LoginModel;
import com.car.toll_car.Model.SignUpModel;

public class LoginViewModel extends AndroidViewModel {
    SignUpModel signUpModel;
    LoginModel loginModel;
    public LoginViewModel(@NonNull Application application) {
        super(application);

    }

    public int CheckValidity(String name, String mobile, String email, String password,
                             String rePassword){

        signUpModel= new SignUpModel(name, mobile, email, password, rePassword);

        if (TextUtils.isEmpty(signUpModel.getName())){
            return 1;
        } else if (TextUtils.isEmpty(signUpModel.getMobile())){
            return 2;
        } else if (signUpModel.getPassword().length()<6){
            return 3;
        } else if (!signUpModel.getPassword().equals(signUpModel.getRePassword())){
            return 4;
        }
        return 6;
    }

    public int CheckLoginValidity(String mobile, String password){
        loginModel= new LoginModel(mobile, password);
        if (TextUtils.isEmpty(loginModel.getMobile())) {
            return 0;
        } else if (loginModel.getPassword().length() < 6) {
            return 2;
        } else {
            return 3;
        }
    }
}
