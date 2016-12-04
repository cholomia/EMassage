package com.capstone.tip.emassage.app;

import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.USERNAME) String username,
                              @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<User> register(@Field(Constants.USERNAME) String username,
                        @Field(Constants.FIRST_NAME) String firstName,
                        @Field(Constants.LAST_NAME) String lastName,
                        @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.COURSES)
    Call<List<Course>> courses();

}
