package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;

public class AdminRegisterPageActivity extends AppCompatActivity {
    private EditText etSchoolName, etCity, etAddress, etMobileNumber, etSchoolEmail, etPassword;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        etSchoolName = findViewById(R.id.et_school_name);
        etCity = findViewById(R.id.et_city);
        etAddress = findViewById(R.id.et_address);
        etMobileNumber = findViewById(R.id.et_mobile_number);
        etSchoolEmail = findViewById(R.id.et_school_email);
        etPassword = findViewById(R.id.et_password);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> proceedToAddCoursePage());
    }

    private void proceedToAddCoursePage() {
        String schoolName = etSchoolName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etSchoolEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (schoolName.isEmpty() || city.isEmpty() || address.isEmpty() ||
                mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AddCoursePageActivity.class);
        intent.putExtra("schoolName", schoolName);
        intent.putExtra("city", city);
        intent.putExtra("address", address);
        intent.putExtra("mobileNumber", mobileNumber);
        intent.putExtra("email", email);
        intent.putExtra("password", password);

        startActivity(intent);
    }
}
