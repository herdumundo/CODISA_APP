package com.example.codisa_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Utilidades.*;
import java.util.ArrayList;

public class stkw002 extends AppCompatActivity {
    Button btnEliminar;
    public static TextView txtTotalArt;
    public static ProgressDialog ProDialog;
    android.app.AlertDialog.Builder builder;
    android.app.AlertDialog ad;
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this,  lista_stkw002_inv.class,"¿Desea salir del registro de inventario?",1);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Utilidades.controles.volver_atras(this,this,  lista_stkw002_inv.class,"DESEA SALIR DEL REGISTRO DE INVENTARIO'?",1);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    EditText Searchtext,txt_cantidad;

    private  Stkw002Adapter adapter;
    RecyclerView recyclerView ;

     @SuppressLint("WrongConstant")
     public void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.scroll);
         recyclerView= (RecyclerView) findViewById( R.id.RecyclerView);
         btnEliminar=  findViewById( R.id.btn_eliminar);
         txtTotalArt=  findViewById( R.id.txtTotalArt);
         getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
         getSupportActionBar().setCustomView(R.layout.customactionbar);
         TextView search = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
         search.setText("REGISTRO INVENTARIO");
         getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
         upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
         this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

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
           // if(variables.consolidado.equals("SI")){
                for (Stkw002Item item : controles.ListArrayInventarioArticulos )
                {
                    if(item.getProducto().toLowerCase().contains(text)
                            ||item.getCodArticulo().contains(text)
                            ||item.getgrupo().toLowerCase().contains(text)||
                            item.getfamilia().toLowerCase().contains(text)||
                            item.getCantidad().contains(text.trim()))
                    {
                        filteredList.add(item);
                    }
                    // Toast.makeText(this,item.getProducto(),Toast.LENGTH_LONG).show();
                }
                adapter.setFilter(filteredList);
           // }
        /*    else{
                for (Stkw002Item item : controles.ListArrayInventarioArticulos )
                {
                    if(item.getProducto().toLowerCase().contains(text.toLowerCase())||item.getLote().toLowerCase().contains(text.toLowerCase())
                            ||item.getCodArticulo().contains(text)||item.getVencimiento().contains(text)
                            ||item.getgrupo().toLowerCase().contains(text.toLowerCase())||item.getfamilia().toLowerCase().contains(text.toLowerCase())
                            || item.getCantidad().contains(text.trim()))
                    {
                        filteredList.add(item);
                    }
                    // Toast.makeText(this,item.getProducto(),Toast.LENGTH_LONG).show();
                }
                adapter.setFilter(filteredList);
            }*/



        }
        catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }
    }

    public void RegistrarSTKW002(View v){
        Searchtext.requestFocus();
        Searchtext.setText("");

        builder = new android.app.AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_danger));
        builder.setTitle("¡Atención!");
        builder.setMessage("¿Desea registrar las cantidades ingresadas?.");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AsyncRegistrarStkw002 task = new AsyncRegistrarStkw002();
                task.execute();

            }
        });
        builder.setNegativeButton("No",null);
        ad = builder.show();
        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

    }


    public void EliminarSTKW002(View v){
        Searchtext.requestFocus();

        builder = new android.app.AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.ic_danger));
        builder.setTitle("¡Atención!");
        builder.setMessage("¿Desea eliminar el inventario realizado?.");
        builder.setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    SQLiteDatabase db_UPDATE= controles.conSqlite.getReadableDatabase();
                    db_UPDATE.execSQL(" update STKW002INV set  estado ='E'   where winvd_nro_inv="+ variables.nro_registro_toma+" ");

                    new AlertDialog.Builder( stkw002.this)
                            .setTitle("¡INFORME!")
                            .setCancelable(false)
                            .setMessage("Registro eliminado con éxito.")
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
        });
        builder.setNegativeButton("No",null);
        ad = builder.show();
        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.azul_claro));
        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);


    }

      private class AsyncRegistrarStkw002 extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProDialog = ProgressDialog.show(stkw002.this, "PROCESANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            Stkw002Adapter.registrar_inventario();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProDialog.dismiss();

            if(Stkw002Adapter.CodigoRegistro==0){
                new AlertDialog.Builder( stkw002.this)
                        .setTitle("INFORME!!!")
                        .setCancelable(false)
                        .setMessage(Stkw002Adapter.MensajeRegistro)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i=new Intent( stkw002.this,menu_principal.class);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
            else {
                new AlertDialog.Builder( stkw002.this)
                        .setTitle("¡Informe!")
                        .setCancelable(false)
                        .setMessage(Stkw002Adapter.MensajeRegistro).show();
            }



        }
    }
}