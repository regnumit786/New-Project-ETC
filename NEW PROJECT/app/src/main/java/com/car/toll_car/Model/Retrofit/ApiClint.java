package com.car.toll_car.Model.Retrofit;

import com.car.toll_car.Model.PostRequestModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiClint {

    @FormUrlEncoded
    @POST("RFIDApicbank/registr.php")
    Call<PostRequestModel> post (
           // @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile);


    @PUT("RFIDApicbank/registr.php{id}")
    Call<PostRequestModel> update(@Path("id") @Body PostRequestModel postRequestModel);
}
