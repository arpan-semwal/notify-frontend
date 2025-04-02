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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private Spinner spinnerSelection;
    private EditText etMessage;
    private Button btnSendMessage, btnLogout;
    private String selectedCourse;
    private List<String> selectedCourses = new ArrayList<>();  // ✅ Moved to class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        spinnerSelection = findViewById(R.id.spinner_selection);
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnLogout = findViewById(R.id.btn_logout);

        // ✅ Load selected courses from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        String selectedCoursesJson = prefs.getString("selectedCourses", "[]");

        // ✅ Correct way to parse JSON to List<String>
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        selectedCourses = new Gson().fromJson(selectedCoursesJson, listType);

        // ✅ Ensure there's at least one item to show
        if (selectedCourses == null || selectedCourses.isEmpty()) {
            selectedCourses = new ArrayList<>();
            selectedCourses.add("No Courses Found");
        }

        // ✅ Set up adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedCourses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelection.setAdapter(adapter);

        // ✅ Spinner Selection (No more error because `selectedCourses` is a class variable)
        spinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = selectedCourses.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ✅ Button Click Listeners
        btnSendMessage.setOnClickListener(v -> sendMessage());
        btnLogout.setOnClickListener(v -> logoutAdmin());
    }

    private void sendMessage() {
        Toast.makeText(this, "Message sent to " + selectedCourse, Toast.LENGTH_SHORT).show();
    }

    private void logoutAdmin() {
        // ✅ Clear stored login data
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Remove all stored preferences
        editor.apply();  // Apply changes

        // ✅ Redirect to login page
        Intent intent = new Intent(this, AdminLoginPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Prevent back navigation
        startActivity(intent);
        finish();  // ✅ Close this activity
    }
}
