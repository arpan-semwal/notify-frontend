package com.example.notification.auth.Admin.Login;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.Dashboard.AdminDashboard.AdminDashboardActivity;
import com.example.notification.R;
import com.example.notification.auth.Admin.Register.AdminRegisterPageActivity;
import com.example.notification.models.AdminLoginRequest;
import com.example.notification.network.AdminLoginResponse;
import com.example.notification.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginPageActivity extends AppCompatActivity {

    private EditText etSchoolName, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class));
            finish();
        }

        etSchoolName = findViewById(R.id.et_school_name);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> {
            String schoolName = etSchoolName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!schoolName.isEmpty() && !password.isEmpty()) {
                // Prepare login request object
                AdminLoginRequest request = new AdminLoginRequest(schoolName, password);

                // Call the API to login using RetrofitClient's singleton instance
                Call<AdminLoginResponse> call = RetrofitClient.getInstance().getApiService().loginAdmin(request);

                call.enqueue(new Callback<AdminLoginResponse>() {
                    @Override
                    public void onResponse(Call<AdminLoginResponse> call, Response<AdminLoginResponse> response) {
                        if (response.isSuccessful()) {
                            AdminLoginResponse loginResponse = response.body();

                            if (loginResponse != null && loginResponse.isSuccess()) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("schoolUniqueId", loginResponse.getSchoolUniqueId());
                                editor.putString("schoolName", loginResponse.getSchoolName());
                                editor.putString("email", loginResponse.getEmail());
                                editor.apply();

                                Intent intent = new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AdminLoginPageActivity.this, "Login failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminLoginPageActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AdminLoginResponse> call, Throwable t) {
                        Toast.makeText(AdminLoginPageActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(AdminLoginPageActivity.this, "Enter school name & password!", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginPageActivity.this, AdminRegisterPageActivity.class);
            startActivity(intent);
        });
    }
}
