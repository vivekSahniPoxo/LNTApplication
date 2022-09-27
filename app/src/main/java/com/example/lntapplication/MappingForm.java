package com.example.lntapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
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
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

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
//    UHFTAGInfo result;
    Context context = this;
    CardView ReadingCard;
    boolean bleStatus;
    Spinner assetId;
    Button Search, ViewDetals, Mapped;
    String AssetKey, location, serialNo, pO_DATE1;
    ProgressDialog dialog;
    List<String> listId;
    TextView ASSET_CODE, seriaL_NO, planT_CODE, pO_NUMBER, pO_DATE, department, asseT_ID, alloteD_TO_PLANT;
    ReportDb reportDb;
    List<ReportDatabase> listDB,listdball;
    String ProductId,SerialNo,DrawingNo,SapNo,SpoolNo,Weight,Contractor,Location,RfidNo,Remarks,CreatedAt,UpdatedAt,Status;
    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_form);

        dialog=new ProgressDialog(this);
        uhf.init(this);
        uhf.setPower(10);
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

        ReadingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                result = uhf.readData("00000000", 1, 2, 6);
//                result=uhf.inventorySingleTag();
//                UHFTAGInfo info = uhf.readTagFromBuffer();
//                result= info.getEPC();

                if (!(result ==null))
                {

                    Toast.makeText(MappingForm.this, ""+result, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MappingForm.this, "Scan Again...", Toast.LENGTH_SHORT).show();

                }
            }
        });

        SetupID(listDB);


//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Fetch Id....");
//        dialog.setCancelable(false);
//        dialog.show();
//        FetchAssetId();

        uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int i) {
                if (i==1)
                {
                    result = uhf.readData("00000000", 1, 2, 6);
//                    result=uhf.inventorySingleTag();
                    if (!(result ==null))
                    {
                        Toast.makeText(MappingForm.this, ""+result, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MappingForm.this, "Scan Again...", Toast.LENGTH_SHORT).show();

                    }

                     }
            }

            @Override
            public void onKeyUp(int i) {

            }
        });



        Mapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();



                    if (result!=null) {
                        if (RfidNo==null) {
                            dialog.setMessage("Mapping Data...");
                            dialog.setCancelable(false);
                            dialog.show();
                            int a = reportDb.updateContact(new ReportDatabase(ProductId, SerialNo, DrawingNo, SapNo, SpoolNo, Weight, Contractor, Location, String.valueOf(result), Remarks, CreatedAt, now.toString(), "True"), AssetKey);
//                        Toast.makeText(MappingForm.this, ""+a, Toast.LENGTH_SHORT).show();

                            if (a == 1) {
                                dialog.dismiss();
                                Clear();
                                Toast.makeText(MappingForm.this, "Tag Mapped Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(MappingForm.this, "Tag not Mapped...  ", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            new AlertDialog.Builder(context)
                                    .setIcon(R.drawable.ic_baseline_update_24)
                                    .setTitle("Update Rfid Number")
                                    .setMessage("This Rfid already Mapped.Do you want to Update ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            int a = reportDb.updateContact(new ReportDatabase(ProductId, SerialNo, DrawingNo, SapNo, SpoolNo, Weight, Contractor, Location, String.valueOf(result), Remarks, CreatedAt, now.toString(), "True"), AssetKey);
//                        Toast.makeText(MappingForm.this, ""+a, Toast.LENGTH_SHORT).show();

                                            if (a == 1) {
                                                dialog.dismiss();
                                                Clear();
                                                Toast.makeText(MappingForm.this, "Tag Mapped Successfully...", Toast.LENGTH_SHORT).show();
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(MappingForm.this, "Tag not Mapped...  ", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                            Toast.makeText(MappingForm.this, "This "+SpoolNo+" Already Mapped...", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MappingForm.this, "Please Scan Tag...", Toast.LENGTH_SHORT).show();
                    }
                }

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
                    dialog.setMessage("Fetching Data...");
                    dialog.setCancelable(false);
                    dialog.show();
                    listdball= reportDb.getAllDetailsSearch(AssetKey.trim());
                    SetDAta(listdball);

                } else {
                    Toast.makeText(MappingForm.this, "Please Select Asset ID", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void SetupID(List<ReportDatabase> listDB) {
        for (int i = 0; i < listDB.size(); i++) {
            listId.add(listDB.get(i).getSapNo());

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
         RfidNo=listDetails.get(0).getRfidNo();
dialog.dismiss();
    }

    public  void Clear()
    {

        department.setText("");
//                        BookAddedIn.setText();
//                        BookCategory.setText();
        ASSET_CODE.setText("");
//                        SubjectTitle.setText();
        planT_CODE.setText("");
//                        Edition.setText();
        pO_NUMBER.setText("");
//                        RFIDNo.setText(RFIDNo1);
        pO_DATE.setText("");
        seriaL_NO.setText("");
        asseT_ID.setText("");
        alloteD_TO_PLANT.setText("");
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
                        startActivity(new Intent(MappingForm.this, BleSetUPForm.class));
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        uhf.free();
        finish();

    }
}