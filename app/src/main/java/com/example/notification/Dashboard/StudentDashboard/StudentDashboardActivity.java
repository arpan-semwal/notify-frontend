package com.example.notification.Dashboard.StudentDashboard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.models.MessageResponse;
import com.example.notification.models.SyncResponse;
import com.example.notification.network.ApiService;
import com.example.notification.network.RetrofitClient;
import com.example.notification.utils.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvMessages;
    private ApiService apiService;
    private String schoolUniqueId, courseUniqueId;

    private final Handler handler = new Handler();
    private final int SYNC_INTERVAL = 3000; // 3 seconds

    private final Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            if (NetworkUtil.isInternetAvailable(StudentDashboardActivity.this)) {
                fetchMessagesFromServer(); // includes syncing and displaying
            }
            handler.postDelayed(this, SYNC_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tvMessages = findViewById(R.id.tv_messages);
        apiService = RetrofitClient.getInstance().getApiService();

        schoolUniqueId = getIntent().getStringExtra("schoolUniqueId");
        courseUniqueId = getIntent().getStringExtra("courseUniqueId");

        if (schoolUniqueId == null || courseUniqueId == null) {
            Toast.makeText(this, "Missing school or course information.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        handler.post(syncRunnable); // start syncing loop
    }

    private void fetchMessagesFromServer() {
        apiService.syncMessages(schoolUniqueId, courseUniqueId).enqueue(new Callback<SyncResponse>() {
            @Override
            public void onResponse(Call<SyncResponse> call, Response<SyncResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("StudentDashboard", "[SYNC SUCCESS] " + response.body().getMessage());

                    // Now fetch synced local messages
                    fetchLocalMessages();
                } else {
                    Log.e("StudentDashboard", "Sync failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SyncResponse> call, Throwable t) {
                Log.e("StudentDashboard", "[FAILURE] Sync failed: " + t.getMessage());
            }
        });
    }

    private void fetchLocalMessages() {
        apiService.getMessages(schoolUniqueId, courseUniqueId).enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder messageContent = new StringBuilder();
                    for (MessageResponse message : response.body()) {
                        messageContent.append("• ").append(message.getContent()).append("\n\n");
                    }

                    runOnUiThread(() -> {
                        if (response.body().isEmpty()) {
                            tvMessages.setText("No messages found.");
                        } else {
                            tvMessages.setText(messageContent.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.e("StudentDashboard", "Error fetching messages: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(syncRunnable); // stop loop
    }
}
