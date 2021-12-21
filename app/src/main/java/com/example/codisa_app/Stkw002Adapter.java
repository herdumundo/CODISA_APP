package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class Stkw002Adapter extends Adapter<Stkw002Adapter.ExampleViewHolder> {
    private static List<Stkw002Item> listaStkw002;
    public static int CodigoRegistro;
    public static String MensajeRegistro;
    static   int cont=1;
     class ExampleViewHolder extends ViewHolder {
        TextView textProducto;
         TextView textCantidadUnidades;
         TextView textCantidadCajas;
         TextView textCantidadCajetillas;
         TextView textBarra;
         TextView txtFamilia;
         TextView textCantidadTotal;
         TextView txtGrupo;
         TextView txt_ultimo_unidades;
         TextView txt_ultimo_cajas;
         TextView txt_ultimo_cajetillas;
         TextView lbl_cajetilla;


          RelativeLayout relative;
     //    TextView textArea;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textCantidadUnidades = (TextView) itemView.findViewById(R.id.txt_cant_unidades);
            this.textCantidadCajas = (TextView) itemView.findViewById(R.id.txt_cant_cajas);
            this.textCantidadCajetillas = (TextView) itemView.findViewById(R.id.txt_cant_cajetillas);
            this.textCantidadTotal = (TextView) itemView.findViewById(R.id.txt_cantidadTotal);
            this.textBarra = (TextView) itemView.findViewById(R.id.txt_barra);
            this.txtFamilia = (TextView) itemView.findViewById(R.id.txt_familia);
            this.txtGrupo = (TextView) itemView.findViewById(R.id.txt_grupo);
            this.lbl_cajetilla = (TextView) itemView.findViewById(R.id.lbl_cajetilla);

            this.txt_ultimo_unidades = (TextView) itemView.findViewById(R.id.txt_ultimo_unidades);
            this.txt_ultimo_cajas = (TextView) itemView.findViewById(R.id.txt_ultimo_cajas);
            this.txt_ultimo_cajetillas = (TextView) itemView.findViewById(R.id.txt_ultimo_cajetillas);

            this.relative =  itemView.findViewById(R.id.relative);

        }
    }

    Stkw002Adapter(List<Stkw002Item> listaStkw002) {
        this.listaStkw002 = listaStkw002;
      //  this.listaStkw002Full = new ArrayList(listaStkw0022);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_productos, parent, false));
    }

    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        cont=1;
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);

        holder.textProducto.setText(currentItem.getCod_articulo()+" "+ currentItem.getProducto());
        //holder.textCantidad.setText(currentItem.getCantidad());
        holder.textCantidadUnidades.setText("0");
        holder.textCantidadCajas.setText("0");
        holder.textCantidadCajetillas.setText("0");

        holder.textCantidadTotal.setText(currentItem.getCantidadTotal());
        holder.textBarra.setText(currentItem.getCodBarra());
        holder.txtGrupo.setText("Gupo:"+currentItem.getGrupo());
        holder.txtFamilia.setText("Familia:"+currentItem.getFamilia());
        holder.txt_ultimo_unidades.setText(currentItem.getUltimo());

        if(listaStkw002.get(position).getCantidad_cajetillasBD()==999999 ||listaStkw002.get(position).getCantidad_cajetillasBD()==99999){
            holder.textCantidadCajetillas.setVisibility(View.GONE);
            holder.lbl_cajetilla.setVisibility(View.GONE);
            holder.txt_ultimo_cajetillas.setVisibility(View.GONE);

        }

    holder.textCantidadUnidades.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {   int cantidad=0;

                    if(holder.textCantidadUnidades.getText().toString().trim().length()==0||holder.textCantidadUnidades.getText().toString().trim().equals("-")){
                        holder.textCantidadUnidades.setText("0");
                    }
                    if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                        holder.textCantidadTotal.setText("0");
                    }
                    listaStkw002.get(position).setCantidadUnitaria(Integer.parseInt(holder.textCantidadUnidades.getText().toString().trim()) );

                    listaStkw002.get(position).setCantidadTotal( String.valueOf(listaStkw002.get(position).getCantidadUnitaria() +Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));

                    listaStkw002.get(position).setUltimo("Ultimo ingresado:"+holder.textCantidadUnidades.getText());
                   // holder.textCantidadTotal.setText(String.valueOf(Integer.parseInt(holder.textCantidadUnidades.getText().toString().trim())+Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));
                    holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );

                     for (int i = 0; i < listaStkw002.size(); i++)
                    {
                          cantidad =cantidad+ listaStkw002.get(i).getCantidadUnitaria();
                    }
                    stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                    holder.txt_ultimo_unidades.setText("Ultimo ingresado:"+holder.textCantidadUnidades.getText());
                    holder.textCantidadUnidades.setText("0");
                 }
            }
        });

    holder.textCantidadCajas.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {   int cantidad=0;

                    if(holder.textCantidadCajas.getText().toString().trim().length()==0||holder.textCantidadCajas.getText().toString().trim().equals("-")){
                        holder.textCantidadCajas.setText("0");
                    }
                    if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                        holder.textCantidadTotal.setText("0");
                    }
                    listaStkw002.get(position).setCantidad_cajas(Integer.parseInt(holder.textCantidadCajas.getText().toString().trim()));
                    listaStkw002.get(position).setCantidadTotal( String.valueOf((Integer.parseInt(holder.textCantidadCajas.getText().toString().trim())*listaStkw002.get(position).getCantidad_cajasBD())+Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));
                    listaStkw002.get(position).setUltimo("Ultimo ingresado:"+holder.textCantidadCajas.getText());
                     holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );


                    for (int i = 0; i < listaStkw002.size(); i++)
                    {
                        cantidad =cantidad+ listaStkw002.get(i).getCantidad_cajas();
                    }
                    stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                    holder.txt_ultimo_cajas.setText("Ultimo ingresado:"  +holder.textCantidadCajas.getText());
                    holder.textCantidadCajas.setText("0");
                 }
            }
        });

    holder.textCantidadCajetillas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {  int cantidad=0;

                    if(holder.textCantidadCajetillas.getText().toString().trim().length()==0||holder.textCantidadCajetillas.getText().toString().trim().equals("-")){
                        holder.textCantidadCajetillas.setText("0");
                    }
                    if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                        holder.textCantidadTotal.setText("0");
                    }
                    listaStkw002.get(position).setCantidad_cajetillas(Integer.parseInt(holder.textCantidadCajetillas.getText().toString().trim()));
                    listaStkw002.get(position).setCantidadTotal( String.valueOf((Integer.parseInt(holder.textCantidadCajetillas.getText().toString().trim())*listaStkw002.get(position).getCantidad_cajetillasBD())+Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));
                    listaStkw002.get(position).setUltimo("Ultimo ingresado:"+holder.textCantidadCajetillas.getText());
                    holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );


                    for (int i = 0; i < listaStkw002.size(); i++)
                    {
                        cantidad =cantidad+ listaStkw002.get(i).getCantidad_cajetillas();
                    }
                    stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                    holder.txt_ultimo_cajas.setText("Ultimo ingresado:"  +holder.textCantidadCajetillas.getText());
                    holder.textCantidadCajetillas.setText("0");

                }
            }
        });




    }

    public int getItemCount() {
        return this.listaStkw002.size();
    }

    /* access modifiers changed from: 0000 */
   public void setFilter(List<Stkw002Item> filterdNames) {

        this.listaStkw002 = filterdNames;
        notifyDataSetChanged();
     }

    public static void registrar_inventario(){

        try {
            SQLiteDatabase db_UPDATE= controles.conSqlite.getReadableDatabase();
            for (int i = 0; i < listaStkw002.size(); i++) {
                String cantidad =listaStkw002.get(i).getCantidadTotal();
                String secuencia =listaStkw002.get(i).getSecuencia();
                String codArticulo =listaStkw002.get(i).getCod_articulo();
                String tomaRegistro =listaStkw002.get(i).getTomaRegistro();
                if(cantidad.length()==0)
                {
                    cantidad="0";
                }
                if(tomaRegistro.equals("R")){ // SI ES IGUAL A "R", ENTONCES SIGNIFICA, QUE ES UN REINVENTARIO. QUE SE VOLVIO A INVENTARIAR, LUEGO DE EXPORTAR.
                    db_UPDATE.execSQL(" update STKW002INV set  estado ='F' ,winvd_cant_inv ='"+cantidad +"', WINVE_LOGIN_CERRADO_WEB='"+variables.userdb+"' " +
                            " where winvd_nro_inv="+ variables.nro_registro_toma+" and winvd_art="+codArticulo);
                }
                else {
                    db_UPDATE.execSQL(" update STKW002INV set  estado ='P' ,winvd_cant_inv ='"+cantidad +"', WINVE_LOGIN_CERRADO_WEB='"+variables.userdb+"' " +
                            " where winvd_nro_inv="+ variables.nro_registro_toma+" and winvd_art="+codArticulo);

                }

                // SOLO SE COMPARA POR NRO DE INVENTARIO MAS EL NRO DE SECUENCIA.
            }
            CodigoRegistro= 0;
            MensajeRegistro="Registrado con éxito.";
   }catch(Exception e)
        {
            CodigoRegistro= 1;
            MensajeRegistro=e.getMessage();
         }

    }

}