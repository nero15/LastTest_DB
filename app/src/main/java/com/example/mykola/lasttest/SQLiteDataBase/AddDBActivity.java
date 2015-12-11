package com.example.mykola.lasttest.SQLiteDataBase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykola.lasttest.R;

public class AddDBActivity extends AppCompatActivity {

    EditText name,longitude,latitude,phone,site,work_time;
    CheckBox myLocation;
    Context context = this;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String longitude_num,latitude_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_db);
        myLocation = (CheckBox) findViewById(R.id.id_myLocation);
        name = (EditText) findViewById(R.id.id_name);
        longitude = (EditText) findViewById(R.id.id_longitude);
        latitude = (EditText) findViewById(R.id.id_latitude);
        phone = (EditText) findViewById(R.id.id_phone);
        site = (EditText) findViewById(R.id.id_site);
        work_time = (EditText) findViewById(R.id.id_work_time);

    }
    private void active(View obj, boolean state){
        obj.setActivated(state);
        obj.setAlpha(state ? 1f : 0.5f);
        obj.setClickable(state);
    }
    public void checkClick(View v){
        TextView txt;
        if (myLocation.isChecked()){
            active(longitude,false);
            active(latitude,false);
            txt = (TextView) findViewById(R.id.id_longitudetxt);
            active(txt,false);
            txt = (TextView) findViewById(R.id.id_latitudetxt);
            active(txt,false);
        }
        else{
            active(longitude,true);
            active(latitude,true);
            txt = (TextView) findViewById(R.id.id_longitudetxt);
            active(txt,true);
            txt = (TextView) findViewById(R.id.id_latitudetxt);
            active(txt,true);
        }
    }
    public void acceptedClick(View v){

        String Name = name.getText().toString();

        String Longitude,Latitude;
        if (myLocation.isChecked()){
            load();
            Longitude = longitude_num;
            Latitude = latitude_num;
        }
        else {
            Longitude = longitude.getText().toString();
            Latitude = latitude.getText().toString();
        }
        String Phone = phone.getText().toString();
        String Site = site.getText().toString();
        String Work= work_time.getText().toString();
        if (Name == "" || Longitude == "" || Latitude == ""){
            Toast.makeText(getApplicationContext(), "Деяка інформація відсутня", Toast.LENGTH_LONG).show();
            return;
        }
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.addObject(Name,
                Double.parseDouble(Longitude),
                Double.parseDouble(Latitude),
                Phone,Site,4.3,Work,sqLiteDatabase);
        sqLiteDatabase.close();
        dbHelper.close();
        finish();
    }
    public void cancelClick(View v){
        finish();
    }
    void load() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        longitude_num = sharedPreferences.getString(Table.Object_standart.LONGITUDE, "");
        latitude_num = sharedPreferences.getString(Table.Object_standart.LATITUDE, "");
        //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }

}
