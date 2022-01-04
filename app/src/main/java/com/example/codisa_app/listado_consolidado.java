package com.example.codisa_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Utilidades.OnSpinerItemClick;
import Utilidades.Stkw002Item;
import Utilidades.controles;

public class listado_consolidado extends AppCompatActivity {
    public static TextView txt_sucursal,txt_id_sucursal ,txt_fecha_desde,txt_fecha_hasta;
    public static   SpinnerDialog       sp_sucursal ;
    String Mensaje_error="";
    DatePickerDialog picker;
    RecyclerView recyclerView;
    private  ConsultaAdapter adapter;
    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_consolidado);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("INFORME DIFERENCIA DE INVENTARIO");
        ConsultaAdapter.activity=this;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        txt_sucursal     = findViewById(R.id.txt_desc_sucursal);
        txt_id_sucursal     = findViewById(R.id.txt_id_sucursal);
        txt_fecha_desde     = findViewById(R.id.txt_fecha_desde);
        txt_fecha_hasta     = findViewById(R.id.txt_fecha_hasta);
        recyclerView=   findViewById( R.id.RecyclerView);
        String query_det="SELECT               ART_DESC,  winve_fec,dpto_desc AS DEPOSITO,secc_desc,flia_desc,grup_desc,               a.flia_desc   as desc_familiA ,winvd_art,WINVD_LOTE,dif.vto,              dif.cant_sist_cons as cargado , dif.cant_fisi_cons as stock_sys,dif.cant_cons as diferencia                             FROM                                 V_WEB_ARTICULOS_CLASIFICACION  a                         inner join WEB_INVENTARIO_det b on   a.ART_CODIGO=b.winvd_art and a.SECC_CODIGO=b.winvd_secc                              inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  And c.winve_dep=a.ARDE_DEP   and c.winve_area=a.AREA_CODIGO                              and c.winve_suc=a.ARDE_SUC   and c.winve_secc=a.SECC_CODIGO INNER JOIN V_WEB_ART_CONS_DIF dif on b.WINVD_NRO_INV=dif.nro_toma and b.winvd_art=dif.articulo and b.WINVD_LOTE=dif.lote          where    c.winve_empr=1                                  and a.ARDE_SUC=1  AND              WINVD_NRO_INV=1288                        GROUP BY                                 ARDE_SUC,winvd_nro_inv,winvd_art, winvd_area,winvd_dpto,winvd_secc,winve_suc,winvd_flia,                               winvd_grupo,winve_fec,dpto_desc,secc_desc,flia_desc,grup_desc,area_desc,sugr_codigo,winve_grupo                        ,winve_tipo_toma,winve_login,winve_grupo_parcial,winve_flia,winve_dep ,ART_DESC,  WINVD_LOTE,                               dif.cant_sist_cons  , dif.cant_fisi_cons ,dif.cant_cons ,dif.vto  ";
        String query_cab="                             ";


        sp_sucursal = new SpinnerDialog(this, controles.arrSucursales,"Listado de Sucursales");
        txt_sucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sp_sucursal.showSpinerDialog();
            } } );

        sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txt_sucursal.setText(controles.arrSucursales.get(i));
                txt_id_sucursal.setText(controles.arrIdSucursales.get(i));
            }
        });


        txt_fecha_desde.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(listado_consolidado.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd//mm//yyyy");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha_desde.setText(df.format((dayOfMonth))+"/"+df.format((monthOfYear + 1))+"/"+ year);



                            }
                        }, year, month, day);
                picker.show();


            }
        });

        txt_fecha_hasta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(listado_consolidado.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd//mm//yyyy");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha_hasta.setText(df.format((dayOfMonth))+"/"+df.format((monthOfYear + 1))+"/"+ year);



                            }
                        }, year, month, day);
                picker.show();


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void consultar(View v){
        consultar_articulos();
    }

    private void consultar_articulos(){
        try
        {
            Statement stmt2 = controles.connect.createStatement();
            ResultSet rs2 = stmt2.executeQuery("   select b.winve_numero,b.winve_fec, b.winve_login    " +
                    " from V_WEB_ART_CONS_DIF A  inner join web_inventario b on a.nro_toma=b.winve_numero    " +
                    " where winve_suc=1  group by   b.winve_numero,b.winve_fec,b.winve_login ");
            controles.ListArrayInventarioArticulos.clear();
            while ( rs2.next())
            {
                controles.ListArrayInventarioArticulos.add(new Stkw002Item( rs2.getString("winve_fec"), rs2.getInt("winve_numero"),
                        rs2.getString("winve_login"),"", "","",
                        "","",
                        "","","",
                        "", "0", 0,0,0,0,0,"0","0")) ;
            }
            rs2.close();
            stmt2.close();
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new ConsultaAdapter(controles.ListArrayInventarioArticulos);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(this.adapter);


            Mensaje_error="";




        } catch (Exception E){
            Mensaje_error =E.getMessage();
        }
    }
}