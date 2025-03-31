package com.example.notification.auth.Student.Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.Dashboard.StudentDashboard.StudentDashboardActivity;
import com.example.notification.R;
import com.example.notification.network.ApiService;
import com.example.notification.network.RetrofitClient;
import com.example.notification.network.StudentRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRegisterActivity extends AppCompatActivity {
    private EditText edtName, edtFatherName, edtSchoolName, edtMobileNumber;
    private Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        edtName = findViewById(R.id.edt_name);
        edtFatherName = findViewById(R.id.edt_father_name);
        edtSchoolName = findViewById(R.id.edt_school_name);
        edtMobileNumber = findViewById(R.id.edt_mobile_number);
        btnRegister = findViewById(R.id.btn_register);

        apiService = RetrofitClient.getInstance().getApiService(); // Get API instance

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String fatherName = edtFatherName.getText().toString().trim();
        String schoolName = edtSchoolName.getText().toString().trim();
        String mobileNumber = edtMobileNumber.getText().toString().trim();

        if (name.isEmpty() || fatherName.isEmpty() || schoolName.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Mobile Number
        if (!mobileNumber.matches("\\d{10}")) { // Ensures exactly 10 digits
            Toast.makeText(this, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        StudentRegister student = new StudentRegister(name, fatherName, schoolName, mobileNumber);

        // Send data to backend
        apiService.registerStudent(student).enqueue(new Callback<StudentRegister>() {

            @Override
            public void onResponse(Call<StudentRegister> call, Response<StudentRegister> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(StudentRegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                } else {
                    try {
                        // Read the error response body
                        String errorBody = response.errorBody().string();
                        Log.e("Registration Error", "Server Response: " + errorBody);
                        Toast.makeText(StudentRegisterActivity.this, "Failed! Check logs for details.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("Response Error", "Exception: " + e.getMessage());
                        Toast.makeText(StudentRegisterActivity.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<StudentRegister> call, Throwable t) {
                Log.e("API Call Failure", "Error: " + t.getMessage());
                Toast.makeText(StudentRegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToDashboard() {
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(StudentRegisterActivity.this, StudentDashboardActivity.class);
            startActivity(intent);
            finish();
        }, 1000); // 1-second delay for smooth transition
    }
}
