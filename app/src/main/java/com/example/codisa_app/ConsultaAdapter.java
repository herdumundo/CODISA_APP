package com.example.codisa_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;

public class ConsultaAdapter extends Adapter<ConsultaAdapter.ExampleViewHolder> {
    private static List<Stkw002Item> listaStkw002;
    public static Activity activity;
    static   int cont=1;
     class ExampleViewHolder extends ViewHolder {
            TextView txt_columna3;
             TextView txt_columna2;
         TextView txt_columna1;
          LinearLayout linearItem;
         RelativeLayout relative;

        ExampleViewHolder(View itemView)
        {
            super(itemView);
            this.txt_columna1 = (TextView) itemView.findViewById(R.id.txt_columna1);
            this.txt_columna2 = (TextView) itemView.findViewById(R.id.txt_columna2);
            this.txt_columna3 = (TextView) itemView.findViewById(R.id.txt_columna3);
            this.linearItem = (LinearLayout) itemView.findViewById(R.id.linearItem);
        }
    }
    /**
     * Esta clase es utilizada para rellenar los txt del layout con el arrayList
     */
    ConsultaAdapter(List<Stkw002Item> listaStkw002) {
        ConsultaAdapter.listaStkw002 = listaStkw002;
     }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_columnas_consulta, parent, false));
    }

    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position)
    {   if(variables.tipo_lista==0){

        cont=0;
        Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
        holder.txt_columna1.setText(currentItem.getCod_articulo() );
        holder.txt_columna2.setText(currentItem.getCantidadTotal() );
        holder.txt_columna3.setText(currentItem.getProducto());

        holder.linearItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Datos: "+currentItem.getProducto().toString(), Toast.LENGTH_SHORT).show();
                stkw004.consultar_articulos_lotes(activity,currentItem.getCod_articulo());
            }
        });
        }
        else
        {
            cont=0;
            Stkw002Item currentItem = (Stkw002Item) this.listaStkw002.get(position);
            holder.txt_columna1.setText(String.valueOf(currentItem.getCantidadUnitaria()) );
            holder.txt_columna2.setText(currentItem.getProducto());
            holder.txt_columna3.setText( currentItem.getLote());

            holder.linearItem.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    controles.nroTomaConsolidado=currentItem.getCantidadUnitaria();
                    final controles.AsyncListarConsolidados task = new controles.AsyncListarConsolidados();
                    task.execute();

                Toast.makeText(v.getContext(), "Datos: "+String.valueOf(currentItem.getCantidadUnitaria()), Toast.LENGTH_SHORT).show();
                //  consulta_articulos.consultar_articulos_lotes(activity,currentItem.getCod_articulo());
                }
            });
        }
    }

    public int getItemCount() {
        return this.listaStkw002.size();
    }

    public void setFilter(List<Stkw002Item> filterdNames) {
        this.listaStkw002 = filterdNames;
        notifyDataSetChanged();
     }


}