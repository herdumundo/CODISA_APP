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
import java.util.HashMap;
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
             TextView textCantidadGruesa;
             TextView textBarra;
             TextView txtFamilia;
             TextView textCantidadTotal;
             TextView txtGrupo;
             TextView txt_ultimo_unidades;
             TextView txt_ultimo_cajas;
             TextView txt_ultimo_gruesa;
             TextView lbl_gruesa;
         RelativeLayout relative;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textCantidadUnidades = (TextView) itemView.findViewById(R.id.txt_cant_unidades);
            this.textCantidadCajas = (TextView) itemView.findViewById(R.id.txt_cant_cajas);
            this.textCantidadGruesa = (TextView) itemView.findViewById(R.id.txt_cant_cajetillas);
            this.textCantidadTotal = (TextView) itemView.findViewById(R.id.txt_cantidadTotal);
            this.textBarra = (TextView) itemView.findViewById(R.id.txt_barra);
            this.txtFamilia = (TextView) itemView.findViewById(R.id.txt_familia);
            this.txtGrupo = (TextView) itemView.findViewById(R.id.txt_grupo);
            this.lbl_gruesa = (TextView) itemView.findViewById(R.id.lbl_gruesa);
            this.txt_ultimo_unidades = (TextView) itemView.findViewById(R.id.txt_ultimo_unidades);
            this.txt_ultimo_cajas = (TextView) itemView.findViewById(R.id.txt_ultimo_cajas);
            this.txt_ultimo_gruesa = (TextView) itemView.findViewById(R.id.txt_ultimo_gruesa);
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

    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        cont=0;
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
        holder.textProducto.setText(currentItem.getCod_articulo()+" "+ currentItem.getProducto());
        holder.textCantidadUnidades.setText("0");
        holder.textCantidadCajas.setText("0");
        holder.textCantidadGruesa.setText("0");

        holder.textCantidadTotal.setText(currentItem.getCantidadTotal());
        holder.textBarra.setText(currentItem.getCodBarra());
        holder.txtGrupo.setText("Gupo:"+currentItem.getGrupo());
        holder.txtFamilia.setText("Familia:"+currentItem.getFamilia());
        holder.txt_ultimo_unidades.setText(currentItem.getUltimo_unidades());
        holder.txt_ultimo_cajas.setText(currentItem.getUltimo_cajas());
        holder.txt_ultimo_gruesa.setText(currentItem.getUltimo_gruesa());


      //  if(listaStkw002.get(position).getId_familia()!=3)
            if(String.valueOf(currentItem.getId_familia()).equals("3"))
            {
                holder.textCantidadGruesa.setVisibility(View.VISIBLE);
                holder.lbl_gruesa.setVisibility(View.VISIBLE);
                holder.txt_ultimo_gruesa.setVisibility(View.VISIBLE);
                System.out.println(position);
            }
            else{
                holder.textCantidadGruesa.setVisibility(View.GONE);
                holder.lbl_gruesa.setVisibility(View.GONE);
                holder.txt_ultimo_gruesa.setVisibility(View.GONE);
                System.out.println(position);
            }

    holder.textCantidadUnidades.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {   int cantidad=0;
                    if(holder.textCantidadUnidades.getText().toString().equals("") ||holder.textCantidadUnidades.getText().toString().equals("-")){
                        holder.textCantidadUnidades.setText("0");
                    }
                if (Integer.parseInt(holder.textCantidadUnidades.getText().toString())!=0)
                    { // SI ES DISTINTO A CERO, ENTONCES INGRESARA EN ULTIMO INGRESADO LA CANTIDAD. ESTA VALIDACION ES REALIZADA
                        // PORQUE AL HACER EL FOCO EN EL TEXTO CON CANTIDAD CERO, INGRESA ESA CANTIDAD COMO LA ULTIMA
                        if(holder.textCantidadUnidades.getText().toString().trim().length()==0||holder.textCantidadUnidades.getText().toString().trim().equals("-")){
                            holder.textCantidadUnidades.setText("0");
                        }
                        if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                            holder.textCantidadTotal.setText("0");
                        }
                        listaStkw002.get(position).setCantidadUnitaria(Integer.parseInt(holder.textCantidadUnidades.getText().toString().trim()) );

                        listaStkw002.get(position).setCantidadTotal( String.valueOf(listaStkw002.get(position).getCantidadUnitaria() +Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));

                        listaStkw002.get(position).setUltimo_unidades("Ultimo ingresado:"+holder.textCantidadUnidades.getText());
                        holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );
                        holder.txt_ultimo_unidades.setText( listaStkw002.get(position).getUltimo_unidades());

                         for (int i = 0; i < listaStkw002.size(); i++)
                        {
                              cantidad =cantidad+ Integer.parseInt(listaStkw002.get(i).getCantidadTotal());
                        }
                        stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                        holder.textCantidadUnidades.setText("0");
                    }
                }
            }
        });

    holder.textCantidadCajas.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {   int cantidad=0;
                    if (Integer.parseInt(holder.textCantidadCajas.getText().toString())!=0)
                    { // SI ES DISTINTO A CERO, ENTONCES INGRESARA EN ULTIMO INGRESADO LA CANTIDAD. ESTA VALIDACION ES REALIZADA
                        // PORQUE AL HACER EL FOCO EN EL TEXTO CON CANTIDAD CERO, INGRESA ESA CANTIDAD COMO LA ULTIMA

                        if(holder.textCantidadCajas.getText().toString().trim().length()==0||holder.textCantidadCajas.getText().toString().trim().equals("-")){
                        holder.textCantidadCajas.setText("0");
                    }
                    if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                        holder.textCantidadTotal.setText("0");
                    }
                    listaStkw002.get(position).setCantidad_cajas(Integer.parseInt(holder.textCantidadCajas.getText().toString().trim()));
                    listaStkw002.get(position).setCantidadTotal( String.valueOf((Integer.parseInt(holder.textCantidadCajas.getText().toString().trim())*listaStkw002.get(position).getCantidad_cajasBD())+Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));
                    listaStkw002.get(position).setUltimo_cajas("Ultimo ingresado:"+holder.textCantidadCajas.getText());
                     holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );


                    for (int i = 0; i < listaStkw002.size(); i++)
                    {
                        cantidad =cantidad+ Integer.parseInt(listaStkw002.get(i).getCantidadTotal());
                    }
                    stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                    holder.txt_ultimo_cajas.setText(listaStkw002.get(position).getUltimo_cajas());
                    holder.textCantidadCajas.setText("0");
                    }
                }
            }
        });

    holder.textCantidadGruesa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {  int cantidad=0;

                    if(holder.textCantidadGruesa.getText().toString().equals("") ||holder.textCantidadGruesa.getText().toString().equals("-")){
                        holder.textCantidadGruesa.setText("0");
                    }
                    if(holder.textCantidadGruesa.getText().toString().equals("") ||holder.textCantidadGruesa.getText().toString().equals("-")){
                        holder.textCantidadGruesa.setText("0");
                    }
                    if (Integer.parseInt(holder.textCantidadGruesa.getText().toString())!=0)
                    { // SI ES DISTINTO A CERO, ENTONCES INGRESARA EN ULTIMO INGRESADO LA CANTIDAD. ESTA VALIDACION ES REALIZADA
                        // PORQUE AL HACER EL FOCO EN EL TEXTO CON CANTIDAD CERO, INGRESA ESA CANTIDAD COMO LA ULTIMA
                        if(holder.textCantidadGruesa.getText().toString().trim().length()==0||holder.textCantidadGruesa.getText().toString().trim().equals("-")){
                            holder.textCantidadGruesa.setText("0");
                        }
                        if(holder.textCantidadTotal.getText().toString().trim().length()==0){
                            holder.textCantidadTotal.setText("0");
                        }
                        listaStkw002.get(position).setCantidad_gruesa(Integer.parseInt(holder.textCantidadGruesa.getText().toString().trim()));
                        listaStkw002.get(position).setCantidadTotal( String.valueOf((Integer.parseInt(holder.textCantidadGruesa.getText().toString().trim())*listaStkw002.get(position).getCantidad_gruesaBD())+Integer.parseInt(holder.textCantidadTotal.getText().toString().trim())));
                        listaStkw002.get(position).setUltimo_gruesa("Ultimo ingresado:"+holder.textCantidadGruesa.getText());
                        holder.textCantidadTotal.setText(  listaStkw002.get(position).getCantidadTotal() );


                        for (int i = 0; i < listaStkw002.size(); i++)
                        {
                            cantidad =cantidad+ Integer.parseInt(listaStkw002.get(i).getCantidadTotal());
                        }
                        stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");

                        holder.txt_ultimo_gruesa.setText(listaStkw002.get(position).getUltimo_gruesa());
                        holder.textCantidadGruesa.setText("0");
                    }


                }
            }
        });
        cont++;

       // holder.textCantidadUnidades.setText(String.valueOf(cont));

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