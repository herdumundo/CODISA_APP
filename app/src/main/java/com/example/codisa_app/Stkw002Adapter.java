package com.example.codisa_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class Stkw002Adapter extends Adapter<Stkw002Adapter.ExampleViewHolder> {
    private static List<Stkw002Item> listaStkw002;
    public static int CodigoRegistro;
    public static String MensajeRegistro;

     class ExampleViewHolder extends ViewHolder {
        TextView textProducto;
        TextView textCantidad;
         TextView textLote;
     //    TextView textArea;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textCantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
            this.textLote = (TextView) itemView.findViewById(R.id.txt_lote_inv);
       //     this.textArea = (TextView) itemView.findViewById(R.id.txt_area);
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
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
        holder.textProducto.setText(currentItem.getCodArticulo()+" "+ currentItem.getProducto());
        holder.textCantidad.setText(currentItem.getCantidad());
    //    holder.textArea.setText(currentItem.getArea());
        holder.textLote.setText("LOTE:"+currentItem.getLote()+"  VTO.:"+currentItem.getVencimiento());

  holder.textCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    listaStkw002.get(position).setCantidad(holder.textCantidad.getText().toString().trim());

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
                String cantidad =listaStkw002.get(i).getCantidad();
                String lote =listaStkw002.get(i).getLote();
                String cod_articulo =listaStkw002.get(i).getCodArticulo();
                String fecha_vto =listaStkw002.get(i).getVencimiento();
                String secuencia =listaStkw002.get(i).getSecuencia();
                if(cantidad.length()==0)
                {
                    cantidad="0";
                }
                db_UPDATE.execSQL(" update STKW002INV set  estado ='P' ,winvd_cant_inv ='"+cantidad +"', WINVE_LOGIN_CERRADO_WEB='"+variables.userdb+"' " +
                        " where winvd_nro_inv="+ variables.nro_registro_toma+" and winvd_secu="+secuencia);
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