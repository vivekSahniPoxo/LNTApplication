package com.example.lntapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MappingForm extends AppCompatActivity {
    String epc, result;
    CardView ReadingCard;
    Spinner assetId;
    Button Search, ViewDetals, Mapped;
    String AssetKey, location, serialNo, pO_DATE1;
    ProgressDialog dialog;
    List<String> listId;
    TextView ASSET_CODE, seriaL_NO, planT_CODE, pO_NUMBER, pO_DATE, department, asseT_ID, alloteD_TO_PLANT;
    ReportDb reportDb;
    List<ReportDatabase> listDB,listdball;
    String ProductId,SerialNo,DrawingNo,SapNo,SpoolNo,Weight,Contractor,Location,RfidNo,Remarks,CreatedAt,UpdatedAt,Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_form);

        ReadingCard = findViewById(R.id.button_Scan);
        assetId = findViewById(R.id.spinner2value);
        Search = findViewById(R.id.Stop_Search);
        ViewDetals = findViewById(R.id.ViewAll);
        Mapped = findViewById(R.id.submitbn);
        listId = new ArrayList<>();
        asseT_ID = findViewById(R.id.Booktitle);
        seriaL_NO = findViewById(R.id.SolarCell);
        planT_CODE = findViewById(R.id.MonthPV);
        pO_NUMBER = findViewById(R.id.MonthSolar);
        pO_DATE = findViewById(R.id.IECcertificate);
        department = findViewById(R.id.textView4);
        ASSET_CODE = findViewById(R.id.textView6);
        alloteD_TO_PLANT = findViewById(R.id.textView8);
        reportDb = new ReportDb(this);
        listDB = new ArrayList<>();
        listdball=new ArrayList<>();
        listDB = reportDb.getAllContacts();

        SetupID(listDB);


//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Fetch Id....");
//        dialog.setCancelable(false);
//        dialog.show();
//        FetchAssetId();

        Mapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    reportDb.updateContact(new ReportDatabase( ProductId,SerialNo,DrawingNo,SapNo,SpoolNo,Weight,Contractor,Location,"E200001733010",Remarks,CreatedAt,now.toString(),"True"),AssetKey);

                }


                //                if (result == null) {
//                    Toast.makeText(Mapping.this, "Please Read RFID tag...", Toast.LENGTH_SHORT).show();
//                } else {
//                try {
////                        dialog.setMessage("Mapping Tag....");
////                        dialog.setCancelable(false);
////                        dialog.show();
//                    MappingTags();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
//            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AssetKey.length() > 0) {
//                    FetchData(AssetKey);
//                    dialog.setMessage("Fetch data....");
//                    dialog.setCancelable(false);
//                    dialog.show();
                    listdball= reportDb.getAllDetailsSearch(AssetKey.trim());
                    SetDAta(listdball);

                } else {
                    Toast.makeText(MappingForm.this, "Please Select Asset ID", Toast.LENGTH_SHORT).show();
                }

//                try {
//                    if (AssetKey.length() > 0) {
//                        FetchData(AssetKey);
//                        dialog.setMessage("Fetch data....");
//                        dialog.setCancelable(false);
//                        dialog.show();
//                    } else {
//                        Toast.makeText(MappingForm.this, "Please Select Asset ID", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void SetupID(List<ReportDatabase> listDB) {
        for (int i = 0; i < listDB.size(); i++) {
            listId.add(listDB.get(i).getSerialNo());

        }
        final ArrayAdapter<String> SpinnerCountrty = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listId);
        SpinnerCountrty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        assetId.setAdapter(SpinnerCountrty);
        assetId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AssetKey = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void FetchAssetId() {
//        String url = "http://164.52.223.163:4510/api/GetAssetId";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, ApiUrl.GetAssetID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    listId.add("Select ID");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("serialNo");

                        listId.add(id);
                    }

                    SetupIDSpinner(listId);
                    dialog.dismiss();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                    dialog.dismiss();
                }
//                    final List<String> List = new ArrayList<>(Arrays.asList(value));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MappingForm.this, error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });

        queue.add(request);

    }

    private void SetupIDSpinner(List<String> listId) {
        //Country name Spinner

        final ArrayAdapter<String> SpinnerCountrty = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listId);
        SpinnerCountrty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        assetId.setAdapter(SpinnerCountrty);
        assetId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AssetKey = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void MappingTags() throws JSONException {
//        String url = "http://164.52.223.163:4510/api/MapRfidTag";
        JSONObject obj = new JSONObject();
//        obj.put("id", "0");
        obj.put("serialNo", serialNo);
        obj.put("location", location);
        obj.put("rfidNo", "E200001D770302491340DE48");
//        obj.put("updatetime", "2022-06-13T06:45:17.169");

        RequestQueue queue = Volley.newRequestQueue(this);


        final String requestBody = obj.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MapRfidID, response -> {

            try {
                JSONObject object = new JSONObject(response);
                String message = object.optString("message");
                String result = object.optString("status");
                if (!result.matches("false")) {
                    listId.clear();
                    FetchAssetId();
                    asseT_ID.setText("");
                    seriaL_NO.setText("");
                    planT_CODE.setText("");
                    pO_NUMBER.setText("");
                    pO_DATE.setText("");
                    department.setText("");
                    ASSET_CODE.setText("");
                    alloteD_TO_PLANT.setText("");
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Toast.makeText(Mapping.this, "" + response, Toast.LENGTH_SHORT).show();
            Log.i("VOLLEY", response);
            dialog.dismiss();
//            listId.clear();
//            FetchAssetId();
        }, error -> {
            Log.e("VOLLEY Negative", error.toString());
            dialog.dismiss();
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
        };

        queue.add(stringRequest);
    }

    private void FetchData(String epcvalue) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://164.52.223.163:4510/api/GetAssetInfo?Assetid=" + epcvalue;
//        String url = "http://mudvprfidiis:82/api/GetAssetInfo?Assetid=" + epcvalue;
        StringRequest sr = new StringRequest(Request.Method.GET, ApiUrl.AssetIDDAta + epcvalue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    String productId = object.optString("productId");
                    serialNo = object.optString("serialNo");
                    String drawingNo = object.optString("drawingNo");
                    String sapNo = object.optString("sapNo");
                    String spoolNo = object.optString("spoolNo");
                    String weight = object.optString("weight");
                    String contractor = object.optString("contractor");
                    location = object.optString("location");
                    String rfidNo = object.optString("rfidNo");
                    String remarks = object.optString("remarks");
                    String createdAt = object.optString("createdAt");
                    String updatedAt = object.optString("updatedAt");

//
                    asseT_ID.setText(productId);
                    seriaL_NO.setText(serialNo);
                    planT_CODE.setText(drawingNo);
                    pO_NUMBER.setText(sapNo);
                    pO_DATE.setText(spoolNo);
                    department.setText(weight);
                    ASSET_CODE.setText(contractor);
                    alloteD_TO_PLANT.setText(location);
////                    Publisher.setText(LibraryItemType1);
//                    RFIDNo.setText(RFIDNo1);
//                    AccessNo.setText(Publisher1);
//                    Author.setText(ItemStatus1);
//                    Title.setText(Author1);
//                    YearOfPublication.setText(YearOfPublication1);
//                    EntryDate.setText(EntryDate1);
                    dialog.dismiss();


                    Log.e("response", response.toString());
                } catch (Exception e) {
                    Toast.makeText(MappingForm.this, "No Data Found...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MappingForm.this, "No Data Found...", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }


    private void SetDAta(List<ReportDatabase> listDetails) {

        department.setText(listDetails.get(0).getWeight());
//                        BookAddedIn.setText();
//                        BookCategory.setText();
        ASSET_CODE.setText(listDetails.get(0).getContractor());
//                        SubjectTitle.setText();
        planT_CODE.setText(listDetails.get(0).getDrawingNo());
//                        Edition.setText();
        pO_NUMBER.setText(listDetails.get(0).getSapNo());
//                        RFIDNo.setText(RFIDNo1);
        pO_DATE.setText(listDetails.get(0).getSpoolNo());
        seriaL_NO.setText(listDetails.get(0).getSerialNo());
        asseT_ID.setText(listDetails.get(0).getProductId());
        alloteD_TO_PLANT.setText(listDetails.get(0).getLocation());

        ProductId=listDetails.get(0).getProductId();
        SerialNo=listDetails.get(0).getSerialNo();
        DrawingNo=listDetails.get(0).getDrawingNo();
        SapNo=listDetails.get(0).getSapNo();
        Weight=listDetails.get(0).getWeight();
        Contractor=listDetails.get(0).getSerialNo();
        SpoolNo=listDetails.get(0).getSpoolNo();
        Location=listDetails.get(0).getLocation();
        Remarks=listDetails.get(0).getRemarks();
        UpdatedAt=listDetails.get(0).getUpdatedAt();
        CreatedAt=listDetails.get(0).getCreatedAt();

//        asseT_ID.setText(productId);
//        seriaL_NO.setText(serialNo);
//        planT_CODE.setText(drawingNo);
//        pO_NUMBER.setText(sapNo);
//        pO_DATE.setText(spoolNo);
//        department.setText(weight);
//        ASSET_CODE.setText(contractor);
//        alloteD_TO_PLANT.setText(location);
    }
}