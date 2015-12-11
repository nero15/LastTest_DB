package com.example.mykola.lasttest.SQLiteDataBase.DataBaseReview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykola.lasttest.MapActivity;
import com.example.mykola.lasttest.OtherClass.Dialog_;
import com.example.mykola.lasttest.OtherClass.ImageManager_URL;
import com.example.mykola.lasttest.R;
import com.example.mykola.lasttest.SQLiteDataBase.DBHelper;
import com.example.mykola.lasttest.SQLiteDataBase.Table;
import com.google.android.gms.common.images.ImageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykola on 30.11.15.
 */
public class ListDataAdapter extends ArrayAdapter {
    List list = new ArrayList();
    FragmentManager fragmentManager;
    Context context;
    DBReviewActivity dbReviewActivity;
    public ListDataAdapter(Context context, int resource, FragmentManager fragmentManager, DBReviewActivity dbReviewActivity) {
        super(context, resource);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.dbReviewActivity = dbReviewActivity;
    }
    static class LayoutHandler{
        TextView ID_DB;
        TextView Name,Category,Mark,Location,WorkTime,Description;
        ImageView Image;
    }
    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View standart_obj = convertView;
        final LayoutHandler layoutHandler;
        if (standart_obj == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            standart_obj = layoutInflater.inflate(R.layout.standart_db_object,parent,false);
            layoutHandler= new LayoutHandler();
            layoutHandler.ID_DB = (TextView) standart_obj.findViewById(R.id.id_ObjectIdDB_Review);
            layoutHandler.Name = (TextView)standart_obj.findViewById(R.id.id_nameTxt_Review);
            layoutHandler.Category = (TextView)standart_obj.findViewById(R.id.id_CategoryTxt_Review);
            layoutHandler.Mark = (TextView) standart_obj.findViewById(R.id.id_markTxt_Review);
            layoutHandler.Image= (ImageView) standart_obj.findViewById(R.id.id_imageView_Review);
            layoutHandler.Location = (TextView) standart_obj.findViewById(R.id.id_LocationTxt_Review);
            layoutHandler.WorkTime = (TextView) standart_obj.findViewById(R.id.id_WorkTimeTxt_Review);
            layoutHandler.Description = (TextView) standart_obj.findViewById(R.id.id_DescriptionTxt_Review);
            standart_obj.setTag(layoutHandler);
        }
        else{
            layoutHandler = (LayoutHandler) standart_obj.getTag();
        }
        final DataProvider dataProvider = (DataProvider) this.getItem(position);
        standart_obj.setId(Integer.parseInt(dataProvider.getId_db()));
        layoutHandler.ID_DB.setText(dataProvider.getId_db());
        layoutHandler.Name.setText(dataProvider.getName());
        layoutHandler.Category.setText("Категорія " + dataProvider.getCategory());
        layoutHandler.Mark.setText("Оцінка " + dataProvider.getMark());
        layoutHandler.Location.setText("Місцезнаходження " + dataProvider.getLocation());
        layoutHandler.WorkTime.setText("Робочі години " + dataProvider.getWorkTime());
        layoutHandler.Description.setText("Опис: " + dataProvider.getDescription());
        ImageManager_URL imagaManager_url=new ImageManager_URL();
        imagaManager_url.fetchImage(dataProvider.getImageURL(), layoutHandler.Image);
        standart_obj.findViewById(R.id.id_lenearLayout_Review).setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Dialog_ newDialog = new Dialog_();
                newDialog.setTitleDialog("Обеіть дію");
                newDialog.setItemsDialog(new String[]{"Видалити", "Показати на карті"});
                newDialog.setClickListenerDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Review", String.valueOf(which));

                        switch (which) {
                            case 0:
                                SQLiteDatabase db;
                                DBHelper dbHelper = new DBHelper(context);
                                db = dbHelper.getWritableDatabase();
                                Integer x = Integer.parseInt(layoutHandler.ID_DB.getText().toString());
                                final int delete = db.delete(Table.Object_standart.TABLE_NAME, Table.Object_standart.ID + " = " + x, null);
                                Log.i("Review", String.valueOf(delete));
                                break;
                            case 1: {
                                String latitude, longitude, temp;
                                temp = dataProvider.getLocation().replace(context.getString(R.string.LONGITUDE) + " ", "");
                                longitude = temp.substring(0, temp.indexOf(" "));
                                temp = temp.replace(longitude + " \n" + context.getString(R.string.LATITUDE) + " ", "");
                                latitude = temp;//.substring(0, temp.indexOf("\n"));
                                Intent intent =  new Intent(context,MapActivity.class);
                                intent.setData(Uri.parse(dataProvider.getName() + ";"+longitude + ";" + latitude));
                                dbReviewActivity.startActivity(intent);
                                break;
                            }
                        }
                        dbReviewActivity.showObject("All");
                    }

                });
                newDialog.show(fragmentManager, "dialog");
                return true;

            }
        });

        return standart_obj;
    }
}
