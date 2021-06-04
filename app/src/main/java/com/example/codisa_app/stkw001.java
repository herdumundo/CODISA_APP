package com.example.codisa_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Utilidades.controles;
import Utilidades.variables;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class stkw001 extends AppCompatActivity {
    public static SpinnerDialog sp_sucursal,sp_deposito,sp_area,sp_departamento,sp_seccion,sp_familia,sp_grupo,sp_subgrupo;
    public static TextView txt_sucursal,txt_id_sucursal,txt_deposito,
            txt_id_deposito,txt_area,txt_id_area,txt_departamento,
            txt_id_departamento,txt_id_seccion,txt_seccion,txt_familia,
            txt_id_familia,txt_grupo,txt_id_grupo,txt_subgrupo,txt_id_subgrupo,lbl_articulos;
   public static  MultiSpinnerSearch multiSelectSpinnerWithSearch,spinerArticulos;
     AlertDialog mDialog = null;
     boolean selectAll = true;
    int length;
    @Override
    public void onBackPressed()  {
        controles.volver_atras(this,this,menu_principal.class,"¿DESEA VOLVER AL MENU PRINCIPAL?",1);

    }
//tipo_stkw001 1 = MANUAL, 2= AUTOMATICO
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stkw001);
        getSupportActionBar().setTitle(variables.titulo_stkw001);
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
            lbl_articulos = (TextView)findViewById(R.id.lbl_articulos) ;
        controles.listar_sucursales(this);
        controles.listar_areas(this,this,variables.tipo_stkw001);
            multiSelectSpinnerWithSearch = findViewById(R.id.multipleItemSelectionSpinner);
            spinerArticulos = findViewById(R.id.spinerArticulos);
         multiSelectSpinnerWithSearch.setSearchHint("Busqueda de Sub-Grupo");
         multiSelectSpinnerWithSearch.setEmptyTitle("No se encontraron resultados");
        // multiSelectSpinnerWithSearch.setShowSelectAllButton(true);
         multiSelectSpinnerWithSearch.setClearText("Ninguno");
         multiSelectSpinnerWithSearch.setColorSeparation(true);

         if(variables.tipo_stkw001==1){
             spinerArticulos.setVisibility(View.VISIBLE);
             lbl_articulos.setVisibility(View.VISIBLE);
           }

     }



}