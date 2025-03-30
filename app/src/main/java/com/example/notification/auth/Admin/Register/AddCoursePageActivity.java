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
import com.example.notification.network.AdminCourse;

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

        List<AdminCourse> courseList = new ArrayList<>();
        for (String courseName : selectedCourses) {
            courseList.add(new AdminCourse(courseName));  // ✅ Now this should work!
        }

        AdminRegister admin = new AdminRegister(schoolName, city, address, mobileNumber, email, password, courseList);
        Log.d("RegisterAdmin", "Sending data: " + new Gson().toJson(admin));

        apiService.registerAdmin(admin).enqueue(new Callback<AdminRegister>() {
            @Override
            public void onResponse(Call<AdminRegister> call, Response<AdminRegister> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AdminRegister registeredAdmin = response.body();
                    String institutionId = registeredAdmin.getInstitutionId();  // ✅ Get institutionId

                    Log.d("RegisterAdmin", "Institution ID: " + institutionId);
                    Toast.makeText(AddCoursePageActivity.this, "Registered Successfully! ID: " + institutionId, Toast.LENGTH_LONG).show();

                    // TODO: Store institutionId in SharedPreferences or Database

                    startActivity(new Intent(AddCoursePageActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddCoursePageActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegister> call, Throwable t) {
                Toast.makeText(AddCoursePageActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}