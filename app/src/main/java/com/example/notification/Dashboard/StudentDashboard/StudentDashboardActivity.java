package com.example.notification.Dashboard.StudentDashboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.Database.AppDatabase;
import com.example.notification.models.LocalMessage;
import com.example.notification.models.MessageResponse;
import com.example.notification.network.ApiService;
import com.example.notification.network.RetrofitClient;
import com.example.notification.utils.NetworkUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvMessages;
    private ApiService apiService;
    private AppDatabase db;
    private String schoolUniqueId, course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_student_dashboard);

            tvMessages = findViewById(R.id.tv_messages);
            db = AppDatabase.getInstance(this);
            apiService = RetrofitClient.getInstance().getApiService();

            // Get schoolUniqueId and course from the intent
            schoolUniqueId = getIntent().getStringExtra("schoolUniqueId");
            course = getIntent().getStringExtra("course");

            if (schoolUniqueId == null || course == null) {
                Toast.makeText(this, "Missing school or course information.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            Log.d("StudentDashboard", "schoolUniqueId: " + schoolUniqueId + ", course: " + course);

            // Check internet availability and fetch messages accordingly
            if (NetworkUtil.isInternetAvailable(this)) {
                fetchMessagesFromServer();
            } else {
                Toast.makeText(this, "Offline mode: showing saved messages.", Toast.LENGTH_SHORT).show();
                fetchMessagesFromLocalDb();
            }
        } catch (Exception e) {
            Log.e("StudentDashboard", "Error in onCreate", e);
            finish();  // Finish the activity if there's an error
        }
    }

    private void fetchMessagesFromServer() {
        Log.d("StudentDashboard", "Fetching messages from server...");

        apiService.getMessages(schoolUniqueId, course).enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        List<LocalMessage> existingMessages = db.localMessageDao().getMessages(schoolUniqueId, course);
                        Set<String> existingSet = new HashSet<>();
                        for (LocalMessage m : existingMessages) {
                            existingSet.add(m.getMessage());
                        }

                        // Insert new messages that are not already saved locally
                        for (MessageResponse messageResponse : response.body()) {
                            if (!existingSet.contains(messageResponse.getContent())) {
                                LocalMessage localMessage = new LocalMessage(
                                        messageResponse.getSchoolUniqueId(),
                                        messageResponse.getCourse(),
                                        messageResponse.getContent()
                                );
                                db.localMessageDao().insertMessage(localMessage);
                            }
                        }

                        runOnUiThread(() -> fetchMessagesFromLocalDb());
                    }).start();
                } else {
                    Log.e("StudentDashboard", "Server response unsuccessful");
                    runOnUiThread(() -> tvMessages.setText("Failed to load messages from server."));
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.e("StudentDashboard", "API call failed: " + t.getMessage(), t);
                runOnUiThread(() -> tvMessages.setText("Failed to fetch messages."));
            }
        });
    }

    private void fetchMessagesFromLocalDb() {
        Log.d("StudentDashboard", "Fetching messages from local DB...");

        new Thread(() -> {
            List<LocalMessage> messages = db.localMessageDao().getMessages(schoolUniqueId, course);
            StringBuilder messageContent = new StringBuilder();

            // Prepare the message content for display
            for (LocalMessage message : messages) {
                messageContent.append("• ").append(message.getMessage()).append("\n\n");
            }

            runOnUiThread(() -> {
                // Display messages or show a "No messages" message
                if (messages.isEmpty()) {
                    Log.d("StudentDashboard", "No messages found.");
                    tvMessages.setText("No messages found.");
                } else {
                    Log.d("StudentDashboard", "Messages found: " + messages.size());
                    tvMessages.setText(messageContent.toString());
                }
            });
        }).start();
    }
}
