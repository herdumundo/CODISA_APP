package com.example.codisa_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import Utilidades.*;
import java.util.ArrayList;

public class stkw002 extends AppCompatActivity {
    EditText Searchtext,txt_cantidad;
    private  Stkw002Adapter adapter;
    RecyclerView recyclerView ;

     public void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.stkw002);
        recyclerView= (RecyclerView) findViewById( R.id.RecyclerView);
         controles.llenar_lista_stkw002();
        listar_recicler();
        this.Searchtext = (EditText) findViewById(R.id.search_input);
        this.txt_cantidad = (EditText) findViewById(R.id.txt_cantidad);
         this.Searchtext.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //MainActivity.this.filter(charSequence.toString());
            }
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        } );
     }



    private void listar_recicler()
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new Stkw002Adapter(controles.ListArrayInventarioArticulos);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);
    }

    private void filter(String text){
        try {

            for (int i = 0; i < adapter.getItemCount(); i++)
            {
                String cantidad =   ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_cantidad)).getText().toString();
                String position =   ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_posicion)).getText().toString();
                String producto =   ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_producto)).getText().toString();
                String lote =       ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_lote_inv)).getText().toString();
                controles.ListArrayInventarioArticulos.set( Integer.parseInt(position), new Stkw002Item( producto, position, cantidad,lote));

            }
            ArrayList<Stkw002Item> filteredList = new ArrayList<>();
            for (Stkw002Item item : controles.ListArrayInventarioArticulos )
            {
                int as =adapter.getItemCount();
                if(item.getProducto().toLowerCase().contains(text) || item.getPosicion().toLowerCase().contains(text)|| item.getLote().toLowerCase().contains(text))
                {
                    filteredList.add(item);
                }

            }
            adapter.setFilter(filteredList);

            // Toast.makeText(this,item.getText3(),Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            String error =e.toString();
        }
    }


}