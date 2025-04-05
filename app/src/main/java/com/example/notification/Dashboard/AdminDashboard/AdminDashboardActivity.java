package com.example.notification.Dashboard.AdminDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.network.RetrofitClient;
import com.example.notification.models.MessageRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private Spinner spinnerSelection;
    private EditText etMessage;
    private Button btnSendMessage, btnLogout;
    private TextView tvSchoolName;

    private String selectedCourse;
    private List<String> selectedCourses = new ArrayList<>();
    private String schoolName;
    private String schoolUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        spinnerSelection = findViewById(R.id.spinner_selection);
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnLogout = findViewById(R.id.btn_logout);
        tvSchoolName = findViewById(R.id.tv_school_name);

        loadAdminData();
        setupSpinner();

        btnSendMessage.setOnClickListener(v -> sendMessage());
        btnLogout.setOnClickListener(v -> logoutAdmin());
    }

    private void loadAdminData() {
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        schoolName = prefs.getString("schoolName", "Unknown School");
        schoolUniqueId = prefs.getString("schoolUniqueId", "Unknown ID");

        tvSchoolName.setText("School: " + schoolName + "\nUnique ID: " + schoolUniqueId);

        String selectedCoursesJson = prefs.getString("selectedCourses", "[]");
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        selectedCourses = new Gson().fromJson(selectedCoursesJson, listType);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedCourses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelection.setAdapter(adapter);

        spinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = selectedCourses.get(position);
                Toast.makeText(AdminDashboardActivity.this, "Selected Course: " + selectedCourse, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), "Selected Course: " + selectedCourse, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCourse = null;
            }
        });
    }

    private void sendMessage() {
        String messageContent = etMessage.getText().toString().trim();

        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Enter a message!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCourse == null || selectedCourse.equals("No Courses Found")) {
            Toast.makeText(this, "Select a valid course!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Use schoolUniqueId instead of schoolName
        MessageRequest request = new MessageRequest(schoolUniqueId, selectedCourse, messageContent);

        RetrofitClient.getInstance().getApiService().sendMessage(request)
                .enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String message = response.body().get("message");
                            Toast.makeText(AdminDashboardActivity.this, "✅ " + message, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("API_ERROR", "Response Code: " + response.code());
                            Toast.makeText(AdminDashboardActivity.this, "❌ Failed to send message!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(AdminDashboardActivity.this, "❌ Network error! But check if message is saved.", Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(android.R.id.content), "Network Issue! Try Again Later.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void logoutAdmin() {
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, AdminLoginPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
