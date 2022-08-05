package com.example.lntapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchForm extends AppCompatActivity {
    Button add_accession_btn, Search_btn, retry_btn, stop_btn, New_, SubmitDetails;
    EditText accession_no;
    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    Handler handler;
    boolean bleStatus = false;
    RecyclerView recyclerView;
    List<Data_Model_Search> list_data_Recyclerview = new ArrayList<>();
    Adapter_list adapter_list;
    ProgressDialog dialog;
    String result;
    private LooperDemo looperDemo;
    List<String> rfidlist = new ArrayList<>();
    ReportDb reportDb;
    List<ReportDatabase> listDb;
    String buttonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);
        recyclerView = findViewById(R.id.recyclerView_Accession);
        Search_btn = findViewById(R.id.Reading_btn);
        stop_btn = findViewById(R.id.Stop_Search);
        retry_btn = findViewById(R.id.Retry);
        add_accession_btn = findViewById(R.id.Add_accession);
        accession_no = findViewById(R.id.Accession_no);
        New_ = findViewById(R.id.New_accession);
        SubmitDetails = findViewById(R.id.buttonrfidDetails);
        reportDb = new ReportDb(this);
        listDb = new ArrayList<>();
        uhf.init(this);
        looperDemo = new LooperDemo();
        dialog = new ProgressDialog(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter_list.getFilter((List) msg.obj);


            }
        };

        uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int i) {
                if (i == 1) {
                    buttonText = Search_btn.getText().toString();
//                    if (bleStatus) {
                    if (list_data_Recyclerview.size() > 0) {
                        Scan(buttonText);
                    } else {
                        Toast.makeText(SearchForm.this, "No Data to Scan", Toast.LENGTH_SHORT).show();
                    }

//                    } else {
//                        ShowDailog();
//                    }
                }
            }

            @Override
            public void onKeyUp(int i) {

            }
        });

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter_list.RetrySearch();
            }
        });
        add_accession_btn.setOnClickListener(v -> {
            if (accession_no.length() > 0) {

                listDb = reportDb.getAllDetailsSearch(accession_no.getText().toString().trim());
                if (listDb.size() > 0) {
                    dialog.setMessage("Fetching Data...");
                    dialog.setCancelable(false);
                    dialog.show();
                    SetDataRecyclerview(listDb);
                } else {
                    Toast.makeText(SearchForm.this, "No Data of this Spool number " + accession_no.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                accession_no.setError("Enter Input...");
            }
            accession_no.setText("");
        });

        Search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                buttonText = b.getText().toString();
//                if (bleStatus) {
                if (list_data_Recyclerview.size() > 0) {
                    Scan(buttonText);
                } else {
                    Toast.makeText(SearchForm.this, "No Data to Scan", Toast.LENGTH_SHORT).show();
                }
//                }
//                 else{
//                        ShowDailog();
//                    }
            }
        });
    }

    private void Scan(String buttonText) {
        ArrayList list = new ArrayList();
        if (buttonText.matches("Start")) {
            Search_btn.setText("STOP");
            uhf.startInventoryTag();
            List<UHFTAGInfo> info = uhf.readTagFromBufferList();
            try {
                for (int i = 0; i < info.size(); i++) {
                    list.add(info.get(i).getEPC());

                }
//                adapter_list.getFilter(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            looperDemo.execute(() -> {
                Message message = Message.obtain();
                message.obj = list;
                handler.sendMessage(message);
            });


//                UHFTAGInfo info = uhf.readTagFromBuffer();

        } else {
            add_accession_btn.setEnabled(true);
            Search_btn.setText("Start");
            uhf.stopInventory();
//            adapter_list.getFilter(list);
        }


    }

    private void SetDataRecyclerview(List<ReportDatabase> listDb) {
        for (int i = 0; i < listDb.size(); i++) {
            list_data_Recyclerview.add(new Data_Model_Search(listDb.get(i).getProductId(), listDb.get(i).getSerialNo(), listDb.get(i).getDrawingNo(), listDb.get(i).getSapNo(), listDb.get(i).getSpoolNo(), listDb.get(i).getWeight(), listDb.get(i).getContractor(), listDb.get(i).getLocation(), listDb.get(i).getRfidNo(), listDb.get(i).getRemarks(), listDb.get(i).getCreatedAt(), listDb.get(i).getUpdatedAt()));

        }
        adapter_list = new Adapter_list(list_data_Recyclerview, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter_list);
        dialog.dismiss();
    }

    private void FetchData() throws JSONException {
//        String url = "http://164.52.223.163:4510/api/GetAssetInfoBySearch?AssetId=" + accession_no.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, ApiUrl.SearchApi + accession_no.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    JSONArray array = new JSONArray(response);
//                    if (array.length() == 0) {
//                        dialog.dismiss();
//                        Toast.makeText(SearchForm.this, "No Data Available of this Access Id...", Toast.LENGTH_SHORT).show();
//                    } else {

//                        for (int i = 0; i < array.length(); i++) {
                    JSONObject object = new JSONObject(response);

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


//                            dialog.dismiss();

                    list_data_Recyclerview.add(new Data_Model_Search(productId, serialNo, drawingNo, sapNo, spoolNo, weight, contractor, location, rfidNo, remarks, createdAt, updatedAt));
                    adapter_list = new Adapter_list(list_data_Recyclerview, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter_list);
                    if (list_data_Recyclerview.size() > 0) {
                        Search_btn.setEnabled(true);
//                                dialog.dismiss();
                    }
//                        }

//                    }

                    Log.e("response", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SearchForm.this, "Server Error", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                Toast.makeText(SearchForm.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);

//        queue.add(jsObjRequest);

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        BluetoothDevice device;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                bleStatus = true;
                Toast.makeText(getApplicationContext(), "Device is now Connected", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Toast.makeText(getApplicationContext(), "Device is disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void ShowDailog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.exitapp)
                .setTitle("Connect Bluetooth")
                .setMessage("Do You want to Connect device with bluetooth ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uhf.free();
                        startActivity(new Intent(SearchForm.this, BleSetUPForm.class));
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private synchronized List<UHFTAGInfo> getUHFInfo() {
        List<UHFTAGInfo> list = null;

        //读写器主板版本 2.20-2.29 readTagFromBufferList 函数支持输出Rssi，无需调用readTagFromBufferList_EpcTidUser
        list = uhf.readTagFromBufferList();
        return list;
    }

}
