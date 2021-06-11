package com.example.codisa_app;

import Utilidades.Stkw002Item;
import Utilidades.Stkw002List;
import Utilidades.controles;
import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class lista_stkw002_inv extends AppCompatActivity {
    ArrayList<Stkw002List> listaStkw002;
    ListView listView;
   public static Button btn_buscar;
    int tipo=1;
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_stkw002_inv);
        controles.conexion_sqlite(this);
        listView =(ListView)findViewById(R.id.listViewInv);
        btn_buscar =(Button)findViewById(R.id.btn_pendientes_exportacion);
        consultar_tomas_generadas("A");
        btn_buscar.setBackgroundColor(Color.GREEN);
        getSupportActionBar().setTitle("LISTA DE INVENTARIOS PENDIENTES");
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
            Cursor cursor=db_consulta.rawQuery("select" +
                    " distinct winvd_nro_inv,winve_fec,flia_desc,grup_desc " +
                    "from STKW002INV WHERE ESTADO='"+estado+"' " +
                    "and arde_suc="+variables.ID_SUCURSAL_LOGIN+" order by 1" ,null);
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
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.listitem3, R.id.text1, listaStkw002) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    text1.setText("NRO. DE TOMA: "+listaStkw002.get(position).getNroToma());
                    text2.setText("FAMILIA: "+listaStkw002.get(position).getFamilia());
                    text3.setText("GRUPO: "+listaStkw002.get(position).getGrupo());
                    if(tipo==1){
                        view.setBackgroundColor(Color.RED);
                    }
                    else {
                        view.setBackgroundColor(Color.GREEN);
                    }
                    return view;
                }
            };
            listView.setAdapter(adapter);
            }
        catch (Exception e){
            String err=e.toString();
        }
    }
    public void cambio_consulta(View v ){

        if(tipo==1){
            consultar_tomas_generadas("P");
            tipo=2;
            btn_buscar.setText("VER PENDIENTES A INVENTARIAR");
            btn_buscar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gen, 0, 0, 0);
            btn_buscar.setBackgroundColor(Color.RED);
            getSupportActionBar().setTitle("LISTA DE INVENTARIOS REALIZADOS");

        }
        else if (tipo==2){
            consultar_tomas_generadas("A");
            tipo=1;
            btn_buscar.setText("VER REGISTROS REALIZADOS PENDIENTES A EXPORTAR");
            btn_buscar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_export, 0, 0, 0);
            btn_buscar.setBackgroundColor(Color.GREEN);
            getSupportActionBar().setTitle("LISTA DE INVENTARIOS PENDIENTES");

        }
    }
}