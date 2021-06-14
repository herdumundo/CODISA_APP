package com.example.codisa_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import Utilidades.MultiSpinnerSearch;
import Utilidades.controles;
import Utilidades.variables;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class stkw001 extends AppCompatActivity {
    public static   SpinnerDialog       sp_sucursal,sp_deposito,sp_area,sp_departamento,sp_seccion,sp_familia,sp_grupo;
    public static   TextView            txt_sucursal,txt_id_sucursal,txt_deposito, txt_id_deposito,txt_area,txt_id_area,txt_departamento,txt_id_departamento,txt_id_seccion,txt_seccion,txt_familia, txt_id_familia,txt_grupo,txt_id_grupo, lbl_articulos;
    public static MultiSpinnerSearch spinerSubGrupo,spinerArticulos;
    public static   RadioButton         radioLoteSi,radioLoteNo,radioExistenciaSi,radioExistenciaNo,radioArticuloSi,radioArticuloNo;
    RadioGroup radioGrupoLote,radioGrupoExistencia,radioGrupoArticulo;
    public static ProgressDialog progress;
    public static   Boolean             BolLote=true,Bolexistencia=false,BolDescontinuados=false;

    @Override
    public void onBackPressed()
    {
        controles.volver_atras(this,this,menu_principal.class,"¿DESEA VOLVER AL MENU PRINCIPAL?",1);
    }
//tipo_stkw001 1 = MANUAL, 2= AUTOMATICO
        @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stkw001);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ variables.titulo_stkw001+" </font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        controles.context_stkw001=this;
        controles.activity_stkw001=this;
        txt_sucursal        = (TextView)findViewById(R.id.txt_desc_sucursal) ;
        txt_id_sucursal     = (TextView)findViewById(R.id.txt_id_sucursal) ;
        txt_deposito        = (TextView)findViewById(R.id.txt_deposito) ;
        txt_id_deposito     = (TextView)findViewById(R.id.txt_id_deposito) ;
        txt_area            = (TextView)findViewById(R.id.txt_area) ;
        txt_id_area         = (TextView)findViewById(R.id.txt_id_area) ;
        txt_departamento    = (TextView)findViewById(R.id.txt_departamento) ;
        txt_id_departamento = (TextView)findViewById(R.id.txt_id_departamento) ;
        txt_id_seccion      = (TextView)findViewById(R.id.txt_id_seccion) ;
        txt_seccion         = (TextView)findViewById(R.id.txt_seccion) ;
        txt_familia         = (TextView)findViewById(R.id.txt_familia) ;
        txt_id_familia      = (TextView)findViewById(R.id.txt_id_familia) ;
        txt_id_grupo        = (TextView)findViewById(R.id.txt_id_grupo) ;
        txt_grupo           = (TextView)findViewById(R.id.txt_grupo) ;

        lbl_articulos       = (TextView)findViewById(R.id.lbl_articulos) ;

        spinerSubGrupo      = findViewById(R.id.spinerSubGrupo);
        spinerArticulos     = findViewById(R.id.spinerArticulos);

        spinerSubGrupo.setSearchHint("Busqueda de Sub-Grupo");
        spinerSubGrupo.setEmptyTitle("No se encontraron resultados");
        spinerSubGrupo.setClearText("Ninguno");
        spinerSubGrupo.setColorSeparation(true);
        spinerArticulos.setSearchHint("Busqueda de Articulos");
        spinerArticulos.setEmptyTitle("No se encontraron resultados");
        spinerArticulos.setClearText("Ninguno");
        spinerArticulos.setColorSeparation(true);

        radioLoteSi             = (RadioButton)findViewById(R.id.radioLoteSi);
        radioLoteNo             = (RadioButton)findViewById(R.id.radioLoteNo);
        radioExistenciaSi       = (RadioButton)findViewById(R.id.radioExistenciaSi);
        radioExistenciaNo       = (RadioButton)findViewById(R.id.radioExistenciaNo);
        radioArticuloSi         = (RadioButton)findViewById(R.id.radioArticuloSi);
        radioArticuloNo         = (RadioButton)findViewById(R.id.radioArticuloNo);
        radioGrupoLote          = (RadioGroup) findViewById(R.id.radioGrupoLote);
        radioGrupoExistencia    = (RadioGroup) findViewById(R.id.radioGrupoExistencia);
        radioGrupoArticulo      = (RadioGroup) findViewById(R.id.radioGrupoArticulo);

        radioLoteSi.        setChecked(true);
        radioExistenciaNo.  setChecked(true);
        radioArticuloNo.    setChecked(true);
        if(variables.tipo_stkw001==1)
        {
            spinerArticulos.setVisibility(View.VISIBLE);
            lbl_articulos.setVisibility(View.VISIBLE);
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
                        }
                        break;
                    case R.id.radioLoteNo:
                        BolLote=false;
                        controles.INVE_IND_LOTE="N";
                    //    num=2;
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                        }
                        break;
                }
            }
        });

        radioGrupoExistencia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioExistenciaSi:

                        Bolexistencia=true;
                        controles.INVE_ART_EXIST="S";
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                        }
                       // Toast.makeText(getApplicationContext(),Bolexistencia.toString(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioExistenciaNo:
                        Bolexistencia=false;
                        controles.INVE_ART_EXIST="N";
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
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
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                        }
                      //  Toast.makeText(getApplicationContext(),BolDescontinuados.toString(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioArticuloNo:
                        BolDescontinuados=false;
                        controles.INVE_ART_EST="N";

                        //    num=2;
                        if (variables.tipo_stkw001==1){
                            controles.listarArticulos();
                        }
                      //  Toast.makeText(getApplicationContext(),BolDescontinuados.toString(),Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        controles.listar_sucursales(this);
        controles.listar_areas(this,this,variables.tipo_stkw001);


    }

    public void registrarToma( View view)
    {

       controles.ValidarStkw001(this,this);
    }


}