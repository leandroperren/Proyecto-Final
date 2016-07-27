package edu.fich.pfcii.snoredetection.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Snore.db";
    public static final int SCHEMA_VERSION = 1;
    public static final String TABLE_NAME = "snore";
    public static final String ID = "id";
    public static final String HORA_INICIO = "hora_inicio";
    public static final String HORA_FIN = "hora_fin";
    public static final String T0 = "t0";
    public static final String AMPLITUD = "amplitud";
    public static final String TIEMPO = "tiempo";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " +
                ID + " integer primary key autoincrement, " +
                HORA_INICIO + " datetime not null, " +
                HORA_FIN + " datetime null, " +
                T0 + " text null, " +
                AMPLITUD + " text null, " +
                TIEMPO + " text null )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
