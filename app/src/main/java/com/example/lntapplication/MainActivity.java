package com.example.lntapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.rscja.deviceapi.RFIDWithUHFBLE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RFIDWithUHFBLE uhfble = RFIDWithUHFBLE.getInstance();
    ConstraintLayout mappingForm, Searchform, IdentifyForm;
    ReportDb reportDb;
    List<ReportDatabase> listDB;
    Button SyncBtn;
boolean StatusTable=false;
    NetworkInfo wifiCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mappingForm = findViewById(R.id.Mapping);
        IdentifyForm = findViewById(R.id.Identifyform);
        Searchform = findViewById(R.id.SearchForml);
        reportDb = new ReportDb(this);
        listDB = new ArrayList<>();
        SyncBtn = findViewById(R.id.buttonSync);




        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);



        SyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StatusTable = reportDb.GetDataInfo();
                    System.out.print("System "+StatusTable);
                    if (wifiCheck.isConnected()) {
                        // Do whatever here
                        if (StatusTable) {

                            listDB = reportDb.getAllContacts();
                            submit_Report();
                            Toast.makeText(MainActivity.this, "Start Updating....", Toast.LENGTH_SHORT).show();
                        } else {
                            FetchData();
                            Toast.makeText(MainActivity.this, "No Data  Getting....", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(MainActivity.this, "WiFi is not Connected....", Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(MainActivity.this, SearchForm.class));
            }
        });
        IdentifyForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IdentifyForm.class));

            }
        });
        mappingForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MappingForm.class));
            }
        });


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
                    JSONArray array = new JSONArray(response);
                    if (array.length() == 0) {
//                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "No Data Available of this tag...", Toast.LENGTH_SHORT).show();
                    } else {

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
                }
            }
        }, error -> {
//            dialog.dismiss();
            Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
        });


        queue.add(jsObjRequest);

    }


    private void submit_Report() throws JSONException {

        JSONObject object = new JSONObject();

        JSONArray array = new JSONArray();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date date = format.parse(dtStart);



        for (int i = 0; i < listDB.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            if (listDB.get(i).getStatus().matches("True")) {

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

            }
            array.put(jsonObject);
        }


        object.put("ListData", array);
        if (array.length()>0){
            RequestQueue queue = Volley.newRequestQueue(this);
            System.out.println("JSON DATA " + object);

            final String requestBody = object.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GetUpdateData, response -> {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

                Log.i("VOLLEY Submit", response);
//            dialog.dismiss();
            }, error -> {
                try {

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
        }else {
            Toast.makeText(MainActivity.this, "Not Data To Update....", Toast.LENGTH_SHORT).show();
        }
//        String url = "http://164.52.223.163:4510/api/WriteRfidTagDetails";

    }



}