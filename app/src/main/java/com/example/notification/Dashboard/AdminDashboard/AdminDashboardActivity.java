package com.example.notification.Dashboard.AdminDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.R;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import java.util.Arrays;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private Spinner spinnerClass, spinnerCourse;
    private EditText etMessage;
    private Button btnSendMessage, btnLogout;

    private String selectedClass, selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        spinnerClass = findViewById(R.id.spinner_class);
        spinnerCourse = findViewById(R.id.spinner_course);
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnLogout = findViewById(R.id.btn_logout); // ✅ Logout button

        // Sample data for spinners
        List<String> classList = Arrays.asList("Class 1", "Class 2", "Class 3");
        List<String> courseList = Arrays.asList("Math", "Science", "English");

        // Setup adapters for spinners
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(courseAdapter);

        // Spinner item selection listeners
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = classList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = courseList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Button Click Listener for Sending Message
        btnSendMessage.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();

            if (selectedClass == null || selectedCourse == null || message.isEmpty()) {
                Toast.makeText(AdminDashboardActivity.this, "Please select class, course, and enter a message!", Toast.LENGTH_SHORT).show();
            } else {
                // Here, you can call your API to send the message
                Toast.makeText(AdminDashboardActivity.this, "Message sent to " + selectedClass + " - " + selectedCourse, Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Logout Button Click Listener
        btnLogout.setOnClickListener(v -> logoutAdmin());
    }

    private void logoutAdmin() {
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);  // ✅ Clear login session
        editor.apply();

        Toast.makeText(AdminDashboardActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to login screen
        startActivity(new Intent(AdminDashboardActivity.this, AdminLoginPageActivity.class));
        finish(); // ✅ Prevent going back to Dashboard after logout
    }
}
