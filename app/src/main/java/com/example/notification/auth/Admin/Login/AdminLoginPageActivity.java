package com.example.notification.auth.Admin.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.Dashboard.AdminDashboard.AdminMessageActivity;
import com.example.notification.R;
import com.example.notification.auth.Admin.Register.AdminRegisterPageActivity;
import com.example.notification.dto.AdminLogin.AdminLoginRequest;
import com.example.notification.dto.AdminLogin.AdminLoginResponse;
import com.example.notification.dto.SchoolResponse;
import com.example.notification.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginPageActivity extends AppCompatActivity {

    private AutoCompleteTextView etSchoolName;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    private ArrayAdapter<String> schoolNameAdapter;
    private boolean userSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        SharedPreferences prefs = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(AdminLoginPageActivity.this, AdminMessageActivity.class));
            finish();
        }

        etSchoolName = findViewById(R.id.et_school_name);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        // Set text color explicitly to black to ensure visibility
        etSchoolName.setTextColor(getResources().getColor(android.R.color.black));

        schoolNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        etSchoolName.setAdapter(schoolNameAdapter);
        etSchoolName.setThreshold(1); // Show suggestions after 1 character

        etSchoolName.setOnItemClickListener((parent, view, position, id) -> {
            userSelected = true;
            etSchoolName.dismissDropDown();
            etSchoolName.clearFocus();

            String selectedSchool = schoolNameAdapter.getItem(position);
            etSchoolName.setText(selectedSchool);
        });

        etSchoolName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userSelected) {
                    userSelected = false; // reset flag
                    return; // skip fetch to avoid reopening dropdown after selection
                }
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    fetchSchoolSuggestions(query);
                }
            }
        });

        btnLogin.setOnClickListener(v -> {
            String schoolName = etSchoolName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!schoolName.isEmpty() && !password.isEmpty()) {
                AdminLoginRequest request = new AdminLoginRequest(schoolName, password);

                RetrofitClient.getInstance().getApiService().loginAdmin(request)
                        .enqueue(new Callback<AdminLoginResponse>() {
                            @Override
                            public void onResponse(Call<AdminLoginResponse> call, Response<AdminLoginResponse> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                    AdminLoginResponse loginResponse = response.body();

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("schoolUniqueId", loginResponse.getSchoolUniqueId());
                                    editor.putString("schoolName", loginResponse.getSchoolName());
                                    editor.putString("email", loginResponse.getEmail());
                                    editor.apply();

                                    startActivity(new Intent(AdminLoginPageActivity.this, AdminMessageActivity.class));
                                    finish();
                                } else {
                                    String message = (response.body() != null) ? response.body().getMessage() : "Login failed";
                                    Toast.makeText(AdminLoginPageActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AdminLoginResponse> call, Throwable t) {
                                Toast.makeText(AdminLoginPageActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(AdminLoginPageActivity.this, "Enter school name & password!", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(AdminLoginPageActivity.this, AdminRegisterPageActivity.class));
        });
    }

    private void fetchSchoolSuggestions(String query) {
        RetrofitClient.getInstance().getApiService().searchSchools(query)
                .enqueue(new Callback<List<SchoolResponse>>() {
                    @Override
                    public void onResponse(Call<List<SchoolResponse>> call, Response<List<SchoolResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<String> updatedList = new ArrayList<>();
                            for (SchoolResponse school : response.body()) {
                                updatedList.add(school.getSchoolName());
                            }
                            schoolNameAdapter.clear();
                            schoolNameAdapter.addAll(updatedList);
                            schoolNameAdapter.notifyDataSetChanged();
                            etSchoolName.showDropDown(); // Show dropdown with new suggestions
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SchoolResponse>> call, Throwable t) {
                        // Optional: handle failure
                    }
                });
    }
}
