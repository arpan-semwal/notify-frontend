package com.example.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.auth.Admin.Register.AdminRegisterPageActivity;
import com.example.notification.auth.Student.Login.StudentLoginActivity;


public class AskPageActivity extends AppCompatActivity {

    private TextView tvStudentQuestion, tvSchoolQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_page);

        // Initialize TextViews
        tvSchoolQuestion = findViewById(R.id.tv_school_question);
        tvStudentQuestion = findViewById(R.id.tv_student_question);

        // Click Listener for School (Navigates to AdminRegisterPageActivity)
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
