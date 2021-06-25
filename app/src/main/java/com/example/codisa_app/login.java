package com.example.codisa_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import Utilidades.Connection_Oracle;
import Utilidades.controles;
import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity
    {
    public void onBackPressed()  {
            Utilidades.controles.volver_atras(this,this,  login.class,"DESEA SALIR DE LA APLICACION?",5);
        }
        Connection connection=null;
        Connection connect;
        Connection_Oracle conexion = new Connection_Oracle();

        TextView txt_usuario,txt_pass;
        String mensaje,passwd,user="";
        ProgressDialog pdLoading;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            txt_usuario=(TextView)findViewById(R.id.txt_usuario);
            txt_pass=(TextView)findViewById(R.id.txt_pass);
            getSupportActionBar().hide();
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
           // /getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>CODISA APP V.1.0 </font>"));
            controles.conexion_sqlite(this);

           // Multi t1=new Multi();
            //t1.start();
        }
        public void login (View v)
        {
            final AsyncCaller task = new AsyncCaller();
            task.execute();

        }


        class AsyncCaller extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading = ProgressDialog.show(login.this, "VERIFICANDO",
                        "ESPERE...", true);

            }
            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    user=  txt_usuario.getText().toString().trim().toString();
                    passwd=txt_pass.getText().toString().trim().toString();
                    String url = "jdbc:oracle:thin:@(DESCRIPTION= (ADDRESS=(PROTOCOL=TCP)(HOST=192.168.0.19)(PORT=1521)) (CONNECT_DATA=(SERVICE_NAME=codisaprod)))";
                    String driver = "oracle.jdbc.OracleDriver";
                    StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    if(user.length()==0)
                    {
                        mensaje="INGRESE USUARIO";
                    }
                    else if(passwd.length()==0)
                    {
                        mensaje="INGRESE CONTRASEÑA";
                    }
                    else
                    {
                        Class.forName(driver);
                        DriverManager.setLoginTimeout(5);

                        connection= DriverManager.getConnection(url,user ,passwd );
                        // Toast.makeText(login.this,"REGISTRO EXITOSO",Toast.LENGTH_LONG).show();
                        mensaje="1";
                        variables.userdb=user;
                        variables.passdb=passwd;
                    }
                }
                catch (SQLException se)
                {
                    if(se.getErrorCode()==1017){
                        mensaje="USUARIO O CONTRASEÑA INCORRECTA, FAVOR VERIFIQUE.";
                    }
                    else if (se.getErrorCode()==17002)
                    {
                        mensaje="2";//  mensaje="ERROR DE CONEXION, VERIFIQUE LA RED1.";
                    }
                    else if (se.getErrorCode()==20)
                    {
                        // mensaje="ERROR DE CONEXION, VERIFIQUE LA RED2.";
                          mensaje="2";




                    }
                    else if (se.getErrorCode()==17452)
                    {
                        mensaje="USUARIO O CONTRASEÑA INCORRECTA, FAVOR VERIFIQUE.";
                    }
                    else
                    {
                        mensaje= String.valueOf(se.getErrorCode())  ;
                    }

                }
                catch (ClassNotFoundException e)
                {

                    // Log.e("error here 2 : ", e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                 if(mensaje=="1"){

                    try {
                        int i=0;
                        String contenedor_opciones="";
                        String nombre_usuario="";
                         connect = conexion.Connections();
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery("select distinct formulario,nombre from v_web_operador_rol_prog where login_o='"+user.trim().toUpperCase()+"' " +
                                "and formulario in ('STKW001','STKW002')");
                        while ( rs.next())
                        {
                            if(i==0){
                                contenedor_opciones=rs.getString("formulario") ;
                                nombre_usuario=rs.getString("nombre") ;
                            }
                            else
                            {
                                contenedor_opciones=contenedor_opciones+","+rs.getString("formulario");
                            }

                            i++;
                        }

                        if(i>0){// SI ES MAYOR A CERO, ENTONCES POSEE PERMISOS PARA ACCEDER A LA APP.
                            Utilidades.variables.contenedor_menu=contenedor_opciones;
                            variables.NOMBRE_LOGIN=nombre_usuario;
                             //AQUI SE COLOCARA EL LISTVIEW PARA CONSULTAR LAS SUCURSALES DISPONIBLES. Y LUEGO DE SELECCIONARLA IR AL MENU PRINCIPAL

                            SQLiteDatabase db1= controles.conSqlite.getReadableDatabase();
                            db1.execSQL("delete from USUARIOS_FORMULARIOS_SUCURSALES  WHERE LOGIN_O =upper('"+txt_usuario.getText().toString().trim()+"')");
                            db1.close();
                            Statement stmt2= connect.createStatement();
                            ResultSet rs2 = stmt2.executeQuery("select distinct formulario,nombre,LOGIN_O,SUCURSAL_DESCRIPCION ,ROL_SUCURSAL   " +
                                    "from v_web_operador_rol_prog where  formulario in ('STKW001','STKW002') and login_o='"+user.toUpperCase()+"'");
                            while ( rs2.next())
                            {
                                SQLiteDatabase dblogin=controles.conSqlite.getReadableDatabase();
                                dblogin.execSQL(" INSERT INTO  USUARIOS_FORMULARIOS_SUCURSALES (FORMULARIO,NOMBRE,LOGIN_O,LOGIN_PASS,SUCURSAL_DESCRIPCION,ROL_SUCURSAL) " +
                                        "VALUES ('"+rs2.getString("formulario")+"','"+rs2.getString("nombre")+"','"+rs2.getString("LOGIN_O")+"'," +
                                        "upper('"+txt_pass.getText().toString().trim()+"'),'"+rs2.getString("SUCURSAL_DESCRIPCION")+"'," +
                                        "'"+rs2.getString("ROL_SUCURSAL")+"' )") ;
                                //ESTADO PENDIENTE A INVENTARIAR.
                                dblogin.close();
                            }


                            ListarSucursal();
                        }
                        else {
                            new AlertDialog.Builder(login.this)
                                    .setTitle("ATENCION!!!")
                                    .setMessage("EL USUARIO "+user+" NO POSEE PERMISOS PARA LA APLICACION MOVIL.")
                                    .setNegativeButton("CERRAR", null).show();
                        }


                    }catch (Exception e)
                    {
                        new AlertDialog.Builder(login.this)
                                .setTitle("ATENCION!!!")
                                .setMessage(e.toString())
                                .setNegativeButton("CERRAR", null).show();
                    }

                 }
                 else if(mensaje=="2")
                 {
                     SQLiteDatabase db_consulta= controles.conSqlite.getReadableDatabase();
                     Cursor cursorlog=db_consulta.rawQuery("select distinct formulario,nombre from  USUARIOS_FORMULARIOS_SUCURSALES " +
                             "where upper(LOGIN_O)=upper('"+txt_usuario.getText().toString().trim()+"') and upper(LOGIN_PASS)=upper('"+txt_pass.getText().toString().trim()+"')",null);
                     String contenedor_opciones="";
                     String nombre_usuario="";
                     int i=0;

                     while ( cursorlog.moveToNext())
                     {
                         if(i==0){
                             contenedor_opciones=cursorlog.getString(0) ;
                             nombre_usuario=cursorlog.getString(1) ;
                         }
                         else
                         {
                             contenedor_opciones=contenedor_opciones+","+cursorlog.getString(0);
                         }

                         i++;
                     }

                     if(i==0){
                         new AlertDialog.Builder(login.this)
                                 .setTitle("ATENCION!!!")
                                 .setMessage("ERROR DE CONEXION.")
                                 .setNegativeButton("CERRAR", null).show();
                     }
                     else {
                         variables.contenedor_menu=contenedor_opciones;
                         variables.NOMBRE_LOGIN=nombre_usuario;

                         ListarSucursalLite();


                     }
                 }
                 else {
                     new AlertDialog.Builder(login.this)
                             .setTitle("ATENCION!!!")
                             .setMessage(mensaje)
                             .setNegativeButton("CERRAR", null).show();
                 }

                 pdLoading.dismiss();
            }
        }

        private  void  ListarSucursal()
        {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(login.this);
            builderSingle.setTitle("SUCURSALES DISPONIBLES");
            ArrayAdapter<String> arrayDescSucursal = new ArrayAdapter<String>(login.this, android.R.layout.select_dialog_singlechoice);
            ArrayList<String> arrayIdSucursal = new ArrayList<>();
            try
            {
                arrayDescSucursal.clear();
                arrayIdSucursal.clear();
                controles.arrSucursales.clear();
                controles.arrIdSucursales.clear();
                connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select distinct SUCURSAL_DESCRIPCION ,ROL_SUCURSAL " +
                        "from v_web_operador_rol_prog where " +
                        "login_o='"+user.toUpperCase()+"' order by 2");
                while ( rs.next())
                {   controles.arrSucursales.add(rs.getString("SUCURSAL_DESCRIPCION"));
                    controles.arrIdSucursales.add(rs.getString("ROL_SUCURSAL"));
                    arrayIdSucursal.add(rs.getString("ROL_SUCURSAL"));
                    arrayDescSucursal.add(rs.getString("SUCURSAL_DESCRIPCION"));
                }

                builderSingle.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayDescSucursal, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int posicion)
                    {
                        variables.DESCRIPCION_SUCURSAL_LOGIN= arrayDescSucursal.getItem(posicion);
                        variables.ID_SUCURSAL_LOGIN         =arrayIdSucursal.get(posicion);

                        Intent is=new Intent(login.this,menu_principal.class);
                        startActivity(is);
                        finish();
                    }
                });
                builderSingle.show();

            }
            catch (Exception e)
            {
                new AlertDialog.Builder(login.this)
                .setTitle("ATENCION!!!")
                .setMessage(e.toString())
                .setNegativeButton("CERRAR", null).show();
            }

        }

        private  void  ListarSucursalLite()
        {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(login.this);
            builderSingle.setTitle("SUCURSALES DISPONIBLES");
            ArrayAdapter<String> arrayDescSucursal = new ArrayAdapter<String>(login.this, android.R.layout.select_dialog_singlechoice);
            ArrayList<String> arrayIdSucursal = new ArrayList<>();
            try
            {
                arrayDescSucursal.clear();
                arrayIdSucursal.clear();
                controles.arrSucursales.clear();
                controles.arrIdSucursales.clear();

                SQLiteDatabase db_consultaSuc= controles.conSqlite.getReadableDatabase();
                Cursor cursorlogSuc=db_consultaSuc.rawQuery("select distinct SUCURSAL_DESCRIPCION ,ROL_SUCURSAL from USUARIOS_FORMULARIOS_SUCURSALES " +
                        "where upper(LOGIN_O)=upper('"+txt_usuario.getText().toString().trim()+"') and " +
                        "upper(LOGIN_PASS)=upper('"+txt_pass.getText().toString().trim()+"') order by 2",null);

                while ( cursorlogSuc.moveToNext())
                {   controles.arrSucursales.add(cursorlogSuc.getString(0));
                    controles.arrIdSucursales.add(cursorlogSuc.getString(1));
                    arrayIdSucursal.add(cursorlogSuc.getString(1));
                    arrayDescSucursal.add(cursorlogSuc.getString(0));
                }
                builderSingle.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayDescSucursal, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int posicion)
                    {
                        variables.DESCRIPCION_SUCURSAL_LOGIN= arrayDescSucursal.getItem(posicion);
                        variables.ID_SUCURSAL_LOGIN         =arrayIdSucursal.get(posicion);
                        Intent is=new Intent(login.this,menu_principal.class);
                        startActivity(is);
                        finish();
                    }
                });
                builderSingle.show();
            }
            catch (Exception e)
            {
                new AlertDialog.Builder(login.this)
                        .setTitle("ATENCION!!!")
                        .setMessage(e.toString())
                        .setNegativeButton("CERRAR", null).show();
            }

        }






    }