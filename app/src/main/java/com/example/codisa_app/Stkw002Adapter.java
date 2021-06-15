package com.example.codisa_app;

import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Utilidades.Stkw002Item;
import Utilidades.controles;
import Utilidades.variables;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class Stkw002Adapter extends Adapter<Stkw002Adapter.ExampleViewHolder> {
    private static List<Stkw002Item> exampleList;
 //   private List<Stkw002Item> exampleListFull;

    class ExampleViewHolder extends ViewHolder {
       // ImageView imageView;
        TextView textProducto;
       // TextView textPosicion;
        TextView textCantidad;
        TextView textLote;

        ExampleViewHolder(View itemView) {
            super(itemView);
            this.textProducto = (TextView) itemView.findViewById(R.id.txt_producto);
            this.textCantidad = (TextView) itemView.findViewById(R.id.txt_cantidad);
            this.textLote = (TextView) itemView.findViewById(R.id.txt_lote_inv);
        }
    }

    Stkw002Adapter(List<Stkw002Item> exampleList2) {
        this.exampleList = exampleList2;
      //  this.exampleListFull = new ArrayList(exampleList2);
    }

    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_productos, parent, false));
    }

    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Stkw002Item currentItem = (Stkw002Item) this.exampleList.get(position);
        holder.textProducto.setText(currentItem.getCodArticulo()+" "+ currentItem.getProducto());
        // holder.textPosicion.setText(currentItem.getPosicion());
        holder.textCantidad.setText(currentItem.getCantidad());
        holder.textLote.setText("LOTE:"+currentItem.getLote()+"  VTO.:"+currentItem.getVencimiento());
 //ESTA SENTENCIA SE UTILIZA PARA QUE AL CAMBIAR EL TEXT, YA EJECUTE LA ACTUALIZACION DEL ARRAYLIST
     /*   holder.textCantidad.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                exampleList.get(position).setCantidad(holder.textCantidad.getText().toString().trim());
            }
            public void afterTextChanged(Editable editable) {
             }
        } );
*/ holder.textCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    exampleList.get(position).setCantidad(holder.textCantidad.getText().toString().trim());

                }
            }
        });
         }

    public int getItemCount() {
        return this.exampleList.size();
    }

    /* access modifiers changed from: 0000 */
   public void setFilter(List<Stkw002Item> filterdNames) {

        this.exampleList = filterdNames;
        notifyDataSetChanged();
     }

    public static void registrar_inventario(){

        try {
            SQLiteDatabase db_UPDATE= controles.conSqlite.getReadableDatabase();
            for (int i = 0; i < exampleList.size(); i++) {
                String cantidad =exampleList.get(i).getCantidad();
                String lote =exampleList.get(i).getLote();
                String cod_articulo =exampleList.get(i).getCodArticulo();
                String fecha_vto =exampleList.get(i).getVencimiento();
                db_UPDATE.execSQL(" update STKW002INV set  estado ='P' ,winvd_cant_inv ='"+cantidad +"' " +
                        " where winvd_nro_inv="+ variables.nro_registro_toma+" and winvd_art="+cod_articulo+" and winvd_lote='"+lote+"'  and date(  winvd_fec_vto)='"+fecha_vto+"'");
                // A FECHA DE VENCIMIENTO SE LE PARSEO A DATE, PARA QUE DEVUELBA YYYY-MM-DD, YA QUE EL VALOR QUE RECIBIMOS YA SE ENCUENTRA TAMBIEN PARSEADO.
            }
   }catch(Exception e)
        {
            // Toast.makeText(context, e.toString() ,Toast.LENGTH_LONG).show();
            String eas=e.toString();
        }

    }
}