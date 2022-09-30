package com.example.lntapplication;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rscja.deviceapi.RFIDWithUHFBLE;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.KeyEventCallback;

import java.util.ArrayList;
import java.util.List;

public class SearchForm extends AppCompatActivity {
    Button add_accession_btn, Search_btn, retry_btn, stop_btn, New_, SubmitDetails;
    AutoCompleteTextView accession_no;
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
    List<ReportDatabase> listDb, ListDbAll;
    String[] res = null;
    List<String> listDbSerial = new ArrayList<>();

    int count = 0;
    String buttonText;
    CheckBox SelectAll;
    boolean loopFlag = false;
    private ToneGenerator toneG;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        SelectAll = findViewById(R.id.checkBox_SelectAll);
        listDb = new ArrayList<>();
        uhf.init(this);
        uhf.setPower(30);

        looperDemo = new LooperDemo();
        dialog = new ProgressDialog(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(broadcastReceiver, filter);
        ListDbAll = reportDb.getAllContacts();

        New_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear();
            }
        });

        SelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (SelectAll.isChecked()) {
                    if (count == 0) {
                        count = 1;
                        listDb = reportDb.getAllContacts();
                        if (listDb.size() > 0) {
                            dialog.setMessage("Fetching Data...");
                            dialog.setCancelable(false);
                            dialog.show();
                            SetDataRecyclerview(listDb);
                        } else {
                            Toast.makeText(SearchForm.this, "No Data of this Spool number ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                adapter_list.getFilter((List) msg.obj);


            }
        };

        for (int i = 0; i < ListDbAll.size(); i++) {
            listDbSerial.add(ListDbAll.get(i).getSapNo());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, listDbSerial);

        accession_no.setThreshold(1);
        accession_no.setAdapter(adapter);

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
            loopFlag = true;
            list.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Search_btn.setText("STOP");
                    uhf.startInventoryTag();
                    uhf.setBeep(true);
                    UHFTAGInfo uhfTaginfo=null;
                    while (loopFlag && !interrupted()) {
                        try {
                            uhfTaginfo = uhf.readTagFromBuffer();
                            String tag = uhfTaginfo.getEPC().substring(4,28);
                            if (tag != null && tag.length() > 0) {
                                Log.e("Testing", "EPC:" + tag);
                                if (needtoProcess(tag, list)) {

                                    looperDemo.execute(() -> {
                                        Message message = Message.obtain();
                                        message.obj = list;
                                        handler.sendMessage(message);
                                    });
                                    if (list_data_Recyclerview.size() == list.size()) {
                                        StopSearch();
                                    }
                                    // toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();


//                UHFTAGInfo info = uhf.readTagFromBuffer();

        } else {
            StopSearch();
//            adapter_list.getFilter(list);
        }


    }

    public void StopSearch() {
        try {
            uhf.stopInventory();
            add_accession_btn.setEnabled(true);
            loopFlag = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Search_btn.setText("Start");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean needtoProcess(String strEPC, ArrayList list) {
        if (strEPC == null || strEPC.equals("")) {
            return false;
        }
        try {
            if (list.contains(strEPC)) {
                return false;
            } else {
                for (int i = 0; i < list_data_Recyclerview.size(); i++) {
                    if (list_data_Recyclerview.get(i).getRfidNo().equals(strEPC)) {
                        list.add(strEPC);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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


    private synchronized List<UHFTAGInfo> getUHFInfo() {
        List<UHFTAGInfo> list = null;

        //The reader board version 2.20-2.29 readTagFromBufferList function supports output Rssi, no need to call readTagFromBufferList_EpcTidUser
        list = uhf.readTagFromBufferList();
        return list;
    }

    //Method for Clear Data from Components
    public void Clear() {
        list_data_Recyclerview.clear();
        adapter_list = new Adapter_list(list_data_Recyclerview, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter_list);
//        search_data.setText("");
//        accession_no.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uhf.stopInventory();
        finish();

    }
}
