package com.example.notification.Dashboard.AdminDashboard;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.notification.R;

public class AdminDashboardHome extends AppCompatActivity{
    CardView cardMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard_home);

        cardMessages = findViewById(R.id.cardMessages);

        cardMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardHome.this, AdminMessageActivity.class);
                startActivity(intent);
            }
        });
    }
}
