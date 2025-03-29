package com.example.notification;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notification.network.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private TextView messageTextView, schoolNameTextView; // Added schoolNameTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        messageTextView = findViewById(R.id.messageTextView);
        schoolNameTextView = findViewById(R.id.schoolNameTextView); // Initialize the TextView

        String schoolName = getIntent().getStringExtra("schoolName");

        Log.d("HOME_DEBUG", "School Name in HomePageActivity: " + schoolName); // ✅ Debugging

        if (schoolName == null || schoolName.isEmpty()) {
            Log.e("HOME_ERROR", "School name is null or empty!");
            messageTextView.setText("Error: School name missing");
            return;
        }

        // Display the school name on the screen
        schoolNameTextView.setText("🏫 School: " + schoolName);

        fetchMessages(schoolName);
    }

    private void fetchMessages(String schoolName) {
        Log.d("FETCH_DEBUG", "Fetching messages for: " + schoolName);

        RetrofitClient.getInstance().getApiService().getMessages(schoolName).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                Log.d("API_CALL", "Request URL: " + call.request().url()); // ✅ Print API URL
                Log.d("API_RESPONSE", "HTTP Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Messages received: " + response.body().size());
                    if (response.body().isEmpty()) {
                        Log.w("API_WARNING", "No messages found!");
                        messageTextView.setText("No messages available");
                        return;
                    }
                    displayMessages(response.body());
                } else {
                    try {
                        Log.e("API_ERROR", "Response failed: " + response.code() + " Error: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Failed to read error body", e);
                    }
                    messageTextView.setText("Failed to load messages");
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch messages", t);
                messageTextView.setText("Failed to load messages");
            }
        });
    }

    private void displayMessages(List<Message> messages) {
        StringBuilder messageDisplay = new StringBuilder();
        for (Message msg : messages) {
            messageDisplay.append("📩 ").append(msg.getContent())
                    .append("\n🕒 ").append(msg.getTimestamp())
                    .append("\n\n");
        }
        messageTextView.setText(messageDisplay.toString());
    }
}
