package com.example.codisa_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import Utilidades.variables;

public class stkw003 extends AppCompatActivity {
    public static TextView txt_sucursal,txt_id_sucursal ,txt_fecha_desde,txt_fecha_hasta;
    public static   SpinnerDialog       sp_sucursal ;
    String Mensaje_error="";
    DatePickerDialog picker;
    RecyclerView recyclerView;
    public static ProgressDialog pgDialog;
    public static   AlertDialog.Builder builder;
    public static   AlertDialog ad;
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
        variables.tipo_lista=1;
        controles.contextListadoConsolidado=this;
        txt_sucursal     = findViewById(R.id.txt_desc_sucursal);
        txt_id_sucursal     = findViewById(R.id.txt_id_sucursal);
        txt_fecha_desde     = findViewById(R.id.txt_fecha_desde);
        txt_fecha_hasta     = findViewById(R.id.txt_fecha_hasta);
        recyclerView=   findViewById( R.id.RecyclerView);
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
                picker = new DatePickerDialog(stkw003.this,
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
                picker = new DatePickerDialog(stkw003.this,
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
        if(txt_fecha_desde.getText().toString().equals("") ||txt_fecha_hasta.getText().toString().equals("") || txt_id_sucursal.getText().toString().equals("")  ){

            builder = new android.app.AlertDialog.Builder(this);
            builder.setIcon(this.getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("¡Atención!");
            builder.setMessage("Complete los datos requeridos.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad = builder.show();
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.azul_claro));
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
        else{
            consultar_articulos();

        }
    }

    private void consultar_articulos(){
        try
        {
            Statement stmt2 = controles.connect.createStatement();
            ResultSet rs2 = stmt2.executeQuery("   select b.winve_numero,b.winve_fec, b.winve_login    " +
                    " from V_WEB_ART_CONS_DIF A  inner join web_inventario b on a.nro_toma=b.winve_numero    " +
                    " where winve_suc="+txt_id_sucursal.getText().toString()+"  and TO_DATE(b.winve_fec, 'dd/mm/yyyy')  between '"+txt_fecha_desde.getText().toString()+"' and '"+txt_fecha_hasta.getText().toString()+"' " +
                    "group by   b.winve_numero,b.winve_fec,b.winve_login ORDER BY 1 ASC ");
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