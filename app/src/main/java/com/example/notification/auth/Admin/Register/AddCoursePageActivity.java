package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.Dashboard.AdminDashboard.AdminDashboardActivity;
import com.example.notification.R;
import com.example.notification.network.AdminRegister;
import com.example.notification.network.ApiService;
import com.example.notification.network.RegisterResponse;
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
    private ArrayAdapter<String> coursesAdapter;
    private ApiService apiService;

    private String schoolName, city, address, mobileNumber, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent intent = getIntent();
        schoolName = intent.getStringExtra("schoolName");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        mobileNumber = intent.getStringExtra("mobileNumber");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        etCourseName = findViewById(R.id.et_course_name);
        tvSelectedCourses = findViewById(R.id.tv_selected_courses);
        btnAddCourse = findViewById(R.id.btn_add_course);
        btnRegister = findViewById(R.id.btn_register);

        apiService = RetrofitClient.getInstance().getApiService();

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
            selectedCourses.add(courseName);
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

        List<AdminRegister.AdminCourse> courseList = new ArrayList<>();
        for (String courseName : selectedCourses) {
            courseList.add(new AdminRegister.AdminCourse(courseName));
        }

        AdminRegister admin = new AdminRegister(schoolName, city, address, mobileNumber, email, password, courseList);
        Log.d("RegisterAdmin", "Sending data: " + new Gson().toJson(admin));

        apiService.registerAdmin(admin).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("RegisterAdmin", "Response Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddCoursePageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCoursePageActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RegisterAdmin", "Error: " + errorBody);
                        Toast.makeText(AddCoursePageActivity.this, "Server Error: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(AddCoursePageActivity.this, "Unexpected response!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("RegisterAdmin", "Network error: " + t.getMessage());
                Toast.makeText(AddCoursePageActivity.this, "Network Error! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}