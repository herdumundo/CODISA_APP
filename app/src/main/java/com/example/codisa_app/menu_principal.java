package com.example.codisa_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;

public class menu_principal extends AppCompatActivity {
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this, com.example.codisa_app.login.class,"DESEA SALIR DE LA APLICACION?",3);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        getSupportActionBar().setTitle("USUARIO:"+ variables.NOMBRE_LOGIN);
        getSupportActionBar().setSubtitle("SUCURSAL: "+ variables.DESCRIPCION_SUCURSAL_LOGIN);
        String[] array_opciones=variables.contenedor_menu.split(",");

        for(int i=0; i<array_opciones.length; i++)
        {
            int ID_BOTONES = getResources().getIdentifier(array_opciones[i], "id", getPackageName());
            Button stock = ((Button) findViewById(ID_BOTONES));

            stock.setVisibility(View.VISIBLE);
        }

    }

    public void ir_registro(View v){
        Intent i=new Intent(this,registro_inventario.class);
        startActivity(i);
    }

    public void ir_stkw001(View v){


        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("SELECCIONE EL TIPO DE INVENTARIO QUE DESEA GENERAR")
                .setPositiveButton("SELECCION MANUAL", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="GENERACION DE TOMAS DE INVENTARIO MANUAL";
                        variables.tipo_stkw001=1;
                        variables.tipo_stkw001_insert="M";

                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                         startActivity(intent);

                    }
                })
                .setNeutralButton("SELECCION AUTOMATICA",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variables.titulo_stkw001="GENERACION DE TOMAS DE INVENTARIO AUTOMATICA";
                        variables.tipo_stkw001=2;
                        variables.tipo_stkw001_insert="C";
                        Intent intent = new Intent(menu_principal.this, stkw001.class);
                        finish();
                        startActivity(intent);

                    }
                })
                .show();

    }

    public void ir_test(View v){
        Intent i=new Intent(this,test_selec_multiple.class);
        startActivity(i);
    }
}