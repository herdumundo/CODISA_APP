package com.example.codisa_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Utilidades.ExampleItem;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class ExampleAdapter extends Adapter<ExampleAdapter.ExampleViewHolder> {
    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;

    class ExampleViewHolder extends ViewHolder {
        ImageView imageView;
        TextView textProducto;
        TextView textPosicion;
        TextView textCantidad;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_app);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textPosicion = (TextView) itemView.findViewById(R.id.txt_posicion);
            this.textCantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
        }
    }

    ExampleAdapter(List<ExampleItem> exampleList2) {
        this.exampleList = exampleList2;
        this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_productos, parent, false));
    }

    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = (ExampleItem) this.exampleList.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textProducto.setText(currentItem.getProducto());
        holder.textPosicion.setText(currentItem.getPosicion());
        holder.textCantidad.setText(currentItem.getCantidad());
    }

    public int getItemCount() {
        return this.exampleList.size();
    }

    /* access modifiers changed from: 0000 */
    public void setFilter(List<ExampleItem> filterdNames) {
        this.exampleList = filterdNames;
        notifyDataSetChanged();
     }


}