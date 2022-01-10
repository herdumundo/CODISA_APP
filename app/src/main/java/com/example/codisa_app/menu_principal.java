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

import java.sql.ResultSet;
import java.sql.Statement;

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
               error_importador=1;
               //controles.connect = controles.conexion.Connections();
               Statement stmt = controles.connect.createStatement();
               ResultSet rs = stmt.executeQuery(
                       "select count(*) as contador FROM  (" +
                               " SELECT '1' AS CONT " +
                               "        from  " +
                               "        WEB_INVENTARIO a  " +
                               "        inner join WEB_INVENTARIO_det b on a.winve_numero=b.winvd_nro_inv    " +
                               "        where a.WINVE_ESTADO_WEB='A' and a.winve_empr=1 AND a.winve_suc='"+variables.ID_SUCURSAL_LOGIN+"' " +
                               "        UNION ALL " +
                               "        select '1' AS CONT from web_stk_carga_inv a  " +
                               "        inner join web_stk_carga_inv_det b on a.inve_numero=b.invd_nro_inv and a.inve_empr=1 and" +
                               "        a.inve_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND a.invew_est='R' and UPPER(inve_login)=UPPER('"+variables.userdb+"'))");


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
           // controles.connect = controles.conexion.Connections();
            Statement stmt = controles.connect.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT " +
                    "           'A' as toma,0 as invd_cant_inv,ART_DESC, ARDE_SUC,winvd_nro_inv,winvd_art,'' AS winvd_lote,'' AS winvd_fec_vto,winvd_area," +
                            "   winvd_dpto,winvd_secc,winvd_flia," +
                            "   winvd_grupo, 0  as winvd_cant_act,winve_fec,dpto_desc,secc_desc,flia_desc,grup_desc,area_desc,sugr_codigo,'' AS winvd_secu," +
                            "   case c.winve_tipo_toma when 'C' then 'CRITERIO' ELSE 'MANUAL' END AS tipo_toma,winve_login, ''  AS winvd_consolidado ," +
                            "   case when c.winve_grupo IS NULL and  c.winve_grupo_parcial IS NULL then 'TODOS'" +
                            "   WHEN c.winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' ELSE grup_desc END AS desc_grupo_parcial," +
                            "   case when c.winve_flia is null then 'TODAS' else a.flia_desc end as desc_familia,winve_dep,winve_suc, a.coba_codigo_barra," +
                            "     a.caja,a.GRUESA  ,a.UNID_IND  " +
                    "    FROM" +
                            "   V_WEB_ARTICULOS_CLASIFICACION  a" +
                            "   inner join WEB_INVENTARIO_det b on   a.ART_CODIGO=b.winvd_art and a.SECC_CODIGO=b.winvd_secc" +
                            "   inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  And c.winve_dep=a.ARDE_DEP   and c.winve_area=a.AREA_CODIGO" +
                            "   and c.winve_suc=a.ARDE_SUC   and c.winve_secc=a.SECC_CODIGO" +
                    "   where   c.winve_empr=1   and a.ARDE_SUC="+variables.ID_SUCURSAL_LOGIN+" AND WINVE_ESTADO_WEB='A'" +
                    "   GROUP BY  " +
                            "   ARDE_SUC,winvd_nro_inv,winvd_art, winvd_area,winvd_dpto,winvd_secc,winve_suc,winvd_flia," +
                            "   winvd_grupo,winve_fec,dpto_desc,secc_desc,flia_desc,grup_desc,area_desc,sugr_codigo,winve_grupo" +
                            "   ,winve_tipo_toma,winve_login,winve_grupo_parcial,winve_flia,winve_dep ,ART_DESC, a.coba_codigo_barra," +
                            "  a.caja,a.GRUESA  ,a.UNID_IND " +
                "       union all" +
                    "   select "+
                    "      'R' as toma,0 AS invd_cant_inv,ART_DESC,  ARDE_SUC,a.inve_numero as winvd_nro_inv,b.ARTICULO as winvd_art,'' AS winvd_lote,'' AS winvd_fec_vto," +
                    "       c.AREA_CODIGO as winvd_area,d.winve_dpto as winvd_dpto,d.winve_secc as winvd_secc,c.FLIA_CODIGO as winvd_flia,0 AS winvd_grupo," +
                    "       0 as winvd_cant_act,a.inve_fec as winve_fec, c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC," +
                    "       c.GRUP_DESC, c.AREA_DESC,c.SUGR_CODIGO as sugr_codigo,'' as winvd_secu," +
                    "       case d.winve_tipo_toma when 'C' then 'CRITERIO' ELSE 'MANUAL' END AS tipo_toma,a.inve_login as winve_login,''  AS winvd_consolidado ," +
                    "       case when d.winve_grupo IS NULL and  d.winve_grupo_parcial IS NULL then 'TODOS'" +
                    "       WHEN d.winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' ELSE grup_desc END AS desc_grupo_parcial," +
                    "       case when d.winve_flia is null then 'TODAS' else c.flia_desc end as desc_familia,winve_dep,winve_suc, c.coba_codigo_barra," +
                    "       c.caja,c.GRUESA  ,c.UNID_IND" +
                    "   from" +
                    "       web_stk_carga_inv a" +
                    "       inner join V_WEB_ART_CONS_DIF b on a.inve_numero=b.NRO_CARGA" +
                    "       inner join V_WEB_ARTICULOS_CLASIFICACION c on b.ARTICULO=c.ART_CODIGO" +
                    "       INNER JOIN WEB_INVENTARIO d on a.inve_ref=d.winve_numero and d.winve_suc=c.ARDE_SUC" +
                    "   where" +
                    "       a.invew_est='R' and d.winve_suc="+variables.ID_SUCURSAL_LOGIN+" and  UPPER(inve_login)=UPPER('"+variables.userdb+"') " +
                    "   group by ARDE_SUC,a.inve_numero ,b.ARTICULO  ," +
                    "       c.AREA_CODIGO  ,d.winve_dpto  ,d.winve_secc  ,c.FLIA_CODIGO ," +
                    "       a.inve_fec , c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC,c.GRUP_DESC,d.winve_tipo_toma," +
                    "       c.AREA_DESC,a.inve_login,d.winve_grupo_parcial,d.winve_grupo,d.winve_flia,c.flia_desc,winve_dep," +
                    "       winve_suc,c.SUGR_CODIGO,ART_DESC, c.coba_codigo_barra,c.caja,c.GRUESA  ,c.UNID_IND ");

            /* "       select " +
                            "   'R' as toma,b.invd_cant_inv,ART_DESC,  ARDE_SUC,a.inve_numero as winvd_nro_inv,b.invd_art as winvd_art,'' AS winvd_lote,'' AS winvd_fec_vto," +
                            "   c.AREA_CODIGO as winvd_area,d.winve_dpto as winvd_dpto,d.winve_secc as winvd_secc,c.FLIA_CODIGO as winvd_flia,0 AS winvd_grupo," +
                            "   b.invd_cant_inv as winvd_cant_act,a.inve_fec as winve_fec, c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC," +
                            "   c.GRUP_DESC, c.AREA_DESC,c.SUGR_CODIGO as sugr_codigo,'' as winvd_secu," +
                            "   case d.winve_tipo_toma when 'C' then 'CRITERIO' ELSE 'MANUAL' END AS tipo_toma,a.inve_login as winve_login,''  AS winvd_consolidado ," +
                            "   case when d.winve_grupo IS NULL and  d.winve_grupo_parcial IS NULL then 'TODOS'" +
                            "   WHEN d.winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' ELSE grup_desc END AS desc_grupo_parcial," +
                            "   case when d.winve_flia is null then 'TODAS' else c.flia_desc end as desc_familia,winve_dep,winve_suc, c.coba_codigo_barra," +
                            "   c.caja,c.GRUESA  ,c.UNID_IND" +
                "       from " +
                            "   web_stk_carga_inv a " +
                            "   inner join web_stk_carga_inv_det b on a.inve_numero=b.invd_nro_inv" +
                            "   inner join V_WEB_ARTICULOS_CLASIFICACION c on b.invd_art=c.ART_CODIGO" +
                            "   INNER JOIN WEB_INVENTARIO d on a.inve_ref=d.winve_numero and d.winve_suc=c.ARDE_SUC" +
                "       where " +
                            "   a.invew_est='R'   and d.winve_suc="+variables.ID_SUCURSAL_LOGIN+" and  UPPER(inve_login)=UPPER('"+variables.userdb+"')" +
                    "   group by ARDE_SUC,a.inve_numero ,b.invd_art  , " +
                            "   c.AREA_CODIGO  ,d.winve_dpto  ,d.winve_secc  ,c.FLIA_CODIGO ," +
                            "   b.invd_cant_inv  ,a.inve_fec , c.DPTO_DESC,c.SECC_DESC,c.FLIA_DESC,c.GRUP_DESC,d.winve_tipo_toma," +
                            "   c.AREA_DESC,a.inve_login,d.winve_grupo_parcial,d.winve_grupo,d.winve_flia,c.flia_desc,winve_dep," +
                            "   winve_suc,c.SUGR_CODIGO,ART_DESC, c.coba_codigo_barra,c.caja,c.GRUESA  ,c.UNID_IND");*/


            int i=1;
            int contadorMensaje=0;
        while (rs.next())
        {// SI SE QUIERE VOLVER A IMPORTAR UNA TOMA, QUE YA SE ENCUENTRA INVENTARIADO PERO CON PENDIENTE DE EXPORTACION,
            // ENTONCES NO HACE INSERT AL SQLITE.
            contadorMensaje++;
            SQLiteDatabase db_consulta= controles.conSqlite.getReadableDatabase();
            Cursor cursor=db_consulta.rawQuery("select * from STKW002INV where  winvd_nro_inv ='"+rs.getInt("winvd_nro_inv")+"' and estado in ('P','F')" ,null);
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
                "estado," +
                "tipo_toma, " +
                "winve_login," +
                "winvd_consolidado," +
                "desc_grupo_parcial," +
                "desc_familia," +
                "winve_dep, " +
                "winve_suc," +
                "toma_registro," +
                "cod_barra, " +
                "caja, " +
                "GRUESA  , " +
                "UNID_IND) " + //coba_codigo_barra
                "VALUES ('"+
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
                rs.getString("area_desc")           +"','"+
                rs.getString("invd_cant_inv")           +"','"+
                rs.getString("sugr_codigo")         +"','" +
                rs.getString("winvd_secu")          +"','" +
                "A','"+rs.getString("tipo_toma")    +"','"
                +rs.getString("winve_login")   +"','"
                +rs.getString("winvd_consolidado")+"','"
                +rs.getString("desc_grupo_parcial")+"','"
                +rs.getString("desc_familia")+"','"
                +rs.getString("winve_dep")+"','"
                +rs.getString("winve_suc")+"','"
                +rs.getString("toma")+"','"
                +rs.getString("coba_codigo_barra")+"','"
                +rs.getString("caja")+"','"
                +rs.getString("GRUESA")+"','"
                +rs.getString("UNID_IND")+"'"  +
                ") "); //ESTADO PENDIENTE A INVENTARIAR.
                dbdbSTKW002INV.close();
            }
            db_consulta.close();
            prodialog.setProgress(i);
            i++;
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