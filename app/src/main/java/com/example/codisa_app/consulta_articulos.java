package com.example.codisa_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Utilidades.ArrayListContenedor;
import Utilidades.MultiSpinnerListener;
import Utilidades.MultiSpinnerSearch;
import Utilidades.OnSpinerItemClick;
import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;

public class consulta_articulos extends AppCompatActivity {
    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
    }
    public static   SpinnerDialog       sp_sucursal,sp_deposito ;
    static SpinnerDialog        sp_lote ;
    public static   TextView            txt_sucursal,txt_id_sucursal,txt_deposito, txt_id_deposito,txt_fecha;
     public static MultiSpinnerSearch spinerArticulos;
    public static   ArrayList<String> arr_lote     =   new ArrayList<>();
    private static String Mensaje_error="";
    RecyclerView recyclerView;
    private  ConsultaAdapter adapter;
    public static LinearLayout linearItem;
    EditText Searchtext;
    public static ProgressDialog prodialog,ProDialogExport;
    DatePickerDialog picker;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_articulos);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("CONSULTA DE ARTICULOS");
        ConsultaAdapter.activity=this;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        variables.tipo_lista=0;
        Searchtext = (EditText) findViewById(R.id.search_input);
        txt_sucursal        = findViewById(R.id.txt_desc_sucursal) ;
        txt_id_sucursal        = findViewById(R.id.txt_id_sucursal) ;
        txt_deposito        = findViewById(R.id.txt_desc_deposito) ;
        txt_id_deposito        = findViewById(R.id.txt_id_deposito) ;
        spinerArticulos     = findViewById(R.id.spinerArticulos);
        recyclerView= (RecyclerView) findViewById( R.id.RecyclerView);
        txt_fecha=(TextView)findViewById(R.id.txt_fecha);
        linearItem=findViewById(R.id.linearItem);

        txt_fecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(consulta_articulos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("dd//mm//yyyy");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha.setText(df.format((dayOfMonth))+"/"+df.format((monthOfYear + 1))+"/"+ year);



                            }
                        }, year, month, day);
                picker.show();


            }
        });



        Searchtext.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }
            public void afterTextChanged(Editable editable) {
                //    filter(editable.toString());
            }
        } );
        sp_sucursal = new SpinnerDialog(this,controles.arrSucursales,"Listado de Sucursales");
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

                controles.listar_depositos(consulta_articulos.this,controles.arrIdSucursales.get(i),2);
                sp_deposito = new SpinnerDialog(consulta_articulos.this,controles.arr_deposito,"Listado de depositos");

                sp_deposito.showSpinerDialog();
                txt_deposito.setOnClickListener(new View.OnClickListener() {  @Override
                public void onClick(View v)
                {
                sp_deposito.showSpinerDialog();
                } } );
                 sp_deposito.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i)
                    {
                        txt_deposito.setText(controles.arr_deposito.get(i));
                        txt_id_deposito.setText(controles.arr_id_deposito.get(i));



                    }
                });
            }
        });
        controles.conexion_sqlite(this);


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

    private void consultar_articulos(){
        try
        {
        Statement stmt2 = controles.connect.createStatement();
        ResultSet rs2 = stmt2.executeQuery(" " +
                "   select * from ( SELECT V.DETA_ART, V.DOCU_SUC_ORIG, V.DOCU_DEP_ORIG,  " +
                "   v.art_desc, " +
                "   SUM(V.DETA_CANT_ENTRADA - V.DETA_CANT_SALIDA) CANTIDAD " +
                "   FROM STKC005 V  " +
                "   WHERE   V.DOCU_SUC_ORIG="   +txt_id_sucursal.getText()  +"  " +
                "   and V.DOCU_DEP_ORIG="       +txt_id_deposito.getText()  +"  " +
                "   and V.DOCU_FEC_EMIS <= '"   +txt_fecha.getText()        +"'" +
                "   GROUP BY V.DETA_ART," +
                "   V.DOCU_SUC_ORIG, V.DOCU_DEP_ORIG,  v.art_desc) t where cantidad>0 ");
            controles.ListArrayInventarioArticulos.clear();
        while ( rs2.next())
        {
            controles.ListArrayInventarioArticulos.add(new Stkw002Item( rs2.getString("art_desc"), rs2.getInt("CANTIDAD"),
                    "0",rs2.getString("DETA_ART"), "0","secue",
                    "AREA","grupse",
                    "flia","tomanro","codBarra",
                    rs2.getString("CANTIDAD"), "0", 0,0,0,0,0,"0","0")) ;
        }
        rs2.close();
        stmt2.close();


            SQLiteDatabase db1= controles.conSqlite.getReadableDatabase();
            db1.execSQL("delete from STKC005 ");
            db1.close();

            Mensaje_error="";

            Statement stmt3 = controles.connect.createStatement();
            ResultSet rs3 = stmt3.executeQuery(" "                                                          +
                    "   select * from ( SELECT V.DETA_ART, V.DOCU_SUC_ORIG, V.DOCU_DEP_ORIG, V.DETA_LOTE, "     +
                    "   v.art_desc, V.DETA_FEC_VTO_LOTE,"                                                       +
                    "   SUM(V.DETA_CANT_ENTRADA - V.DETA_CANT_SALIDA) CANTIDAD "                                +
                    "   FROM STKC005 V  "                                                                       +
                    "   WHERE   V.DOCU_SUC_ORIG="   +txt_id_sucursal.getText()  +"  "                           +
                    "   and V.DOCU_DEP_ORIG="       +txt_id_deposito.getText()  +"  "                           +
                    "   and V.DOCU_FEC_EMIS <= '"   +txt_fecha.getText()        +"' "                           +
                    "    GROUP BY " +
                    "   V.DETA_ART, V.DOCU_SUC_ORIG, V.DOCU_DEP_ORIG, V.DETA_LOTE, V.DETA_FEC_VTO_LOTE,v.art_desc) t where cantidad>0 ");
             while ( rs3.next())
            {
                SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
                db.execSQL(" INSERT INTO  STKC005 ( DETA_ART  ,art_desc,DETA_LOTE  ,cantidad ) " +
                        "VALUES ('"+rs3.getString("DETA_ART")+"','"+rs3.getString("art_desc").replaceAll("'"," ")+"','"+rs3.getString("DETA_LOTE").replaceAll("'"," ")+"','"+rs3.getString("CANTIDAD")+"' )") ;
                db.close();
            }
            rs3.close();
            stmt3.close();


    } catch (Exception E){
            Mensaje_error =E.getMessage();
        }
    }


    public static void  consultar_articulos_lotes( Activity activity, String cod_articulo){
        try
        {


            SQLiteDatabase db_consulta= controles.conSqlite.getReadableDatabase();
            Cursor cursor =db_consulta.rawQuery(" SELECT art_desc,DETA_LOTE,CANTIDAD from STKC005 where DETA_ART="+cod_articulo+" ",null);
             arr_lote.clear();
             String articulo="";
             int cantidad=0;
            while ( cursor.moveToNext())
            {
                arr_lote.add( "Lote:"+cursor.getString(1) +"  Cant:"+  cursor.getString(2) );
                articulo= cursor.getString(0);
                cantidad=cantidad+cursor.getInt(2);
            }
            db_consulta.close();
            sp_lote = new SpinnerDialog( activity, arr_lote,articulo+": Total "+cantidad);
            sp_lote.showSpinerDialog();
            sp_lote.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {

                }
            });
        } catch (Exception E){
            Toast.makeText(activity, "Error: "+E.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void listar_recicler()
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ConsultaAdapter(controles.ListArrayInventarioArticulos);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);
    }


    private void filter(String text){
        try {

            ArrayList<Stkw002Item> filteredList = new ArrayList<>();
            for (Stkw002Item item : controles.ListArrayInventarioArticulos )
            {
                if(item.getProducto().toLowerCase().contains(text)
                        ||item.getCod_articulo().contains(text)
                  )
                {
                    filteredList.add(item);
                }
            }
            adapter.setFilter(filteredList);


        }
        catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }
    }

    public void consultar (View v){
        final HiloConsultar task = new HiloConsultar();
        task.execute();
    }


    public   class HiloConsultar extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prodialog = ProgressDialog.show(consulta_articulos.this, "CONSULTANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            consultar_articulos();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



            prodialog.dismiss();
            if (Mensaje_error==""){
                listar_recicler();

            }
            else {
                Toast.makeText(consulta_articulos.this, "Error: "+Mensaje_error, Toast.LENGTH_LONG).show();
            }
        }
    }

}