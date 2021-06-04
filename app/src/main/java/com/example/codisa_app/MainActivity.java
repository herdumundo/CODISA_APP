package com.example.codisa_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this, com.example.codisa_app.login.class,"DESEA SALIR DE LA APLICACION?",3);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent i=new Intent(this,stkw001.class);
        startActivity(i);
    }

    public void ir_test(View v){
        Intent i=new Intent(this,test_selec_multiple.class);
        startActivity(i);
    }
}