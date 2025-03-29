package com.example.notification;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    TextView ipAddressTv;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressTv = findViewById(R.id.ip_address);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final String IP_ADDRESS = intToInetAddress(wifiInfo.getIpAddress()).getHostAddress();
        ipAddressTv.setText(IP_ADDRESS);
        ipAddressTv.setVisibility(View.VISIBLE);
    }

    private InetAddress intToInetAddress(int ipAddress) {
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipAddress).array();
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
