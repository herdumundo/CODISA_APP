package com.example.codisa_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Utilidades.*;
import java.util.ArrayList;

public class stkw002 extends AppCompatActivity {
    Button btnEliminar;
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this,  lista_stkw002_inv.class,"DESEA SALIR DEL REGISTRO DE INVENTARIO'?",1);
    }
    EditText Searchtext,txt_cantidad;

    private  Stkw002Adapter adapter;
    RecyclerView recyclerView ;

     public void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.scroll);
         recyclerView= (RecyclerView) findViewById( R.id.RecyclerView);
         btnEliminar=  findViewById( R.id.btn_eliminar);
         if(variables.tipoStkw002==1){
             btnEliminar.setVisibility(View.VISIBLE);
         }
         else {
             btnEliminar.setVisibility(View.GONE);

         }
        controles.listarStkw002();
        listar_recicler();
        controles.conexion_sqlite(this);
        Searchtext = (EditText) findViewById(R.id.search_input);
        txt_cantidad = (EditText) findViewById(R.id.txt_cantidad);
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
     }

    private void listar_recicler()
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new Stkw002Adapter(controles.ListArrayInventarioArticulos);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);
    }

    private void filter(String text){
        try {

            ArrayList<Stkw002Item> filteredList = new ArrayList<>();
            for (Stkw002Item item : controles.ListArrayInventarioArticulos )
            {
                if(item.getProducto().toLowerCase().contains(text))
                {
                    filteredList.add(item);
                }
              // Toast.makeText(this,item.getProducto(),Toast.LENGTH_LONG).show();
            }
            adapter.setFilter(filteredList);


        }
        catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }
    }

    public void RegistrarSTKW002(View v){
        Searchtext.requestFocus();
        Searchtext.setText("");
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA REGISTRAR LOS DATOS INGRESADOS?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            Stkw002Adapter.registrar_inventario();
                            new AlertDialog.Builder( stkw002.this)
                                    .setTitle("INFORME!!!")
                                    .setCancelable(false)
                                    .setMessage("REGISTRADO CON EXITO")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i=new Intent(stkw002.this,menu_principal.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }).show();
                        }
                        catch (Exception e)
                        {
                            new AlertDialog.Builder(stkw002.this)
                                    .setTitle("ATENCION!!!")
                                    .setMessage(e.toString()).show();
                        }
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }


    public void EliminarSTKW002(View v){
        Searchtext.requestFocus();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA ELIMINAR EL INVENTARIO REALIZADO?")
                .setPositiveButton("SI, ELIMINAR", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            SQLiteDatabase db_UPDATE= controles.conSqlite.getReadableDatabase();
                            db_UPDATE.execSQL(" update STKW002INV set  estado ='E'   where winvd_nro_inv="+ variables.nro_registro_toma+" ");

                                     new AlertDialog.Builder( stkw002.this)
                                    .setTitle("INFORME!!!")
                                    .setCancelable(false)
                                    .setMessage("REGISTRO ELIMINADO CON EXITO.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                                        public void onClick(DialogInterface dialog, int id) {
                                            variables.tipoListaStkw002=1;

                                            Intent i=new Intent(stkw002.this,lista_stkw002_inv.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }).show();
                        }
                        catch (Exception e)
                        {
                            new AlertDialog.Builder(stkw002.this)
                                    .setTitle("ATENCION!!!")
                                    .setMessage(e.toString()).show();
                        }
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }

}