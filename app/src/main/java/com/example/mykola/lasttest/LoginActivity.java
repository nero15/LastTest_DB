package com.example.mykola.lasttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.Table;

//import com.example.mykola.lasttest.SQLiteDataBase.DatabaseHandler;
//import com.example.mykola.lasttest.SQLiteDataBase.UsersDatabase;

/**
 * Created by natalia on 09.12.15.
 */
public class LoginActivity extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    Button loginBtn;
    Button SingButton;
    CheckBox stayInSysem;
    TextView newAcBtn;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    final String login_userName= "Login_userName", login_userPass = "Login_userPass", login_CB = "Login_CB";

    public void define(){
        usernameTxt = (EditText)findViewById(R.id.txtUsername);
        passwordTxt = (EditText)findViewById(R.id.txtPassword);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        newAcBtn = (TextView)findViewById(R.id.tvRegisterLink);
        stayInSysem = (CheckBox)findViewById(R.id.id_Login_CB);
        SingButton = (Button)findViewById(R.id.SingButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
////*******
//        sqLiteDatabase = dbHelper.getWritableDatabase();
//        dbHelper.repairDB(sqLiteDatabase, false, true, false, false);
//        sqLiteDatabase.close();
////*******
        define();
        NewAccountBtnOnClick();
        LoginBtnEnable();
        LoginBtnOnClick();
        load();
    }
    public void onClickLoginCB(View v){

    }
    public void Save(String name, String pass, Boolean CB){
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(login_userName, name);
        ed.putString(login_userPass, pass);
        ed.putBoolean(login_CB,CB);
        ed.commit();
        //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }
    public void load(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getPreferences(MODE_PRIVATE);
                usernameTxt.setText(sharedPreferences.getString(login_userName, ""));
                passwordTxt.setText(sharedPreferences.getString(login_userPass, ""));
                stayInSysem.setSelected(sharedPreferences.getBoolean(login_CB, false));
        //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        define();
        usernameTxt.setText("");
        passwordTxt.setText("");
    }
    public void NewAccountBtnOnClick(){
        newAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginToMain = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(LoginToMain);
            }
        });
    }
    public void LoginBtnOnClick(){
        sqLiteDatabase = dbHelper.getReadableDatabase();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.logAllRecords(sqLiteDatabase, Table.User_table.TABLE_NAME);
                String[] projection = new String[]{
                        Table.User_table.NAME,
                        Table.User_table.PASSWORD
                };
                String[] Args = new String[] {
                        usernameTxt.getText().toString(),
                        passwordTxt.getText().toString()
                };
                Cursor cursor = dbHelper.getDataSelect(sqLiteDatabase, Table.User_table.TABLE_NAME,projection,
                        Table.User_table.NAME + " = ? AND " + Table.User_table.PASSWORD + " = ? ",Args);
                if (cursor.moveToFirst()) {
                    if (stayInSysem.isChecked()) {
                        Save(usernameTxt.getText().toString(),passwordTxt.getText().toString(),true);
                    }
                    else{
                        Save("","",false);
                    }
                    Intent LoginToMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(LoginToMain);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Неправильний логін або пароль", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void SingButtonClick(View view){
        Intent LoginToMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(LoginToMain);
        finish();
    }
    public void LoginBtnEnable(){
        usernameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (String.valueOf(usernameTxt.getText()).trim().length() > 0) {
                    passwordTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            loginBtn.setEnabled(String.valueOf(passwordTxt.getText()).trim().length() > 0);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}