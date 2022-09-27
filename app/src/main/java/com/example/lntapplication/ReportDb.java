package com.example.lntapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportDb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LNTApplication";
    private static final String TABLE_Report = "ProductDetail_LNT1";
    private static final String ProductId = "ProductId";
    private static final String SerialNo = "SerialNo";
    private static final String DrawingNo = "DrawingNo";
    private static final String SapNo = "SapNo";
    private static final String SpoolNo = "SpoolNo";
    private static final String Weight = "Weight";
    private static final String Contractor = "Contractor";
    private static final String Location = "Location";
    private static final String RfidNo = "RfidNo";
    private static final String Remarks = "Remarks";
    private static final String CreatedAt = "CreatedAt";
    private static final String UpdatedAt = "UpdatedAt";
    private static final String Status = "Status";

    public ReportDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_Report + " ("
                + ProductId + " TEXT,"
                + SerialNo + " TEXT,"
                + DrawingNo + " TEXT,"
                + SapNo + " TEXT,"
                + SpoolNo + " TEXT,"
                + Weight + " TEXT,"
                + Contractor + " TEXT,"
                + Location + " TEXT,"
                + RfidNo + " TEXT,"
                + Remarks + " TEXT,"
                + CreatedAt + " TEXT,"
                + UpdatedAt + " TEXT,"
                + Status + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Report);
        // Create tables again
        onCreate(db);
    }

    public void addContact(ReportDatabase dataModelClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(ID, dataModelClass.getID());
        values.put(ProductId, dataModelClass.getProductId());
        values.put(SerialNo, dataModelClass.getSerialNo());
        values.put(DrawingNo, dataModelClass.getDrawingNo());
        values.put(SapNo, dataModelClass.getSapNo());
        values.put(SpoolNo, dataModelClass.getSpoolNo());
        values.put(Weight, dataModelClass.getWeight());
        values.put(Contractor, dataModelClass.getContractor());
        values.put(Location, dataModelClass.getLocation());
        values.put(RfidNo, dataModelClass.getRfidNo());
        values.put(Remarks, dataModelClass.getRemarks());
        values.put(CreatedAt, dataModelClass.getCreatedAt());
        values.put(UpdatedAt, dataModelClass.getUpdatedAt());
        values.put(Status, dataModelClass.getStatus());
        // Inserting Row
        long result = db.insert(TABLE_Report, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        System.out.print("Value insert" + result);


    }
    // code to add the new contact

    public List<ReportDatabase> getAllContacts() {
        List<ReportDatabase> contactList = new ArrayList<ReportDatabase>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Report;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and/ adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportDatabase dataModelClass = new ReportDatabase();
                dataModelClass.setProductId(cursor.getString(0));
                dataModelClass.setSerialNo(cursor.getString(1));
                dataModelClass.setDrawingNo(cursor.getString(2));
                dataModelClass.setSapNo(cursor.getString(3));
                dataModelClass.setSpoolNo(cursor.getString(4));
                dataModelClass.setWeight(cursor.getString(5));
                dataModelClass.setContractor(cursor.getString(6));
                dataModelClass.setLocation(cursor.getString(7));
                dataModelClass.setRfidNo(cursor.getString(8));
                dataModelClass.setRemarks(cursor.getString(9));
                dataModelClass.setCreatedAt(cursor.getString(10));
                dataModelClass.setUpdatedAt(cursor.getString(11));
                dataModelClass.setStatus(cursor.getString(12));

                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }


    public List<ReportDatabase> getAllDetails(String Modelname) {
        List<ReportDatabase> contactList = new ArrayList<ReportDatabase>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_Report + "WHERE" + PVmodleName + "=" + Modelname;
        String selectQuery = "SELECT * FROM " + TABLE_Report + " WHERE " + RfidNo + " = '" + Modelname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportDatabase dataModelClass = new ReportDatabase();
                dataModelClass.setProductId(cursor.getString(0));
                dataModelClass.setSerialNo(cursor.getString(1));
                dataModelClass.setDrawingNo(cursor.getString(2));
                dataModelClass.setSapNo(cursor.getString(3));
                dataModelClass.setSpoolNo(cursor.getString(4));
                dataModelClass.setWeight(cursor.getString(5));
                dataModelClass.setContractor(cursor.getString(6));
                dataModelClass.setLocation(cursor.getString(7));
                dataModelClass.setRfidNo(cursor.getString(8));
                dataModelClass.setRemarks(cursor.getString(9));
                dataModelClass.setCreatedAt(cursor.getString(10));
                dataModelClass.setUpdatedAt(cursor.getString(11));
                dataModelClass.setStatus(cursor.getString(12));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<ReportDatabase> getAllDetailsSearch(String Modelname) {
        List<ReportDatabase> contactList = new ArrayList<ReportDatabase>();
        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_Report + "WHERE" + PVmodleName + "=" + Modelname;
        String selectQuery = "SELECT * FROM " + TABLE_Report + " WHERE " + SapNo + " = '" + Modelname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportDatabase dataModelClass = new ReportDatabase();
                dataModelClass.setProductId(cursor.getString(0));
                dataModelClass.setSerialNo(cursor.getString(1));
                dataModelClass.setDrawingNo(cursor.getString(2));
                dataModelClass.setSapNo(cursor.getString(3));
                dataModelClass.setSpoolNo(cursor.getString(4));
                dataModelClass.setWeight(cursor.getString(5));
                dataModelClass.setContractor(cursor.getString(6));
                dataModelClass.setLocation(cursor.getString(7));
                dataModelClass.setRfidNo(cursor.getString(8));
                dataModelClass.setRemarks(cursor.getString(9));
                dataModelClass.setCreatedAt(cursor.getString(10));
                dataModelClass.setUpdatedAt(cursor.getString(11));
                dataModelClass.setStatus(cursor.getString(12));
                // Adding contact to list
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }




    public int GetCountUnMAppedData() {
        List<ReportDatabase> contactList = new ArrayList<ReportDatabase>();
              String selectQuery = "SELECT * FROM " + TABLE_Report + " WHERE " + Status + " = '" + "True" + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count=0;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReportDatabase dataModelClass = new ReportDatabase();
                dataModelClass.setProductId(cursor.getString(0));
                dataModelClass.setSerialNo(cursor.getString(1));
                dataModelClass.setDrawingNo(cursor.getString(2));
                dataModelClass.setSapNo(cursor.getString(3));
                dataModelClass.setSpoolNo(cursor.getString(4));
                dataModelClass.setWeight(cursor.getString(5));
                dataModelClass.setContractor(cursor.getString(6));
                dataModelClass.setLocation(cursor.getString(7));
                dataModelClass.setRfidNo(cursor.getString(8));
                dataModelClass.setRemarks(cursor.getString(9));
                dataModelClass.setCreatedAt(cursor.getString(10));
                dataModelClass.setUpdatedAt(cursor.getString(11));
                dataModelClass.setStatus(cursor.getString(12));
                // Adding contact to list
                count++;
                contactList.add(dataModelClass);
            } while (cursor.moveToNext());
        }
        // return contact list
        return count;
    }



    public boolean GetDataInfo() {
        String selectQuery = "SELECT  * FROM " + TABLE_Report;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0) {
            return false;
        } else return true;
    }
    // Deleting single contact
//    public void deleteContact(ReportModelClass contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_Report, ID + " = ?",
//                new String[]{String.valueOf(contact.getID())});
//        db.close();
//    }
//    public void deleteRow(String value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_Report + " WHERE " + ID + "='" + value + "'");
//        db.close();
//    }

    //    public void delete(int position) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        String table = TABLE_Report;
//        String whereClause = ID;
//        String [] whereArgs = new String[] {String.valueOf(position)};
//        db.delete (table, whereClause, whereArgs);
//
//    }
//    Method for Update
    public int updateContact(ReportDatabase dataModelClass, String valueID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductId, dataModelClass.getProductId());
        values.put(SerialNo, dataModelClass.getSerialNo());
        values.put(DrawingNo, dataModelClass.getDrawingNo());
        values.put(SapNo, dataModelClass.getSapNo());
        values.put(SpoolNo, dataModelClass.getSpoolNo());
        values.put(Weight, dataModelClass.getWeight());
        values.put(Contractor, dataModelClass.getContractor());
        values.put(Location, dataModelClass.getLocation());
        values.put(RfidNo, dataModelClass.getRfidNo());
        values.put(Remarks, dataModelClass.getRemarks());
        values.put(CreatedAt, dataModelClass.getCreatedAt());
        values.put(UpdatedAt, dataModelClass.getUpdatedAt());
        values.put(Status, dataModelClass.getStatus());


        // updating row
        return db.update(TABLE_Report, values, SapNo + " = ?",
                new String[]{String.valueOf(valueID)});
    }

    public boolean isTableExists() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE_Report;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        } else {
            mCursor.close();
            return false;
        }
    }

//    public boolean isEmpty() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//
////        if (cursor.getCount() > 0)
////        {
////            return false;
////        }
////        else
////            return true;
//    }

//    public void deleteAll()
//    {
//        String query="TRUNCATE TABLE"+ TABLE_Report;
//        SQLiteDatabase db = this.getWritableDatabase();
//        // db.delete(TABLE_NAME,null,null);
//        //db.execSQL("delete * from"+ TABLE_NAME);
//        db.execSQL(query);
//        db.close();
//    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_Report);
        db.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int UpdateLocation(String Location1, String valueID) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Location, Location1);
        values.put(Status,"True");
        values.put(UpdatedAt, now.toString());





        // updating row
        return db.update(TABLE_Report, values, SapNo + " = ?",
                new String[]{String.valueOf(valueID)});
    }

}
