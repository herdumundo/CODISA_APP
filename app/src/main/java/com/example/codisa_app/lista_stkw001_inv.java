package com.example.codisa_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
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
         getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>CANCELACION DE TOMAS GENERADAS </font>"));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
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
                alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.setNeutralButton("CANCELAR TOMA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controles.CancelarToma(nro_registro,lista_stkw001_inv.this);
                        dialog.dismiss();
                    }
                });


                alert.show();
                /*
                int nro_registro =Integer.parseInt(controles.listaStkw001.get(pos).getNroToma());

                new AlertDialog.Builder(lista_stkw001_inv.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ATENCION!!!.")
                        .setMessage("¿DESEA CANCELAR LA TOMA GENERADA?")
                        .setPositiveButton("SI, CANCELAR", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                controles.CancelarToma(nro_registro,lista_stkw001_inv.this);
                             //   Toast.makeText(lista_stkw001_inv.this,String.valueOf(nro_registro),Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();

*/
            }
        });

    }


}