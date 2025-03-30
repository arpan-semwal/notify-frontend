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

public class AdminLoginPageActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Check if already logged in
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Redirect to Dashboard automatically
            startActivity(new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class));
            finish();
        }

        // Initialize UI elements
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        // Set login button click action
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    // ✅ Save login status
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Redirect to Dashboard
                    Intent intent = new Intent(AdminLoginPageActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    finish();  // Prevent going back to login screen
                } else {
                    Toast.makeText(AdminLoginPageActivity.this, "Enter username & password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set Register link click action
        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginPageActivity.this, AdminRegisterPageActivity.class);
            startActivity(intent);
        });
    }
}
