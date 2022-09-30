package com.example.lntapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IdentifyForm extends AppCompatActivity {
    Button Search, NEW_data, Retry;
    public RFIDWithUHFBLE uhf = RFIDWithUHFBLE.getInstance();
    ReportDb reportDb;
    //    EditText search_key;
    TextView LibraryItemType, BookAddedIn, BookCategory, ItemStatus, SubjectTitle, Language, Edition, Publisher, RFIDNo, AccessNo, Author, Title, YearOfPublication, EntryDate;
    ProgressDialog dialog;
    CardView scan_button;
    public List<String> tempList;
    String epc;
    boolean bleStatus;
    String spoolID, Sapno;
    private List<ReportDatabase> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_identify_form);
        reportDb = new ReportDb(this);
        scan_button = findViewById(R.id.button_Scan);
//        search_key = findViewById(R.id.Search_key);
        Retry = findViewById(R.id.Retry);
        Search = findViewById(R.id.Search_rfid_button);
        LibraryItemType = findViewById(R.id.Library_item);
        NEW_data = findViewById(R.id.New_accession);
        BookAddedIn = findViewById(R.id.Book_Add);
        BookCategory = findViewById(R.id.BookCategory);
        ItemStatus = findViewById(R.id.Item_status);
        SubjectTitle = findViewById(R.id.Subject_t);
        Language = findViewById(R.id.Language);
        Edition = findViewById(R.id.Edition);
        Publisher = findViewById(R.id.Publisher);
        RFIDNo = findViewById(R.id.RFID_NO);
        AccessNo = findViewById(R.id.Access_No);
        Author = findViewById(R.id.Authorname);
        Title = findViewById(R.id.Booktitle);
        YearOfPublication = findViewById(R.id.YearOfPublication);
        EntryDate = findViewById(R.id.EntryDate);
        tempList = new ArrayList<>();
        dialog = new ProgressDialog(this);
        listDetails = new ArrayList<>();

        YearOfPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(IdentifyForm.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.dailoglocationupdate, null);
                android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(IdentifyForm.this);
                alertDialogBuilderUserInput.setView(mView);
                EditText editText = mView.findViewById(R.id.editTextTextPersonName);
                Button updatebtn, cancelbtn;
                updatebtn = mView.findViewById(R.id.button_update);
                cancelbtn = mView.findViewById(R.id.button_cancel);
                android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

                updatebtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        if (editText.length() > 0) {
                            if (Sapno != null) {

                                int a = reportDb.UpdateLocation(editText.getText().toString().trim(), Sapno);
                                Toast.makeText(IdentifyForm.this, "Value " + String.valueOf(a), Toast.LENGTH_SHORT).show();
                                System.out.println("Value of Return Type" + a);
                                if (a == 1 || a == 2) {
                                    YearOfPublication.setText(editText.getText().toString().trim());

                                    editText.setText("");
                                    alertDialogAndroid.dismiss();
                                    Toast.makeText(IdentifyForm.this, "Location Update  Successfully...", Toast.LENGTH_SHORT).show();
                                } else {
                                    alertDialogAndroid.dismiss();
                                    Toast.makeText(IdentifyForm.this, "Location not updated...  ", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                alertDialogAndroid.dismiss();
                                Toast.makeText(IdentifyForm.this, "Scan Tag first for update Location", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            editText.setError("Enter value...");
                        }
                    }
                });

                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogAndroid.dismiss();
                    }
                });


                alertDialogAndroid.show();
            }


        });
        uhf.init(this);
        //Listeners
        uhf.setPower(5);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);

//

        uhf.setKeyEventCallback(new KeyEventCallback() {
            @Override
            public void onKeyDown(int i) {
                if (i == 1) {
//                    if (bleStatus) {
                    String data = null;
                    data = uhf.readData("00000000", 1, 2, 6);
//                    UHFTAGInfo data=uhf.inventorySingleTag();
                    if (data == null) {
                        Toast.makeText(IdentifyForm.this, "Please Close the Tag to Reader...", Toast.LENGTH_SHORT).show();
                    } else {
                        listDetails = reportDb.getAllDetails(data);
                        if (listDetails.size() > 0) {
                            dialog.setMessage("Fetching Data...");
                            dialog.setCancelable(false);
                            dialog.show();
                            SetDAta(listDetails);
                        } else {
                            Toast.makeText(IdentifyForm.this, data + " No Data for this Tag...", Toast.LENGTH_SHORT).show();
                        }
//                    }else {
//                        ShowDailog();
//                    }
                    }
                }
            }

            @Override
            public void onKeyUp(int i) {

            }
        });


        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (bleStatus) {
                String data = null;
                data = uhf.readData("00000000", 1, 2, 6);
//                UHFTAGInfo data=uhf.inventorySingleTag();
                if (data == null) {
                    Toast.makeText(IdentifyForm.this, "Please Close the Tag to Reader...", Toast.LENGTH_SHORT).show();
                } else {
                    listDetails = reportDb.getAllDetails(data);
                    if (listDetails.size() > 0) {
                        dialog.setMessage("Fetching Data...");
                        dialog.setCancelable(false);
                        dialog.show();
                        SetDAta(listDetails);
                    } else {
                        Toast.makeText(IdentifyForm.this, data + " No Data for this Tag...", Toast.LENGTH_SHORT).show();
                    }
//                    }else {
//                        ShowDailog();
//                    }
                }

//                }
//                else {
//                    ShowDailog();
//
//                }
            }
        });

        NEW_data.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                new AlertDialog.Builder(IdentifyForm.this)
//                        .setIcon(R.drawable.exit_icon)
//                        .setTitle("Quit")
//                        .setMessage("Are you sure you want to Clear the List ?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                ClearData();
//                                tempList.clear();
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
                                            ClearData();
                                        }
                                    }

        );
    }

    private void SetDAta(List<ReportDatabase> listDetails) {

        LibraryItemType.setText(listDetails.get(0).getWeight());
//                        BookAddedIn.setText();
//                        BookCategory.setText();
        ItemStatus.setText(listDetails.get(0).getContractor());
//                        SubjectTitle.setText();
        Language.setText(listDetails.get(0).getDrawingNo());
//                        Edition.setText();
        Publisher.setText(listDetails.get(0).getSapNo());
//                        RFIDNo.setText(RFIDNo1);
        AccessNo.setText(listDetails.get(0).getSpoolNo());
        Author.setText(listDetails.get(0).getSerialNo());
        Title.setText(listDetails.get(0).getProductId());
        YearOfPublication.setText(listDetails.get(0).getLocation());
        spoolID = listDetails.get(0).getSpoolNo();
        Sapno = listDetails.get(0).getSapNo();
        dialog.dismiss();
    }


    //Method For Clear Components
    private void ClearData() {
        LibraryItemType.setText("--");
        BookAddedIn.setText("--");
        BookCategory.setText("--");
        ItemStatus.setText("--");
        SubjectTitle.setText("--");
        Language.setText("--");
        Edition.setText("--");
        Publisher.setText("--");
        RFIDNo.setText("--");
        AccessNo.setText("--");
        Author.setText("--");
        Title.setText("--");
        YearOfPublication.setText("--");
        EntryDate.setText("--");


    }

    private void FetchData(String epcvalue) throws JSONException {
//        String url = "http://mudvprfidiis:82/api/GetAssetInfoBySearch?RfidTagId=" + epcvalue;
//        String url = "http://164.52.223.163:4510/api/GetAssetInfoBySearch?RfidTagId=" + epcvalue;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, ApiUrl.IdentifyApi + epcvalue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    JSONArray array = new JSONArray(response);
//                    if (array.length() == 0) {
//                        dialog.dismiss();
//                        Toast.makeText(IdentifyForm.this, "No Data Available of this tag...", Toast.LENGTH_SHORT).show();
//                    } else {


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
                    dialog.dismiss();


                    LibraryItemType.setText(weight);
//                        BookAddedIn.setText();
//                        BookCategory.setText();
                    ItemStatus.setText(contractor);
//                        SubjectTitle.setText();
                    Language.setText(drawingNo);
//                        Edition.setText();
                    Publisher.setText(sapNo);
//                        RFIDNo.setText(RFIDNo1);
                    AccessNo.setText(spoolNo);
                    Author.setText(serialNo);
                    Title.setText(productId);
                    YearOfPublication.setText(location);
//                        EntryDate.setText(EntryDate1);
                    dialog.dismiss();
//                    }
                    Log.e("response", response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            dialog.dismiss();
            Toast.makeText(IdentifyForm.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
        });


        queue.add(jsObjRequest);

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
                        startActivity(new Intent(IdentifyForm.this, BleSetUPForm.class));
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