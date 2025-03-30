package com.example.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminDashboardActivity;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.auth.Student.Login.StudentLoginActivity;

public class AskPageActivity extends AppCompatActivity {

    private TextView tvStudentQuestion, tvSchoolQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Check if user is already logged in
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // ✅ Skip AskPageActivity & go to Dashboard
            startActivity(new Intent(AskPageActivity.this, AdminDashboardActivity.class));
            finish(); // ✅ Close this activity
            return; // ✅ Stop further execution
        }

        setContentView(R.layout.ask_page);

        // Initialize TextViews
        tvSchoolQuestion = findViewById(R.id.tv_school_question);
        tvStudentQuestion = findViewById(R.id.tv_student_question);

        // Click Listener for School (Navigates to AdminLoginPageActivity)
        tvSchoolQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskPageActivity.this, AdminLoginPageActivity.class);
                startActivity(intent);
            }
        });

        // Click Listener for Student (Navigates to StudentSignInActivity)
        tvStudentQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskPageActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
