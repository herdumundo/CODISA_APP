package com.example.codisa_app;

import android.os.Bundle;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Connection_Oracle;
import androidx.appcompat.app.AppCompatActivity;

public class test_con extends AppCompatActivity {


    public static Connection connect ;
    public static Connection_Oracle conexion = new Connection_Oracle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_con);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String asd="";
        try {
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from test_cab");
            while ( rs.next())
            {

                asd=  rs.getString("descripcion") ;

            }

            rs.close();
            connect.close();

        }catch(Exception e){
            String mensaje_importador=e.toString();
        }



    }
}