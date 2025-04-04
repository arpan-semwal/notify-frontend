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
    private String schoolName, course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tvMessages = findViewById(R.id.tv_messages);
        db = AppDatabase.getInstance(this);
        apiService = RetrofitClient.getInstance().getApiService();

        schoolName = getIntent().getStringExtra("schoolName");
        course = getIntent().getStringExtra("course");

        if (schoolName == null || course == null) {
            Toast.makeText(this, "Missing school or course information.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtil.isInternetAvailable(this)) {
            fetchMessagesFromServer();
        } else {
            Toast.makeText(this, "Offline mode: showing saved messages.", Toast.LENGTH_SHORT).show();
            fetchMessagesFromLocalDb();
        }
    }

    private void fetchMessagesFromServer() {
        apiService.getMessages(schoolName, course).enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        List<LocalMessage> existingMessages = db.localMessageDao().getMessages(schoolName, course);
                        Set<String> existingSet = new HashSet<>();
                        for (LocalMessage m : existingMessages) {
                            existingSet.add(m.getMessage());
                        }

                        for (MessageResponse messageResponse : response.body()) {
                            if (!existingSet.contains(messageResponse.getContent())) {
                                LocalMessage localMessage = new LocalMessage(
                                        messageResponse.getSchoolName(),
                                        messageResponse.getCourse(),
                                        messageResponse.getContent()
                                );
                                db.localMessageDao().insertMessage(localMessage);
                            }
                        }

                        runOnUiThread(StudentDashboardActivity.this::fetchMessagesFromLocalDb);
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
        new Thread(() -> {
            List<LocalMessage> messages = db.localMessageDao().getMessages(schoolName, course);
            StringBuilder messageContent = new StringBuilder();

            for (LocalMessage message : messages) {
                messageContent.append("• ").append(message.getMessage()).append("\n\n");
            }

            runOnUiThread(() -> {
                if (messages.isEmpty()) {
                    tvMessages.setText("No messages found.");
                } else {
                    tvMessages.setText(messageContent.toString());
                }
            });
        }).start();
    }
}
