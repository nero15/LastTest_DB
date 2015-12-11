package com.example.mykola.lasttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.DataBaseActivity;
import com.example.mykola.lasttest.SQLiteDataBase.Table;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Save(0d,0d);
    }

    public void onClickMap(View v){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }
    public void onClickDB(View v){
        Intent intent = new Intent(this,DataBaseActivity.class);
        startActivity(intent);
    }

    public void onClickExit(View v){
        finish();
    }
    public void Save(Double longitude, Double latitude){
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(Table.Object_standart.LONGITUDE, longitude.toString());
        ed.putString(Table.Object_standart.LATITUDE, latitude.toString());
        ed.commit();
        //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }
}
