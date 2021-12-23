package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;

public class ConsultaAdapter extends Adapter<ConsultaAdapter.ExampleViewHolder> {
    private static List<Stkw002Item> listaStkw002;

    static   int cont=1;
     class ExampleViewHolder extends ViewHolder {
            TextView txtArticulo;
             TextView txtCantidad;
         TextView txtCodArticulo;
         TextView txtCodLote;

         RelativeLayout relative;

        ExampleViewHolder(View itemView)
        {
            super(itemView);
            this.txtCodArticulo = (TextView) itemView.findViewById(R.id.txt_columna1);
            this.txtCantidad = (TextView) itemView.findViewById(R.id.txt_columna2);
            this.txtArticulo = (TextView) itemView.findViewById(R.id.txt_columna3);
            this.txtCodLote = (TextView) itemView.findViewById(R.id.txt_columna4);
        }
    }

    ConsultaAdapter(List<Stkw002Item> listaStkw002) {
        this.listaStkw002 = listaStkw002;
     }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_columnas_consulta, parent, false));
    }

    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        cont=0;
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
        holder.txtArticulo.setText(currentItem.getProducto());
        holder.txtCantidad.setText(currentItem.getCantidadTotal() );
        holder.txtCodArticulo.setText(currentItem.getCod_articulo() );
        holder.txtCodLote.setText(currentItem.getLote() );

    }

    public int getItemCount() {
        return this.listaStkw002.size();
    }

    /* access modifiers changed from: 0000 */
   public void setFilter(List<Stkw002Item> filterdNames) {
        this.listaStkw002 = filterdNames;
        notifyDataSetChanged();
     }


}