package com.example.notification.network;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.notification.models.AdminCourse;
import com.example.notification.models.AdminLoginRequest;
import com.example.notification.models.AdminRegister;
import com.example.notification.models.MessageRequest;
import com.example.notification.models.MessageResponse;
import com.example.notification.models.StudentRegister;
import com.example.notification.models.SchoolResponse;
import com.example.notification.models.SyncResponse;

public interface ApiService {

    // Use  // Admin login (ADDED)
    @POST("api/admin/login")
    Call<AdminLoginResponse> loginAdmin(@Body AdminLoginRequest request);
    @POST("api/users/login")
    Call<LoginResponse> loginUser(@Body UserRequest userRequest);

    // Fetch messages based on schoolUniqueId and courseUniqueId
    @GET("api/messages/fetch")
    Call<List<MessageResponse>> getMessages(@Query("schoolUniqueId") String schoolId, @Query("courseUniqueId") String courseId);

    // Send message (used by admin panel)
    @POST("/api/messages/send")
    Call<MessageResponse> sendMessage(@Body MessageRequest messageRequest);

    // Register new admin
    @POST("/api/admin/register")
    Call<RegisterResponse> registerAdmin(@Body AdminRegister adminRegister);

    // Register new student
    @POST("api/student/register")
    Call<StudentRegister> registerStudent(@Body StudentRegister student);

    // Fetch all schools with uniqueId
    @GET("/api/schools/all")
    Call<List<SchoolResponse>> getAllSchools();  // Returns list of SchoolResponse with uniqueId and name

    // Fetch all courses for a specific school using schoolUniqueId
    @GET("/api/fetchstudentcourse/{schoolUniqueId}")
    Call<List<AdminCourse>> getCoursesBySchool(@Path("schoolUniqueId") String schoolUniqueId);

    @GET("api/messages/sync")
    Call<SyncResponse> syncMessages(@Query("schoolUniqueId") String schoolUniqueId,
                                    @Query("courseUniqueId") String courseUniqueId);









}
