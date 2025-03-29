package com.example.notification.network;

public class AdminRegister {
    private String schoolName;
    private String city;
    private String address;
    private String mobileNumber;
    private String email;
    private String password;

    public AdminRegister(String schoolName, String city, String address, String mobileNumber, String email, String password) {
        this.schoolName = schoolName;
        this.city = city;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getSchoolName() { return schoolName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public String getMobileNumber() { return mobileNumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
