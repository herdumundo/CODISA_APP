package com.example.codisa_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.controles;
import Utilidades.variables;

public class menu_principal2 extends AppCompatActivity {
    public static ProgressDialog prodialog,ProDialogExport;
    public  static TextView txt_total;
    CardView tomasGen;
    int ContProgressBarImportador=0;
    public void onBackPressed()  {
        controles.volver_atras(this,this, login.class,"DESEA SALIR DE LA APLICACION?",3);
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        txt_total=findViewById(R.id.txt_total);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>USUARIO:"+ variables.NOMBRE_LOGIN+" </font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        getSupportActionBar().setSubtitle(Html.fromHtml("<font color='#FFFFFF'>SUCURSAL:"+ variables.DESCRIPCION_SUCURSAL_LOGIN+" </font>"));
        controles.conexion_sqlite(this);
        controles.ConsultarPendientesExportar();
        controles.context_menuPrincipal=this;
        tomasGen=findViewById(R.id.tomasGen);
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

                        Intent intent = new Intent(menu_principal2.this, stkw001.class);
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
                        Intent intent = new Intent(menu_principal2.this, stkw001.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .show();

    }

    public void OnclickIrStkw001Cancelacion(View v){


        Intent intent = new Intent(menu_principal2.this, lista_stkw001_inv.class);
        finish();
        startActivity(intent);

    }

    public void OnclickExportar( View v){

        controles.ExportarStkw002();
    }

    public void OnclickSincronizarDatos(View v){
       /* Intent intent = new Intent(menu_principal.this, MainActivity.class);
        finish();
        startActivity(intent);
*/
        ImportarTomas();
    }

    private void ImportarTomas(){
        try {
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
            new AlertDialog.Builder(menu_principal2.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("SINCRONIZACION.")
                    .setMessage("¿DESEA ACTUALIZAR LOS DATOS DISPONIBLES?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            menu_principal2.prodialog =  new ProgressDialog( menu_principal2.this);
                            menu_principal2.prodialog.setMax(ContProgressBarImportador);
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
                            menu_principal2.prodialog.setTitle("SINCRONIZANDO DATOS");
                            menu_principal2.prodialog.setMessage("ESPERE...");
                            menu_principal2.prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            menu_principal2.prodialog.setProgressDrawable(progressBarDrawable);
                            menu_principal2.prodialog.show();
                            menu_principal2.prodialog.setCanceledOnTouchOutside(false);
                            menu_principal2.prodialog.setCancelable(false);
                            final AsyncImportador task = new  AsyncImportador();
                            task.execute();

                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
            }
        catch (Exception e){

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