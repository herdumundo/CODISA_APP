package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.ECField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Utilidades.Stkw002List;
import Utilidades.controles;
import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class menu_principal extends AppCompatActivity {
    public static ProgressDialog prodialog,ProDialogExport;
   public  static TextView txt_total;
    CardView tomasGen;
    int error_importador=1;
//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 TOKEN

    static int   ContProgressBarImportador=0;
      String mensajeImporError="";
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this, com.example.codisa_app.login.class,"DESEA SALIR DE LA APLICACION?",3);
    }
    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        txt_total=findViewById(R.id.txttotalpendiente);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txt1 = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        TextView txt2 = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title2);
        txt2.setVisibility(View.VISIBLE);
        txt1.setText("USUARIO:"+variables.NOMBRE_LOGIN);
        txt2.setText("SUCURSAL:"+variables.DESCRIPCION_SUCURSAL_LOGIN);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
     /*   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

*/







        controles.conexion_sqlite(this);
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
    }

    public void OnclickIrStkw001(View v){


        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCIÓN!!!.")
                .setMessage("SELECCIONE EL TIPO DE TOMA QUE DESEA GENERAR")
                .setPositiveButton("MANUAL", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="TOMA MANUAL";
                        variables.tipo_stkw001=1;
                        variables.tipo_stkw001_insert="M";

                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .setNeutralButton("POR CRITERIO DE SELECCIÓN",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="TOMA POR CRITERIO DE SELECCION";
                        variables.tipo_stkw001=2;
                        variables.tipo_stkw001_insert="C";
                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .show();

    }

    public void OnclickIrStkw001Cancelacion(View v){


        Intent intent = new Intent(this, lista_stkw001_inv.class);
        finish();
        startActivity(intent);

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

    public void OnclickSincronizarDatos(View v){

        final async task = new async();
        task.execute();
      //ImportarTomas();


    }

    public   class async extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prodialog = ProgressDialog.show(menu_principal.this, "PROCESANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
           try {
               error_importador=1;
               controles.connect = controles.conexion.Connections();
               Statement stmt = controles.connect.createStatement();
               ResultSet rs = stmt.executeQuery("" +
                       "   SELECT " +
                       "       count(*) as  contador " +
                       "   FROM   " +
                       "       V_WEB_ARTICULOS_CLASIFICACION  a   " +
                       "       inner join WEB_INVENTARIO_det b on a.arde_lote=b.winvd_lote  " +
                       "       and a.ART_CODIGO=b.winvd_art       " +
                       "       and a.SECC_CODIGO=b.winvd_secc     " +
                       "       and a.ARDE_FEC_VTO_LOTE=b.winvd_fec_vto   " +
                       "       inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  " +
                       "       and c.winve_dep=a.ARDE_DEP  " +
                       "       and c.winve_area=a.AREA_CODIGO  " +
                       "       and c.winve_suc=a.ARDE_SUC   " +
                       "       and c.winve_secc=a.SECC_CODIGO  " +
                       "   where " +
                       "       c.winve_empr=1 " +
                       "       and a.ARDE_SUC="+variables.ID_SUCURSAL_LOGIN+" AND WINVE_ESTADO_WEB='A'");

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
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            prodialog.dismiss();
            if (error_importador==0){

                new AlertDialog.Builder(menu_principal.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("SINCRONIZACION.")
                        .setMessage("¿DESEA ACTUALIZAR LOS DATOS DISPONIBLES?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
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
                                                                new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                        Gravity.START,
                                                        ClipDrawable.HORIZONTAL),
                                                new ClipDrawable(
                                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                                new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                        Gravity.START,
                                                        ClipDrawable.HORIZONTAL)
                                        });
                                progressBarDrawable.setId(0,android.R.id.background);
                                progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                                progressBarDrawable.setId(2,android.R.id.progress);
                                prodialog.setTitle("SINCRONIZANDO DATOS");
                                prodialog.setMessage("ESPERE...");
                                prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                prodialog.setProgressDrawable(progressBarDrawable);
                                prodialog.show();
                                prodialog.setCanceledOnTouchOutside(false);
                                prodialog.setCancelable(false);
                                   final AsyncImportador task = new  AsyncImportador();
                                task.execute();

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }

            else {
                new androidx.appcompat.app.AlertDialog.Builder(menu_principal.this)
                        .setTitle("ATENCION!!!")
                        .setCancelable(false)
                        .setMessage(mensajeImporError)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }



        }
    }

    private void ImportarTomas(){
        try {
          /*  controles.connect = controles.conexion.Connections();
            Statement stmt = controles.connect.createStatement();
            ResultSet rs = stmt.executeQuery("" +
                    "   SELECT " +
                    "       count(*) as  contador " +
                    "   FROM   " +
                    "       V_WEB_ARTICULOS_CLASIFICACION  a   " +
                    "       inner join WEB_INVENTARIO_det b on a.arde_lote=b.winvd_lote  " +
                    "       and a.ART_CODIGO=b.winvd_art       " +
                    "       and a.SECC_CODIGO=b.winvd_secc     " +
                    "       and a.ARDE_FEC_VTO_LOTE=b.winvd_fec_vto   " +
                    "       inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  " +
                    "       and c.winve_dep=a.ARDE_DEP  " +
                    "       and c.winve_area=a.AREA_CODIGO  " +
                    "       and c.winve_suc=a.ARDE_SUC   " +
                    "       and c.winve_secc=a.SECC_CODIGO  " +
                    "   where " +
                    "       c.winve_empr=1 " +
                    "       and a.ARDE_SUC="+variables.ID_SUCURSAL_LOGIN+" AND WINVE_ESTADO_WEB='A'");

            while (rs.next())
            {
                ContProgressBarImportador=rs.getInt("contador");
            }
            rs.close();*/
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("SINCRONIZACION.")
                    .setMessage("¿DESEA ACTUALIZAR LOS DATOS DISPONIBLES?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
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
                                                            new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                    Gravity.START,
                                                    ClipDrawable.HORIZONTAL),
                                            new ClipDrawable(
                                                    new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                            new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                    Gravity.START,
                                                    ClipDrawable.HORIZONTAL)
                                    });
                            progressBarDrawable.setId(0,android.R.id.background);
                            progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                            progressBarDrawable.setId(2,android.R.id.progress);
                            prodialog.setTitle("SINCRONIZANDO DATOS");
                            prodialog.setMessage("ESPERE...");
                            prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            prodialog.setProgressDrawable(progressBarDrawable);
                            prodialog.show();
                            prodialog.setCanceledOnTouchOutside(false);
                            prodialog.setCancelable(false);
                            final AsyncImportador task = new  AsyncImportador();
                            task.execute();

                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
            }
        catch (Exception e){

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("ATENCION!!!")
                    .setCancelable(false)
                    .setMessage(e.getMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();
        }

    }

    private void InsertarSqliteToma(){
        try {
            SQLiteDatabase db1= controles.conSqlite.getReadableDatabase();
            db1.execSQL("delete from STKW002INV  WHERE estado IN ('A','C','E')");
            db1.close();
            controles.connect = controles.conexion.Connections();
            Statement stmt = controles.connect.createStatement();

            ResultSet rs = stmt.executeQuery("" +
                    "   SELECT " +
                    "       a.ARDE_SUC, b.winvd_nro_inv, b.winvd_art,a.ART_DESC,b.winvd_lote,b.winvd_fec_vto,b.winvd_area,  " +
                    "       b.winvd_dpto,b.winvd_secc,b.winvd_flia,b.winvd_grupo,b.winvd_cant_act,c.winve_fec," +
                    "       dpto_desc,secc_desc,flia_desc,grup_desc,area_desc,sugr_codigo,b.winvd_secu  " +
                    "   FROM   " +
                    "       V_WEB_ARTICULOS_CLASIFICACION  a   " +
                    "       inner join WEB_INVENTARIO_det b on a.arde_lote=b.winvd_lote  " +
                    "       and a.ART_CODIGO=b.winvd_art       " +
                    "       and a.SECC_CODIGO=b.winvd_secc     " +
                    "        AND TO_DATE(a.ARDE_FEC_VTO_LOTE,'DD/MM/YYYY')=TO_DATE(b.winvd_fec_vto,'DD/MM/YYYY')     " +
                    "       inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  " +
                    "       and c.winve_dep=a.ARDE_DEP  " +
                    "       and c.winve_area=a.AREA_CODIGO  " +
                    "       and c.winve_suc=a.ARDE_SUC   " +
                    "       and c.winve_secc=a.SECC_CODIGO  " +
                    "   where " +
                    "       c.winve_empr=1 " +
                    "       and a.ARDE_SUC="+variables.ID_SUCURSAL_LOGIN+" AND WINVE_ESTADO_WEB='A'");
            int i=1;
        while (rs.next())
        {// SI SE QUIERE VOLVER A IMPORTAR UNA TOMA, QUE YA SE ENCUENTRA INVENTARIADO PERO CON PENDIENTE DE EXPORTACION,
            // ENTONCES NO HACE INSERT AL SQLITE.
            SQLiteDatabase db_consulta= controles.conSqlite.getReadableDatabase();
            Cursor cursor=db_consulta.rawQuery("select * from STKW002INV where  winvd_nro_inv ='"+rs.getInt("winvd_nro_inv")+"' and estado='P'" ,null);
            if (cursor.moveToNext())
            {

            }
            else
            {
                String decripcionArt=  rs.getString("ART_DESC").replaceAll("'","");
                SQLiteDatabase dbdbSTKW002INV=controles.conSqlite.getReadableDatabase();
                dbdbSTKW002INV.execSQL(" INSERT INTO  STKW002INV (" +
                "ARDE_SUC," +
                "winvd_nro_inv," +
                "winvd_art," +
                "ART_DESC," +
                "winvd_lote," +
                "winvd_fec_vto," +
                "winvd_area," +
                "winvd_dpto," +
                "winvd_secc," +
                "winvd_flia," +
                "winvd_grupo," +
                "winvd_cant_act," +
                "winve_fec," +
                "dpto_desc," +
                "secc_desc," +
                "flia_desc," +
                "grup_desc," +
                "area_desc," +
                "winvd_cant_inv," +
                "winvd_subgr," +
                "winvd_secu," +
                "estado) VALUES ('"+
                rs.getInt("ARDE_SUC")               +"','"+
                rs.getInt("winvd_nro_inv")          +"','"+
                rs.getString("winvd_art")           +"','"+
                decripcionArt           +"','"+
                rs.getString("winvd_lote")          +"','"+
                rs.getString("winvd_fec_vto")       +"','"+
                rs.getString("winvd_area")          +"','"+
                rs.getString("winvd_dpto")          +"','"+
                rs.getString("winvd_secc")          +"','"+
                rs.getString("winvd_flia")          +"','"+
                rs.getString("winvd_grupo")         +"','"+
                rs.getString("winvd_cant_act")      +"','"+
                rs.getString("winve_fec")           +"','"+
                rs.getString("dpto_desc")           +"','"+
                rs.getString("secc_desc")           +"','"+
                rs.getString("flia_desc")           +"','"+
                rs.getString("grup_desc")           +"','"+
                rs.getString("area_desc")           +"','" +
                "0','" +
                rs.getString("sugr_codigo")         +"','" +
                rs.getString("winvd_secu")          +"','" +
                "A') "); //ESTADO PENDIENTE A INVENTARIAR.
                dbdbSTKW002INV.close();
            }
            db_consulta.close();
            prodialog.setProgress(i);
            i++;
        }
        rs.close();
        prodialog.dismiss();
        }
        catch (Exception e)
        {
            String asd=e.toString();
           // Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            prodialog.dismiss();
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
            super.onPostExecute(result);
        }
    }
}