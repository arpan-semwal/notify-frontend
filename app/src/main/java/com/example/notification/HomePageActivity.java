package com.example.notification;  // ✅ Ensure this matches your project structure

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.models.Message;
import com.example.notification.network.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private TextView messageTextView, schoolNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        messageTextView = findViewById(R.id.messageTextView);
        schoolNameTextView = findViewById(R.id.schoolNameTextView);

        String schoolName = getIntent().getStringExtra("schoolName");
        schoolNameTextView.setText("🏫 School: " + schoolName);
    }
}
