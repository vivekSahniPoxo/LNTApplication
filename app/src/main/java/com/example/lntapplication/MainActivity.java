package com.example.lntapplication;

import static com.example.lntapplication.BleConnectFrom.BTStatus;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout mappingForm, Searchform, IdentifyForm;
    ReportDb reportDb;
    List<ReportDatabase> listDB;
    ImageButton SyncBtn;
    boolean StatusTable = false;
    ProgressDialog dialog;
    NetworkInfo wifiCheck;
    ImageView StatusBTImg;
    TextView StatusBTtxt;
    BluetoothAdapter bluetoothAdapter;
    ImageView setting;
    String DNSAddress="null";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mappingForm = findViewById(R.id.Mapping);
        IdentifyForm = findViewById(R.id.Identifyform);
        Searchform = findViewById(R.id.SearchForml);
        reportDb = new ReportDb(this);
        StatusBTImg = findViewById(R.id.BTStatusImage);
        StatusBTtxt = findViewById(R.id.BtStatusTextView);
        listDB = new ArrayList<>();
        SyncBtn = findViewById(R.id.buttonSync);
        dialog = new ProgressDialog(this);
        setting = findViewById(R.id.imageView4);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothState();




        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);


        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        SyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);

                    /* you can print your active network via using below */
//                    Log.i("myNetworkType: ", connectivityManager.getActiveNetworkInfo().getTypeName());
//                    WifiManager wifiManager= (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
//
//
////
////
//                    Log.i("ip address ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getLinkAddresses().toString());
//                    Log.i("dns address ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getDnsServers().toString());
                  try{
                      DNSAddress=connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getDnsServers().toString();
                      System.out.print("My DNS Address"+DNSAddress);
//                      Toast.makeText(MainActivity.this, ""+DNSAddress, Toast.LENGTH_SHORT).show();
                  }catch (Exception e)
                  {
                      e.printStackTrace();
                      Toast.makeText(MainActivity.this, "Unable to find Network", Toast.LENGTH_SHORT).show();
                  }


                    StatusTable = reportDb.GetDataInfo();
                    System.out.print("System " + StatusTable);
                    if (DNSAddress.equals("[/192.168.1.1]")){
                    // Do whatever here
                    if (StatusTable) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            dialog.setCancelable(false);
                            dialog.setMessage("Uploading Data...");
                            dialog.show();
//                                String DateNow=now.toString();
                            listDB = reportDb.getAllContacts();
                            submit_Report(now);
                        }
//                            listDB = reportDb.getAllContacts();
//                            submit_Report(DateNow);
//                        Toast.makeText(MainActivity.this, "Start Updating....", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        FetchData();
                        dialog.setCancelable(false);
                        dialog.setMessage("Fetching Latest Data...");
                        dialog.show();

//                        Toast.makeText(MainActivity.this, "No Data  Getting....", Toast.LENGTH_SHORT).show();
                    }

                    }
                    else {
//                        ShowDailogBox();
                        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }


//                    FetchData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Searchform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BTStatus){
                startActivity(new Intent(MainActivity.this, SearchForm.class));}else {
                    Toast.makeText(MainActivity.this, "Device is not Connected With RFid Reader...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        IdentifyForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BTStatus) {
                    startActivity(new Intent(MainActivity.this, IdentifyForm.class));
                }else {
                    Toast.makeText(MainActivity.this, "Device is not Connected With RFid Reader...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mappingForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BTStatus) {
                    startActivity(new Intent(MainActivity.this, MappingForm.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "Device is not Connected With RFid Reader...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void ShowDailogBox() {
        new AlertDialog.Builder(getApplicationContext())
                .setIcon(R.drawable.ic_baseline_network_check_24)
                .setTitle("Quit")
                .setMessage("Are you sure you want to Quit this App ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void onBackPressed() {
        //Dialog Box for Exit User
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.exitapp)
                .setTitle("Quit")
                .setMessage("Are you sure you want to Quit this App ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    private void FetchData() throws JSONException {
//
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, ApiUrl.GetAllData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();

                    JSONArray array = new JSONArray(response);
                    if (array.length() == 0) {
//                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "No Data Available ", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Updating New Data...", Toast.LENGTH_SHORT).show();
                        if (StatusTable) {
                            reportDb.deleteAll();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String productId = object.optString("productId");
                            String serialNo = object.optString("serialNo");
                            String drawingNo = object.optString("drawingNo");
                            String sapNo = object.optString("sapNo");
                            String spoolNo = object.optString("spoolNo");
                            String weight = object.optString("weight");
                            String contractor = object.optString("contractor");
                            String location = object.optString("location");
                            String rfidNo = object.optString("rfidNo");
                            String remarks = object.optString("remarks");
                            String createdAt = object.optString("createdAt");
                            String updatedAt = object.optString("updatedAt");


                            reportDb.addContact(new ReportDatabase(productId, serialNo, drawingNo, sapNo, spoolNo, weight, contractor, location, rfidNo, remarks, createdAt, updatedAt, "False"));
                        }
                        Log.e("response", response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, error -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
        });


        queue.add(jsObjRequest);

    }


    private void submit_Report(LocalDateTime dateNow) throws JSONException {

        JSONObject object = new JSONObject();

        JSONArray array = new JSONArray();

        for (int i = 0; i < listDB.size(); i++) {
            JSONObject jsonObject = new JSONObject();


            jsonObject.put("productId", Integer.parseInt(listDB.get(i).getProductId()));
            jsonObject.put("serialNo", listDB.get(i).getSerialNo());
            jsonObject.put("drawingNo", listDB.get(i).getDrawingNo());
            jsonObject.put("sapNo", listDB.get(i).getSapNo());
            jsonObject.put("spoolNo", listDB.get(i).getSpoolNo());
            jsonObject.put("weight", new BigDecimal(listDB.get(i).getWeight()));
            jsonObject.put("contractor", listDB.get(i).getContractor());
            jsonObject.put("location", listDB.get(i).getLocation());
            jsonObject.put("rfidNo", listDB.get(i).getRfidNo());
            jsonObject.put("remarks", listDB.get(i).getRemarks());
//            jsonObject.put("createdAt",listDB.get(i).getCreatedAt());
            jsonObject.put("updatedAt", listDB.get(i).getUpdatedAt());


            array.put(jsonObject);
        }


//        object.put("Product", array);
        if (array.length() > 0) {
            RequestQueue queue = Volley.newRequestQueue(this);
            System.out.println("JSON DATA " + object);

            final String requestBody = array.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GetUpdateData, response -> {
//                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object1 = new JSONObject(response);
                    dialog.dismiss();
                    String status = object1.getString("status");
                    String message = object1.getString("message");
                    Log.i("Update Data Message", message);
                    if (status.matches("true")) {

                        FetchData();
                        dialog.setCancelable(false);
                        dialog.setMessage("Getting Latest Data...");
                        dialog.show();
                    } else {
                        Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }


                Log.i("VOLLEY Submit", response);
//            dialog.dismiss();
            }, error -> {
                try {
                    dialog.dismiss();

                    Log.e("VOLLEY Negative", String.valueOf(error.networkResponse.statusCode));
                    Log.e("VOLLEY Negative", String.valueOf(error.getMessage()));
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(MainActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable to process the request", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.e("VOLLEY Negative", String.valueOf(error.getMessage()));
                    dialog.dismiss();
//                    Toast.makeText(this, "Update...", Toast.LENGTH_SHORT).show();

                }
//            dialog.dismiss();
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    System.out.println("Response Code " + response.statusCode);
                    return super.parseNetworkResponse(response);
                }
            };

            queue.add(stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
        } else {
            Toast.makeText(MainActivity.this, "Not Data To Update....", Toast.LENGTH_SHORT).show();
        }
//        String url = "http://164.52.223.163:4510/api/WriteRfidTagDetails";

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        BluetoothDevice device;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                StatusBTtxt.setText("Connected");
                StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);
                Toast.makeText(getApplicationContext(), "Device is now Connected", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {

                Toast.makeText(getApplicationContext(), "Device is disconnected", Toast.LENGTH_SHORT).show();
                StatusBTtxt.setText("Disconnected");
                StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_searching_24);
            }

        }
    };

    public void BluetoothState()
    {
        if (!bluetoothAdapter.isEnabled()) {
            StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_24);
            StatusBTtxt.setText("Bluetooth OFF");

            StatusBTImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                }
            });


        }else { if (bluetoothAdapter.isEnabled())
        {
            if (BTStatus)
            {
                StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);
                StatusBTtxt.setText("Bluetooth Connected");
            }else {
                StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_24orange);
                StatusBTtxt.setText("Bluetooth ON");
            }
            StatusBTImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,BleConnectFrom.class));
                }
            });


        }

        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if(requestCode==0){
            StatusBTImg.setImageResource(R.drawable.ic_baseline_bluetooth_24orange);
            StatusBTtxt.setText("Bluetooth ON");

            StatusBTImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,BleConnectFrom.class));
                }
            });
        }
    }
}