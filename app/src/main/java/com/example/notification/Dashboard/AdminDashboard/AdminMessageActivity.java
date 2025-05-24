package com.example.notification.Dashboard.AdminDashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.auth.Admin.Login.AdminLoginPageActivity;
import com.example.notification.models.AdminCourse;
import com.example.notification.dto.Message.MessageRequest;
import com.example.notification.dto.Message.MessageResponse;
import com.example.notification.models.GlobalMessage;
import com.example.notification.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMessageActivity extends AppCompatActivity {

    private Spinner spinnerSelection;
    private EditText etMessage;
    private Button btnSendMessage, btnLogout;
    private TextView tvSchoolName;
    private ListView lvMessages;

    private List<AdminCourse> courseList = new ArrayList<>();
    private AdminCourse selectedCourse;

    private String schoolName;
    private String schoolUniqueId;

    private ArrayAdapter<String> messagesAdapter;
    private final List<String> messageStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);

        // Initialize views
        spinnerSelection = findViewById(R.id.spinner_selection);
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnLogout = findViewById(R.id.btn_logout);
        tvSchoolName = findViewById(R.id.tv_school_name);
        lvMessages = findViewById(R.id.lv_messages);

        // Setup ListView adapter
        messagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageStrings);
        lvMessages.setAdapter(messagesAdapter);

        // Load admin info and courses
        loadAdminData();

        btnSendMessage.setOnClickListener(v -> sendMessage());
        btnLogout.setOnClickListener(v -> logoutAdmin());

        // Spinner item selection to load messages for selected course
        spinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedCourse = courseList.get(position);
                Log.d("AdminMessageActivity", "Selected Course Unique ID: " + selectedCourse.getCourseUniqueId());
                fetchMessagesForCourse(selectedCourse.getCourseUniqueId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCourse = null;
                messageStrings.clear();
                messagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedCourse != null) {
            fetchMessagesForCourse(selectedCourse.getCourseUniqueId());
        }
    }

    private void loadAdminData() {
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        schoolName = prefs.getString("schoolName", "Unknown School");
        schoolUniqueId = prefs.getString("schoolUniqueId", "Unknown ID");

        tvSchoolName.setText("School: " + schoolName + "\nUnique ID: " + schoolUniqueId);

        // Fetch courses for the school
        RetrofitClient.getInstance().getApiService().getCoursesBySchool(schoolUniqueId)
                .enqueue(new Callback<List<AdminCourse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AdminCourse>> call, @NonNull Response<List<AdminCourse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            courseList = response.body();
                            setupSpinner();
                        } else {
                            Toast.makeText(AdminMessageActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AdminCourse>> call, @NonNull Throwable t) {
                        Toast.makeText(AdminMessageActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSpinner() {
        ArrayAdapter<AdminCourse> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelection.setAdapter(adapter);

        if (!courseList.isEmpty()) {
            selectedCourse = courseList.get(0);
            spinnerSelection.setSelection(0);
            fetchMessagesForCourse(selectedCourse.getCourseUniqueId());
        }
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();

        if (selectedCourse == null || messageText.isEmpty()) {
            Toast.makeText(this, "Select a course and enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageRequest request = new MessageRequest(
                schoolUniqueId,
                selectedCourse.getCourseUniqueId(),
                messageText
        );

        RetrofitClient.getInstance().getApiService().sendMessage(request)
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminMessageActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            etMessage.setText("");
                            // Refresh messages immediately after sending
                            fetchMessagesForCourse(selectedCourse.getCourseUniqueId());
                        } else {
                            Toast.makeText(AdminMessageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                        Toast.makeText(AdminMessageActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchMessagesForCourse(String courseUniqueId) {
        RetrofitClient.getInstance().getApiService().fetchGlobalMessages(schoolUniqueId, courseUniqueId)
                .enqueue(new Callback<List<GlobalMessage>>() {
                    @Override
                    public void onResponse(Call<List<GlobalMessage>> call, Response<List<GlobalMessage>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<GlobalMessage> messages = response.body();

                            messageStrings.clear();
                            for (GlobalMessage msg : messages) {
                                messageStrings.add("Message: " + msg.getContent());
                            }
                            messagesAdapter.notifyDataSetChanged();

                            lvMessages.post(() -> lvMessages.setSelection(messagesAdapter.getCount() - 1));
                        } else {
                            Toast.makeText(AdminMessageActivity.this, "Failed to load global messages", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GlobalMessage>> call, Throwable t) {
                        Toast.makeText(AdminMessageActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logoutAdmin() {
        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("schoolUniqueId");
        editor.apply();

        startActivity(new Intent(this, AdminLoginPageActivity.class));
        finish();
    }
}
