package com.example.codisa_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Utilidades.*;
import java.util.ArrayList;

public class stkw002 extends AppCompatActivity {
    public void onBackPressed()  {
        Utilidades.controles.volver_atras(this,this,  lista_stkw002_inv.class,"DESEA SALIR DEL REGISTRO DE INVENTARIO'?",1);
    }
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
        controles.conexion_sqlite(this);

        this.Searchtext = (EditText) findViewById(R.id.search_input);
        this.txt_cantidad = (EditText) findViewById(R.id.txt_cantidad);
        this.Searchtext.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }
            public void afterTextChanged(Editable editable) {
            //    filter(editable.toString());
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

            ArrayList<Stkw002Item> filteredList = new ArrayList<>();
            for (Stkw002Item item : controles.ListArrayInventarioArticulos )
            {
                if(item.getProducto().toLowerCase().contains(text) || item.getPosicion().toLowerCase().contains(text) )
                {
                    filteredList.add(item);
                }
              // Toast.makeText(this,item.getProducto(),Toast.LENGTH_LONG).show();
            }
            adapter.setFilter(filteredList);


        }
        catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }
    }

public void reg(View v){
    Stkw002Adapter.registrar_inventario();
}

}