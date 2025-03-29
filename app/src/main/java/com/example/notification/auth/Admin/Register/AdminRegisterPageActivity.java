package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminDashboardActivity;
import com.example.notification.R;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.network.AdminRegister;
import com.example.notification.network.ApiService;
import com.example.notification.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRegisterPageActivity extends AppCompatActivity {

    private EditText etSchoolName, etCity, etAddress, etMobileNumber, etSchoolEmail, etPassword;
    private Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        // Initialize Views
        etSchoolName = findViewById(R.id.et_school_name);
        etCity = findViewById(R.id.et_city);
        etAddress = findViewById(R.id.et_address);
        etMobileNumber = findViewById(R.id.et_mobile_number);
        etSchoolEmail = findViewById(R.id.et_school_email);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);

        // Initialize Retrofit API Service
        apiService = RetrofitClient.getInstance().getApiService();

        // Handle Registration Click
        btnRegister.setOnClickListener(v -> registerAdmin());
    }

    private void registerAdmin() {
        String schoolName = etSchoolName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etSchoolEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic Validation
        if (schoolName.isEmpty() || city.isEmpty() || address.isEmpty() ||
                mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create AdminRegister Object
        AdminRegister admin = new AdminRegister(schoolName, city, address, mobileNumber, email, password);

        // Make API Call
        apiService.registerAdmin(admin).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminRegisterPageActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                    // Navigate to Admin Login or Next Page
                    startActivity(new Intent(AdminRegisterPageActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(AdminRegisterPageActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AdminRegisterPageActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
