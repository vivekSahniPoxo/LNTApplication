package com.example.lntapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalDataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LNTApplication";
    private static final String TABLE_Report = "ProductDetail_LNT1";
    private static final String ProductId = "";
    private static final String SerialNo = "";
    private static final String DrawingNo = "";
    private static final String SapNo = "";
    private static final String SpoolNo = "";
    private static final String Weight = "";
    private static final String Contractor = "";
    private static final String Location = "";
    private static final String RfidNo = "";
    private static final String Remarks = "";
    private static final String CreatedAt = "";
    private static final String UpdatedAt = "";

    public LocalDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
