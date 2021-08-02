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
        TextView textCantidad;
         TextView textBarra;
         TextView txtFamilia;
         TextView txtGrupo;
         RelativeLayout relative;
     //    TextView textArea;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textCantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
            this.textBarra = (TextView) itemView.findViewById(R.id.txt_barra);
            this.txtFamilia = (TextView) itemView.findViewById(R.id.txt_familia);
            this.txtGrupo = (TextView) itemView.findViewById(R.id.txt_grupo);
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

    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        cont=1;
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
        holder.textProducto.setText(currentItem.getCodArticulo()+" "+ currentItem.getProducto());
        holder.textCantidad.setText(currentItem.getCantidad());
        holder.textBarra.setText(currentItem.getCodBarra());
        holder.txtGrupo.setText("Gupo:"+currentItem.getgrupo());
        holder.txtFamilia.setText("Familia:"+currentItem.getfamilia());



  holder.textCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {   int cantidad=0;
                    listaStkw002.get(position).setCantidad(holder.textCantidad.getText().toString().trim());
                    for (int i = 0; i < listaStkw002.size(); i++) {
                          cantidad =cantidad+Integer.parseInt(listaStkw002.get(i).getCantidad());
                    }
                    stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ controles.contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");
                 }
            }
        });
      //  int as=cont;
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
                String cantidad =listaStkw002.get(i).getCantidad();
                String secuencia =listaStkw002.get(i).getSecuencia();
                String codArticulo =listaStkw002.get(i).getCodArticulo();
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