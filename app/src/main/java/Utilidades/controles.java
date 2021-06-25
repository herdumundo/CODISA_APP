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
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    public static   String table;
    public static int nroTomaCancelacion;
    public static   String  ids_subgrupos="",INVE_ART_EST="N",INVE_ART_EXIST="N",INVE_CANT_TOMA="1",INVE_IND_LOTE="S";
    static          List<ArrayListContenedor>   listArraySubgrupo   = new ArrayList<>();
    static          List<ArrayListContenedor>   listArrayArticulos  = new ArrayList<>();
    static          List<ArrayListContenedor>   listInsertArticulos = new ArrayList<>();
    public static   ArrayList<Stkw002List> listaStkw001;

    public static   List<Stkw002Item> ListArrayInventarioArticulos;
    public static   ConexionSQLiteHelper  conSqlite,   conn_gm;
    public static   Connection_Oracle   conexion = new Connection_Oracle();
    public static   Connection        connect ;
    public static   Context context_stkw001;
    public static   Context contextListaStkw001;
    public static   Context context_menuPrincipal;
    public static   Activity activity_stkw001;
    static int      tipoRespuestaStkw001; // 1=CORRECTO, 0=ERROR
    static int      tipoRespuestaExportStkw002; // 1=CORRECTO, 0=ERROR
    static String   mensajeRespuestaStkw001;
    static String   mensajeRespuestaExportStkw002;
    static int      ContExportStkw002;

    public static void conexion_sqlite(Context context) {
        conSqlite=      new ConexionSQLiteHelper(context,"CODISA_INV",null,2);
     }

    public static void volver_atras(Context context, Activity activity, Class clase_destino, String texto, int tipo)  {
        if(tipo==1){
            variables.tipoListaStkw002=1;
            variables.tipoStkw002=2;
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
                arr_deposito.add(rs.getString("dep_codigo")+"-"+rs.getString("dep_desc"));
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
                arr_area.add(rs.getString("area_codigo")+"-"+rs.getString("area_desc"));
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
                arr_departamento.add(rs.getString("dpto_codigo")+"-"+rs.getString("dpto_desc"));
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
                arr_seccion.add(rs.getString("secc_codigo")+"-"+rs.getString("secc_desc"));
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
                arr_familia.add(rs.getString("flia_codigo")+"-"+rs.getString("flia_desc"));
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
                arr_grupo.add(rs.getString("grup_codigo")+"-"+rs.getString("grup_desc"));
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
            limpiarListaViewArticulosSTKW001();
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
                    stkw001.txtTotalArticuloGrilla.setText("TOTAL ARTICULOS SELECCIONADOS: "+total_articulos);
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
        limpiarListaViewArticulosSTKW001();
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
                            {
                                final  AsyncInsertStkw001 task = new  AsyncInsertStkw001();
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
                int contador_stkw002=0;
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
                contador_stkw002++;
                ListArrayInventarioArticulos.add(new Stkw002Item(  cursor.getString(1), cursor.getString(11),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(12)));
                cont++;
            }
            stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ contador_stkw002);




        }
        catch (Exception e){
            String err=e.toString();
        }

    }

    public static void ExportarStkw002(){
        try {

            SQLiteDatabase dbConsultaCont= conSqlite.getReadableDatabase();
            Cursor cursorCont=dbConsultaCont.rawQuery("select  count(*) from stkw002inv" +
                    " WHERE arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' AND UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+ "')" ,null);
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
                " WHERE estado='P' AND UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+"')" ,null);
         ListArrayInventarioArticulos = new ArrayList();
        if (cursor.moveToNext())
        {
            menu_principal.txt_total.setText("PENDIENTES DE ENVIO :"+cursor.getString(0));
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
            ps.close();
            connect.commit();
            ConsultarTomasServer(context);

            AlertDialog.Builder alert3 = new AlertDialog.Builder(context);
            alert3.setTitle("REGISTRO CANCELADO CON EXITO.");
            alert3.setNeutralButton("CERRAR", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog3, int which)
                {
                    dialog3.dismiss();

                }
            });
            alert3.show();
        }
        catch (Exception error){
            new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("ATENCIÓN!!!.")
            .setMessage(error.getMessage()).show();

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
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.listitem_card, R.id.text1, listaStkw001) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    TextView text4 = (TextView) view.findViewById(R.id.text4);
                    ImageView txtimagen =   view.findViewById(R.id.txtimagen);

                    text1.setText("NRO. DE TOMA: "+listaStkw001.get(position).getNroToma());
                    text2.setText("FECHA: "+listaStkw001.get(position).getFechaToma());
                    text3.setText("FAMILIA:"+listaStkw001.get(position).getFamilia());
                    text4.setText("GRUPO:"+listaStkw001.get(position).getGrupo());
                    txtimagen.setImageResource(R.drawable.ic_consulta);

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
        int con=0;
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
                ps.close();
                int secuencia=1;
                if(variables.tipo_stkw001_insert.equals("M"))//SI LA TOMA ES MANUAL
                {
                    for (int i = 0; i < listInsertArticulos.size(); i++) {
                        int cantidad_actual=Integer.parseInt(listInsertArticulos.get(i).getCantidad());
                        String fechaVto=listInsertArticulos.get(i).getFechaVencimiento();

                        String insertar_detalle=" insert into WEB_INVENTARIO_DET (" +
                                "WINVD_NRO_INV," + //1
                                "WINVD_ART," +//2
                                "WINVD_SECU," +//3
                                "WINVD_CANT_ACT," +//4
                                "WINVD_CANT_INV," +//5
                                "WINVD_UBIC," +//6
                                "WINVD_CODIGO_BARRA," +//7
                                "WINVD_CANT_PED_RECEP," +//8
                                "WINVD_LOTE," +//9
                                "WINVD_FEC_VTO," +//10
                                "WINVD_LOTE_CLAVE," +//11
                                "WINVD_UM," +//12
                                "WINVD_area," +//13
                                "WINVD_dpto," +//14
                                "WINVD_secc," +//15
                                "WINVD_flia," +//16
                                "WINVD_grupo," +//17
                                "WINVD_subgr," +//18
                                "WINVD_indiv" +//19
                                ")  VALUES ("+
                                id_cabecera+",'"+
                                listInsertArticulos.get(i).getId()+"',"+
                                secuencia+","+
                                cantidad_actual+"," +
                                "''," +
                                "''," +
                                "''," +
                                "''," +  "'"+
                                listInsertArticulos.get(i).getLote()+"'," +
                                "TO_DATE('"+fechaVto+"', 'yyyy/mm/dd hh24:mi:ss') ," +
                                "''," +
                                "''," +
                                "'"+stkw001.txt_id_area.getText().toString()+"','"+stkw001.txt_id_departamento.getText().toString()+"','"+stkw001.txt_id_seccion.getText().toString()+"'," +
                                "'"+stkw001.txt_id_familia.getText().toString()+"','"+stkw001.txt_id_grupo.getText().toString()+"','"+listInsertArticulos.get(i).getSubgrupo()+"','')";


                        PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                        ps2.executeUpdate();
                        ps2.close();
                        secuencia++;
                        con++;
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
                        ps2.close();
                        secuencia++;
                        con++;
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


    public static class AsyncListarCancelaciones extends AsyncTask<Void, Void, Void>
    {
         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lista_stkw001_inv.pgDialog = ProgressDialog.show(contextListaStkw001, "CONSULTANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            listarWebViewStkw001Cancelacion(nroTomaCancelacion);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lista_stkw001_inv.pgDialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(contextListaStkw001);
            alert.setTitle("ARTICULOS CARGADOS");
            WebView wv = new WebView(contextListaStkw001);
            wv.loadData(table, "text/html", "utf-8");
            alert.setView(wv);
            alert.setNegativeButton("CERRAR", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.dismiss();
                }
            });
            alert.setNeutralButton("CANCELAR TOMA", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    AlertDialog.Builder alert2 = new AlertDialog.Builder(contextListaStkw001);
                    alert2.setTitle("¿ESTÁ SEGURO QUE DESEA CANCELAR LA TOMA NRO."+nroTomaCancelacion+"?");
                    alert2.setNegativeButton("NO", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog2, int id)
                        {
                            dialog2.dismiss();
                        }
                    });
                    alert2.setNeutralButton("SI", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog2, int which)
                        {
                            CancelarToma(nroTomaCancelacion,contextListaStkw001);
                            dialog2.dismiss();

                        }
                    });
                    alert2.show();
                }
            });
            alert.show();
        }
    }

    public static void listarWebViewStkw001Cancelacion(int nroToma) {
        try {
            String html="";
            table="";
            connect =  conexion.Connections();
            Statement stmt =  connect.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT  " +
                    " to_char(a.ARDE_FEC_VTO_LOTE) as ARDE_FEC_VTO_LOTE ,b.winvd_fec_vto,  a.ARDE_SUC, " +
                    "b.winvd_nro_inv, b.winvd_art,a.ART_DESC,b.winvd_lote,b.winvd_fec_vto,b.winvd_area,    " +
                    " b.winvd_dpto,b.winvd_secc,b.winvd_flia,b.winvd_grupo,b.winvd_cant_act,c.winve_fec,  dpto_desc,secc_desc,flia_desc,grup_desc,area_desc   " +
                    " FROM   V_WEB_ARTICULOS_CLASIFICACION  a    " +
                    " inner join WEB_INVENTARIO_det b on a.arde_lote=b.winvd_lote    " +
                    " and a.ART_CODIGO=b.winvd_art    " +
                    "  and a.SECC_CODIGO=b.winvd_secc   " +
                    " and a.ARDE_FEC_VTO_LOTE=b.winvd_fec_vto    " +
                    "inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero   " +
                    " and c.winve_dep=a.ARDE_DEP    and c.winve_area=a.AREA_CODIGO    " +
                    " and c.winve_suc=a.ARDE_SUC    and c.winve_secc=a.SECC_CODIGO  " +
                    "  where C.WINVE_NUMERO="+nroToma );
            int cont=0;
            while (rs.next()){
                cont++;
                html=html+  "<tr>" +
                        "<td>"+rs.getString("winvd_art")+"</td>" +
                        "<td>"+rs.getString("ART_DESC")+"</td>" +
                        "<td>"+rs.getString("winvd_lote")+"</td>" +
                        "<td>"+rs.getString("ARDE_FEC_VTO_LOTE")+"</td>" +
                        "</tr>";

            }
            rs.close();

            table = "<div>TOTAL DE ARTICULOS "+cont+"<table border=1> " +
                    "<thead> " +
                    "<tr>" +
                    "<td>COD</td>" +
                    "<td>ARTICULO</td>" +
                    "<td>LOTE</td>" +
                    "<td>FECHA VENCIMIENTO</td>" +
                    "</tr> </thead><tbody>"+html+" </tbody></table></div>" ;


        }
        catch (Exception e){
            String mens=e.toString();

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
            SQLiteDatabase db_UPDATE= conSqlite.getReadableDatabase();
            SQLiteDatabase dbAnularSqliteCab= conSqlite.getReadableDatabase();

            int tipoRegistro=0;
           try {
               SQLiteDatabase db_consulta= conSqlite.getReadableDatabase();
               SQLiteDatabase db_consultaCab= conSqlite.getReadableDatabase();

                Cursor cursorCab=db_consultaCab.rawQuery("select distinct winvd_nro_inv,WINVE_LOGIN_CERRADO_WEB from stkw002inv " +
                "where '"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' and UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+"')",null);
               connect = conexion.Connections();
               connect.setAutoCommit(false);
               int contadorMensaje=0; // EN EL CASO DE QUE QUEDE EN CERO, ENTONCES EL MENSAJE SERA, DE QUE NO HAY DATOS PARA EXPORTAR
               while (cursorCab.moveToNext())
               {
                   int nroCabecera=cursorCab.getInt(0);

                   contadorMensaje++;
                   String WINVE_LOGIN_CERRADO_WEB=cursorCab.getString(1);
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
                           "arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' AND winvd_nro_inv="+nroCabecera+
                           " and UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+"')"  ,null);

                   //PRIMERO CONSULTA SI EL NRO DE TOMA A EXPORTAR, NO SE ENCUENTRA ANULADO EN EL SERVER
                   Statement stmtAnulados =  connect.createStatement();

                   ResultSet rsAnulados = stmtAnulados.executeQuery("SELECT WINVE_estado_web FROM  web_inventario WHERE WINVE_NUMERO="+nroCabecera+" " );
                   String anulado="";
                   while (rsAnulados.next()){
                       anulado=rsAnulados.getString("WINVE_estado_web");
                    }
                   //SI SE ENCUENTRA ANULADO, VA AL SQLITE Y ACTUALIZA EL ESTADO A "E"
                    if(anulado.trim().equals("E"))
                    { //
                        dbAnularSqliteCab.execSQL(" update stkw002inv set estado='E' where winvd_nro_inv="  +nroCabecera+" "   );
                    }
                   //SI NO ESTA ANULADO, ENTONCES EXPORTA
                   else {
                            db_UPDATE.beginTransaction();
                            int i=1;
                            // ESTE CURSOR RECORRE EL DETALLE DEL SQLITE, PARA ACTUALIZAR,
                            // PRIMERO EL DETALLE DEL SERVER, LUEGO EL DETALLE DEL SQLITE
                            while (cursor.moveToNext())
                            {
                                // SE ACTUALIZAR EL DETALLE DEL SERVIDOR.
                                PreparedStatement ps = connect.prepareStatement(" update web_inventario_det set winvd_cant_inv="+cursor.getString(10)+" " + "" +
                                       " where winvd_nro_inv="  +cursor.getString(0)+"   and winvd_secu="+ cursor.getString(11)+"");
                                ps.executeUpdate();
                                ps.close();
                               //SQLITE ACTUALIZA EL ESTADO DEL INVENTARIO A C
                               db_UPDATE.execSQL(" update stkw002inv set estado='C' where winvd_nro_inv="  +cursor.getString(0)+"  and winvd_secu="+ cursor.getString(11)+""   );
                               menu_principal.ProDialogExport.setProgress(i);
                               i++;
                            }
                            //SERVER ACTUALIZA LA CABECERA
                            PreparedStatement pscAB = connect.prepareStatement("UPDATE web_inventario SET WINVE_ESTADO_WEB='C' , " +
                                   "WINVE_FEC_CERRADO_WEB=CURRENT_TIMESTAMP," +
                                   "WINVE_LOGIN_CERRADO_WEB=UPPER('"+WINVE_LOGIN_CERRADO_WEB+"') " +
                                   "WHERE WINVE_NUMERO="+nroCabecera);
                            pscAB.executeUpdate();
                        pscAB.close();
                            db_UPDATE.setTransactionSuccessful();
                            db_UPDATE.endTransaction();

                   }
                }//FIN DEL CURSOR CABECERA

                if(contadorMensaje==0){
                    mensajeRespuestaExportStkw002="NO SE ENCONTRARON REGISTROS POR EXPORTAR.";
                }
                else {
                    mensajeRespuestaExportStkw002="DATOS EXPORTADOS CON EXITO.";

                }
                db_consulta.close();

           }catch (Exception e)
           {
               try
               {
                   db_UPDATE.endTransaction();
                   connect.rollback();
                   tipoRegistro=1;
                   db_UPDATE.close();

               } catch (Exception es)
               {
                  // throwables.printStackTrace();
                   mensajeRespuestaExportStkw002=es.toString();

               }
               mensajeRespuestaExportStkw002=e.toString();
               return null;
           }
            //
            finally
           {
                try {
                        if (tipoRegistro==0)
                        {
                            connect.commit();
                            db_UPDATE.close();
                            ConsultarPendientesExportar();
                        }

                    } catch (Exception e)
                        {
                           // throwables.printStackTrace();
                            mensajeRespuestaExportStkw002=e.toString();
                        }
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

    public static void limpiarListaViewArticulosSTKW001(){
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
        stkw001.txtTotalArticuloGrilla.setText("TOTAL ARTICULOS SELECCIONADOS: 0");
    }
}
