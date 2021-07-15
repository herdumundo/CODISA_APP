package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import Utilidades.MultiSpinnerSearch;
import Utilidades.controles;
import Utilidades.variables;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class stkw001 extends AppCompatActivity {
    public static   SpinnerDialog       sp_sucursal,sp_deposito,sp_area,sp_departamento,sp_seccion,sp_familia,sp_grupo;
    public static   TextView            txt_sucursal,txt_id_sucursal,txt_deposito, txt_id_deposito,txt_area,txt_id_area,
            txt_departamento,txt_id_departamento,txt_id_seccion,txt_seccion,txt_familia,
            txt_id_familia, lbl_articulos,
            txt_lv_lote,txt_lv_vencimiento,txtTotalArticuloGrilla,lbl_subgrupo,lbl_grupo;
     static   AlertDialog.Builder builder;
     static   AlertDialog ad;
    public static MultiSpinnerSearch    spinerSubGrupo,spinerArticulos,spinerGrupo;

    public static   RadioButton         radioLoteSi,radioLoteNo,radioExistenciaSi,
            radioExistenciaNo,radioArticuloSi,radioArticuloNo,radioConsolidarSi,radioConsolidarNo;
    RadioGroup radioGrupoLote,radioGrupoExistencia,radioGrupoArticulo,radioGrupoConsolidar;
    public static ProgressDialog progress;
    public static   Boolean   BolLote=true,Bolexistencia=false,BolDescontinuados=false,BolConsolidar=true;
    public static ListView LvArticulosStkw001;


    @Override
    public void onBackPressed()
    {
        controles.volver_atras(this,this,menu_principal.class,"¿Desea volver al menu principal?",1);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                controles.volver_atras(this,this,menu_principal.class,"¿Desea volver al menu principal?",1);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //tipo_stkw001 1 = MANUAL, 2= AUTOMATICO
    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollstkw0012);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView search = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        search.setText("REGISTRO DE "+variables.titulo_stkw001);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorlogin)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        controles.context_stkw001=this;
        controles.activity_stkw001=this;

        // txt_total=findViewById(R.id.txt_totalArticulos);
        txtTotalArticuloGrilla    = findViewById(R.id.txtTotalArticuloGrilla) ;
        BolLote=true;
        Bolexistencia=false;
        BolDescontinuados=false;
        BolConsolidar=true;
        txt_sucursal        = findViewById(R.id.txt_desc_sucursal) ;
        lbl_subgrupo        = findViewById(R.id.lbl_subgrupo) ;
        lbl_grupo        = findViewById(R.id.lbl_grupo) ;
        txt_id_sucursal     = findViewById(R.id.txt_id_sucursal) ;
        txt_deposito        = findViewById(R.id.txt_deposito) ;
        txt_id_deposito     = findViewById(R.id.txt_id_deposito) ;
        txt_area            = findViewById(R.id.txt_area) ;
        txt_id_area         = findViewById(R.id.txt_id_area) ;
        txt_departamento    = findViewById(R.id.txt_departamento) ;
        txt_id_departamento = findViewById(R.id.txt_id_departamento) ;
        txt_id_seccion      = findViewById(R.id.txt_id_seccion) ;
        txt_seccion         = findViewById(R.id.txt_seccion) ;
        txt_familia         = findViewById(R.id.txt_familia) ;
        txt_id_familia      = findViewById(R.id.txt_id_familia) ;
        spinerGrupo        = findViewById(R.id.spinerGrupo) ;
        LvArticulosStkw001  = findViewById(R.id.listViewDet_art);
        lbl_articulos       = findViewById(R.id.lbl_articulos) ;
        spinerSubGrupo      = findViewById(R.id.spinerSubGrupo);
        spinerArticulos     = findViewById(R.id.spinerArticulos);
        spinerGrupo.setSearchHint("Busqueda");
        spinerGrupo.setEmptyTitle("No se encontraron resultados");
        spinerGrupo.setClearText("Ninguno");
        spinerGrupo.setColorSeparation(true);
        spinerGrupo.setHintText("Listado de grupos");


        spinerSubGrupo.setSearchHint("Busqueda");
        spinerSubGrupo.setEmptyTitle("No se encontraron resultados");
        spinerSubGrupo.setClearText("Ninguno");
        spinerSubGrupo.setColorSeparation(true);
        spinerSubGrupo.setHintText("Listado de sub-grupos");
        spinerArticulos.setHintText("Listado de articulos");
        spinerArticulos.setSearchHint("Busqueda");
        spinerArticulos.setEmptyTitle("No se encontraron resultados");
        spinerArticulos.setClearText("Ninguno");
        spinerArticulos.setColorSeparation(true);

        radioConsolidarSi             = findViewById(R.id.radioConsolidarSi);
        radioConsolidarNo             = findViewById(R.id.radioConsolidarNo);

        radioLoteSi             = findViewById(R.id.radioLoteSi);
        radioLoteNo             = findViewById(R.id.radioLoteNo);
        radioExistenciaSi       = findViewById(R.id.radioExistenciaSi);
        radioExistenciaNo       = findViewById(R.id.radioExistenciaNo);
        radioArticuloSi         = findViewById(R.id.radioArticuloSi);
        radioArticuloNo         = findViewById(R.id.radioArticuloNo);
        radioGrupoLote          = findViewById(R.id.radioGrupoLote);
        radioGrupoExistencia    = findViewById(R.id.radioGrupoExistencia);
        radioGrupoArticulo      = findViewById(R.id.radioGrupoArticulo);
        radioGrupoConsolidar         = findViewById(R.id.radioGrupoConsolidar);

        radioLoteSi         .setChecked(true);
        radioConsolidarSi         .setChecked(true);
        radioExistenciaNo   .setChecked(true);
        radioArticuloNo     .setChecked(true);
        controles.limpiarSubGrupo();
        LvArticulosStkw001.setNestedScrollingEnabled(true);
        if(variables.tipo_stkw001==1)
        {
            spinerArticulos.setVisibility(View.VISIBLE);
            lbl_articulos.setVisibility(View.VISIBLE);
            txtTotalArticuloGrilla.setVisibility(View.VISIBLE);
        }
        else
        {
            LinearLayout relative=(LinearLayout)findViewById(R.id.relative);
            relative.setVisibility(View.GONE);
            txtTotalArticuloGrilla.setVisibility(View.GONE);
            lbl_articulos.setVisibility(View.GONE);
            spinerArticulos.setVisibility(View.GONE);
            LvArticulosStkw001.setVisibility(View.GONE);
        }

        radioGrupoLote.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioLoteSi:
                        // num=1;
                        BolLote=true;
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                            controles.INVE_IND_LOTE="S";
                            controles.limpiarListaViewArticulosSTKW001();

                        }
                        break;
                    case R.id.radioLoteNo:
                        BolLote=false;
                        controles.INVE_IND_LOTE="N";
                        //    num=2;
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                            controles.limpiarListaViewArticulosSTKW001();

                        }
                        break;
                }
            }
        });



        radioGrupoConsolidar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioConsolidarSi:
                        // num=1;
                        BolConsolidar=true;
                        controles.listarArticulos();
                        controles.limpiarListaViewArticulosSTKW001();
                        break;
                    case R.id.radioConsolidarNo:
                        BolConsolidar=false;
                        controles.listarArticulos();
                        controles.limpiarListaViewArticulosSTKW001();
                        break;
                }
            }
        });



        radioGrupoExistencia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.radioExistenciaSi:
                        Bolexistencia=true;
                        controles.INVE_ART_EXIST="S";
                        if (variables.tipo_stkw001==1)
                        {
                            controles.listarArticulos();
                            controles.limpiarListaViewArticulosSTKW001();
                        }
                        // Toast.makeText(getApplicationContext(),Bolexistencia.toString(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioExistenciaNo:
                        Bolexistencia=false;
                        controles.INVE_ART_EXIST="N";
                        if (variables.tipo_stkw001==1)
                        {
                            controles.listarArticulos();
                            controles.limpiarListaViewArticulosSTKW001();
                        }
                        //   Toast.makeText(getApplicationContext(),Bolexistencia.toString(),Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        radioGrupoArticulo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioArticuloSi:
                        // num=1;
                        controles.INVE_ART_EST="S";
                        BolDescontinuados=true;
                        if (variables.tipo_stkw001==1)
                        {
                            controles.listarArticulos();
                            controles.limpiarListaViewArticulosSTKW001();
                        }
                        //  Toast.makeText(getApplicationContext(),BolDescontinuados.toString(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioArticuloNo:
                        BolDescontinuados=false;
                        controles.INVE_ART_EST="N";

                        //    num=2;
                        if (variables.tipo_stkw001==1)
                        {

                            controles.listarArticulos();
                            controles.limpiarListaViewArticulosSTKW001();
                        }
                        //  Toast.makeText(getApplicationContext(),BolDescontinuados.toString(),Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        controles.listar_sucursales(this);
        //controles.VerificarRed(this);
    }

    public void registrarToma( View view)
    {
        controles.ValidarStkw001(this,this);
    }

}