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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.interfaces.ConnectionStatus;
import com.rscja.deviceapi.interfaces.ConnectionStatusCallback;

import java.util.ArrayList;

public class BleSetUPForm extends AppCompatActivity {
    TextView bleStatus;
    Button SearchScanButton;
    ListView listView;
    TextView back;
    public static boolean BTStatus1 = false;
    RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    BTStatus btStatus = new BTStatus();
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
        ArrayAdapter arrayAdapter;
//    CustomAdapter arrayAdapter;
    BluetoothAdapter bluetoothAdapter;
    ProgressDialog dialog;
    ArrayList<BleDeviceDetails> listdatable = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_set_upform);
        bleStatus = findViewById(R.id.statusTextView);
        SearchScanButton = findViewById(R.id.button_Search);
        back = findViewById(R.id.textView28);
        listView = findViewById(R.id.listView);
        uhf.init(this);
        dialog = new ProgressDialog(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.cancelDiscovery();
                        Intent i = new Intent(BleSetUPForm.this, MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
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
                dialog.setCancelable(false);
                dialog.setMessage("Searching Devices....");
                dialog.show();

            }
        });

//        arrayAdapter = new CustomAdapter(listdatable, this);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String addressDEvice = adapterView.getAdapter().getItem(i).toString();
            Toast.makeText(BleSetUPForm.this, "" + addressDEvice, Toast.LENGTH_SHORT).show();
//          /
//            connect(namedecice);
            connect(addressDEvice);
            dialog.setCancelable(false);
            dialog.setMessage("Connecting Device...");
            dialog.show();
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
                dialog.dismiss();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                Log.i("Device Found", "Nmae :" + name + " Address :" + address + " RSSI :" + rssi);

                if (!addresses.contains(address)) {
                    addresses.add(address);
                    names.add(name);
                    listdatable.add(new BleDeviceDetails(address, name));
                    String deviceString = "";
//
//                    }
                    bluetoothDevices.add(address);
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        }
    };

    public void searchFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bluetoothDevices.clear();
                addresses.clear();
                bluetoothDevices.add("ADDING NEW DEVICES...");
                bleStatus.setText("Searching...");
                SearchScanButton.setEnabled(false);
                arrayAdapter.notifyDataSetChanged();


            }
        });

        bluetoothAdapter.startDiscovery();
    }

    public void connect(String deviceAddress) {
        if (uhf.getConnectStatus() == ConnectionStatus.CONNECTING) {
            Toast.makeText(BleSetUPForm.this, "Connecting...", Toast.LENGTH_SHORT).show();
        } else {
            try {
                uhf.connect(deviceAddress, btStatus);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(BleSetUPForm.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ActivityCompat.requestPermissions(BleSetUPForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    @Override
    protected void onDestroy() {
        uhf.free();
        super.onDestroy();
        android.os.Process.killProcess(Process.myPid());
        unregisterReceiver(broadcastReceiver);
    }

    class BTStatus implements ConnectionStatusCallback<Object> {
        @Override
        public void getStatus(final ConnectionStatus connectionStatus, final Object device1) {
            runOnUiThread(new Runnable() {
                public void run() {
                    BluetoothDevice device = (BluetoothDevice) device1;
                    if (connectionStatus == ConnectionStatus.CONNECTED) {
//                        Toast.makeText(BleSetUPForm.this, "Connected Device ...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BleSetUPForm.this, MainActivity.class));
                        dialog.dismiss();
                        BTStatus1 = true;

                    } else if (connectionStatus == ConnectionStatus.DISCONNECTED) {
                        Toast.makeText(BleSetUPForm.this, "Device Disconnect...", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    } else {
                        Toast.makeText(BleSetUPForm.this, "Error Disconnect...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(BleSetUPForm.this, MainActivity.class);

                startActivity(i);
            }
        });
    }
}