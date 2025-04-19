package com.example.notification.Dashboard.AdminDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.models.AdminCourse;
import com.example.notification.models.MessageRequest;
import com.example.notification.models.MessageResponse;
import com.example.notification.network.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private Spinner spinnerSelection;
    private EditText etMessage;
    private Button btnSendMessage, btnLogout;
    private TextView tvSchoolName;

    private List<AdminCourse> courseList = new ArrayList<>();
    private AdminCourse selectedCourse;

    private String schoolName;
    private String schoolUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        spinnerSelection = findViewById(R.id.spinner_selection);
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnLogout = findViewById(R.id.btn_logout);
        tvSchoolName = findViewById(R.id.tv_school_name);

        loadAdminData();
        setupSpinner();

        // Handle button clicks
        btnSendMessage.setOnClickListener(v -> sendMessage());
        btnLogout.setOnClickListener(v -> logoutAdmin());
    }

    private void loadAdminData() {
        // Retrieve admin data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        schoolName = prefs.getString("schoolName", "Unknown School");
        schoolUniqueId = prefs.getString("schoolUniqueId", "Unknown ID");

        tvSchoolName.setText("School: " + schoolName + "\nUnique ID: " + schoolUniqueId);

        // Fetch the courses from the backend (via API call)
        RetrofitClient.getInstance().getApiService().getCoursesBySchool(schoolUniqueId)  // Corrected method name
                .enqueue(new Callback<List<AdminCourse>>() {
                    @Override
                    public void onResponse(Call<List<AdminCourse>> call, Response<List<AdminCourse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            courseList = response.body();  // Set courses fetched from backend

                            // Log all course info for debugging
                            Log.d("AdminDashboard", "Loaded Courses from backend:");
                            for (AdminCourse course : courseList) {
                                Log.d("AdminDashboard", "Course Name: " + course.getCourseName() +
                                        ", Course Unique ID: " + course.getCourseUniqueId());
                            }

                            // Setup spinner after courses are fetched
                            setupSpinner();
                        } else {
                            Toast.makeText(AdminDashboardActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AdminCourse>> call, Throwable t) {
                        Toast.makeText(AdminDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSpinner() {
        // Set up the spinner to display the course list
        ArrayAdapter<AdminCourse> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelection.setAdapter(adapter);

        // Listen for course selection in the spinner
        spinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = courseList.get(position);
                Log.d("AdminDashboard", "Selected Course Unique ID: " + selectedCourse.getCourseUniqueId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCourse = null;
            }
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (selectedCourse == null || messageText.isEmpty()) {
            Toast.makeText(this, "Select a course and enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log message and course ID for debugging
        Log.d("AdminDashboard", "Sending Message with Course Unique ID: " + selectedCourse.getCourseUniqueId());

        MessageRequest request = new MessageRequest(
                schoolUniqueId,
                selectedCourse.getCourseUniqueId(),  // Send only unique ID
                messageText
        );

        RetrofitClient.getInstance().getApiService().sendMessage(request)
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminDashboardActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            etMessage.setText("");
                        } else {
                            Toast.makeText(AdminDashboardActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(AdminDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logoutAdmin() {
        // Log out the admin and clear SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("schoolUniqueId");
        editor.apply();

        // Redirect to login page
        startActivity(new Intent(this, AdminLoginPageActivity.class));
        finish();
    }
}
