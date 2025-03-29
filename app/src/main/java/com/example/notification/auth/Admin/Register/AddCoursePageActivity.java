package com.example.notification.auth.Admin.Register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminDashboardActivity;
import com.example.notification.R;


import java.util.ArrayList;
import java.util.List;

public class AddCoursePageActivity extends AppCompatActivity {

    private AutoCompleteTextView etCourseName;
    private TextView tvSelectedCourses;
    private Button btnAddCourse, btnNext, btnSkip;
    private List<String> allCourses = new ArrayList<>();
    private List<String> selectedCourses = new ArrayList<>();
    private ArrayAdapter<String> coursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        etCourseName = findViewById(R.id.et_course_name);
        tvSelectedCourses = findViewById(R.id.tv_selected_courses);
        btnAddCourse = findViewById(R.id.btn_add_course);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        // Static courses that can be added
        allCourses.add("B.Tech in Computer Science");
        allCourses.add("B.Tech in Civil Engineering");
        allCourses.add("B.Tech in Mechanical Engineering");
        allCourses.add("BBA in Business Administration");
        allCourses.add("BBA in Marketing");
        allCourses.add("B.Com in Accounting and Finance");

        // Set up AutoCompleteTextView adapter
        coursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allCourses);
        etCourseName.setAdapter(coursesAdapter);
        etCourseName.setThreshold(1);
        etCourseName.setDropDownHeight(500);

        etCourseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btnAddCourse.setOnClickListener(v -> {
            String courseName = etCourseName.getText().toString().trim();
            if (!courseName.isEmpty() && !selectedCourses.contains(courseName)) {
                selectedCourses.add(courseName);
                updateSelectedCoursesDisplay();
                etCourseName.setText("");
            } else {
                Toast.makeText(this, "Course already selected or empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // Next button click - Moves to AdminDashboardActivity
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(AddCoursePageActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish(); // Finish this activity so the user can't go back
        });

        // Skip button click - Moves to AdminDashboardActivity directly
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(AddCoursePageActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateSelectedCoursesDisplay() {
        if (selectedCourses.isEmpty()) {
            tvSelectedCourses.setText("Selected Courses: ");
        } else {
            tvSelectedCourses.setText("Selected: " + String.join(", ", selectedCourses));
        }
    }
}
