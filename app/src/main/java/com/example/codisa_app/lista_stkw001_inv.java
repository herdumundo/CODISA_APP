package com.example.codisa_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Utilidades.Stkw002List;
import Utilidades.controles;
import Utilidades.variables;

public class lista_stkw001_inv extends AppCompatActivity {
   public static ListView listView,listView2;
    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_stkw001_inv);
        listView =(ListView)findViewById(R.id.listViewInvStkw001);
        controles.ConsultarTomasServer( this);
         getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>CANCELACIÓN DE TOMAS </font>"));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int pos, long l) {
                int nro_registro =Integer.parseInt(controles.listaStkw001.get(pos).getNroToma());

                AlertDialog.Builder alert = new AlertDialog.Builder(lista_stkw001_inv.this);
                alert.setTitle("ARTICULOS CARGADOS");
                controles.listarWebViewStkw001Cancelacion(nro_registro);
                WebView wv = new WebView(lista_stkw001_inv.this);
                wv.loadData(controles.table, "text/html", "utf-8");


                alert.setView(wv);
                alert.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.setNeutralButton("CANCELAR TOMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AlertDialog.Builder alert2 = new AlertDialog.Builder(lista_stkw001_inv.this);
                        alert2.setTitle("¿ESTÁ SEGURO QUE DESEA CANCELAR LA TOMA NRO."+nro_registro+"?");
                        alert2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int id) {
                                dialog2.dismiss();
                            }
                        });
                        alert2.setNeutralButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int which) {
                                controles.CancelarToma(nro_registro,lista_stkw001_inv.this);
                                dialog2.dismiss();

                                AlertDialog.Builder alert3 = new AlertDialog.Builder(lista_stkw001_inv.this);
                                alert3.setTitle("REGISTRO CANCELADO CON EXITO.");
                                alert3.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog3, int which) {
                                        dialog3.dismiss();

                                    }
                                });

                                alert3.show();




                            }
                        });

                        alert2.show();

                            }
                });
                alert.show();
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
}