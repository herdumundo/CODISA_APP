package com.example.codisa_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Utilidades.controles;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class stkw001 extends AppCompatActivity {
    public static SpinnerDialog sp_sucursal,sp_deposito,sp_area,sp_departamento,sp_seccion,sp_familia,sp_grupo,sp_subgrupo;
    public static TextView txt_sucursal,txt_id_sucursal,txt_deposito,
            txt_id_deposito,txt_area,txt_id_area,txt_departamento,
            txt_id_departamento,txt_id_seccion,txt_seccion,txt_familia,
            txt_id_familia,txt_grupo,txt_id_grupo,txt_subgrupo,txt_id_subgrupo;
   public static  MultiSpinnerSearch multiSelectSpinnerWithSearch;
     AlertDialog mDialog = null;
     boolean selectAll = true;
    int length;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stkw001);
        getSupportActionBar().setTitle("GENERACION DE TOMAS DE INVENTARIO");
        txt_sucursal = (TextView)findViewById(R.id.txt_desc_sucursal) ;
        txt_id_sucursal = (TextView)findViewById(R.id.txt_id_sucursal) ;
        txt_deposito = (TextView)findViewById(R.id.txt_deposito) ;
        txt_id_deposito = (TextView)findViewById(R.id.txt_id_deposito) ;
        txt_area = (TextView)findViewById(R.id.txt_area) ;
        txt_id_area = (TextView)findViewById(R.id.txt_id_area) ;
        txt_departamento = (TextView)findViewById(R.id.txt_departamento) ;
        txt_id_departamento = (TextView)findViewById(R.id.txt_id_departamento) ;
        txt_id_seccion = (TextView)findViewById(R.id.txt_id_seccion) ;
        txt_seccion = (TextView)findViewById(R.id.txt_seccion) ;
        txt_familia = (TextView)findViewById(R.id.txt_familia) ;
        txt_id_familia = (TextView)findViewById(R.id.txt_id_familia) ;
        txt_id_grupo = (TextView)findViewById(R.id.txt_id_grupo) ;
        txt_grupo = (TextView)findViewById(R.id.txt_grupo) ;

        controles.listar_sucursales(this);
        controles.listar_areas(this,this);
        multiSelectSpinnerWithSearch = findViewById(R.id.multipleItemSelectionSpinner);
         multiSelectSpinnerWithSearch.setSearchHint("Busqueda de Sub-Grupo");
         multiSelectSpinnerWithSearch.setEmptyTitle("No se encontraron resultados");
        // multiSelectSpinnerWithSearch.setShowSelectAllButton(true);
         multiSelectSpinnerWithSearch.setClearText("Ninguno");
         multiSelectSpinnerWithSearch.setColorSeparation(true);

     }



}