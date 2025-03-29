package com.example.notification.auth.Student.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.HomePageActivity;
import com.example.notification.R;
import com.example.notification.network.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class StudentLoginActivity extends AppCompatActivity {
    private EditText edtSchoolName, edtRegistration, edtMobile;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtSchoolName = findViewById(R.id.SchoolName);
        edtRegistration = findViewById(R.id.RegistrationNumber);
        edtMobile = findViewById(R.id.MobileNumber);
        btnLogin = findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String schoolName = edtSchoolName.getText().toString().trim();
        String registrationNumber = edtRegistration.getText().toString().trim();
        String mobileNumber = edtMobile.getText().toString().trim();

        if (schoolName.isEmpty() || registrationNumber.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest request = new UserRequest(schoolName, registrationNumber, mobileNumber);

        RetrofitClient.getInstance().getApiService().loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String schoolName = response.body().getSchoolName();
                    Log.d("LOGIN_DEBUG", "Received School Name: " + schoolName); // ✅ Debugging

                    // Ensure the schoolName is not null before passing it
                    if (schoolName == null || schoolName.isEmpty()) {
                        Log.e("LOGIN_ERROR", "School name is null or empty after login!");
                        Toast.makeText(StudentLoginActivity.this, "Error: School name missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(StudentLoginActivity.this, HomePageActivity.class);
                    intent.putExtra("schoolName", schoolName);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LOGIN_ERROR", "Login failed, response: " + response.errorBody());
                    Toast.makeText(StudentLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", "Network request failed", t);
                Toast.makeText(StudentLoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
