package com.example.mykola.lasttest.SQLiteDataBase.DataBaseReview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mykola.lasttest.OtherClass.Dialog_;
import com.example.mykola.lasttest.R;
import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Vector;

public class DBReviewActivity extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = "All";
        showObject(category);
        Log.i("Review", "onCreate");
    }
    public void onChecked(View v){ //TODO: Checked function

        switch (v.getId()){
            case R.id.id_RD_All:
                if (((RadioButton)v).isChecked())
                    category = "All";
                break;
            case R.id.id_RD_Cafe:
                if (((RadioButton)v).isChecked())
                    category = "cafe";
                break;
            case R.id.id_RD_Cinema:
                if (((RadioButton)v).isChecked())
                    category = "cinema";
                break;
            case R.id.id_RD_Hotel:
                if (((RadioButton)v).isChecked())
                    category = "hotel";
                break;
            case R.id.id_RD_Monument:
                if (((RadioButton)v).isChecked())
                    category = "monument";
                break;
            case R.id.id_RD_Museum:
                if (((RadioButton)v).isChecked())
                    category = "museum";
                break;
            case R.id.id_RD_Shop:
                if (((RadioButton)v).isChecked())
                    category = "shop";
                break;
            case R.id.id_RD_Theatre:
                if (((RadioButton)v).isChecked())
                    category = "theatre";
                break;
        }
    }
    public void onClickSearch(View v){
        if (category == null )
            category = "All";
        showObject(category);
    }
    protected void showObject(String args){
        setContentView(R.layout.activity_dbreview);
        listView = (ListView) findViewById(R.id.DBlist_review);
        listDataAdapter = new ListDataAdapter(
                getApplicationContext(),
                R.layout.standart_db_object,
                getFragmentManager(),
                this);
        listDataAdapter.clear();
        listView.setAdapter(listDataAdapter);
        dbHelper = new DBHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        String[] projections = new String[]
                {
                        Table.Object_standart.ID,
                        Table.Object_standart.NAME,
                        Table.Object_standart.IMAGE_URL,
                        Table.Object_standart.CATEGORY,
                        Table.Object_standart.LONGITUDE,
                        Table.Object_standart.LATITUDE,
                        Table.Object_standart.DESCRIPTION,
                        Table.Object_standart.MARK,
                        Table.Object_standart.WORK_TIME};
        if (args.equals("All"))
            cursor = dbHelper.getData(sqLiteDatabase, Table.Object_standart.TABLE_NAME, projections);
        else{
            cursor = dbHelper.getDataSelect(sqLiteDatabase, Table.Object_standart.TABLE_NAME,projections,
                    Table.Object_standart.CATEGORY + " = ?", new String[]{args});
        }
        addObjectToScreen(cursor);
    }
    protected void addObjectToScreen(Cursor cursor) {
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    String id_db, name, image_url, category, mark, longitude, latitude, description, workTime;
                    id_db = cursor.getString(cursor.getColumnIndex(Table.Object_standart.ID));
                    name = cursor.getString(cursor.getColumnIndex(Table.Object_standart.NAME));
                    image_url = cursor.getString(cursor.getColumnIndex(Table.Object_standart.IMAGE_URL));
                    category = cursor.getString(cursor.getColumnIndex(Table.Object_standart.CATEGORY));
                    mark = cursor.getString(cursor.getColumnIndex(Table.Object_standart.MARK));
                    longitude = cursor.getString(cursor.getColumnIndex(Table.Object_standart.LONGITUDE));
                    latitude = cursor.getString(cursor.getColumnIndex(Table.Object_standart.LATITUDE));
                    description = cursor.getString(cursor.getColumnIndex(Table.Object_standart.DESCRIPTION));
                    workTime = cursor.getString(cursor.getColumnIndex(Table.Object_standart.WORK_TIME));
                    DataProvider dataProvider = new DataProvider(id_db, name, category, mark, image_url,
                            getApplicationContext().getString(R.string.LONGITUDE) + " " + longitude.toString()
                                    + " \n" + getApplicationContext().getString(R.string.LATITUDE)+ " " + latitude.toString(),
                            workTime, description);
                    listDataAdapter.add(dataProvider);
                } while (cursor.moveToNext());
            }
        cursor.close();
    }


}

