package edu.fich.pfcii.snoredetection.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import edu.fich.pfcii.snoredetection.helper.Helper;
import edu.fich.pfcii.snoredetection.model.Snore;

public class DatabaseManagerSnore extends DatabaseManager {

    private Helper helper = new Helper();

    private static final String TABLE_NAME = "snore";
    private static final String ID = "id";
    private static final String HORA_INICIO = "hora_inicio";
    private static final String HORA_FIN = "hora_fin";
    private static final String T0 = "t0";
    private static final String AMPLITUD = "amplitud";
    private static final String TIEMPO = "tiempo";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + ID + " integer PRIMARY KEY AUTOINCREMENT, "
            + HORA_INICIO + " integer NOT NULL, "
            + HORA_FIN + " integer NULL, "
            + T0 + " varchar(512) NULL, "
            + AMPLITUD + " varchar(512) NULL, "
            + TIEMPO + " varchar(1024) NULL"
            + ");";


    public DatabaseManagerSnore(Context context) {
        super(context);
    }

    @Override
    public void cerrar() {
        super.getDb().close();
    }

    private ContentValues generarContentValues(String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo) {
        ContentValues valores = new ContentValues();
        valores.put(HORA_INICIO, hora_inicio);
        valores.put(HORA_FIN, hora_fin);
        valores.put(T0, t0);
        valores.put(AMPLITUD, amplitud);
        valores.put(TIEMPO, tiempo);

        return valores;
    }

    @Override
    public long insertar(String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo) {
        return super.getDb().insert(TABLE_NAME, null, generarContentValues(hora_inicio, hora_fin, t0, amplitud, tiempo));
    }

    @Override
    public void actualizar(String id, String hora_inicio, String hora_fin, String t0, String amplitud, String tiempo) {
        ContentValues valores = new ContentValues();
        valores.put(ID, id);
        valores.put(HORA_INICIO, hora_inicio);
        valores.put(HORA_FIN, hora_fin);
        valores.put(T0, t0);
        valores.put(AMPLITUD, amplitud);
        valores.put(TIEMPO, tiempo);

        String[] args = new String[] {id};

        super.getDb().update(TABLE_NAME, valores, ID + "=?", args);
    }

    @Override
    public void eliminar(String id) {
        super.getDb().delete(TABLE_NAME, ID + "=?", new String[]{id});
    }

    @Override
    public void eliminarTodo() {
        super.getDb().execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

    @Override
    public Cursor cargarCursor() {
        String[] columnas = new String[] { ID, HORA_INICIO, HORA_FIN, T0, AMPLITUD, TIEMPO };

        return super.getDb().query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    @Override
    Boolean compruebaRegistro(String id) {
        boolean existe = true;

        Cursor resultSet = super.getDb().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id, null);
        if (resultSet.getCount() <= 0) {
            existe = false;
        }

        return existe;
    }

    /**
     * Creamos un ArrayList de tipo Snore donde guardamos todos los registros de la DB
     *
     */
    public ArrayList<Snore> getSnoreList() {
        ArrayList<Snore> list = new ArrayList<>();
        // Cargar el curson con todos los datos de la DB
        Cursor c = cargarCursor();
        // Recorrer el cursor y cargar la lista con los objetos
        if (c != null) {
            while (c.moveToNext()) {
                Snore snore = new Snore();
                // Setear el objeto con todos los valores obtenidos desde la DB
                snore.setId(c.getString(0));
                snore.setHora_inicio(c.getInt(1));
                snore.setHora_fin(c.getInt(2));
                snore.setT0(helper.getDoubleFromString(c.getString(3)));
                snore.setAmplitud(helper.getDoubleFromString(c.getString(4)));
                snore.setTiempo(helper.getIntegerFromString(c.getString(5)));
                // Cargar este objeto seteado en la lista
                list.add(snore);
            }
        }
        return list;
    }
}
