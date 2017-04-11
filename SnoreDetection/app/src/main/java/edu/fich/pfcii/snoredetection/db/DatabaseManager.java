package edu.fich.pfcii.snoredetection.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DatabaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    abstract public void cerrar();

    abstract long insertar(String fecha_inicio, String fecha_fin, String t0, String amplitud, String tiempo);
    abstract void actualizar(String id, String fecha_inicio, String fecha_fin, String t0, String amplitud, String tiempo);

    abstract public void eliminar(String id);
    abstract public void eliminarTodo();
    abstract public Cursor cargarCursor();
    abstract Boolean compruebaRegistro(String id);

    /**
     * All Getters ans Setters
     * @return
     */
    public DatabaseHelper getHelper() {
        return helper;
    }

    public void setHelper(DatabaseHelper helper) {
        this.helper = helper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
}
