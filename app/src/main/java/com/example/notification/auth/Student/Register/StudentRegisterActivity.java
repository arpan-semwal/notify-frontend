package com.example.notification.auth.Student.Register;


import android.content.Intent;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.Dashboard.StudentDashboard.StudentDashboardActivity;
import com.example.notification.R;
import com.example.notification.models.AdminCourse;
import com.example.notification.models.StudentRegister;
import com.example.notification.dto.SchoolResponse;
import com.example.notification.network.ApiService;
import com.example.notification.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StudentRegisterActivity extends AppCompatActivity {
    private EditText edtName, edtFatherName, edtMobileNumber;
    private Spinner schoolSpinner, courseSpinner;
    private Button btnRegister;
    private ApiService apiService;

    private List<String> schoolNames = new ArrayList<>();
    private Map<String, String> schoolNameToIdMap = new HashMap<>();
    private List<String> courseNames = new ArrayList<>();
    private Map<String, String> courseNameToIdMap = new HashMap<>();  // Map to store course name and ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        edtName = findViewById(R.id.edt_name);
        edtFatherName = findViewById(R.id.edt_father_name);
        edtMobileNumber = findViewById(R.id.edt_mobile_number);
        schoolSpinner = findViewById(R.id.spinner_school_name);
        courseSpinner = findViewById(R.id.spinner_course);
        btnRegister = findViewById(R.id.btn_register);

        apiService = RetrofitClient.getInstance().getApiService();

        fetchSchools(); // Fetch the list of schools

        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSchool = schoolNames.get(position);
                String schoolUniqueId = schoolNameToIdMap.get(selectedSchool);
                fetchCoursesBySchool(schoolUniqueId); // Fetch courses by schoolUniqueId
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnRegister.setOnClickListener(v -> registerUser());
    }

    // Fetch schools and map schoolName to schoolUniqueId
    private void fetchSchools() {
        apiService.getAllSchools().enqueue(new Callback<List<SchoolResponse>>() {
            @Override
            public void onResponse(Call<List<SchoolResponse>> call, Response<List<SchoolResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    schoolNames.clear();
                    schoolNameToIdMap.clear();

                    for (SchoolResponse school : response.body()) {
                        schoolNames.add(school.getSchoolName());
                        schoolNameToIdMap.put(school.getSchoolName(), school.getUniqueId());
                    }
                    setupSchoolSpinner();
                } else {
                    Toast.makeText(StudentRegisterActivity.this, "Failed to load schools", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SchoolResponse>> call, Throwable t) {
                Toast.makeText(StudentRegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fetch courses using schoolUniqueId and store course names with their unique IDs
    private void fetchCoursesBySchool(String schoolUniqueId) {
        apiService.getCoursesBySchool(schoolUniqueId).enqueue(new Callback<List<AdminCourse>>() {
            @Override
            public void onResponse(Call<List<AdminCourse>> call, Response<List<AdminCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseNames.clear();
                    courseNameToIdMap.clear();

                    for (AdminCourse course : response.body()) {
                        courseNames.add(course.getCourseName()); // show course name in spinner
                        courseNameToIdMap.put(course.getCourseName(), course.getCourseUniqueId()); // map name to ID
                    }
                    setupCourseSpinner();
                } else {
                    Toast.makeText(StudentRegisterActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminCourse>> call, Throwable t) {
                Toast.makeText(StudentRegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Set up the school spinner
    private void setupSchoolSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schoolNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapter);
    }

    // Set up the course spinner
    private void setupCourseSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }

    // Register user and send schoolUniqueId and courseUniqueId
    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String fatherName = edtFatherName.getText().toString().trim();
        String selectedSchoolName = schoolSpinner.getSelectedItem().toString();
        String selectedCourseName = courseSpinner.getSelectedItem().toString();
        String mobileNumber = edtMobileNumber.getText().toString().trim();

        if (name.isEmpty() || fatherName.isEmpty() || selectedCourseName.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mobileNumber.matches("\\d{10}")) {
            Toast.makeText(this, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolUniqueId = schoolNameToIdMap.get(selectedSchoolName);
        String courseUniqueId = courseNameToIdMap.get(selectedCourseName);

        if (schoolUniqueId == null || courseUniqueId == null) {
            Toast.makeText(this, "School or Course ID not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the StudentRegister object with the correct parameters
        StudentRegister student = new StudentRegister(name, fatherName, courseUniqueId, mobileNumber, schoolUniqueId);

        apiService.registerStudent(student).enqueue(new Callback<StudentRegister>() {
            @Override
            public void onResponse(Call<StudentRegister> call, Response<StudentRegister> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StudentRegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard(name, fatherName, selectedSchoolName, selectedCourseName, mobileNumber);
                } else {
                    Toast.makeText(StudentRegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentRegister> call, Throwable t) {
                Toast.makeText(StudentRegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Navigate to the dashboard after successful registration
    private void navigateToDashboard(String name, String fatherName, String schoolName, String courseName, String mobileNumber) {
        String schoolUniqueId = schoolNameToIdMap.get(schoolName);
        String courseUniqueId = courseNameToIdMap.get(courseName);

        if (schoolUniqueId == null || courseUniqueId == null) {
            Toast.makeText(StudentRegisterActivity.this, "School or Course ID not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(StudentRegisterActivity.this, StudentDashboardActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("fatherName", fatherName);
            intent.putExtra("schoolUniqueId", schoolUniqueId);  // Sending schoolUniqueId
            intent.putExtra("courseUniqueId", courseUniqueId);  // Sending courseUniqueId
            intent.putExtra("mobileNumber", mobileNumber);
            startActivity(intent);
            finish();
        }, 1000);
    }
}
