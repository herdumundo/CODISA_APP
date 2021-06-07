package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.codisa_app.MultiSpinnerListener;
import com.example.codisa_app.R;
import com.example.codisa_app.SpinnerDialog;
import com.example.codisa_app.stkw001;
import com.tapadoo.alerter.Alerter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class controles {
    public static ArrayList<String> arrSucursales       =   new ArrayList<>();
    public static ArrayList<String> arrIdSucursales     =   new ArrayList<>();
    public static ArrayList<String> arr_id_deposito     =   new ArrayList<>();
    public static ArrayList<String> arr_deposito        =   new ArrayList<>();
    public static ArrayList<String> arr_area            =   new ArrayList<>();
    public static ArrayList<String> arr_id_area         =   new ArrayList<>();
    public static ArrayList<String> arr_id_departamento =   new ArrayList<>();
    public static ArrayList<String> arr_departamento    =   new ArrayList<>();
    public static ArrayList<String> arr_seccion         =   new ArrayList<>();
    public static ArrayList<String> arr_id_seccion      =   new ArrayList<>();
    public static ArrayList<String> arr_id_familia      =   new ArrayList<>();
    public static ArrayList<String> arr_familia         =   new ArrayList<>();
    public static ArrayList<String> arr_id_grupo        =   new ArrayList<>();
    public static ArrayList<String> arr_grupo           =   new ArrayList<>();
    public static String            ids_subgrupos="",INVE_ART_EST="N",INVE_ART_EXIST="N",INVE_CANT_TOMA="1",INVE_IND_LOTE="S";
    static List<ArrayListContenedor>    listArraySubgrupo = new ArrayList<>();
    static List<ArrayListContenedor>    listArrayArticulos = new ArrayList<>();
    static List<ArrayListContenedor>    listInsertArticulos = new ArrayList<>();
    public static Connection        connection=null;
    public static Connection_Oracle conexion = new Connection_Oracle();
    public static Connection        connect ;

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

        stkw001.sp_sucursal = new SpinnerDialog(activity,arrSucursales,"Listado de arrSucursales");
        stkw001_txt_sucursalOnclick(activity);
        controles.limpiarSubGrupo();

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
             limpiarSubGrupo();
        }
        catch (Exception e){

        }

    }

    public static void listar_areas(Activity activity, Context context,int tipo_toma) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_AREA  ");
            arr_id_area.clear();
            arr_area.clear();
            listArraySubgrupo.clear();
            while ( rs.next())
            {
                arr_id_area.add(rs.getString("area_codigo"));
                arr_area.add(rs.getString("area_desc"));
            }
            stkw001.sp_area = new SpinnerDialog(activity,arr_area,"Listado de areas");
            Stkw001AreaOnclick(activity,context,tipo_toma);
        }
        catch (Exception e){

        }

    }

    public static void listar_departamentos(Activity activity, String id_area, Context context,int tipo_toma) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_DPTO   where dpto_area='"+id_area+"'");
            arr_id_departamento.clear();
            arr_departamento.clear();
            listArraySubgrupo.clear();

            while ( rs.next())
            {
                arr_id_departamento.add(rs.getString("dpto_codigo"));
                arr_departamento.add(rs.getString("dpto_desc"));
            }
            stkw001.sp_departamento = new SpinnerDialog(activity,arr_departamento,"Listado de departamentos");
            Stkw001DepartamentoOnclick(activity,context,  tipo_toma);
            limpiarSubGrupo();
        }
        catch (Exception e){

        }

    }

    public static void listar_seccion(Activity activity, String id_departamento, Context context,int tipo_toma) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SECC   where secc_dpto='"+id_departamento+"'" +
                    " and secc_area='"+stkw001.txt_id_area.getText().toString().trim()+"'");
            arr_id_seccion.clear();
            arr_seccion.clear();
            listArraySubgrupo.clear();

            while ( rs.next())
            {
                arr_id_seccion.add(rs.getString("secc_codigo"));
                arr_seccion.add(rs.getString("secc_desc"));
            }
            stkw001.sp_seccion = new SpinnerDialog(activity,arr_seccion,"Listado de secciones");
            Stkw001SeccionOnclick(activity,context,  tipo_toma);
            limpiarSubGrupo();
        }
        catch (Exception e){

        }

    }

    public static void listar_familia(Activity activity, String id_seccion, Context context,int tipo_toma) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_FLIA   where flia_seccion='"+id_seccion+"'" +
                    " and flia_area='"+stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and flia_dpto='"+stkw001.txt_id_departamento.getText().toString().trim()+"'");
            arr_id_familia.clear();
            arr_familia.clear();
           // listArraySubgrupo.clear();
           // stkw001.spinerSubGrupo.setSearchHint("Busqueda de Sub-Grupo");

            while ( rs.next())
            {
                arr_id_familia.add(rs.getString("flia_codigo"));
                arr_familia.add(rs.getString("flia_desc"));
            }
            stkw001.sp_familia = new SpinnerDialog(activity,arr_familia,"Listado de familias");
            Stkw001FamiliaOnclick(activity, context, tipo_toma);
            limpiarSubGrupo();
        }
        catch (Exception e){

        }

    }

    public static void listar_grupo(Activity activity, String id_familia, Context context,int tipo_toma) {
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
            Stkw001GrupoOnclick(activity,context,  tipo_toma);



        }
        catch (Exception e){

        }

    }

    public static  void limpiarSubGrupo(){
        listArraySubgrupo.clear();
        listArrayArticulos.clear();
        listInsertArticulos.clear();
        stkw001.spinerSubGrupo.setSearchHint("Busqueda de Sub-Grupo");
        stkw001.spinerArticulos.setSearchHint("Busqueda de Articulos");
        stkw001.spinerSubGrupo.setItems(listArraySubgrupo, new MultiSpinnerListener()
            {
            @Override
            public void onItemsSelected(List<ArrayListContenedor> items) {
            }
            });
        stkw001.spinerArticulos.setItems(listArrayArticulos, new MultiSpinnerListener()
        {
            @Override
            public void onItemsSelected(List<ArrayListContenedor> items) {
            }
        });
    }

    public static void listar_SubGrupo(Activity activity, String id_grupo, Context context,int tipo_toma) {
        try
        {
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SUBGRUPO  " +
                    " where " +
                    "       sugr_grupo='"   +id_grupo+"'" +
                    " and   sugr_area='"    +stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and   sugr_seccion='" +stkw001.txt_id_seccion.getText().toString().trim()+"'  " +
                    " and   sugr_flia='"    +stkw001.txt_id_familia.getText().toString().trim()+"'  " +
                    " and   sugr_dpto='"    +stkw001.txt_id_departamento.getText().toString().trim()+"'");
            listArraySubgrupo.clear();
            ids_subgrupos="";
            while ( rs.next())
            {
                ArrayListContenedor h = new ArrayListContenedor();
                h.setId(Integer.parseInt(rs.getString("sugr_codigo")));
                h.setName(rs.getString("sugr_desc"));
                h.setLote("");
                listArraySubgrupo.add(h);
            }

            stkw001.spinerSubGrupo.setItems(listArraySubgrupo, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<ArrayListContenedor> items) {
                 //FORMULA PARA RECUPERAR SOLO LOS ITEMS SELECCIONADOS, SE PUEDE CREAR UNA ARRAYLIST PARA SOLO LOS SELECCIONADOS.
                    ids_subgrupos="";

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            if(i==0){
                                ids_subgrupos=ids_subgrupos+items.get(i).getId();
                            }
                            else {
                                ids_subgrupos=ids_subgrupos+","+items.get(i).getId();
                            }
                         }
                    }

                    if(tipo_toma==1){
                        try {
                            listarArticulos();

                        }
                        catch (Exception e){
                            String as=e.toString();

                        }
                    }
                }
            });
        }
        catch (Exception e){

        }

    }

    public static void listarArticulos(){

        try {
            String inLote="";
            String inEstado="";
            String inExistenciaCero="";
            String TotalJoin="";
            if(!stkw001.BolLote){
                inLote="and arde_lote='000000' AND ARDE_FEC_VTO_LOTE='31/12/3000'";
            }
            if(!stkw001.BolDescontinuados){
                inEstado="and art_est='A'";
            }
            if(!stkw001.Bolexistencia){
                inExistenciaCero="and ARDE_CANT_ACT > 0";
            }
            TotalJoin= inLote+inEstado+inExistenciaCero;
            Statement stmt2 = connect.createStatement();
            ResultSet rs2 = stmt2.executeQuery("" +
                    "select " +
                    "   TO_NUMBER (arde_cant_act) as cantidad  ,v_web_articulos_clasificacion.* " +
                    "from " +
                    "   v_web_articulos_clasificacion " +
                    "where " +
                    "   arde_suc="+stkw001.txt_id_sucursal.getText().toString()+"      and " +
                    "   arde_dep="+stkw001.txt_id_deposito.getText().toString()+"      and " +
                    "   area_codigo="+stkw001.txt_id_area.getText().toString()+"   and " +
                    "   dpto_codigo="+stkw001.txt_id_departamento.getText().toString()+"   and " +
                    "   secc_codigo="+stkw001.txt_id_seccion.getText().toString()+"   and " +
                    "   flia_codigo="+stkw001.txt_id_familia.getText().toString()+"   and " +
                    "   grup_codigo="+stkw001.txt_id_grupo.getText().toString()+" and " +
                    "   sugr_codigo in ("+ids_subgrupos+") "+TotalJoin);
            listArrayArticulos.clear();

            while ( rs2.next())
            {
                ArrayListContenedor contenedor = new ArrayListContenedor();
                contenedor.setId(Integer.parseInt(rs2.getString("art_codigo")));
                contenedor.setName(rs2.getString("art_desc"));
                contenedor.setLote(rs2.getString("arde_lote"));
                contenedor.setCantidad(rs2.getString("cantidad"));
                contenedor.setFechaVencimiento(rs2.getString("arde_fec_vto_lote"));
                listArrayArticulos.add(contenedor);
            }


            stkw001.spinerArticulos.setItems(listArrayArticulos, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<ArrayListContenedor> items) {

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            ArrayListContenedor insArt = new ArrayListContenedor();

                            insArt.setId(items.get(i).getId());
                            insArt.setName(items.get(i).getName());
                            insArt.setLote(items.get(i).getLote());
                            insArt.setCantidad(items.get(i).getCantidad());
                            insArt.setFechaVencimiento(items.get(i).getFechaVencimiento());
                             listInsertArticulos.add(insArt);
                        }
                    }
                }


            });
        }
        catch (Exception E){

        }
    }


    public static void stkw001_txt_sucursalOnclick( Activity activity){
        stkw001.txt_sucursal.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v)
        {
            stkw001.sp_sucursal.showSpinerDialog();
        } } );

        stkw001.sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                stkw001.txt_sucursal.setText(arrSucursales.get(i));
                stkw001.txt_id_sucursal.setText(arrIdSucursales.get(i));
                stkw001.txt_deposito.setText("");
                stkw001.txt_id_deposito.setText("");
                listar_depositos(activity,arrIdSucursales.get(i));
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
               // listar_depositos(activity,controles.arrIdSucursales.get(i));
            }
        });
    }

    public static void Stkw001AreaOnclick(Activity activity, Context context,int tipo_toma){
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


                listar_departamentos(activity,arr_id_area.get(i),context,  tipo_toma);
             }
        });
    }

    public static void Stkw001DepartamentoOnclick(Activity activity, Context context,int tipo_toma){
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

                listar_seccion(activity,arr_id_departamento.get(i),context, tipo_toma);
            }
        });
    }

    public static void Stkw001SeccionOnclick(Activity activity, Context context,int tipo_toma){
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
                listar_familia(activity,arr_id_seccion.get(i),context, tipo_toma);
            }
        });
    }

    public static void Stkw001FamiliaOnclick(Activity activity, Context context,int tipo_toma){
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
                listar_grupo(activity,arr_id_familia.get(i),context, tipo_toma);
            }
        });
    }

    public static void Stkw001GrupoOnclick(Activity activity, Context context,int tipo_toma){
        stkw001.txt_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_grupo.showSpinerDialog();
            } } );
        stkw001.sp_grupo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                limpiarSubGrupo();
               stkw001.txt_grupo.setText(arr_grupo.get(i));
                stkw001.txt_id_grupo.setText(arr_id_grupo.get(i));
                listar_SubGrupo(activity,arr_id_grupo.get(i),context,  tipo_toma);
            }
        });
    }

    public static void validacione_toma(Activity activity,Context context){
        if (variables.tipo_stkw001==1){
            if( stkw001.txt_id_sucursal.getText().toString().equals("")
                    ||stkw001.txt_id_deposito.getText().toString().equals("")
                    ||stkw001.txt_id_area.getText().toString().equals("")
                    ||stkw001.txt_id_departamento.getText().toString().equals("")
                    ||stkw001.txt_id_seccion.getText().toString().equals("")
                    ||stkw001.txt_id_familia.getText().toString().equals("")
                    ||stkw001.txt_id_grupo.getText().toString().equals("")) {
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("COMPLETE LOS DATOS REQUERIDOS.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else if (controles.ids_subgrupos.equals("")){
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("SELECCIONE SUB-GRUPO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else if (controles.ids_subgrupos.equals("")){
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("SELECCIONE SUB-GRUPO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else if (controles.listInsertArticulos.size()==0){
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("SELECCIONE ARTICULOS.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else {
                insert_toma();
                for (int i = 0; i < controles.listInsertArticulos.size(); i++) {

                    Toast.makeText(context,controles.listInsertArticulos.get(i).getName(),Toast.LENGTH_LONG).show();

                }
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("REGISTRADO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.colorAccent)
                        .show();
            }
        }

        else {
            if( stkw001.txt_id_sucursal.getText().toString().equals("")
                    ||stkw001.txt_id_deposito.getText().toString().equals("")
                    ||stkw001.txt_id_area.getText().toString().equals("")
                    ||stkw001.txt_id_departamento.getText().toString().equals("")
                    ||stkw001.txt_id_seccion.getText().toString().equals("")
                    ||stkw001.txt_id_familia.getText().toString().equals("")
                    ||stkw001.txt_id_grupo.getText().toString().equals("")) {
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("COMPLETE LOS DATOS REQUERIDOS.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else if (controles.ids_subgrupos.equals("")){
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("SELECCIONE SUB-GRUPO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }

            else {
                insert_toma();
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("REGISTRADO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.colorAccent)
                        .show();
            }
        }
    }

    public static void insert_toma(){

        try {
            String id_cabecera="";
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SEQ_NRO_INV.NEXTVAL    FROM   DUAL");
            while (rs.next()){
                id_cabecera=rs.getString(1);
            }

            connect.setAutoCommit(false);
            String insertar = "insert into WEB_INVENTARIO(" +
                    "WINVE_SUC,                          " +
                    "WINVE_DEP,                       " +
                    "WINVE_GRUPO,                 " +
                    "WINVE_FEC,       " +
                    "WINVE_LOGIN," +
                    "WINVE_TIPO_TOMA,                    " +
                    "WINVE_SECC,                      " +
                    "WINVE_AREA,                  " +
                    "WINVE_DPTO,      " +
                    "WINVE_FLIA," +
                    "WINVE_IND_LOTE," +
                    "WINVE_ESTADO," +
                    "WINVE_ART_EST," +
                    "WINVE_ART_EXIST," +
                    "WINVE_CANT_TOMA," +
                    "WINVE_EMPR,WINVE_NUMERO) values  " +
                    "('"+stkw001.txt_id_sucursal.getText().toString()+"',    " +
                    "'"+stkw001.txt_id_deposito.getText().toString()+"'," +
                    "'"+stkw001.txt_id_grupo.getText().toString()+"'," +
                    "CURRENT_TIMESTAMP," +
                    "UPPER('" +variables.userdb+"')," +
                    "'"+variables.tipo_stkw001_insert+"'," +
                    "'"+stkw001.txt_id_seccion.getText().toString()+"'," +
                    "'"+stkw001.txt_id_area.getText().toString()+"'," +
                    "'"+stkw001.txt_id_departamento.getText().toString()+"'," +
                    "'"+stkw001.txt_id_familia.getText().toString()+"'," +
                    "'"+INVE_IND_LOTE+"'," +
                    "'A'," +
                    "'"+INVE_ART_EST+"'," +
                    "'"+INVE_ART_EXIST+"'," +
                    "'"+INVE_CANT_TOMA+"'," +
                    "'1', "+id_cabecera+")";
            PreparedStatement ps = connect.prepareStatement(insertar);
            ps.executeUpdate();
                    int secuencia=1;
            for (int i = 0; i < listInsertArticulos.size(); i++) {
                    int cantidad_actual=Integer.parseInt(listInsertArticulos.get(i).getCantidad());
                String insertar_detalle=" insert into WEB_INVENTARIO_DET (" +
                        "WINVD_NRO_INV," +
                        "WINVD_ART," +
                        "WINVD_SECU," +
                        "WINVD_CANT_ACT," +
                        "WINVD_CANT_INV," +
                        "WINVD_UBIC," +
                        "WINVD_CODIGO_BARRA," +
                        "WINVD_CANT_PED_RECEP," +
                        "WINVD_LOTE," +
                        "WINVD_FEC_VTO," +
                        "WINVD_LOTE_CLAVE," +
                        "WINVD_UM)  VALUES ("+id_cabecera+",'"+listInsertArticulos.get(i).getId()+"',"+secuencia+","+cantidad_actual+",'','','',''," +
                        "'"+listInsertArticulos.get(i).getLote()+"',TO_DATE('"+listInsertArticulos.get(i).getFechaVencimiento()+"', 'yyyy/mm/dd hh24:mi:ss') ,'','')";
                PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                ps2.executeUpdate();

                secuencia++;
            }
            connect.commit();

        }
        catch (Exception error){
            String e=error.toString();
            try {
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
