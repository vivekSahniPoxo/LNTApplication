package com.example.lntapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;

import java.util.ArrayList;
import java.util.List;

public class BleConnectFrom extends AppCompatActivity implements bleListener {
    TextView bleStatus;
    Button SearchScanButton;
   public static  boolean BTStatus=false;
    RecyclerView recyclerView;
    RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    BluetoothAdapter bluetoothAdapter;
    ProgressDialog dialog;
    List<BleDeviceDetails> list = new ArrayList<>();
    BTStatus btStatus = new BTStatus();
    AdapterRecyclerview adapter_list;
    Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_connect_from);
        backbtn = findViewById(R.id.buttonback);
        bleStatus = findViewById(R.id.statusTextView1);
        SearchScanButton = findViewById(R.id.button_Search1);
        recyclerView = findViewById(R.id.Recycclerview);

        uhf.init(this);
        dialog = new ProgressDialog(this);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);

        SearchScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFunction();

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BleConnectFrom.this,MainActivity.class));
                finish();
            }
        });

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                bleStatus.setText("Finished");
                SearchScanButton.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name=null;
                        name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                Log.i("Device Found", "Nmae :" + name + " Address :" + address + " RSSI :" + rssi);
                if (name == null) {
                    list.add(new BleDeviceDetails(address, address));
                } else {
                    list.add(new BleDeviceDetails(address, name));
                }
                adapter_list = new AdapterRecyclerview(getApplicationContext(), list, BleConnectFrom.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter_list);
//                if (!addresses.contains(address)) {
//                    addresses.add(address);
//                    names.add(name);
//                    String deviceString = "";
////
////                    }
//                    bluetoothDevices.add(address);
//                    arrayAdapter.notifyDataSetChanged();
//                }


            }
        }
    };

    public void searchFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.clear();
//                addresses.clear();
//                list.add("ADDING NEW DEVICES...");
                bleStatus.setText("Searching...");
                SearchScanButton.setEnabled(false);
//                arrayAdapter.notifyDataSetChanged();

            }
        });

        bluetoothAdapter.startDiscovery();
    }

    public void connect(String deviceAddress) {
        if (uhf.getConnectStatus() == ConnectionStatus.CONNECTING) {
            Toast.makeText(BleConnectFrom.this, "Connecting...", Toast.LENGTH_SHORT).show();
        } else {
            try {
                uhf.connect(deviceAddress, btStatus);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(BleConnectFrom.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int premissionCehck = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            premissionCehck = checkSelfPermission("Manifest.premission.ACCES_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            premissionCehck += checkSelfPermission("Manifest.premission.ACCES_FINE_LOCATION");
        }
        if (premissionCehck != 0) {
            ActivityCompat.requestPermissions(BleConnectFrom.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    @Override
    protected void onDestroy() {
        uhf.free();
        super.onDestroy();
        android.os.Process.killProcess(Process.myPid());
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onItemClick(int position) {

//        Toast.makeText(BleConnectFrom.this, "" + list.get(position).getName(), Toast.LENGTH_SHORT).show();
        connect(list.get(position).getAddress());
        dialog.setCancelable(false);
        dialog.setMessage("Connecting Device...");
        dialog.show();
    }

    class BTStatus implements ConnectionStatusCallback<Object> {
        @Override
        public void getStatus(final ConnectionStatus connectionStatus, final Object device1) {
            runOnUiThread(new Runnable() {
                public void run() {
                    BluetoothDevice device = (BluetoothDevice) device1;
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
//                        Toast.makeText(BleSetUPForm.this, "Connected Device ...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BleConnectFrom.this, MainActivity.class));
                        BTStatus=true;
                        dialog.dismiss();

                    } else if (connectionStatus == ConnectionStatus.DISCONNECTED) {

                        Toast.makeText(BleConnectFrom.this, "Device Disconnect...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(BleConnectFrom.this, "Error Disconnect...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BleConnectFrom.this,MainActivity.class));
    }
}