package com.example.notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminMessageActivity;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.auth.Student.Login.StudentLoginActivity;

public class AskPageActivity extends AppCompatActivity {

    private View cardSchool, cardStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(AskPageActivity.this, AdminMessageActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.ask_page);

        // Use CardViews for click listeners
        cardSchool = findViewById(R.id.card_school);
        cardStudent = findViewById(R.id.card_student);

        cardSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskPageActivity.this, AdminLoginPageActivity.class);
                startActivity(intent);
            }
        });

        cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskPageActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
