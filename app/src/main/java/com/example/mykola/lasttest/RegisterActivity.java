package com.example.mykola.lasttest;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykola.lasttest.AnketaReview.AnketaActivity;
import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.Table;

public class RegisterActivity extends AppCompatActivity {

    Button btn;
    TextView userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn = (Button) findViewById(R.id.btnNewAccount);
        userName = (TextView) findViewById(R.id.txtUsername);
        password = (TextView) findViewById(R.id.txtPassword);
        RegisterBtnOnClick();
    }
    public void onClickRegisterAnketa(View v){
        Intent intent = new Intent(this,AnketaActivity.class);
        startActivity(intent);
    }
    public void RegisterBtnOnClick(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                String[] projections = new String[] {
                        Table.User_table.NAME
                };
                String[] args = new String[]{
                        userName.getText().toString()
                };
                if ((dbHelper.getDataSelect(sqLiteDatabase, Table.User_table.TABLE_NAME,
                        projections, projections[0] + " = ?",args)).moveToFirst()){
                    Toast.makeText(getApplicationContext(), "Логін існує", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                dbHelper.addUser(userName.getText().toString(),password.getText().toString(),"",sqLiteDatabase);
                sqLiteDatabase.close();
                sqLiteDatabase = dbHelper.getReadableDatabase();
                dbHelper.logAllRecords(sqLiteDatabase, Table.User_table.TABLE_NAME);
                sqLiteDatabase.close();
                finish();
            }
        });
    }
}
