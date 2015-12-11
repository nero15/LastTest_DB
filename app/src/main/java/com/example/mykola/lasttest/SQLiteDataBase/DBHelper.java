package com.example.mykola.lasttest.SQLiteDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.mykola.lasttest.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by mykola on 30.11.15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ObjectDB";
    private static final int DATABASE_VERSION = 1;
    private final Context fContext;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }


    public void addObject(String name, Double longitude, Double latitude,
                          String phone_number, String site, Double mark,
                          String work_time, SQLiteDatabase db)
    {
        logAllRecords(db, Table.Object_standart.TABLE_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Object_standart.NAME,name);
        contentValues.put(Table.Object_standart.IMAGE_URL,"");
        contentValues.put(Table.Object_standart.CATEGORY,"cinema");
        contentValues.put(Table.Object_standart.LONGITUDE,longitude);
        contentValues.put(Table.Object_standart.LATITUDE,latitude);
        contentValues.put(Table.Object_standart.PHONE_NUMBER,phone_number);
        contentValues.put(Table.Object_standart.WEBSITE,site);
        contentValues.put(Table.Object_standart.MARK,mark);
        contentValues.put(Table.Object_standart.WORK_TIME,work_time);
        db.insert(Table.Object_standart.TABLE_NAME, null, contentValues);
        logAllRecords(db, Table.Object_standart.TABLE_NAME);
        db.close();
        Toast.makeText(fContext, "Data Save!", Toast.LENGTH_LONG).show();
    }

    public void addWaybill(String name,String metaData,SQLiteDatabase db){
        //TODO: Checked function
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Waybill_table.NAME,name);
        contentValues.put(Table.Waybill_table.META_DATA,metaData);
        db.insert(Table.Object_standart.TABLE_NAME, null, contentValues);
        logAllRecords(db, Table.Object_standart.TABLE_NAME);
        db.close();
        Toast.makeText(fContext, "Data Save!", Toast.LENGTH_LONG).show();
    }
    public void addUser(String name,String password, String metaData, SQLiteDatabase db)
    {
        logAllRecords(db, Table.User_table.TABLE_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.User_table.NAME,name);
        contentValues.put(Table.User_table.PASSWORD,password);
        contentValues.put(Table.User_table.META_DATA,metaData);
        db.insert(Table.User_table.TABLE_NAME, null, contentValues);
        logAllRecords(db, Table.User_table.TABLE_NAME);
        db.close();
        Toast.makeText(fContext, "Data Save!", Toast.LENGTH_LONG).show();
    }

    public void repairDB(SQLiteDatabase db,
                         Boolean objectTable, Boolean userTable,
                         Boolean WaybillTable, Boolean Question){
        if (objectTable)
            deleteTable(db, Table.Object_standart.TABLE_NAME);
        if (userTable)
            deleteTable(db, Table.User_table.TABLE_NAME);
        if (WaybillTable)
            deleteTable(db,Table.Waybill_table.TABLE_NAME);
        if (Question)
            deleteTable(db, Table.Question_table.TABLE_NAME);
//        deleteTable(db, Table.Cinema_table.TABLE_NAME);
//        deleteTable(db, Table.Cafe_table.TABLE_NAME);
//        deleteTable(db, Table.Hotel_table.TABLE_NAME);
//        deleteTable(db, Table.Monuments_table.TABLE_NAME);
//        deleteTable(db, Table.Museum_table.TABLE_NAME);
//        deleteTable(db, Table.Shop_table.TABLE_NAME);
//        deleteTable(db, Table.Theatre_table.TABLE_NAME);
        createAllTable(db);
        FillAllTable(db);
        Log.i("DATABASE_OPERATIONS", "Database repaired");
        Toast.makeText(fContext, "Data base repair!", Toast.LENGTH_LONG).show();
    }

    public Cursor getData(SQLiteDatabase db,String table_name, String[] projections){
        Cursor cursor;
        cursor = db.query(table_name,projections,null,null,null,null,null);
        return cursor;
    }

    public Cursor getDataSelect(SQLiteDatabase db,String table_name, String[] projections, String selection, String[] args){
        Cursor cursor;
        cursor = db.query(table_name,projections,selection,args,null,null,null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("DATABASE_OPERATIONS", "Database created");
        createAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteTable(SQLiteDatabase db,String table_name){
        db.execSQL("DROP TABLE IF EXISTS " + table_name + " ;");
        Log.i("DATABASE_OPERATIONS", table_name + " deleted");
    }

    public void FillAllTable(SQLiteDatabase db) {
        Resources res = fContext.getResources();
        XmlResourceParser _xml = res.getXml(R.xml.all_records);
        fillTable(db, Table.Object_standart.allField, Table.Object_standart.TABLE_NAME, _xml);
        _xml = res.getXml(R.xml.question_records);
        fillTable(db, Table.Question_table.allField, Table.Question_table.TABLE_NAME, _xml);
        DBHelper dbHelper = new DBHelper(fContext);
        db.close();
        db = dbHelper.getReadableDatabase();
        logAllRecords(db, Table.Question_table.TABLE_NAME);
        db.close();
        db = dbHelper.getWritableDatabase();
    }
    public void createAllTable(SQLiteDatabase db){
        db.execSQL(Table.Object_standart.CREATE_OBJECT);
        db.execSQL(Table.User_table.CREATE_USER);
        db.execSQL(Table.Waybill_table.CREATE_WAYBILL);
        db.execSQL(Table.Question_table.CREATE_QUESTION);
        //logAllRecords(db, Table.Object_standart.TABLE_NAME);
//        db.execSQL(Table.Notebook_table.CREATE_NOTEBOOK);
//        db.execSQL(Table.Cinema_table.CREATE_CINEMA);
//        db.execSQL(Table.Cafe_table.CREATE_CAFE);
//        db.execSQL(Table.Hotel_table.CREATE_HOTEL);
//        db.execSQL(Table.Monuments_table.CREATE_MONUMENT);
//        db.execSQL(Table.Museum_table.CREATE_MUSEUM);
//        db.execSQL(Table.Shop_table.CREATE_SHOP);
//        db.execSQL(Table.Theatre_table.CREATE_THEATRE);

        Log.i("DATABASE_OPERATIONS", "All table created");
    }
    public void logAllRecords(SQLiteDatabase db, String table_name) {
        Cursor cursor;
        Log.i("DATABASE_OPERATIONS", table_name + " all records");
        cursor = db.query(table_name, null, null, null, null, null, null);
        if (cursor != null)
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String i : cursor.getColumnNames()) {
                        str = str.concat(i + " - " + cursor.getString(cursor.getColumnIndex(i)) + ";");
                    }
                    Log.i("DATABASE_OPERATIONS", str);
                } while (cursor.moveToNext());
            }
        cursor.close();
    }
    public void fillTable(SQLiteDatabase db, String[] allField,String table_name,XmlResourceParser _xml){
        ContentValues values = new ContentValues();
        try {
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && (_xml.getName().equals("record"))) {
                    values.clear();
                    for(int i=0;i< allField.length;i++) {
                        values.put(allField[i], _xml.getAttributeValue(i));
                        String str = _xml.getAttributeValue(i);
                        Log.i("DATABASE_OPERATIONS",allField[i] + ";" + str);
                    }
                    db.insert(table_name, null, values);
                }
                eventType = _xml.next();
            }
        }
        catch (XmlPullParserException e) {Log.i("DATABASE_OPERATIONS", e.getMessage(), e);}
        catch (IOException e) {Log.i("DATABASE_OPERATIONS", e.getMessage(), e);}
        finally {_xml.close();}
        logAllRecords(db, table_name);
        Log.i("DATABASE_OPERATIONS", table_name+ " completion");
    }
//    public void fillTable(SQLiteDatabase db, XmlResourceParser _xml){
//        ContentValues values = new ContentValues();
//        try {
//            int eventType = _xml.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if ((eventType == XmlPullParser.START_TAG) && (_xml.getName().equals("record"))) {
//                    values.clear();
//                    for(int i=0;i< Table.Object_standart.allField.length;i++) {
//                        values.put(Table.Object_standart.allField[i], _xml.getAttributeValue(i));
//                        String str = _xml.getAttributeValue(i);
//                        Log.i("DATABASE_OPERATIONS",Table.Object_standart.allField[i] + ";" + str);
//                    }
//                    db.insert(Table.Object_standart.TABLE_NAME, null, values);
//                }
//                eventType = _xml.next();
//            }
//        }
//        catch (XmlPullParserException e) {Log.i("DATABASE_OPERATIONS", e.getMessage(), e);}
//        catch (IOException e) {Log.i("DATABASE_OPERATIONS", e.getMessage(), e);}
//        finally {_xml.close();}
//        logAllRecords(db, Table.Object_standart.TABLE_NAME);
//        Log.i("DATABASE_OPERATIONS", Table.Object_standart.TABLE_NAME + " completion");
//    }
}
