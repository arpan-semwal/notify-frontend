package com.example.notification.auth.Student.Register;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;

public class StudentRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        // Set text dynamically (optional)
        TextView textView = findViewById(R.id.tv_student_register_message);
        textView.setText("This is the Student Register Page");
    }
}
