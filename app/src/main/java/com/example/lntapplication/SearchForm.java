package com.example.lntapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchForm extends AppCompatActivity {
    Button add_accession_btn, Search_btn, retry_btn, stop_btn, New_, SubmitDetails;
    EditText accession_no;

    Handler handler;
    RecyclerView recyclerView;
    List<Data_Model_Search> list_data_Recyclerview = new ArrayList<>();
    Adapter_list adapter_list;
    ProgressDialog dialog;
    String result;
    private LooperDemo looperDemo;
    List<String> rfidlist = new ArrayList<>();
    ReportDb reportDb;
    List<ReportDatabase> listDb;

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
        add_accession_btn.setOnClickListener(v -> {
            if (accession_no.length() > 0) {
//                if (list_data_Recyclerview.size() > 0) {
//                    adapter_list.notifyDataSetChanged();
//                    adapter_list.RetrySearch();
//                } else {
////                    dialog.setMessage("List Already Clear");
////                    dialog.setCancelable(true);
////                    dialog.show();
//                }
//                try {
//
//                    FetchData();
////                    dialog.show();
////                    dialog.setMessage(getString(R.string.Dialog_Text));
////                    dialog.setCancelable(false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                listDb = reportDb.getAllDetailsSearch(accession_no.getText().toString().trim());

                SetDataRecyclerview(listDb);
            } else {
                accession_no.setError("Enter Input...");
            }
            accession_no.setText("");
        });



    }

    private void SetDataRecyclerview(List<ReportDatabase> listDb) {
        for (int i = 0; i < listDb.size(); i++) {
            list_data_Recyclerview.add(new Data_Model_Search(listDb.get(i).getProductId(), listDb.get(i).getSerialNo(), listDb.get(i).getDrawingNo(), listDb.get(i).getSapNo(), listDb.get(i).getSpoolNo(), listDb.get(i).getWeight(), listDb.get(i).getContractor(), listDb.get(i).getLocation(), listDb.get(i).getRfidNo(), listDb.get(i).getRemarks(), listDb.get(i).getCreatedAt(), listDb.get(i).getUpdatedAt()));

        }
        adapter_list = new Adapter_list(list_data_Recyclerview, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter_list);
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




}