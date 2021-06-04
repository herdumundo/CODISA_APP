package com.example.codisa_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Utilidades.ExampleItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class registro_inventario extends AppCompatActivity {
    EditText Searchtext,txt_cantidad;
    private com.example.codisa_app.ExampleAdapter adapter;
    ImageButton bt_mic;
    private List<ExampleItem> exampleList;
    private List<ExampleItem> examples;
    RecyclerView recyclerView ;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.registro_inventario);
        recyclerView= (RecyclerView) findViewById( R.id.RecyclerView);
        fillExampleList();
        listar_recicler();
        //initToolbar();
     
        this.Searchtext = (EditText) findViewById(R.id.search_input);
        this.txt_cantidad = (EditText) findViewById(R.id.txt_cantidad);
         this.Searchtext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //MainActivity.this.filter(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void fillExampleList()
    {
        exampleList = new ArrayList();
        exampleList.add(new ExampleItem( R.drawable.ic_filter_list_black_24dp, "CIGARRILLOS KENT", "0","1"));
        exampleList.add(new ExampleItem( R.drawable.ic_filter_list_black_24dp, "CHOCOLATE BATON", "1","2"));
        exampleList.add(new ExampleItem( R.drawable.ic_filter_list_black_24dp, "CHOCOLATE MILKA", "2","3"));


    }

    private void listar_recicler()
    {
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(this);
        this.adapter = new com.example.codisa_app.ExampleAdapter(this.exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);
    }



    private void filter(String text){
        try {

            for (int i = 0; i < adapter.getItemCount(); i++)
            {
                String cantidad =  ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_cantidad)).getText().toString();
                String position = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_posicion)).getText().toString();
                String producto = ((TextView) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.txt_producto)).getText().toString();
                exampleList.set( Integer.parseInt(position) ,   new ExampleItem( R.drawable.ic_filter_list_black_24dp, producto, position, cantidad));

            }
            ArrayList<ExampleItem> filteredList = new ArrayList<>();
            for (ExampleItem item : exampleList )
            {
                int as =adapter.getItemCount();
                if(item.getProducto().toLowerCase().contains(text) || item.getPosicion().toLowerCase().contains(text))
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