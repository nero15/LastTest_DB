package com.example.mykola.lasttest.SQLiteDataBase;

import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by mykola on 30.11.15.
 */
public class Table {
    public Table(){}
    public static abstract class DefaultValue{
        public static final String CATEGORY = "other";
        public static final String IMAGE_URL = "";
        public static final String SPECIAL_FIELD = "no information";
    }

    public static abstract class Object_standart{
        public static final String TABLE_NAME = "ObjectTabV2";

        public static final String ID = "id";
        public static final String IMAGE_URL = "image_url";
        public static final String NAME = "name";
        public static final String CATEGORY = "category";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String DESCRIPTION = "description";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String WEBSITE = "website";
        public static final String MARK = "mark";
        public static final String WORK_TIME = "work_time";
        public static final String COSTS = "costs";

        public static final String[] allField = new String[]{
                Object_standart.NAME,
                Object_standart.IMAGE_URL,
                Object_standart.CATEGORY,
                Object_standart.LONGITUDE,
                Object_standart.LATITUDE,
                Object_standart.DESCRIPTION,
                Object_standart.PHONE_NUMBER,
                Object_standart.WEBSITE,
                Object_standart.MARK,
                Object_standart.WORK_TIME,
                Object_standart.COSTS
        };

        public static final String CREATE_OBJECT=
                "CREATE TABLE IF NOT EXISTS " +  Object_standart.TABLE_NAME + " (" +
                        Object_standart.ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                        Object_standart.IMAGE_URL + " TEXT NOT NULL DEFAULT \""+ DefaultValue.IMAGE_URL+"\", " +
                        Object_standart.NAME + " TEXT NOT NULL, " +
                        Object_standart.CATEGORY + " TEXT NOT NULL DEFAULT \""+ DefaultValue.CATEGORY +"\", " +
                        Object_standart.LONGITUDE + " REAL NOT NULL, " +
                        Object_standart.LATITUDE + " REAL NOT NULL, " +
                        Object_standart.DESCRIPTION + " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\", " +
                        Object_standart.PHONE_NUMBER + " TEXT," +
                        Object_standart.WEBSITE + " TEXT, " +
                        Object_standart.MARK + " REAL," +
                        Object_standart.WORK_TIME + " TEXT, " +
                        Object_standart.COSTS + " TEXT ) ;";
    }

    public static abstract class Cinema_table{
        public static final String TABLE_NAME = "CinemaTab";
        public static BitmapDescriptor COLOR_MARKER =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        public static final String ID = "id";
        public static final String TECHNOLOGY = "technology";

        public static final String CREATE_CINEMA =
                "CREATE TABLE IF NOT EXISTS " +  Table.Cinema_table.TABLE_NAME + " (" +
                        Table.Cinema_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Table.Cinema_table.TECHNOLOGY + " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\") ;";
    }

    public static abstract class Cafe_table{
        public static final String TABLE_NAME = "CafeTab";
        public static BitmapDescriptor COLOR_MARKER =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        public static final String ID = "id";
        public static final String CUISINE = "cuisine";

        public static final String CREATE_CAFE =
                "CREATE TABLE IF NOT EXISTS " +  Table.Cafe_table.TABLE_NAME + " (" +
                        Table.Cafe_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Table.Cafe_table.CUISINE + " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";

    }

    public static abstract class Hotel_table{
        public static final String TABLE_NAME = "HotelTab";
        public static BitmapDescriptor COLOR_MARKER =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);

        public static final String ID = "id";
        public static final String STARS = "stars";

        public static final String CREATE_HOTEL =
                "CREATE TABLE IF NOT EXISTS " +  Table.Hotel_table.TABLE_NAME + " (" +
                        Table.Hotel_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Hotel_table.STARS + " TEXT DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";

    }

    public static abstract class Theatre_table{
        public static final String TABLE_NAME = "theatreTab";
        public static final String ID = "id";
        public static final String REPERTOIRE = "repertoire";

        public static final String CREATE_THEATRE =
                "CREATE TABLE IF NOT EXISTS " + Table.Theatre_table.TABLE_NAME + " (" +
                        Table.Theatre_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Theatre_table.REPERTOIRE + " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";
    }

    public static abstract class Shop_table{
        public static final String TABLE_NAME = "shopTab";
        public static final String ID = "id";
        public static final String DESCRIPTION = "description";

        public static final String CREATE_SHOP =
                "CREATE TABLE IF NOT EXISTS " + Table.Shop_table.TABLE_NAME + " (" +
                        Table.Shop_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Shop_table.DESCRIPTION+ " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";
    }

    public static abstract class Museum_table{
        public static final String TABLE_NAME = "museumTab";
        public static final String ID = "id";
        public static final String DESCARIPTION = "description";

        public static final String CREATE_MUSEUM =
                "CREATE TABLE IF NOT EXISTS " + Table.Museum_table.TABLE_NAME + " (" +
                    Table.Museum_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                    Museum_table.DESCARIPTION+ " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";
    }

    public static abstract class Monuments_table{
        public static final String TABLE_NAME = "monumentTab";
        public static final String ID = "id";
        public static final String DESCARIPTION = "description";

        public static final String CREATE_MONUMENT =
                "CREATE TABLE IF NOT EXISTS " + Table.Monuments_table.TABLE_NAME + " (" +
                    Table.Monuments_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                    Monuments_table.DESCARIPTION+ " TEXT NOT NULL DEFAULT \""+ DefaultValue.SPECIAL_FIELD +"\" ) ;";
    }

    /* notebooks */

    public static abstract class Notebook_table{
        public static final String TABLE_NAME = "NotebooksTab";

        public static final String ID = "id";
        public static final String TIME= "time";
        public static final String DATA= "data";
        public static final String GEODATA = "geodata";

        public static final String CREATE_NOTEBOOK=
                "CREATE TABLE IF NOT EXISTS " +  Notebook_table.TABLE_NAME + " (" +
                        Notebook_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Notebook_table.TIME + " TEXT NOT NULL," +
                        Notebook_table.DATA + " TEXT NOT NULL, " +
                        Notebook_table.GEODATA + "INTEGER NOT NULL DEFAULT 0);";

    }
    /* user table */

    public static abstract class User_table{
        public static final String TABLE_NAME = "UserTab";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String META_DATA = "metaData";

        public static final String CREATE_USER=
                "CREATE TABLE IF NOT EXISTS " +  User_table.TABLE_NAME + " (" +
                        User_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        User_table.NAME + " TEXT NOT NULL," +
                        User_table.PASSWORD + " TEXT NOT NULL," +
                        User_table.META_DATA + " TEXT );";

    }

    /* waybill */

    public static abstract class Waybill_table{
        public static final String TABLE_NAME = "WaybillTab";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String META_DATA = "meta data";

        public static final String CREATE_WAYBILL =
                "CREATE TABLE IF NOT EXISTS " +  Waybill_table.TABLE_NAME + " (" +
                        Waybill_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Waybill_table.NAME + " TEXT NOT NULL," +
                        Waybill_table.META_DATA + " TEXT NOT NULL);";
        // 23.56;45.67;\n45.665;34.5657;
    }
    /* Anketa */
    public static abstract  class Question_table{
        public static final String TABLE_NAME = "QuestionTab";

        public static final String ID = "id";
        public static final String QUESTION = "question";
        public static final String ANSWER = "ANSWER";
        public static final String[] allField = new String[]{
                Question_table.QUESTION,
                Question_table.ANSWER
        };
        public static final String CREATE_QUESTION =
                "CREATE TABLE IF NOT EXISTS " +  Question_table.TABLE_NAME + " (" +
                        Question_table.ID + " integer PRIMARY KEY AUTOINCREMENT," +
                        Question_table.QUESTION + " TEXT NOT NULL," +
                        Question_table.ANSWER + " TEXT NOT NULL);";
        // answer1;answer2;
    }
}
