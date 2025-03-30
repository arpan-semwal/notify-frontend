package com.example.notification.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.notification.network.AdminRegister;

public interface ApiService {
    @POST("api/users/login")
    Call<LoginResponse> loginUser(@Body UserRequest userRequest);

    @GET("api/messages") // ✅ Change from @Path to @Query
    Call<List<Message>> getMessages(@Query("schoolName") String schoolName);


    @POST("/api/admin/register")
    Call<AdminRegister> registerAdmin(@Body AdminRegister adminRegister);  // ✅ Updated response type


}
