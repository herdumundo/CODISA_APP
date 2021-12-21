package Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


   public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "CODISA_INV.db";

    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE STKW002INV (winvd_nro_inv INTEGER, winvd_secu INTEGER  ," +
                "   winvd_cant_act INTEGER,winvd_cant_inv INTEGER,winvd_fec_vto TEXT,winve_fec TEXT," +
                "   ARDE_SUC INTEGER,  winvd_art TEXT ,art_desc TEXT, winvd_lote TEXT," +
                "   winvd_area INTEGER,area_desc TEXT, " +
                "   winvd_dpto INTEGER,dpto_desc TEXT," +
                "   winvd_secc INTEGER,secc_desc TEXT," +
                "   winvd_flia INTEGER,flia_desc TEXT," +
                "   winvd_grupo INTEGER,grup_desc TEXT, " +
                "   winvd_subgr INTEGER," +
                "   estado TEXT,WINVE_LOGIN_CERRADO_WEB TEXT," +
                "   tipo_toma TEXT,winve_login TEXT, winvd_consolidado TEXT," +
                "   desc_grupo_parcial TEXT,desc_familia TEXT, " +
                "   winve_dep TEXT, winve_suc TEXT,toma_registro TEXT, cod_barra TEXT,caja INTEGER,GRUESA INTEGER,UNID_IND INTEGER)");

        db.execSQL("CREATE TABLE USUARIOS_FORMULARIOS_SUCURSALES (FORMULARIO TEXT,NOMBRE TEXT ,LOGIN_O TEXT,LOGIN_PASS" +
                " TEXT,SUCURSAL_DESCRIPCION TEXT, ROL_SUCURSAL INTEGER)");
     }

    /*
    NOTA: INFORMES DE ESTADO EN STAW002INV
    A= PENDIENTE DE REALIZACION DEL INVENTARIO
    P= INVENTARIO REALIZADO PENDIENTE DE EXPORTACION AL SERVER CENTURY.
    R= INVENTARIO  PENDIENTE A REINVENTARIAR.
    F= INVENTARIO   REINVENTARIADO PENDIENTE A EXPORTAR.
    C= INVENTARIO EXPORTADO CON EXITO.
    E= INVENTARIO CANCELADO.


     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva)
    {
        db.execSQL("DROP TABLE IF EXISTS STKW002INV");
        db.execSQL("DROP TABLE IF EXISTS USUARIOS_FORMULARIOS_SUCURSALES");

         onCreate(db);
    }




}




