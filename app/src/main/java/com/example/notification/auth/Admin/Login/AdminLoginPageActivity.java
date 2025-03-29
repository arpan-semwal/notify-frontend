package com.example.notification.auth.Admin.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.auth.Admin.Register.AdminRegisterPageActivity;

public class AdminLoginPageActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize UI elements
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        // Set the login button click action
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for login (validation or API call can be added here)
                // For now, we just show a success message or a login process.
            }
        });

        // Set the Register link click action
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AdminRegisterPageActivity
                Intent intent = new Intent(AdminLoginPageActivity.this, AdminRegisterPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
