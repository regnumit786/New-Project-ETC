package com.car.toll_car.Model.Retrofit;

import com.car.toll_car.Model.Example;
import com.car.toll_car.Model.PostRequestModel;
import com.car.toll_car.Model.UpdateModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiClint {

    /*@FormUrlEncoded
    @POST("RFIDApicbank/registr.php")
    Call<List<Example>> post (
           // @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile);*/


    /*@Multipart
    @FormUrlEncoded
    @POST("RFIDApicbank/registr.php")
    Call<List<PostRequestModel>> post (@Body PostRequestModel postRequestModel);*/

    @POST("RFIDApicbank/registr.php")
    Call<List<Example>> post(@Body Example example);


    @GET("RFIDApicbank/registr.php")
    Call<List<Example>> getPostData();

    @PUT("RFIDApicbank/registr.php{mobile}")
    Call<UpdateModel> update(@Path ("mobile") String mobile, @Body UpdateModel updateModel);

}
