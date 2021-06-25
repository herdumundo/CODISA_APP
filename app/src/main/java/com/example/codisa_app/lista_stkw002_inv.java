package com.example.codisa_app;

import Utilidades.Stkw002Item;
import Utilidades.Stkw002List;
import Utilidades.controles;
import Utilidades.variables;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class lista_stkw002_inv extends AppCompatActivity
{
    ArrayList<Stkw002List> listaStkw002;
    public static TextView txtSinresultado,actionbar;
    ListView listView;
    public static Button btn_buscar;

    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
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

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_stkw002_inv);
        controles.conexion_sqlite(this);
        listView =(ListView)findViewById(R.id.listViewInv);
        txtSinresultado=findViewById(R.id.txtSinresultado);
        btn_buscar =(Button)findViewById(R.id.btn_pendientes_exportacion);
        consultar_tomas_generadas("A");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
          actionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        actionbar.setText("PENDIENTES A INVENTARIAR");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int pos, long l) {

                int nro_registro =Integer.parseInt(listaStkw002.get(pos).getNroToma());
                variables.nro_registro_toma=nro_registro;
                ir_stkw001_registro();


            }
        });

    }
    private void   ir_stkw001_registro(){
            Intent intent = new Intent(lista_stkw002_inv.this, stkw002.class);
            finish();
            startActivity(intent);
    }
    private void consultar_tomas_generadas(String estado) {
        try {

            SQLiteDatabase db_consulta= controles.conSqlite.getReadableDatabase();
            String sql="";
            if(estado.equals("A")){
                sql="select  distinct winvd_nro_inv,winve_fec,flia_desc,grup_desc  from STKW002INV WHERE ESTADO='"+estado+"'  " +
                        "and arde_suc="+variables.ID_SUCURSAL_LOGIN+" order by 1";
            }else {
                sql="select  distinct winvd_nro_inv,winve_fec,flia_desc,grup_desc  from STKW002INV WHERE ESTADO='"+estado+"'  " +
                        "and arde_suc="+variables.ID_SUCURSAL_LOGIN+" and upper(WINVE_LOGIN_CERRADO_WEB)=upper('"+variables.userdb+"') order by 1";
            }
            Cursor cursor=db_consulta.rawQuery(sql ,null);
            int cont=0;
            Stkw002List Stkw002List=null;
            listaStkw002=new ArrayList<Stkw002List>();
            while (cursor.moveToNext())
            {
                Stkw002List=new Stkw002List();
                Stkw002List.setNroToma(cursor.getString(0));
                Stkw002List.setFechaToma(cursor.getString(1));
                Stkw002List.setFamilia(cursor.getString(2));
                Stkw002List.setGrupo(cursor.getString(3));
                listaStkw002.add(Stkw002List);
                cont++;
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.listitem_card, R.id.text1, listaStkw002) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    ImageView  txtimagen =   view.findViewById(R.id.txtimagen);
                    text1.setText("NRO. DE TOMA: "+listaStkw002.get(position).getNroToma());
                    text2.setText("FAMILIA: "+listaStkw002.get(position).getFamilia());
                    text3.setText("GRUPO: "+listaStkw002.get(position).getGrupo());
                    if(variables.tipoListaStkw002==1){
                      //  view.setBackgroundColor(Color.RED);
                        txtimagen.setImageResource(R.drawable.ic_list);

                    }
                    else {
                        //view.setBackgroundColor(Color.GREEN);
                        txtimagen.setImageResource(R.drawable.ic_pendiente);

                    }
                    return view;
                }
            };
            listView.setAdapter(adapter);
            if(cont==0){
                txtSinresultado.setVisibility(View.VISIBLE);
            }
            else {
                txtSinresultado.setVisibility(View.GONE);

            }


        }
        catch (Exception e){
            String err=e.toString();
        }
    }


    public void cambio_consulta(View v ){

        if(variables.tipoListaStkw002==1){//SI ES IGUAL A 1 ENTONCES ES UN INVENTARIO YA REALIZADO.
            consultar_tomas_generadas("P");
            variables.tipoStkw002=1;
            variables.tipoListaStkw002=2;
            btn_buscar.setText("VER PENDIENTES A INVENTARIAR");
            btn_buscar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gen, 0, 0, 0);
         //   btn_buscar.setBackgroundColor(Color.RED);
           // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
             actionbar.setText("TOMAS INVENTARIADAS");


        }
        else if (variables.tipoListaStkw002==2){
            consultar_tomas_generadas("A");
            variables.tipoStkw002=2;
            variables.tipoListaStkw002=1;
            btn_buscar.setText("VER REGISTROS REALIZADOS PENDIENTES A EXPORTAR");
            btn_buscar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_export, 0, 0, 0);
           // btn_buscar.setBackgroundColor(Color.GREEN);
         //   getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
             actionbar.setText("PENDIENTES A INVENTARIAR");



        }
    }
}