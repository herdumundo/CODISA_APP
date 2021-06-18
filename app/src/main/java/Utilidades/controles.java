package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.codisa_app.R;
import com.example.codisa_app.SpinnerDialog;
import com.example.codisa_app.lista_stkw001_inv;
import com.example.codisa_app.menu_principal;
import com.example.codisa_app.stkw001;
import com.example.codisa_app.stkw002;
import com.tapadoo.alerter.Alerter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class controles {
    public static   ArrayList<String> arrSucursales       =   new ArrayList<>();
    public static   ArrayList<String> arrIdSucursales     =   new ArrayList<>();
    public static   ArrayList<String> arr_id_deposito     =   new ArrayList<>();
    public static   ArrayList<String> arr_deposito        =   new ArrayList<>();
    public static   ArrayList<String> arr_area            =   new ArrayList<>();
    public static   ArrayList<String> arr_id_area         =   new ArrayList<>();
    public static   ArrayList<String> arr_id_departamento =   new ArrayList<>();
    public static   ArrayList<String> arr_departamento    =   new ArrayList<>();
    public static   ArrayList<String> arr_seccion         =   new ArrayList<>();
    public static   ArrayList<String> arr_id_seccion      =   new ArrayList<>();
    public static   ArrayList<String> arr_id_familia      =   new ArrayList<>();
    public static   ArrayList<String> arr_familia         =   new ArrayList<>();
    public static   ArrayList<String> arr_id_grupo        =   new ArrayList<>();
    public static   ArrayList<String> arr_grupo           =   new ArrayList<>();

    public static   String  ids_subgrupos="",INVE_ART_EST="N",INVE_ART_EXIST="N",INVE_CANT_TOMA="1",INVE_IND_LOTE="S";
    static          List<ArrayListContenedor>   listArraySubgrupo   = new ArrayList<>();
    static          List<ArrayListContenedor>   listArrayArticulos  = new ArrayList<>();
    static          List<ArrayListContenedor>   listInsertArticulos = new ArrayList<>();
    public static          ArrayList<Stkw002List> listaStkw001;

    public static   List<Stkw002Item> ListArrayInventarioArticulos;
    public static   ConexionSQLiteHelper  conSqlite,   conn_gm;
   // public static   Connection          connection=null;
    public static   Connection_Oracle   conexion = new Connection_Oracle();
    public static   Connection        connect ;
    public static   Context context_stkw001;
    public static   Context context_menuPrincipal;
    public static   Activity activity_stkw001;
    static int      tipoRespuestaStkw001; // 1=CORRECTO, 0=ERROR
    static int      tipoRespuestaExportStkw002; // 1=CORRECTO, 0=ERROR
    static String   mensajeRespuestaStkw001;
    static String   mensajeRespuestaExportStkw002;
    static int      ContExportStkw002;

    public static void conexion_sqlite(Context context) {
        conSqlite=      new ConexionSQLiteHelper(context,"CODISA_INV",null,1);
     }

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

                            context.startActivity(intent);
                            activity.finish();
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
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(context, clase_destino);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            context.startActivity(intent);
                            activity.finish();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }
        else if(tipo==5){
            activity.finish();
        }
        else {
            Intent intent = new Intent(context, clase_destino);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(intent);
            activity.finish();
        }
    }

    public static void listar_sucursales(Activity activity) {

        stkw001.sp_sucursal = new SpinnerDialog(activity,arrSucursales,"Listado de Sucursales");
        stkw001_txt_sucursalOnclick(activity);


    }

    public static void listar_depositos(Activity activity, String id_sucursal) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SUC_DEP   where suc_codigo='"+id_sucursal+"'");
            //arr_id_deposito.clear();
            //arr_deposito.clear();

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

    public static void listar_areas(Activity activity, Context context,int tipo_toma) {
        try {

            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_AREA  ");
            arr_id_area.clear();
            arr_area.clear();
           // listArraySubgrupo.clear();
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

            while ( rs.next())
            {
                arr_id_departamento.add(rs.getString("dpto_codigo"));
                arr_departamento.add(rs.getString("dpto_desc"));
            }
            stkw001.sp_departamento = new SpinnerDialog(activity,arr_departamento,"Listado de departamentos");
            Stkw001DepartamentoOnclick(activity,context,  tipo_toma);
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
         //   listArraySubgrupo.clear();

            while ( rs.next())
            {
                arr_id_seccion.add(rs.getString("secc_codigo"));
                arr_seccion.add(rs.getString("secc_desc"));
            }
            stkw001.sp_seccion = new SpinnerDialog(activity,arr_seccion,"Listado de secciones");
            Stkw001SeccionOnclick(activity,context,  tipo_toma);
          //  limpiarSubGrupo();
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
            //listArraySubgrupo.clear();

            while ( rs.next())
            {
                arr_id_familia.add(rs.getString("flia_codigo"));
                arr_familia.add(rs.getString("flia_desc"));
            }
            stkw001.sp_familia = new SpinnerDialog(activity,arr_familia,"Listado de familias");
            Stkw001FamiliaOnclick(activity, context, tipo_toma);

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
                    stkw001.spinerArticulos.setSearchHint("Busqueda de Articulos");
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
                    else   if(tipo_toma==2){

                        listarArticulos();
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
            if(ids_subgrupos.length()>0){

            ResultSet rs2 = stmt2.executeQuery("" +
                    "select " +
                    "   TO_NUMBER (arde_cant_act) as cantidad  ,TO_CHAR(arde_fec_vto_lote,'DD-MM-YYYY') as vencimiento,v_web_articulos_clasificacion.* " +
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
            listInsertArticulos.clear();
            while ( rs2.next())
            {
                String vencimiento=rs2.getString("vencimiento");
                ArrayListContenedor contenedor = new ArrayListContenedor();
                contenedor.setId(Integer.parseInt(rs2.getString("art_codigo")));
                contenedor.setName(rs2.getString("art_desc"));
                contenedor.setLote(rs2.getString("arde_lote"));
                contenedor.setCantidad(rs2.getString("cantidad"));
                contenedor.setFechaVencimiento(rs2.getString("ARDE_FEC_VTO_LOTE"));
                contenedor.setFecha_vencimientoParseado(vencimiento);
                contenedor.setSubgrupo(rs2.getString("sugr_codigo"));
                listArrayArticulos.add(contenedor);
            }
            }
            else {
                //EN CASO DE QUE NO HAYAN IDS, SELECCIONADOS EN EL SUBGRUPO, ENTONCES LIMPIA EL ARRAY DE ARTICULOS Y DE LOS
                //ARTICULOS SELECCIONADOS.
                listArrayArticulos.clear();
                listInsertArticulos.clear();

            }

            stkw001.spinerArticulos.setItems(listArrayArticulos, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<ArrayListContenedor> items) {
                    listInsertArticulos.clear(); //CADA VEZ QUE SELECCIONAMOS SUB-GRUPO, DEBE LIMPIAR EL ARRAY DE LOS ARTICULOS SELECCIONADOS EN EL SPINNER ARTICULOS.
                    int total_articulos=0;
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected())
                        {
                            ArrayListContenedor insArt = new ArrayListContenedor();
                            insArt.setId(items.get(i).getId());
                            insArt.setName(items.get(i).getName());
                            insArt.setLote(items.get(i).getLote());
                            insArt.setCantidad(items.get(i).getCantidad());
                            insArt.setFechaVencimiento(items.get(i).getFechaVencimiento());
                            insArt.setFecha_vencimientoParseado(items.get(i).getFecha_vencimientoParseado());
                            insArt.setSubgrupo(items.get(i).getSubgrupo());
                            listInsertArticulos.add(insArt);
                            total_articulos++;
                        }
                    }
                    stkw001.txt_total.setText(String.valueOf(total_articulos));
                    ArrayAdapter adapter = new ArrayAdapter(context_stkw001, R.layout.fila_columnas, R.id.txt_nro, listInsertArticulos) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView txt_nro = (TextView) view.findViewById(R.id.txt_nro);
                            TextView txt_producto = (TextView) view.findViewById(R.id.txt_producto);
                            TextView txt_lote = (TextView) view.findViewById(R.id.txt_lote);
                            TextView txt_vto = (TextView) view.findViewById(R.id.txt_vto);

                            txt_nro.setText(""+listInsertArticulos.get(position).getId());
                            txt_producto.setText(""+listInsertArticulos.get(position).getName());
                            txt_lote.setText(""+listInsertArticulos.get(position).getLote());
                            txt_vto.setText(""+listInsertArticulos.get(position).getFecha_vencimientoParseado());

                            return view;
                        }
                    };
                    stkw001.LvArticulosStkw001.setAdapter(adapter);
                }
            });
        }
        catch (Exception E){
            String error= E.toString();
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


    public static void stkw001_txt_sucursalOnclick( Activity activity){

        stkw001.txt_sucursal.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v)
        {
            stkw001.sp_sucursal.showSpinerDialog();
        } } );

        stkw001.sp_sucursal.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                stkw001.txt_sucursal.setText(arrSucursales.get(i));
                stkw001.txt_id_sucursal.setText(arrIdSucursales.get(i));
                arr_id_deposito.clear();
                arr_deposito.clear();
                stkw001.txt_deposito.setText("");
                stkw001.txt_id_deposito.setText("");
                stkw001.txt_id_area.setText("");
                stkw001.txt_area.setText("");
                stkw001.txt_id_departamento.setText("");
                stkw001.txt_departamento.setText("");
                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");
                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");
                limpiarSubGrupo();
               // stkw001.txt_deposito.setText(arr_deposito.get(i));
              //  stkw001.txt_id_deposito.setText(arr_id_deposito.get(i));

                listar_depositos(activity,arrIdSucursales.get(i));
            }
        });
    }

    public static void Stkw001DepositoOnclick(){
        stkw001.sp_deposito.showSpinerDialog();
        stkw001.txt_deposito.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_deposito.showSpinerDialog();
        } } );
        stkw001.sp_deposito.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {



                stkw001.txt_id_area.setText("");
                stkw001.txt_area.setText("");
                stkw001.txt_id_departamento.setText("");
                stkw001.txt_departamento.setText("");
                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");
                stkw001.txt_id_grupo.setText("");
                stkw001.txt_grupo.setText("");
                limpiarSubGrupo();
                stkw001.txt_deposito.setText(arr_deposito.get(i));
                stkw001.txt_id_deposito.setText(arr_id_deposito.get(i));
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
                limpiarSubGrupo();
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
        stkw001.sp_departamento.showSpinerDialog();
        stkw001.txt_departamento.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            stkw001.sp_departamento.showSpinerDialog();
        } } );
        stkw001.sp_departamento.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                limpiarSubGrupo();

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
        stkw001.sp_seccion.showSpinerDialog();
        stkw001.txt_seccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_seccion.showSpinerDialog();
            } } );
        stkw001.sp_seccion.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                limpiarSubGrupo();
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
        stkw001.sp_familia.showSpinerDialog();
        stkw001.txt_familia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stkw001.sp_familia.showSpinerDialog();
            } } );
        stkw001.sp_familia.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                limpiarSubGrupo();
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
        stkw001.sp_grupo.showSpinerDialog();
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


    public static void ValidarStkw001(Activity activity,Context context){
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
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ATENCION!!!.")
                        .setMessage("DESEA REGISTRAR LOS DATOS INGRESADOS?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {  final  AsyncInsertStkw001 task = new  AsyncInsertStkw001();
                                task.execute();

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                }
        }

        else if (variables.tipo_stkw001==2) {
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
            else if (ids_subgrupos.equals("")){
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("SELECCIONE SUB-GRUPO.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else if (listArrayArticulos.size()==0){ //SI EL FILTRO PARA LA SELECCION AUTOMATICA ES CERO, ES PORQUE NO ARROJO NINGUN ARTICULO PARA EL REGISTRO.
                Alerter.create(activity)
                        .setTitle("ATENCION!")
                        .setText("LA COMBINACION DE FILTROS, NO CONTIENEN ARTICULOS PARA GENERAR LA TOMA.")
                        .setDuration(10000)
                        .setBackgroundColor(R.color.design_default_color_error)
                        .show();
            }
            else {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ATENCION!!!.")
                        .setMessage("DESEA REGISTRAR LOS DATOS INGRESADOS?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {  final  AsyncInsertStkw001 task = new  AsyncInsertStkw001();
                                task.execute();

                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        }
    }


    public static void listarStkw002(){

        try {

            SQLiteDatabase db_consulta= conSqlite.getReadableDatabase();
            Cursor cursor=db_consulta.rawQuery("select " +
                    "winvd_nro_inv," + //0
                    "ART_DESC," +//1
                    "winvd_lote," +//2
                    "winvd_art ," +//3
                    "date(winvd_fec_vto) as  winvd_fec_vto," +//4
                    "winvd_area," +//5
                    "winvd_dpto," +//6
                    "winvd_secc," +//7
                    "winvd_flia," +//8
                    "winvd_grupo," +//9
                    "winvd_cant_act," +//10
                    "winvd_cant_inv," +//11
                    "winvd_secu" +//12
                    " from stkw002inv" +
                    " WHERE arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' and winvd_nro_inv="+variables.nro_registro_toma+" " ,null);
            int cont=0;
            ListArrayInventarioArticulos = new ArrayList();
            while (cursor.moveToNext())
            {
                ListArrayInventarioArticulos.add(new Stkw002Item(  cursor.getString(1), cursor.getString(11),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(12)));
                cont++;
            }



        }
        catch (Exception e){
            String err=e.toString();
        }

    }

    public static void ExportarStkw002(){
        try {

            SQLiteDatabase dbConsultaCont= conSqlite.getReadableDatabase();
            Cursor cursorCont=dbConsultaCont.rawQuery("select  count(*) from stkw002inv" +
                    " WHERE arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' " ,null);
            if(cursorCont.moveToNext()){
                ContExportStkw002=cursorCont.getInt(0);
            }


            new AlertDialog.Builder(context_menuPrincipal)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("EXPORTACION.")
                    .setMessage("¿DESEA ENVIAR LOS INVETARIOS REALIZADOS?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            menu_principal.ProDialogExport =  new ProgressDialog(context_menuPrincipal);
                            menu_principal.ProDialogExport.setMax(ContExportStkw002);
                            LayerDrawable progressBarDrawable = new LayerDrawable(
                            new Drawable[]{
                            new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                            new ClipDrawable(
                            new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                            Gravity.START,  ClipDrawable.HORIZONTAL),
                            new ClipDrawable(   new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                            Gravity.START,  ClipDrawable.HORIZONTAL)    });
                            progressBarDrawable.setId(0,android.R.id.background);
                            progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                            progressBarDrawable.setId(2,android.R.id.progress);
                            menu_principal.ProDialogExport.setTitle("INVENTARIOS REGISTRADOS.");
                            menu_principal.ProDialogExport.setMessage("ENVIANDO...");
                            menu_principal.ProDialogExport.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            menu_principal.ProDialogExport.setProgressDrawable(progressBarDrawable);
                            menu_principal.ProDialogExport.show();
                            menu_principal.ProDialogExport.setCanceledOnTouchOutside(false);
                            menu_principal.ProDialogExport.setCancelable(false);
                            final AsyncExportStkw002 task = new AsyncExportStkw002();
                            task.execute();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();

        }
        catch (Exception e){
            new androidx.appcompat.app.AlertDialog.Builder(context_menuPrincipal)
                    .setTitle("INFORME!!!")
                    .setCancelable(false)
                    .setMessage(e.toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();
            try {
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static void ConsultarPendientesExportar(){
        SQLiteDatabase db_consulta= conSqlite.getReadableDatabase();
        Cursor cursor=db_consulta.rawQuery("select  count(distinct winvd_nro_inv)" +
                " from stkw002inv" +
                " WHERE estado='P' " ,null);
         ListArrayInventarioArticulos = new ArrayList();
        if (cursor.moveToNext())
        {
            menu_principal.txt_total.setText(cursor.getString(0));
        }
        db_consulta.close();

    }

    public static void CancelarToma(int nroToma,Context context){
        try {
            connect = conexion.Connections();
            connect.setAutoCommit(false);
            String Cancelar="UPDATE WEB_INVENTARIO SET WINVE_ESTADO_WEB='E',WINVE_FEC_CERRADO_WEB=CURRENT_TIMESTAMP,WINVE_LOGIN_CERRADO_WEB=UPPER('"+variables.userdb+"')" +
                    " WHERE WINVE_NUMERO="+nroToma+" ";
            PreparedStatement ps = connect.prepareStatement(Cancelar);
            ps.executeUpdate();
            connect.commit();
            ConsultarTomasServer(context);

        }
        catch (Exception error){
           String er=error.toString();

            try {
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    public static void ConsultarTomasServer(Context context) {
        try {

            controles.connect = controles.conexion.Connections();
            Statement stmt = controles.connect.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT winve_numero,winve_fec,flia_desc,grup_desc " +
                    "FROM WEB_INVENTARIO inner join V_WEB_FLIA on WEB_INVENTARIO.WINVE_FLIA=V_WEB_FLIA.FLIA_CODIGO " +
                    "inner join V_WEB_GRUPO on WEB_INVENTARIO.WINVE_GRUPO=V_WEB_GRUPO.GRUP_CODIGO AND  " +
                    "V_WEB_FLIA.FLIA_CODIGO=V_WEB_GRUPO.GRUP_FAMILIA WHERE WINVE_ESTADO_WEB='A' AND WEB_INVENTARIO.WINVE_LOGIN=UPPER('"+variables.userdb+"') " );

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
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.listitem3, R.id.text1, listaStkw001) {
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
            lista_stkw001_inv.listView.setAdapter(adapter);
        }
        catch (Exception e){
            String err=e.toString();
        }
    }

////////////////////////////////////////////////HILOS ///////////////////////////////////////////////////////////
    public static class AsyncInsertStkw001 extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stkw001.progress = ProgressDialog.show(context_stkw001, "PROCESANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
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
                        "WINVE_EMPR," +
                        "WINVE_NUMERO," +
                        "WINVE_ESTADO_WEB) values  " +
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
                        "'1', "+id_cabecera+",'A')";
                PreparedStatement ps = connect.prepareStatement(insertar);
                ps.executeUpdate();
                int secuencia=1;
                if(variables.tipo_stkw001_insert.equals("M"))//SI LA TOMA ES MANUAL
                {
                    for (int i = 0; i < listInsertArticulos.size(); i++) {
                        int cantidad_actual=Integer.parseInt(listInsertArticulos.get(i).getCantidad());
                        String fechaVto=listArrayArticulos.get(i).getFechaVencimiento();

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
                                "WINVD_UM," +
                                "WINVD_area," +
                                "WINVD_dpto," +
                                "WINVD_secc," +
                                "WINVD_flia," +
                                "WINVD_grupo," +
                                "WINVD_subgr," +
                                "WINVD_indiv)  VALUES ("+id_cabecera+",'"+listInsertArticulos.get(i).getId()+"',"+secuencia+","+cantidad_actual+",'','','',''," +
                                "'"+listInsertArticulos.get(i).getLote()+"',TO_DATE('"+fechaVto+"', 'yyyy/mm/dd hh24:mi:ss') ,'',''," +
                                "'"+stkw001.txt_id_area.getText().toString()+"','"+stkw001.txt_id_departamento.getText().toString()+"','"+stkw001.txt_id_seccion.getText().toString()+"'," +
                                "'"+stkw001.txt_id_familia.getText().toString()+"','"+stkw001.txt_id_grupo.getText().toString()+"','"+listInsertArticulos.get(i).getSubgrupo()+"','')";


                        PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                        ps2.executeUpdate();
                        secuencia++;
                    }}

                else if(variables.tipo_stkw001_insert.equals("C"))//SI LA TOMA ES SELECCION AUTOMATICA
                {
                    for (int i = 0; i < listArrayArticulos.size(); i++) {
                        int cantidad_actual=Integer.parseInt(listArrayArticulos.get(i).getCantidad());
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
                                "WINVD_UM," +
                                "WINVD_area," +
                                "WINVD_dpto," +
                                "WINVD_secc," +
                                "" +
                                "WINVD_flia," +
                                "WINVD_grupo," +
                                "WINVD_subgr," +
                                "WINVD_indiv)  VALUES ("+id_cabecera+",'"+listArrayArticulos.get(i).getId()+"',"+secuencia+","+cantidad_actual+",'','','',''," +
                                "'"+listArrayArticulos.get(i).getLote()+"',TO_DATE('"+listArrayArticulos.get(i).getFechaVencimiento()+"', 'yyyy/mm/dd hh24:mi:ss') ,'',''," +
                                "'"+stkw001.txt_id_area.getText().toString()+"','"+stkw001.txt_id_departamento.getText().toString()+"','"+stkw001.txt_id_seccion.getText().toString()+"'," +
                                "'"+stkw001.txt_id_familia.getText().toString()+"','"+stkw001.txt_id_grupo.getText().toString()+"','"+listArrayArticulos.get(i).getSubgrupo()+"','')";


                        PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                        ps2.executeUpdate();

                        secuencia++;
                    }}
                connect.commit();
                tipoRespuestaStkw001=1;
                mensajeRespuestaStkw001="REGISTRADO CON EXITO.";

            }
            catch (Exception error){
                mensajeRespuestaStkw001=error.toString();
                tipoRespuestaStkw001=0;

                try {
                    connect.rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stkw001.progress.dismiss();
            if(tipoRespuestaStkw001==1){
                new androidx.appcompat.app.AlertDialog.Builder( context_stkw001)
                        .setTitle("INFORME!!!")
                        .setCancelable(false)
                        .setMessage(mensajeRespuestaStkw001)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i=new Intent(context_stkw001,menu_principal.class);
                                context_stkw001.startActivity(i);
                                activity_stkw001.finish();
                            }
                        }).show();
            }
            else {
                new androidx.appcompat.app.AlertDialog.Builder( context_stkw001)
                        .setTitle("ATENCION!!!")
                        .setCancelable(false)
                        .setMessage(mensajeRespuestaStkw001)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }

        }
    }

    public static class AsyncExportStkw002 extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // menu_principal.ProDialogExport = ProgressDialog.show(context_menuPrincipal, "PROCESANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
           try {
               SQLiteDatabase db_consulta= conSqlite.getReadableDatabase();
               SQLiteDatabase db_consultaCab= conSqlite.getReadableDatabase();

                SQLiteDatabase db_UPDATE= conSqlite.getReadableDatabase();

               Cursor cursorCab=db_consultaCab.rawQuery("select distinct winvd_nro_inv from stkw002inv where '"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P'",null);
               connect = conexion.Connections();
               connect.setAutoCommit(false);
               while (cursorCab.moveToNext())
               {
                   Cursor cursor=db_consulta.rawQuery("select " +
                           "winvd_nro_inv," +  //0
                           "winvd_lote," +     //1
                           "winvd_art ," +     //2
                           "strftime('%d/%m/%Y',winvd_fec_vto)," +  //3
                           "winvd_area," +     //4
                           "winvd_dpto," +     //5
                           "winvd_secc," +     //6
                           "winvd_flia," +     //7
                           "winvd_grupo," +    //8
                           "winvd_cant_act," + //9
                           "winvd_cant_inv," +//10
                           "winvd_secu" +  //11
                           " from stkw002inv" +
                           " WHERE " +
                           "arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' AND winvd_nro_inv="+cursorCab.getString(0)+ " "  ,null);

                   int i=1;
                   while (cursor.moveToNext())
                   {
                       // String fecha_vto=cursor.getString(3);
                       String upd_inventario=" update web_inventario_det set winvd_cant_inv="+cursor.getString(10)+" " +
                               "where winvd_nro_inv="  +cursor.getString(0)+"  " +
                               "and winvd_secu="+ cursor.getString(11)+"";

                       PreparedStatement ps = connect.prepareStatement(upd_inventario);
                       ps.executeUpdate();
                       menu_principal.ProDialogExport.setProgress(i);
                       i++;
                   }

                   String upd_inventarioCab="UPDATE web_inventario SET WINVE_ESTADO_WEB='C' , WINVE_FEC_CERRADO_WEB=CURRENT_TIMESTAMP,WINVE_LOGIN_CERRADO_WEB=UPPER('"+variables.userdb+"') WHERE WINVE_NUMERO="+cursorCab.getString(0);
                   PreparedStatement pscAB = connect.prepareStatement(upd_inventarioCab);
                   pscAB.executeUpdate();

               }

               connect.commit();
               mensajeRespuestaExportStkw002="DATOS EXPORTADOS CON EXITO.";
               db_UPDATE.execSQL(" update stkw002inv set estado='C' where estado='P'");
               db_UPDATE.close();
               db_consulta.close();
               ConsultarPendientesExportar();
               //FALTA ACTUALIZAR LA CABECERA DEL INVENTARIO EN EL ORACLE SERVER.
           }catch (Exception e){
               mensajeRespuestaExportStkw002=e.toString();
           }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            menu_principal.ProDialogExport.dismiss();
            if(tipoRespuestaExportStkw002==1){
                new androidx.appcompat.app.AlertDialog.Builder(context_menuPrincipal)
                        .setTitle("INFORME!!!")
                        .setCancelable(false)
                        .setMessage(mensajeRespuestaExportStkw002)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }
            else {
                new androidx.appcompat.app.AlertDialog.Builder(context_menuPrincipal)
                        .setTitle("ATENCION!!!")
                        .setCancelable(false)
                        .setMessage(mensajeRespuestaExportStkw002)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }

        }
    }


}
