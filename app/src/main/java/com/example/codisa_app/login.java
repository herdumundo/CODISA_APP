package com.example.codisa_app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import Utilidades.OnSpinerItemClick;
import Utilidades.controles;
import Utilidades.variables;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Utilidades.Connection_Oracle;
import Utilidades.OnSpinerItemClick;
import Utilidades.controles;
import Utilidades.variables;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class login extends AppCompatActivity {
    private static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 101;

    TextView txt_usuario, txt_pass;
    SpinnerDialog sp_sucursal;
    String user = "", passwd;
    ProgressDialog pdLoading;
    Connection connect;

    @Override
    public void onBackPressed() {
        Utilidades.controles.volver_atras(this, this, login.class, "¿Desea salir de la aplicación?", 5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txt_usuario = findViewById(R.id.txt_usuario);
        txt_pass = findViewById(R.id.txt_pass);
        getSupportActionBar().hide();

        txt_usuario.requestFocus();
        controles.conexion_sqlite(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
        }
    }

    public void login(View v) {
        final AsyncCaller task = new AsyncCaller();
        task.execute();
    }

    class AsyncCaller extends AsyncTask<Void, Void, Integer> {
        String mensajeError = null;
        String contenedorOpciones = "";
        String nombreUsuario = "";
        int opcionesEncontradas = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = ProgressDialog.show(login.this, "Verificando", "Espere...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            user = txt_usuario.getText().toString().trim();
            passwd = txt_pass.getText().toString().trim();

            if (user.isEmpty()) {
                mensajeError = "Ingrese usuario";
                return 3; // Código de error
            }

            if (passwd.isEmpty()) {
                mensajeError = "Ingrese contraseña";
                return 3; // Código de error
            }

            variables.userdb = user;
            variables.passdb = passwd;

            try {
                Connection_Oracle conexion = new Connection_Oracle();
                connect = conexion.Connections();

                if (connect != null) {
                    // Conexión remota exitosa
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT DISTINCT formulario, nombre FROM v_web_operador_rol_prog " +
                                    "WHERE login_o='" + user.toUpperCase() + "' AND formulario IN ('STKW001','STKW002','STKW004')");

                    while (rs.next()) {
                        if (opcionesEncontradas == 0) {
                            contenedorOpciones = rs.getString("formulario");
                            nombreUsuario = rs.getString("nombre");
                        } else {
                            contenedorOpciones += "," + rs.getString("formulario");
                        }
                        opcionesEncontradas++;
                    }

                    if (opcionesEncontradas > 0) {
                        Utilidades.variables.contenedor_menu = contenedorOpciones;
                        variables.NOMBRE_LOGIN = nombreUsuario;

                        // Guardar datos en SQLite para uso offline
                        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
                        db.execSQL("DELETE FROM USUARIOS_FORMULARIOS_SUCURSALES WHERE LOGIN_O = UPPER('" + user + "')");
                        db.close();

                        Statement stmt2 = connect.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(
                                "SELECT DISTINCT formulario, nombre, LOGIN_O, SUCURSAL_DESCRIPCION, ROL_SUCURSAL " +
                                        "FROM v_web_operador_rol_prog WHERE formulario IN ('STKW001','STKW002','STKW004') AND login_o='" + user.toUpperCase() + "'");

                        while (rs2.next()) {
                            SQLiteDatabase dblogin = controles.conSqlite.getReadableDatabase();
                            dblogin.execSQL(
                                    "INSERT INTO USUARIOS_FORMULARIOS_SUCURSALES (FORMULARIO, NOMBRE, LOGIN_O, LOGIN_PASS, SUCURSAL_DESCRIPCION, ROL_SUCURSAL) " +
                                            "VALUES ('" + rs2.getString("formulario") + "','" + rs2.getString("nombre") + "','" + rs2.getString("LOGIN_O") + "'," +
                                            "UPPER('" + passwd + "'),'" + rs2.getString("SUCURSAL_DESCRIPCION") + "','" + rs2.getString("ROL_SUCURSAL") + "')");
                            dblogin.close();
                        }
                        return 1; // Login remoto exitoso
                    } else {
                        mensajeError = "El usuario no posee permisos para la aplicación móvil.";
                        return 3; // Error
                    }
                } else {
                    // No hay conexión al servidor, intentar login local
                    SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
                    Cursor cursor = db.rawQuery(
                            "SELECT DISTINCT formulario, nombre FROM USUARIOS_FORMULARIOS_SUCURSALES " +
                                    "WHERE UPPER(LOGIN_O) = UPPER('" + user + "') AND UPPER(LOGIN_PASS) = UPPER('" + passwd + "')",
                            null);

                    while (cursor.moveToNext()) {
                        if (opcionesEncontradas == 0) {
                            contenedorOpciones = cursor.getString(0);
                            nombreUsuario = cursor.getString(1);
                        } else {
                            contenedorOpciones += "," + cursor.getString(0);
                        }
                        opcionesEncontradas++;
                    }

                    if (opcionesEncontradas > 0) {
                        variables.contenedor_menu = contenedorOpciones;
                        variables.NOMBRE_LOGIN = nombreUsuario;
                        return 2; // Login local exitoso
                    } else {
                        mensajeError = "Error de conexión.";
                        return 3; // Error
                    }
                }
            } catch (Exception e) {
                mensajeError = e.getMessage();
                return 3; // Error
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pdLoading.dismiss();

            switch (result) {
                case 1:
                    ListarSucursal();
                    break;
                case 2:
                    ListarSucursalLite();
                    break;
                default:
                    new AlertDialog.Builder(login.this)
                            .setTitle(variables.atencion)
                            .setMessage(mensajeError)
                            .setNegativeButton("Cerrar", null).show();
            }
        }
    }

    private  void  ListarSucursal()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(login.this);
        builderSingle.setTitle("Sucursales disponibles");
        ArrayAdapter<String> arrayDescSucursal = new ArrayAdapter<String>(login.this, android.R.layout.select_dialog_singlechoice);
        ArrayList<String> arrayIdSucursal = new ArrayList<>();
        try
        {
            arrayDescSucursal.clear();
            arrayIdSucursal.clear();
            controles.arrSucursales.clear();
            controles.arrIdSucursales.clear();
            Connection_Oracle conexion = new Connection_Oracle();
            connect = conexion.Connections();


            //connect = conexion.Connections();
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

            sp_sucursal = new SpinnerDialog(this,controles.arrSucursales,"Seleccione sucursal");
            sp_sucursal.showSpinerDialog();
            sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int posicion) {
                    variables.DESCRIPCION_SUCURSAL_LOGIN= arrayDescSucursal.getItem(posicion);
                    variables.ID_SUCURSAL_LOGIN         =arrayIdSucursal.get(posicion);
                    Intent is=new Intent(login.this,menu_principal.class);
                    startActivity(is);
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            new AlertDialog.Builder(login.this)
                    .setTitle(variables.atencion)
                    .setMessage(e.toString())
                    .setNegativeButton("Cerrar", null).show();
        }
        finally {
            try {
                connect.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private  void  ListarSucursalLite()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(login.this);
        builderSingle.setTitle("Sucursales disponibles");
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
            sp_sucursal = new SpinnerDialog(this,controles.arrSucursales,"Seleccione sucursal");
            sp_sucursal.showSpinerDialog();
            sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int posicion) {
                    variables.DESCRIPCION_SUCURSAL_LOGIN= arrayDescSucursal.getItem(posicion);
                    variables.ID_SUCURSAL_LOGIN         =arrayIdSucursal.get(posicion);
                    Intent is=new Intent(login.this,menu_principal.class);
                    startActivity(is);
                    finish();
                }
            });
            builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

        }
        catch (Exception e)
        {
            new AlertDialog.Builder(login.this)
                    .setTitle(variables.atencion)
                    .setMessage(e.toString())
                    .setNegativeButton("Cerrar", null).show();
        }

    }



}