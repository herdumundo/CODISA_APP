package com.example.codisa_app;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Utilidades.Stkw002List;
import Utilidades.controles;
import Utilidades.variables;

public class lista_stkw001_inv extends AppCompatActivity {
    ListView listView;
    ArrayList<Stkw002List> listaStkw001;

    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, menu_principal.class,"",4);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_stkw001_inv);
        listView =(ListView)findViewById(R.id.listViewInvStkw001);
        ConsultarTomasServer();
    }

    //SE CONSULTA LA TOMA QUE GENERO EL USUARIO, PARA PODER CANCELARLO.
    private void ConsultarTomasServer() {
        try {

            controles.connect = controles.conexion.Connections();
            Statement stmt = controles.connect.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT winve_numero,winve_fec,flia_desc,grup_desc " +
                    "FROM WEB_INVENTARIO inner join V_WEB_FLIA on WEB_INVENTARIO.WINVE_FLIA=V_WEB_FLIA.FLIA_CODIGO " +
                    "inner join V_WEB_GRUPO on WEB_INVENTARIO.WINVE_GRUPO=V_WEB_GRUPO.GRUP_CODIGO AND  V_WEB_FLIA.FLIA_CODIGO=V_WEB_GRUPO.GRUP_FAMILIA " );

            Stkw002List Stkw001List=null;
            listaStkw001=new ArrayList<Stkw002List>();
            while (rs.next())
            {
                Stkw001List=new Stkw002List();
                Stkw001List.setNroToma(rs.getString("WINVE_NUMERO"));
                Stkw001List.setFechaToma(rs.getString("WINVE_FEC"));
                Stkw001List.setFamilia(rs.getString("flia_desc"));
                Stkw001List.setGrupo(rs.getString("grup_desc"));
                listaStkw001.add(Stkw001List);
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.listitem3, R.id.text1, listaStkw001) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    text1.setText("NRO. DE TOMA: "+listaStkw001.get(position).getNroToma());
                    text2.setText("FAMILIA: "+listaStkw001.get(position).getFamilia());
                    text3.setText("GRUPO:"+listaStkw001.get(position).getGrupo()+"  FECHA DE GENERACION: "+listaStkw001.get(position).getFechaToma());

                    return view;
                }
            };
            listView.setAdapter(adapter);
        }
        catch (Exception e){
            String err=e.toString();
        }
    }
}