package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.example.codisa_app.KeyPairBoolData;
import com.example.codisa_app.MultiSpinnerListener;
import com.example.codisa_app.SpinnerDialog;
import com.example.codisa_app.stkw001;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class controles {
    public static ArrayList<String> sucursales = new ArrayList<>();
    public static ArrayList<String> id_sucursales = new ArrayList<>();
    public static ArrayList<String> arr_id_deposito = new ArrayList<>();
    public static ArrayList<String> arr_deposito = new ArrayList<>();
    public static ArrayList<String> arr_area = new ArrayList<>();
    public static ArrayList<String> arr_id_area = new ArrayList<>();
    public static ArrayList<String> arr_id_departamento = new ArrayList<>();
    public static ArrayList<String> arr_departamento = new ArrayList<>();
    public static ArrayList<String> arr_seccion = new ArrayList<>();
    public static ArrayList<String> arr_id_seccion = new ArrayList<>();
    public static ArrayList<String> arr_id_familia = new ArrayList<>();
    public static ArrayList<String> arr_familia = new ArrayList<>();
    public static ArrayList<String> arr_id_grupo = new ArrayList<>();
    public static ArrayList<String> arr_grupo = new ArrayList<>();
    public static ArrayList<String> arr_id_subgrupo = new ArrayList<>();
    public static ArrayList<String> arr_subgrupo = new ArrayList<>();

   static List<KeyPairBoolData> listArray1 = new ArrayList<>();


    public static Connection connection=null;
    public static Connection_Oracle conexion = new Connection_Oracle();
    public static Connection connect ;

    public static void volver_atras(Context context, Activity activity, Class clase_destino, String texto, int tipo)  {
        if(tipo==1){
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ATENCION!!!.")
                    .setMessage(texto)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, clase_destino);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.finish();
                            context.startActivity(intent);

                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }
        else if(tipo==3){
            new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ATENCION!!!.")
                    .setMessage(texto)
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, clase_destino);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.finish();
                            context.startActivity(intent);

                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }

        else {
            Intent intent = new Intent(context, clase_destino);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.finish();
            context.startActivity(intent);
        }
    }


    public static void listar_sucursales(Activity activity) {

        stkw001.sp_sucursal = new SpinnerDialog(activity,sucursales,"Listado de sucursales");
        stkw001_txt_sucursalOnclick(activity);
    }

    public static void listar_depositos(Activity activity, String id_sucursal) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SUC_DEP   where suc_codigo='"+id_sucursal+"'");
            arr_id_deposito.clear();
            arr_deposito.clear();
            while ( rs.next())
            {
                arr_id_deposito.add(rs.getString("dep_codigo"));
                arr_deposito.add(rs.getString("dep_desc"));
            }
            stkw001.sp_deposito = new SpinnerDialog(activity,arr_deposito,"Listado de depositos");
            Stkw001DepositoOnclick();
        }
        catch (Exception e){

        }

    }

    public static void listar_areas(Activity activity, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_AREA  ");
            arr_id_area.clear();
            arr_area.clear();
            while ( rs.next())
            {
                arr_id_area.add(rs.getString("area_codigo"));
                arr_area.add(rs.getString("area_desc"));
            }
            stkw001.sp_area = new SpinnerDialog(activity,arr_area,"Listado de areas");
            Stkw001AreaOnclick(activity,context);
        }
        catch (Exception e){

        }

    }

    public static void listar_departamentos(Activity activity, String id_area, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_DPTO   where dpto_area='"+id_area+"'");
            arr_id_departamento.clear();
            arr_departamento.clear();


            while ( rs.next())
            {
                arr_id_departamento.add(rs.getString("dpto_codigo"));
                arr_departamento.add(rs.getString("dpto_desc"));
            }
            stkw001.sp_departamento = new SpinnerDialog(activity,arr_departamento,"Listado de departamentos");
            Stkw001DepartamentoOnclick(activity,context);
        }
        catch (Exception e){

        }

    }

    public static void listar_seccion(Activity activity, String id_departamento, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SECC   where secc_dpto='"+id_departamento+"'" +
                    " and secc_area='"+stkw001.txt_id_area.getText().toString().trim()+"'");
            arr_id_seccion.clear();
            arr_seccion.clear();


            while ( rs.next())
            {
                arr_id_seccion.add(rs.getString("secc_codigo"));
                arr_seccion.add(rs.getString("secc_desc"));
            }
            stkw001.sp_seccion = new SpinnerDialog(activity,arr_seccion,"Listado de secciones");
            Stkw001SeccionOnclick(activity,context);
        }
        catch (Exception e){

        }

    }

    public static void listar_familia(Activity activity, String id_seccion, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_FLIA   where flia_seccion='"+id_seccion+"'" +
                    " and flia_area='"+stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and flia_dpto='"+stkw001.txt_id_departamento.getText().toString().trim()+"'");
            arr_id_familia.clear();
            arr_familia.clear();


            while ( rs.next())
            {
                arr_id_familia.add(rs.getString("flia_codigo"));
                arr_familia.add(rs.getString("flia_desc"));
            }
            stkw001.sp_familia = new SpinnerDialog(activity,arr_familia,"Listado de familias");
            Stkw001FamiliaOnclick(activity, context);
        }
        catch (Exception e){

        }

    }

    public static void listar_grupo(Activity activity, String id_familia, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_GRUPO  " +
                    " where " +
                    "       grup_familia='"+id_familia+"'" +
                    " and   grup_area='"+stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and   grup_seccion='"+stkw001.txt_id_seccion.getText().toString().trim()+"'  " +
                    " and   grup_dpto='"+stkw001.txt_id_departamento.getText().toString().trim()+"'");
            arr_id_grupo.clear();
            arr_grupo.clear();



            while ( rs.next())
            {
                arr_id_grupo.add(rs.getString("grup_codigo"));
                arr_grupo.add(rs.getString("grup_desc"));
            }
            stkw001.sp_grupo = new SpinnerDialog(activity,arr_grupo,"Listado de grupos");
            Stkw001GrupoOnclick(activity,context);
        }
        catch (Exception e){

        }

    }

    public static void listar_SubGrupo(Activity activity, String id_grupo, Context context) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SUBGRUPO  " +
                    " where " +
                    "       sugr_grupo='"   +id_grupo+"'" +
                    " and   sugr_area='"    +stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and   sugr_seccion='" +stkw001.txt_id_seccion.getText().toString().trim()+"'  " +
                    " and   sugr_flia='"    +stkw001.txt_id_familia.getText().toString().trim()+"'  " +
                    " and   sugr_dpto='"    +stkw001.txt_id_departamento.getText().toString().trim()+"'");

            listArray1.clear();

            while ( rs.next())
            {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(Integer.parseInt(rs.getString("sugr_codigo")));
                h.setName(rs.getString("sugr_desc"));
                listArray1.add(h);
            }
           /* stkw001.sp_subgrupo = new SpinnerDialog(activity, arr_subgrupo,"Listado de sub-grupos");
            Stkw001SubGrupoOnclick(activity);*/
            stkw001.multiSelectSpinnerWithSearch.setItems(listArray1, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> items) {
                 //FORMULA PARA RECUPERAR SOLO LOS ITEMS SELECCIONADOS, SE PUEDE CREAR UNA ARRAYLIST PARA SOLO LOS SELECCIONADOS.
                    /*   for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {

                            //Log.i(Tag, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        }
                    }*/
                }
            });
        }
        catch (Exception e){

        }

    }


    public  static  void test_subgrupo(Context context){

    }









    public static void stkw001_txt_sucursalOnclick( Activity activity){
        stkw001.txt_sucursal.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_sucursal.showSpinerDialog();


        } } );

        stkw001.sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                stkw001.txt_sucursal.setText(sucursales.get(i));
                stkw001.txt_id_sucursal.setText(id_sucursales.get(i));
                stkw001.txt_deposito.setText("");
                stkw001.txt_id_deposito.setText("");
                listar_depositos(activity,id_sucursales.get(i));
            }
        });
    }
    public static void Stkw001DepositoOnclick(){
        stkw001.txt_deposito.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_deposito.showSpinerDialog();
        } } );
        stkw001.sp_deposito.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                stkw001.txt_deposito.setText(arr_deposito.get(i));
                stkw001.txt_id_deposito.setText(arr_id_deposito.get(i));
               // listar_depositos(activity,controles.id_sucursales.get(i));
            }
        });
    }

    public static void Stkw001AreaOnclick(Activity activity, Context context){
        stkw001.txt_area.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_area.showSpinerDialog();
        } } );
        stkw001.sp_area.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                stkw001.txt_area.setText(arr_area.get(i));
                stkw001.txt_id_area.setText(arr_id_area.get(i));

                arr_id_departamento.clear();
                arr_departamento.clear();
                arr_id_seccion.clear();
                arr_seccion.clear();
                arr_id_familia.clear();
                arr_familia.clear();
                arr_id_grupo.clear();
                arr_grupo.clear();


                stkw001.txt_id_departamento.setText("");
                stkw001.txt_departamento.setText("");
                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");
                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");


                listar_departamentos(activity,arr_id_area.get(i),context);
             }
        });
    }

    public static void Stkw001DepartamentoOnclick(Activity activity, Context context){
        stkw001.txt_departamento.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_departamento.showSpinerDialog();
        } } );
        stkw001.sp_departamento.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                arr_id_seccion.clear();
                arr_seccion.clear();
                arr_id_familia.clear();
                arr_familia.clear();
                arr_id_grupo.clear();
                arr_grupo.clear();


                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");
                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");


                stkw001.txt_departamento.setText(arr_departamento.get(i));
                stkw001.txt_id_departamento.setText(arr_id_departamento.get(i));

                listar_seccion(activity,arr_id_departamento.get(i),context);
            }
        });
    }

    public static void Stkw001SeccionOnclick(Activity activity, Context context){
        stkw001.txt_seccion.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            stkw001.sp_seccion.showSpinerDialog();
        } } );
        stkw001.sp_seccion.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                arr_id_familia.clear();
                arr_familia.clear();
                arr_id_grupo.clear();
                arr_grupo.clear();


                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");
                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");


                stkw001.txt_seccion.setText(arr_seccion.get(i));
                stkw001.txt_id_seccion.setText(arr_id_seccion.get(i));
                listar_familia(activity,arr_id_seccion.get(i),context);
            }
        });
    }

    public static void Stkw001FamiliaOnclick(Activity activity, Context context){
        stkw001.txt_familia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_familia.showSpinerDialog();
            } } );
        stkw001.sp_familia.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                arr_id_grupo.clear();
                arr_grupo.clear();


                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");


                stkw001.txt_familia.setText(arr_familia.get(i));
                stkw001.txt_id_familia.setText(arr_id_familia.get(i));
                listar_grupo(activity,arr_id_familia.get(i),context);
            }
        });
    }

    public static void Stkw001GrupoOnclick(Activity activity, Context context){
        stkw001.txt_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_grupo.showSpinerDialog();
            } } );
        stkw001.sp_grupo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

               stkw001.txt_grupo.setText(arr_grupo.get(i));
                stkw001.txt_id_grupo.setText(arr_id_grupo.get(i));
                listar_SubGrupo(activity,arr_id_grupo.get(i),context);
            }
        });
    }

    public static void Stkw001SubGrupoOnclick(Activity activity){
        stkw001.txt_subgrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_subgrupo.showSpinerDialog();
            } } );
        stkw001.sp_subgrupo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i)
            {
                stkw001.txt_subgrupo.setText(arr_subgrupo.get(i));
                stkw001.txt_id_subgrupo.setText(arr_id_subgrupo.get(i));
            }
        });
    }

}
