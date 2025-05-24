package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminDashboardHome;
import com.example.notification.R;
import com.example.notification.models.AdminCourse;
import com.example.notification.models.AdminRegister;
import com.example.notification.network.ApiService;
import com.example.notification.dto.RegisterResponse;
import com.example.notification.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCoursePageActivity extends AppCompatActivity {
    private AutoCompleteTextView etCourseName;
    private TextView tvSelectedCourses;
    private Button btnAddCourse, btnRegister;
    private List<String> selectedCourses = new ArrayList<>();
    private List<AdminCourse> courseList = new ArrayList<>();
    private ArrayAdapter<String> coursesAdapter;
    private ApiService apiService;

    private String schoolName, city, address, mobileNumber, email, password, institutionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Get data from Intent
        Intent intent = getIntent();
        schoolName = intent.getStringExtra("schoolName");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        mobileNumber = intent.getStringExtra("mobileNumber");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        institutionType = intent.getStringExtra("institutionType");

        if (institutionType == null || institutionType.isEmpty()) {
            institutionType = "School";
        }

        // Initialize UI elements
        etCourseName = findViewById(R.id.et_course_name);
        tvSelectedCourses = findViewById(R.id.tv_selected_courses);
        btnAddCourse = findViewById(R.id.btn_add_course);
        btnRegister = findViewById(R.id.btn_register);

        apiService = RetrofitClient.getInstance().getApiService();

        // Sample courses for selection
        List<String> allCourses = new ArrayList<>();
        allCourses.add("B.Tech in Computer Science");
        allCourses.add("B.Tech in Civil Engineering");
        allCourses.add("B.Tech in Mechanical Engineering");
        allCourses.add("BBA in Business Administration");
        allCourses.add("BBA in Marketing");
        allCourses.add("B.Com in Accounting and Finance");

        coursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allCourses);
        etCourseName.setAdapter(coursesAdapter);

        btnAddCourse.setOnClickListener(v -> addCourse());
        btnRegister.setOnClickListener(v -> registerAdmin());
    }

    private void addCourse() {
        String courseName = etCourseName.getText().toString().trim();
        if (!courseName.isEmpty() && !selectedCourses.contains(courseName)) {
            AdminCourse course = new AdminCourse(courseName); // ✅ No unique ID here
            selectedCourses.add(courseName);
            courseList.add(course);

            updateSelectedCoursesDisplay();
            etCourseName.setText("");
        } else {
            Toast.makeText(this, "Course already selected or empty!", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateSelectedCoursesDisplay() {
        tvSelectedCourses.setText(selectedCourses.isEmpty() ? "Selected Courses: " : "Selected: " + String.join(", ", selectedCourses));
    }

    private void registerAdmin() {
        if (selectedCourses.isEmpty()) {
            Toast.makeText(this, "Please select at least one course!", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminRegister admin = new AdminRegister(schoolName, city, address, mobileNumber, email, password, institutionType, courseList);
        Log.d("RegisterAdmin", "Sending data: " + new Gson().toJson(admin));

        apiService.registerAdmin(admin).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    String institutionId = registerResponse.getUniqueId();

                    Log.d("RegisterAdmin", "Institution ID: " + institutionId);
                    Toast.makeText(AddCoursePageActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();

                    SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("schoolUniqueId", institutionId);
                    editor.putString("schoolName", schoolName);

                    // Store courses list in SharedPreferences as JSON
                    String coursesJson = new Gson().toJson(courseList);
                    editor.putString("selectedCourses", coursesJson); // Save as JSON string
                    editor.apply();

                    // Navigate to the dashboard
                    Intent dashboardIntent = new Intent(AddCoursePageActivity.this, AdminDashboardHome.class);
                    dashboardIntent.putExtra("schoolName", schoolName);
                    startActivity(dashboardIntent);
                    finish();
                } else {
                    Toast.makeText(AddCoursePageActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("RegisterAdmin", "Network error: " + t.getMessage(), t);
                Toast.makeText(AddCoursePageActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
