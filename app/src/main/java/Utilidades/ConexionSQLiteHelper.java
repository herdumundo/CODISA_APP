package Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


   public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CODISA_INV.db";

    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE STKW002INV (winvd_nro_inv INTEGER , winvd_art TEXT ,ART_DESC TEXT, winvd_lote TEXT,winvd_fec_vto TEXT," +
                " winvd_area TEXT,winvd_dpto,winvd_secc,winvd_flia,winvd_grupo,winvd_cant_act,estado TEXT)");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva)
    {
        db.execSQL("DROP TABLE IF EXISTS STKW002INV");

         onCreate(db);
    }




}




