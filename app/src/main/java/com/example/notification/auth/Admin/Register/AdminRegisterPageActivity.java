package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;

public class AdminRegisterPageActivity extends AppCompatActivity {

    private EditText etSchoolName, etCity, etAddress, etMobileNumber, etSchoolEmail, etPassword, etConfirmPassword;
    private TextView tvPasswordError;
    private Button btnNext, btnUploadPhoto;
    private ImageView ivSelectedPhoto;

    private Uri selectedImageUri;

    // Gallery launcher
    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ivSelectedPhoto.setImageURI(selectedImageUri); // Show selected image
                    Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No photo selected.", Toast.LENGTH_SHORT).show();
                }
            });

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
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        tvPasswordError = findViewById(R.id.tv_password_match_error);
        btnNext = findViewById(R.id.btn_next);
        btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        ivSelectedPhoto = findViewById(R.id.iv_selected_photo);

        // Button listeners
        btnUploadPhoto.setOnClickListener(v -> openGallery());
        btnNext.setOnClickListener(v -> proceedToAddCoursePage());
    }

    private void openGallery() {
        galleryLauncher.launch("image/*");
    }

    private void proceedToAddCoursePage() {
        String schoolName = etSchoolName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String email = etSchoolEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate fields
        if (schoolName.isEmpty() || city.isEmpty() || address.isEmpty() ||
                mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            tvPasswordError.setVisibility(TextView.VISIBLE);
            return;
        } else {
            tvPasswordError.setVisibility(TextView.GONE);
        }

        Intent intent = new Intent(this, AddCoursePageActivity.class);
        intent.putExtra("schoolName", schoolName);
        intent.putExtra("city", city);
        intent.putExtra("address", address);
        intent.putExtra("mobileNumber", mobileNumber);
        intent.putExtra("email", email);
        intent.putExtra("password", password);

        // Optional: send image URI as string if needed
        if (selectedImageUri != null) {
            intent.putExtra("photoUri", selectedImageUri.toString());
        }

        startActivity(intent);
    }
}
