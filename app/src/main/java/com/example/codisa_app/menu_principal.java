package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import Utilidades.Connection_Oracle;
import Utilidades.controles;
import Utilidades.variables;
import maes.tech.intentanim.CustomIntent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class menu_principal extends AppCompatActivity {
    public static ProgressDialog prodialog,ProDialogExport;
    public  static TextView txt_total;
    CardView tomasGen;
    AlertDialog.Builder builder;
    AlertDialog ad;
    String mensajeRespuesta="";
    int error_importador=1;
    Connection connect;

    static int   ContProgressBarImportador=0;
      String mensajeImporError="";
    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, login.class,"¿Desea salir de la aplicación?",3);
    }
    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollmenu);
        txt_total=findViewById(R.id.txttotalpendiente);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txt1 = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        TextView txt2 = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title2);
        txt2.setVisibility(View.VISIBLE);
        txt1.setText("Usuario:       "+variables.NOMBRE_LOGIN);
        txt2.setText("Sucursal:     "+variables.DESCRIPCION_SUCURSAL_LOGIN);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));


       // controles.conexion_sqlite(this);
        controles.ConsultarPendientesExportar();
        controles.context_menuPrincipal=this;
        tomasGen=findViewById(R.id.tomasGen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String[] array_opciones=variables.contenedor_menu.split(",");

        for(int i=0; i<array_opciones.length; i++)
        {
            String id=array_opciones[i];
            if(id.equals("STKW001")){
                tomasGen.setVisibility(View.VISIBLE);
            }
            int ID_BOTONES = getResources().getIdentifier(array_opciones[i], "id", getPackageName());
            CardView stock = (findViewById(ID_BOTONES));

            stock.setVisibility(View.VISIBLE);
        }

    }

    public void OnclickIrStkw002(View v){
        variables.tipoListaStkw002=1;
        variables.tipoStkw002=2;
        Intent i=new Intent(this,lista_stkw002_inv.class);
        startActivity(i);
        CustomIntent.customType(menu_principal.this,"left-to-right");
    }

    public void OnclickIrStkw001(View v){
        builder = new AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_danger));
        builder.setTitle("¡Atención!");
        builder.setMessage("Seleccione el tipo de toma que desea generar.");
        builder.setPositiveButton("Manual", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="TOMA MANUAL";
                        variables.tipo_stkw001=1;
                        variables.tipo_stkw001_insert="M";

                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                        startActivity(intent);
                        CustomIntent.customType(menu_principal.this,"left-to-right");

                    }
                });
        builder.setNeutralButton("Por criterio de selección",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="TOMA POR CRITERIO DE SELECCION";
                        variables.tipo_stkw001=2;
                        variables.tipo_stkw001_insert="C";
                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                        startActivity(intent);
                        CustomIntent.customType(menu_principal.this,"left-to-right");

                    }
                });
        ad = builder.show();
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        ad.getButton(AlertDialog.BUTTON_NEUTRAL).setAllCaps(false);


    }

    public void OnclickIrStkw001Cancelacion(View v){

        Intent intent = new Intent(this, lista_stkw001_inv.class);
        finish();
        startActivity(intent);
        CustomIntent.customType(menu_principal.this,"left-to-right");

    }

    public void OnclickExportar( View v){
        try {

            controles.ExportarStkw002();

        }
        catch (Exception e){
            new androidx.appcompat.app.AlertDialog.Builder( this)
                    .setTitle("INFORME!!!")
                    .setMessage(e.toString()).show();

        }
    }

    public void OnclickListadoConssolidado( View v){
        Intent intent = new Intent(this, stkw003.class);
        finish();
        startActivity(intent);
        CustomIntent.customType(menu_principal.this,"left-to-right");

    }

    public void OnclickConsultaArticulos( View v){
        Intent intent = new Intent(this, stkw004.class);
        finish();
        startActivity(intent);
        CustomIntent.customType(menu_principal.this,"left-to-right");

    }

    public void OnclickSincronizarDatos(View v){
        final HiloSincronizar task = new HiloSincronizar();
        task.execute();
    }





    public   class HiloSincronizar extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prodialog = ProgressDialog.show(menu_principal.this, "PROCESANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                String query ="""
                       select count(*) as contador FROM  ( 
                                SELECT DISTINCT winvd_art,WINVD_NRO_INV
                                       from  
                                       WEB_INVENTARIO a   
                                       inner join WEB_INVENTARIO_det b on a.winve_numero=b.winvd_nro_inv    
                                       where a.WINVE_ESTADO_WEB='A' and a.winve_empr=1 AND a.winve_suc="""+variables.ID_SUCURSAL_LOGIN+""" 
                                       AND WINVE_FEC > TO_DATE('2024-12-22 08:48:09', 'YYYY-MM-DD HH24:MI:SS')
                                       
                                      /* UNION ALL  
                                       select '1' AS CONT from web_stk_carga_inv a   
                                       inner join web_stk_carga_inv_det b on a.inve_numero=b.invd_nro_inv and a.inve_empr=1 and 
                                       a.inve_suc="""+variables.ID_SUCURSAL_LOGIN+"""
                                        AND a.invew_est='R' 
                                       and UPPER(inve_login)=UPPER('"""+variables.userdb+"')*/)";

                error_importador=1;
               Connection_Oracle conexion = new Connection_Oracle();
               connect = conexion.Connections();

               Statement stmt = connect.createStatement();
               ResultSet rs = stmt.executeQuery(query);


               while (rs.next())
               {
                   ContProgressBarImportador=rs.getInt("contador");
               }
               rs.close();
               error_importador=0;


           }
           catch (Exception e){

               mensajeImporError=e.getMessage();

           }
           finally {
                try {
                    connect.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (error_importador==0){//SI NO HAY ERRORES

                builder = new AlertDialog.Builder(menu_principal.this);
                builder.setIcon(getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("Sincronización de tomas.");
                builder.setMessage("¿Desea importar las tomas disponibles?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prodialog =  new ProgressDialog( menu_principal.this);
                        prodialog.setMax(ContProgressBarImportador);
                        LayerDrawable progressBarDrawable = new LayerDrawable(
                                new Drawable[]{
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                        new ClipDrawable(
                                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                        new int[]{Color.parseColor("blue"),Color.parseColor("blue")}),
                                                Gravity.START,
                                                ClipDrawable.HORIZONTAL),
                                        new ClipDrawable(
                                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                        new int[]{Color.parseColor("blue"),Color.parseColor("blue")}),
                                                Gravity.START,
                                                ClipDrawable.HORIZONTAL)
                                });
                        progressBarDrawable.setId(0,android.R.id.background);
                        progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                        progressBarDrawable.setId(2,android.R.id.progress);
                        prodialog.setTitle("Sincronizando tomas.");
                        prodialog.setMessage("Favor espere...");
                        prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prodialog.setProgressDrawable(progressBarDrawable);
                        prodialog.show();

                        prodialog.setCanceledOnTouchOutside(false);
                        prodialog.setCancelable(false);
                        final AsyncImportador task = new  AsyncImportador();
                        task.execute();

                    }
                });
                builder.setNegativeButton("No",null);
                ad = builder.show();

                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.azul_claro));
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

            }

            else {
                controles.VerificarRed(menu_principal.this);

            }


            prodialog.dismiss();
        }
    }

    private void InsertarSqliteToma(){
        try {
            SQLiteDatabase db1= controles.conSqlite.getReadableDatabase();
            db1.execSQL("delete from STKW002INV  WHERE estado IN ('A','C','E')");
            db1.close();
            Connection_Oracle conexion = new Connection_Oracle();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();

            ResultSet rs = stmt.executeQuery("""  
                    SELECT  
                        'A' AS toma,
                        0 AS invd_cant_inv,
                        ART_DESC,
                        ARDE_SUC,
                        winvd_nro_inv,
                        winvd_art,
                        '' AS winvd_lote,
                        '' AS winvd_fec_vto,
                        winvd_area,
                        winvd_dpto,
                        winvd_secc,
                        winvd_flia,
                        winvd_grupo,
                        0 AS winvd_cant_act,
                        winve_fec,
                        dpto_desc,
                        secc_desc,
                        flia_desc,
                        grup_desc,
                        area_desc,
                        sugr_codigo,
                        '' AS winvd_secu,
                        CASE 
                            WHEN c.winve_tipo_toma = 'C' THEN 'CRITERIO'
                            ELSE 'MANUAL' 
                        END AS tipo_toma,
                        winve_login,
                        '' AS winvd_consolidado,
                        CASE 
                            WHEN c.winve_grupo IS NULL AND c.winve_grupo_parcial IS NULL THEN 'TODOS'
                            WHEN c.winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' 
                            ELSE grup_desc 
                        END AS desc_grupo_parcial,
                        CASE 
                            WHEN c.winve_flia IS NULL THEN 'TODAS'
                            ELSE a.flia_desc 
                        END AS desc_familia,
                        winve_dep,
                        winve_suc,
                        a.coba_codigo_barra,
                        a.caja,
                        a.GRUESA,
                        a.UNID_IND,
                        suc.SUC_DESC,
                        suc.DEP_DESC
                    FROM V_WEB_ARTICULOS_CLASIFICACION a
                    INNER JOIN WEB_INVENTARIO_det b 
                        ON a.ART_CODIGO = b.winvd_art 
                        AND a.SECC_CODIGO = b.winvd_secc
                    INNER JOIN WEB_INVENTARIO c 
                        ON b.winvd_nro_inv = c.winve_numero 
                        AND c.winve_dep = a.ARDE_DEP 
                        AND c.winve_area = a.AREA_CODIGO 
                        AND c.winve_suc = a.ARDE_SUC 
                        AND c.winve_secc = a.SECC_CODIGO
                    INNER JOIN V_WEB_SUC_DEP suc 
                        --ON c.winve_suc = suc.SUC_CODIGO 
                        --AND c.winve_dep = suc.DEP_CODIGO
                        ON  suc.SUC_CODIGO                = a.ARDE_SUC
                        AND suc.DEP_CODIGO                = a.ARDE_DEP
                    WHERE 
                        c.winve_empr = 1 
                        AND a.ARDE_SUC = """+variables.ID_SUCURSAL_LOGIN+""" 
                        AND c.WINVE_ESTADO_WEB = 'A' 
                        AND c.WINVE_FEC > TO_DATE('2024-12-22 08:48:09', 'YYYY-MM-DD HH24:MI:SS')
                    GROUP BY 
                        ARDE_SUC, winvd_nro_inv,winvd_art,winvd_area,winvd_dpto,winvd_secc,winve_suc, 
                        winvd_flia, winvd_grupo,winve_fec,dpto_desc,secc_desc,flia_desc,grup_desc, 
                        area_desc, sugr_codigo,winve_grupo,winve_tipo_toma,winve_login,winve_grupo_parcial, 
                        winve_flia, winve_dep,ART_DESC,a.coba_codigo_barra,a.caja,a.GRUESA,a.UNID_IND,suc.SUC_DESC,suc.DEP_DESC                     
                       UNION ALL
                        SELECT 
                            'R' as toma,0 AS invd_cant_inv,ART_DESC,  ARDE_SUC,a.inve_numero as winvd_nro_inv,b.ARTICULO as winvd_art,'' AS winvd_lote,'' AS winvd_fec_vto,
                            c.AREA_CODIGO as winvd_area,d.winve_dpto as winvd_dpto,d.winve_secc as winvd_secc,c.FLIA_CODIGO as winvd_flia,0 AS winvd_grupo,
                            0 as winvd_cant_act,a.inve_fec as winve_fec, c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC,
                            c.GRUP_DESC, c.AREA_DESC,c.SUGR_CODIGO as sugr_codigo,'' as winvd_secu,
                            case d.winve_tipo_toma when 'C' then 'CRITERIO' ELSE 'MANUAL' END AS tipo_toma,a.inve_login as winve_login,''  AS winvd_consolidado ,
                            case when d.winve_grupo IS NULL and  d.winve_grupo_parcial IS NULL then 'TODOS'
                            WHEN d.winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' ELSE grup_desc END AS desc_grupo_parcial,
                            case when d.winve_flia is null then 'TODAS' else c.flia_desc end as desc_familia,winve_dep,winve_suc, c.coba_codigo_barra,
                            c.caja,c.GRUESA  ,c.UNID_IND,suc.SUC_DESC,suc.DEP_DESC
                        FROM
                                        web_stk_carga_inv   a
                            INNER JOIN  V_WEB_ART_CONS_DIF  b on a.inve_numero=b.NRO_CARGA
                            INNER JOIN  V_WEB_ARTICULOS_CLASIFICACION c on b.ARTICULO=c.ART_CODIGO
                            INNER JOIN  WEB_INVENTARIO      d on a.inve_ref=d.winve_numero and d.winve_suc=c.ARDE_SUC
                            INNER JOIN  V_WEB_SUC_DEP       suc on d.winve_suc=suc.SUC_CODIGO and d.winve_dep=suc.DEP_CODIGO 
                        WHERE
                            a.invew_est='R' and d.winve_suc="""+variables.ID_SUCURSAL_LOGIN+""" 
                                and  UPPER(inve_login)=UPPER('"""+variables.userdb+"""
                            ') 
                        GROUP BY 
                            ARDE_SUC,a.inve_numero ,b.ARTICULO  ,
                            c.AREA_CODIGO  ,d.winve_dpto  ,d.winve_secc  ,c.FLIA_CODIGO ,
                            a.inve_fec , c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC,c.GRUP_DESC,d.winve_tipo_toma,
                            c.AREA_DESC,a.inve_login,d.winve_grupo_parcial,d.winve_grupo,d.winve_flia,c.flia_desc,winve_dep,
                            winve_suc,c.SUGR_CODIGO,ART_DESC, c.coba_codigo_barra,c.caja,c.GRUESA  ,c.UNID_IND,suc.SUC_DESC,suc.DEP_DESC  """);


            int i=1;
            int contadorMensaje=0;
            // Abre la base de datos una sola vez
            SQLiteDatabase db = controles.conSqlite.getWritableDatabase();
            db.beginTransaction(); // Inicia una transacción para mejorar rendimiento

            try {
                // Prepara la consulta SELECT para verificar existencia
                String selectQuery = "SELECT 1 FROM STKW002INV WHERE winvd_nro_inv = ? AND estado IN ('P', 'F')";
                SQLiteStatement selectStmt = db.compileStatement(selectQuery);

                // Prepara la consulta INSERT con placeholders
                String insertQuery = "INSERT INTO STKW002INV (" +
                        "ARDE_SUC, winvd_nro_inv, winvd_art, ART_DESC, winvd_lote, winvd_fec_vto, winvd_area, winvd_dpto, winvd_secc, " +
                        "winvd_flia, winvd_grupo, winvd_cant_act, winve_fec, dpto_desc, secc_desc, flia_desc, grup_desc, area_desc, " +
                        "winvd_cant_inv, winvd_subgr, winvd_secu, estado, tipo_toma, winve_login, winvd_consolidado, desc_grupo_parcial, " +
                        "desc_familia, winve_dep, winve_suc, toma_registro, cod_barra, caja, GRUESA, UNID_IND, sucursal, deposito" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                SQLiteStatement insertStmt = db.compileStatement(insertQuery);

                // Itera sobre el ResultSet externo
                while (rs.next()) {
                    // Obtiene el valor para verificar existencia
                    int winvd_nro_inv = rs.getInt("winvd_nro_inv");

                    // Verifica si ya existe el registro
                    selectStmt.clearBindings();
                    selectStmt.bindLong(1, winvd_nro_inv);

                    boolean exists = false;
                    try {
                        exists = (selectStmt.simpleQueryForLong() > 0);
                    } catch (android.database.sqlite.SQLiteDoneException e) {
                        // No existe el registro, continúa con el INSERT
                    }

                    if (!exists) {
                        // Si no existe, realiza la inserción
                        insertStmt.clearBindings();

                        insertStmt.bindLong(1, rs.getInt("ARDE_SUC")); // Si es seguro que nunca será null
                        insertStmt.bindLong(2, winvd_nro_inv); // Si es seguro que nunca será null

// Para los valores que pueden ser null
                        insertStmt.bindString(3, rs.getString("winvd_art") != null ? rs.getString("winvd_art") : "");
                        insertStmt.bindString(4, rs.getString("ART_DESC") != null ? rs.getString("ART_DESC").replaceAll("'", "") : "");
                        insertStmt.bindString(5, rs.getString("winvd_lote") != null ? rs.getString("winvd_lote") : "");
                        insertStmt.bindString(6, rs.getString("winvd_fec_vto") != null ? rs.getString("winvd_fec_vto") : "");
                        insertStmt.bindString(7, rs.getString("winvd_area") != null ? rs.getString("winvd_area") : "");
                        insertStmt.bindString(8, rs.getString("winvd_dpto") != null ? rs.getString("winvd_dpto") : "");
                        insertStmt.bindString(9, rs.getString("winvd_secc") != null ? rs.getString("winvd_secc") : "");
                        insertStmt.bindString(10, rs.getString("winvd_flia") != null ? rs.getString("winvd_flia") : "");
                        insertStmt.bindString(11, rs.getString("winvd_grupo") != null ? rs.getString("winvd_grupo") : "");
                        insertStmt.bindString(12, rs.getString("winvd_cant_act") != null ? rs.getString("winvd_cant_act") : "");
                        insertStmt.bindString(13, rs.getString("winve_fec") != null ? rs.getString("winve_fec") : "");
                        insertStmt.bindString(14, rs.getString("dpto_desc") != null ? rs.getString("dpto_desc") : "");
                        insertStmt.bindString(15, rs.getString("secc_desc") != null ? rs.getString("secc_desc") : "");
                        insertStmt.bindString(16, rs.getString("flia_desc") != null ? rs.getString("flia_desc") : "");
                        insertStmt.bindString(17, rs.getString("grup_desc") != null ? rs.getString("grup_desc") : "");
                        insertStmt.bindString(18, rs.getString("area_desc") != null ? rs.getString("area_desc") : "");
                        insertStmt.bindString(19, rs.getString("invd_cant_inv") != null ? rs.getString("invd_cant_inv") : "");
                        insertStmt.bindString(20, rs.getString("sugr_codigo") != null ? rs.getString("sugr_codigo") : "");
                        insertStmt.bindString(21, rs.getString("winvd_secu") != null ? rs.getString("winvd_secu") : "");
                        insertStmt.bindString(22, "A"); // Estado no será null
                        insertStmt.bindString(23, rs.getString("tipo_toma") != null ? rs.getString("tipo_toma") : "");
                        insertStmt.bindString(24, rs.getString("winve_login") != null ? rs.getString("winve_login") : "");
                        insertStmt.bindString(25, rs.getString("winvd_consolidado") != null ? rs.getString("winvd_consolidado") : "");
                        insertStmt.bindString(26, rs.getString("desc_grupo_parcial") != null ? rs.getString("desc_grupo_parcial") : "");
                        insertStmt.bindString(27, rs.getString("desc_familia") != null ? rs.getString("desc_familia") : "");
                        insertStmt.bindString(28, rs.getString("winve_dep") != null ? rs.getString("winve_dep") : "");
                        insertStmt.bindString(29, rs.getString("winve_suc") != null ? rs.getString("winve_suc") : "");
                        insertStmt.bindString(30, rs.getString("toma") != null ? rs.getString("toma") : "");
                        insertStmt.bindString(31, rs.getString("coba_codigo_barra") != null ? rs.getString("coba_codigo_barra") : "");
                        insertStmt.bindString(32, rs.getString("caja") != null ? rs.getString("caja") : "");
                        insertStmt.bindString(33, rs.getString("GRUESA") != null ? rs.getString("GRUESA") : "");
                        insertStmt.bindString(34, rs.getString("UNID_IND") != null ? rs.getString("UNID_IND") : "");
                        insertStmt.bindString(35, rs.getString("SUC_DESC") != null ? rs.getString("SUC_DESC") : "");
                        insertStmt.bindString(36, rs.getString("DEP_DESC") != null ? rs.getString("DEP_DESC") : "");


                        insertStmt.executeInsert(); // Ejecuta la inserción
                    }

                    // Actualiza progreso cada 100 iteraciones
                    if (i % 100 == 0) {
                        prodialog.setProgress(i);
                    }                    i++;
                }
                contadorMensaje=1;
                // Finaliza la transacción
                db.setTransactionSuccessful();
            } catch (Exception e) {
                mensajeRespuesta=e.toString();
                e.printStackTrace(); // Maneja errores si ocurre algo inesperado
            } finally {
                db.endTransaction(); // Finaliza la transacción (éxito o error)
                db.close(); // Cierra la base de datos
            }

            if(contadorMensaje==0){
                mensajeRespuesta="No se encontraron registros por importar.";
            }
            else {
                mensajeRespuesta="Datos sincronizados correctamente.";

            }

        rs.close();
        prodialog.dismiss();
        }
        catch (Exception e)
        {
            mensajeRespuesta=e.toString();
           // Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            prodialog.dismiss();
        }
        finally {
            try {
                connect.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class AsyncImportador extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            InsertarSqliteToma();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


                    builder = new AlertDialog.Builder(menu_principal.this);
            builder.setIcon(getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("¡Atención!");
            builder.setMessage(mensajeRespuesta);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            super.onPostExecute(result);
        }
    }


}