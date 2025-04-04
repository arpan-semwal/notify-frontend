package com.example.notification.network;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.notification.models.AdminRegister;
import com.example.notification.network.LoginResponse;
import com.example.notification.models.MessageRequest;
import com.example.notification.models.MessageResponse;
import com.example.notification.models.StudentRegister;
import com.example.notification.network.UserRequest;

public interface ApiService {

    // ✅ User login
    @POST("api/users/login")
    Call<LoginResponse> loginUser(@Body UserRequest userRequest);

    // ✅ Fetch messages based on school and course
    @GET("/api/messages/fetch")
    Call<List<MessageResponse>> getMessages(
            @Query("schoolName") String schoolName,
            @Query("course") String course
    );




    // ✅ Send message (used by admin panel)
    @POST("/api/messages/send")
    Call<Map<String, String>> sendMessage(@Body MessageRequest messageRequest);

    // ✅ Register new admin
    @POST("/api/admin/register")
    Call<AdminRegister> registerAdmin(@Body AdminRegister adminRegister);

    // ✅ Register new student
    @POST("api/student/register")
    Call<StudentRegister> registerStudent(@Body StudentRegister student);

    // ✅ Fetch all school names
    @GET("/api/schools/all")
    Call<List<String>> getSchoolNames();

    // ✅ Fetch all courses for a specific school
    @GET("/api/fetchstudentcourse/{schoolName}")
    Call<List<String>> getCoursesBySchool(@Path("schoolName") String schoolName);
}
