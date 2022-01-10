package Utilidades;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Struct;

import javax.sql.ConnectionPoolDataSource;

/**
 * Created by hvelazquez on 04/07/2021.
 */

public class Connection_Oracle {
     @SuppressLint("NewaApi")
    public static Connection Connections(){
        String user = variables.userdb;
        String url = "jdbc:oracle:thin:@(DESCRIPTION= (ADDRESS=(PROTOCOL=TCP)(HOST=192.168.0.19)(PORT=1521)) (CONNECT_DATA=(SERVICE_NAME=codisaprod)))";
         //   String url = "jdbc:oracle:thin:@(DESCRIPTION= (ADDRESS=(PROTOCOL=TCP)(HOST=192.168.0.19)(PORT=1521)) (CONNECT_DATA=(SERVICE_NAME=prueba)))";
        String passwd = variables.passdb;
        String driver = "oracle.jdbc.driver.OracleDriver";
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection=null;

        try {
            Class.forName(driver);
            controles.verificadorRed=0;
            DriverManager.setLoginTimeout(5);

            connection= DriverManager.getConnection(url, user, passwd);

            controles.verificadorRed=1;
            controles.resBD=1;
        }
        catch (SQLException se) {

            switch (se.getErrorCode()){
                case 1017:
                    controles.mensajeLogin="USUARIO O CONTRASEÑA INCORRECTA, FAVOR VERIFIQUE.";
                    break;
                case  17002  :
                case  20:
                    controles.mensajeLogin="ERROR DE CONEXION, VERIFIQUE LA RED.";
                    controles.resBD=2;
                    variables.userdb=user;
                    variables.passdb=passwd;

                    break;

                case  17452:
                    controles.mensajeLogin="USUARIO O CONTRASEÑA INCORRECTA, FAVOR VERIFIQUE.";
                    controles.resBD=3;
                    break;
                default :
                    controles.mensajeLogin=se.getMessage();
                    controles.resBD=3;
            }
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        return connection;
 }

 }

