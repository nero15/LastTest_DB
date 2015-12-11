package com.example.mykola.lasttest.OtherClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

/**
 * Created by mykola on 07.12.15.
 */
public class Dialog_ extends DialogFragment {
    String title;
    String[] items;
    DialogInterface.OnClickListener clickListener;

    public void setTitleDialog(String title) {
        this.title = title;
    }

    public void setItemsDialog(String[] items) {
        this.items = items;
    }

    public void setClickListenerDialog(DialogInterface.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb= new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setItems(items, clickListener);
        return adb.create();
    }

    public void onCancel(DialogInterface dialogInterface){
        super.onCancel(dialogInterface);
    }
}
