package com.example.mykola.lasttest.SQLiteDataBase;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mykola.lasttest.R;
import com.example.mykola.lasttest.SQLiteDataBase.DataBaseReview.DBReviewActivity;

public class DataBaseActivity extends AppCompatActivity {
    Button review,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        review = (Button) findViewById(R.id.ReviewDBButton);
        add =(Button) findViewById(R.id.AddDBButton);
    }

    public void onClickReview(View v){
        Intent intent = new Intent(this,DBReviewActivity.class);
        startActivity(intent);
    }

    public void onClickAdd(View v){

        Intent intent = new Intent(this,AddDBActivity.class);
        startActivity(intent);
    }

    public void onClickRepair(View v){
        DBHelper dbHelper;
        SQLiteDatabase sqLiteDatabase;
        dbHelper = new DBHelper(getApplication());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.repairDB(sqLiteDatabase,true,false,false,false);
        sqLiteDatabase.close();
        dbHelper.close();
    }
}
