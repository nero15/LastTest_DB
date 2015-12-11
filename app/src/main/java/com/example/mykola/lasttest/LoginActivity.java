package com.example.mykola.lasttest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mykola.lasttest.SQLiteDataBase.DatabaseHandler;
//import com.example.mykola.lasttest.SQLiteDataBase.UsersDatabase;

import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by natalia on 09.12.15.
 */
public class LoginActivity extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    Button loginBtn;
    TextView newAcBtn;
    DBHelper dbHelper;// = new DBHelper(getApplicationContext());
    SQLiteDatabase sqLiteDatabase;// = dbHelper.getReadableDatabase();

//    DatabaseHandler dbHelper;
//    List<UsersDatabase> Users = new ArrayList<UsersDatabase>();


    public void define(){
        usernameTxt = (EditText)findViewById(R.id.txtUsername);
        passwordTxt = (EditText)findViewById(R.id.txtPassword);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        newAcBtn = (TextView)findViewById(R.id.tvRegisterLink);

        //dbHelper = new DatabaseHandler(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        define();
        NewAccountBtnOnClick();
        LoginBtnEnable();
        LoginBtnOnClick();
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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] projection = new String[]{
                        Table.User_table.NAME,
                        Table.User_table.PASSWORD
                };
                String[] Args = new String[] {
                        usernameTxt.getText().toString(),
                        passwordTxt.getText().toString()
                };
                Cursor cursor = dbHelper.getDataSelect(sqLiteDatabase, Table.User_table.TABLE_NAME,projection,
                        projection[0] + " = ? AND " + projection[1] + " = ? ",Args);
                if (cursor != null) {
                    Intent LoginToMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(LoginToMain);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Неправильний логін або пароль", Toast.LENGTH_LONG).show();
                }
            }
        });
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