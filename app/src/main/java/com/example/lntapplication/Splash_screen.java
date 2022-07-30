package com.example.lntapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Splash_screen extends AppCompatActivity {
    ImageView imageView;
    TextView t1,t2;
    boolean isExpired =false;
    String devicemacaddress;
    NetworkInfo wifiCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.imageView2);
        t1=findViewById(R.id.textView35);
        t2=findViewById(R.id.textView37);

        //Check Connection To Wifi
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (wifiCheck.isConnected()) {
//            // Do whatever here
//            Toast.makeText(Splash_screen.this, "Connect To Wifi", Toast.LENGTH_SHORT).show();
//        } else {
//
//            Toast.makeText(Splash_screen.this, "WiFi is not Connected", Toast.LENGTH_SHORT).show();
//        }
        GregorianCalendar expDate = new GregorianCalendar(2022, 11, 30); // midnight
        GregorianCalendar now = new GregorianCalendar();//Note: Months are 0-based. January = 0, December = 11.

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDateandTime = sdf.format(new Date());
        devicemacaddress=getMacAddr();
        System.out.print("MAC "+devicemacaddress);

        isExpired = now.after(expDate);
        if (isExpired) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dailogbox = LayoutInflater.from(this).inflate(R.layout.expirealertdialog, null);
            builder.setView(dailogbox);
            builder.setCancelable(false);
            builder.show();

        } else {
            imageView.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            // Using handler with postDelayed called runnable run method
            new Handler().postDelayed(() -> {

            Intent i = new Intent(Splash_screen.this, MainActivity.class);
                startActivity(i);
                finish();

            }, 3 * 1000);
        }


    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

}