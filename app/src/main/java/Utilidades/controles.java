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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codisa_app.R;
import com.example.codisa_app.SpinnerDialog;
import com.example.codisa_app.lista_stkw001_inv;
import com.example.codisa_app.lista_stkw002_inv;
import com.example.codisa_app.listado_consolidado;
import com.example.codisa_app.menu_principal;
import com.example.codisa_app.stkw001;
import com.example.codisa_app.stkw002;
import com.tapadoo.alerter.Alerter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import maes.tech.intentanim.CustomIntent;

public class controles {
    public static int resBD;
    public static String mensajeLogin;
    public static int contador_stkw002=0;
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
    public static   int verificadorRed;//SI ES 0 NO HAY RED, O SI ES 1 HAY RED.
    public static   String table;
    public static   int     nroTomaCancelacion;
    public static   int     nroTomaConsolidado;
    public static   String  consolidadoCancelacion="";
    static          String  winveSubGrupoParcial=""; // DATO NECESARIO PARA INSERTAR EL ANIDADO DE GRUPO Y SUBGRUPO A LA BD.
    public static   String  ids_subgrupos="",ids_grupos="",INVE_ART_EST="N",INVE_ART_EXIST="N",INVE_CANT_TOMA="1",INVE_IND_LOTE="S";
    static          String  grupoSeleccionados="";
    static          String  SubgrupoGruposSeleccionados="";
    static          String  SubgrupoSeleccionadosArticulos="";
    static          String  ArticulosSubgruposSeleccionados="";
    static          String  mensajeRespuestaStkw001;
    static          String  mensajeRespuestaExportStkw002;
    static          String  grupoParcial="";
    static          List<ArrayListContenedor>   listArrayGrupo      = new ArrayList<>();
    static          List<ArrayListContenedor>   listArraySubgrupo   = new ArrayList<>();
    static          List<ArrayListContenedor>   listArrayArticulos  = new ArrayList<>();
    static          List<ArrayListContenedor>   listInsertArticulos = new ArrayList<>();
    public static   ArrayList<Stkw002List>      listaStkw001;
    static boolean  GruposTodos=false;
    public static   List<Stkw002Item> ListArrayInventarioArticulos;
    public static   ConexionSQLiteHelper  conSqlite;
    public static   Connection        connect  ;
    public static   Context context_stkw001;
    public static   Context contextListaStkw001;
    public static   Context context_menuPrincipal;
    public static   Context contextListadoConsolidado;
    public static   Activity activity_stkw001;
    static int      tipoRespuestaStkw001; // 1=CORRECTO, 0=ERROR
    static int      tipoRespuestaExportStkw002; // 1=CORRECTO, 0=ERROR
    static  int     gruposSeleccionados=0;
    static  int     ContExportStkw002;
    public static   AlertDialog.Builder builder;
    public static   AlertDialog ad;

    public  static  void connect(){
        try {
            Connection_Oracle   conexion = new Connection_Oracle();
            connect = conexion.Connections();
            }
        catch (Exception e){
            String asd=e.getMessage();
        }
    }

    public static void desconectarBD(){
        try {
            connect.close();
            System.exit(0);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void conexion_sqlite(Context context) {
        conSqlite=      new ConexionSQLiteHelper(context,"CODISA_INV",null,ConexionSQLiteHelper.DATABASE_VERSION);
    }

    public static void volver_atras(Context context, Activity activity, Class clase_destino, String texto, int tipo)  {
        switch(tipo)
        {
            // declaración case
            // los valores deben ser del mismo tipo de la expresión
            case 1 :
                variables.tipoListaStkw002=1;
                variables.tipoStkw002=2;

                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage(texto);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context, clase_destino);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        context.startActivity(intent);
                        activity.finish();
                        CustomIntent.customType(context,"right-to-left");

                    }
                });
                builder.setNegativeButton("No",null);
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                break; // break es opcional

            case 3 :

                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage(texto);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       /* Intent intent = new Intent(context, clase_destino);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        activity.finish();
                        CustomIntent.customType(context,"right-to-left");*/
                        variables.userdb=null;
                        variables.passdb=null;
                        desconectarBD();


                    }
                });
                builder.setNegativeButton("No",null);
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                break; // break es opcional
            case 5 :
                activity.finish();
                break; // break es opcional

            default :
                // Declaraciones
                Intent intent = new Intent(context, clase_destino);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(intent);
                CustomIntent.customType(context,"right-to-left");

                activity.finish();
        }

    }

    public static void listar_sucursales(Activity activity) {

        stkw001.sp_sucursal = new SpinnerDialog(activity,arrSucursales,"Listado de Sucursales");
        stkw001_txt_sucursalOnclick(activity);


    }

    public static void listar_depositos(Activity activity, String id_sucursal,int tipo) { // 1= A LA LLAMADA DESDE EL STKW01 Y 2= A LA LLAMADA DE CONSULTA_ARTICULOS.JAVA
        try {

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SUC_DEP   where suc_codigo='"+id_sucursal+"'");

            while ( rs.next())
            {
                arr_id_deposito.add(rs.getString("dep_codigo"));
                arr_deposito.add(rs.getString("dep_codigo")+" - "+rs.getString("dep_desc"));
            }
            if(tipo==1){
                stkw001.sp_deposito = new SpinnerDialog(activity,arr_deposito,"Listado de depositos");
                Stkw001DepositoOnclick();
            }


        }
        catch (Exception e){

            VerificarRed(context_stkw001);
        }

    }

    public static void listar_areas(Activity activity, Context context,int tipo_toma) {
        try {

            //connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_AREA  ");
            arr_id_area.clear();
            arr_area.clear();
            // listArraySubgrupo.clear();
            while ( rs.next())
            {
                arr_id_area.add(rs.getString("area_codigo"));
                arr_area.add(rs.getString("area_codigo")+" - "+rs.getString("area_desc"));
            }
            stkw001.sp_area = new SpinnerDialog(activity,arr_area,"Listado de areas");
            Stkw001AreaOnclick(activity,context,tipo_toma);


        }
        catch (Exception e){
            VerificarRed(context_stkw001);
        }

    }

    public static void listar_departamentos(Activity activity, String id_area, Context context,int tipo_toma) {
        try {

            //connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_DPTO   where dpto_area='"+id_area+"'");
            arr_id_departamento.clear();
            arr_departamento.clear();

            while ( rs.next())
            {
                arr_id_departamento.add(rs.getString("dpto_codigo"));
                arr_departamento.add(rs.getString("dpto_codigo")+" - "+rs.getString("dpto_desc"));
            }
            stkw001.sp_departamento = new SpinnerDialog(activity,arr_departamento,"Listado de departamentos");
            Stkw001DepartamentoOnclick(activity,context,  tipo_toma);


        }
        catch (Exception e){
            VerificarRed(context_stkw001);
        }

    }

    public static void listar_seccion(Activity activity, String id_departamento, Context context,int tipo_toma) {
        try {

            //connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_SECC   where secc_dpto='"+id_departamento+"'" +
                    " and secc_area='"+stkw001.txt_id_area.getText().toString().trim()+"'");
            arr_id_seccion.clear();
            arr_seccion.clear();
            //   listArraySubgrupo.clear();

            while ( rs.next())
            {
                arr_id_seccion.add(rs.getString("secc_codigo"));
                arr_seccion.add(rs.getString("secc_codigo")+" - "+rs.getString("secc_desc"));
            }
            stkw001.sp_seccion = new SpinnerDialog(activity,arr_seccion,"Listado de secciones");
            Stkw001SeccionOnclick(activity,context,  tipo_toma);

        }
        catch (Exception e){
            VerificarRed(context_stkw001);
        }

    }

    public static void listar_familia(Activity activity, String id_seccion, Context context,int tipo_toma) {
        try {
          //  connect.close();
           // System.exit(0);
            //connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_FLIA   where flia_seccion='"+id_seccion+"'" +
                    " and flia_area='"+stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and flia_dpto='"+stkw001.txt_id_departamento.getText().toString().trim()+"'");
            arr_id_familia.clear();
            arr_familia.clear();
          //  listArraySubgrupo.clear();

            arr_id_familia.add("T");
            arr_familia.add("TODOS");
            while ( rs.next())
            {
                arr_id_familia.add(rs.getString("flia_codigo"));
                arr_familia.add(rs.getString("flia_codigo")+" - "+rs.getString("flia_desc"));
            }
            stkw001.sp_familia = new SpinnerDialog(activity,arr_familia,"Listado de familias");
            Stkw001FamiliaOnclick(activity, context, tipo_toma);

        }
        catch (Exception e){
            VerificarRed(context_stkw001);
          //  Toast.makeText(context_stkw001,e.getMessage(),Toast.LENGTH_LONG).show();

        }

    }

    public static void listar_grupo(Activity activity, String id_familia, Context context,int tipo_toma) {
        try {
            GruposTodos=false;// SE INICIALIZA, DECIMOS DE ENTRADA QUE GRUPOS NO SE VA A SELECCIONAR TODOS.
            String sqlFamilia="";
            if(id_familia.equals("T")){
                sqlFamilia="";
            }
            else {
                sqlFamilia= "grup_familia='"+id_familia+"' and  ";
            }
            //connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from V_WEB_GRUPO  " +
                    " where " +
                        sqlFamilia +
                    " grup_area='"+stkw001.txt_id_area.getText().toString().trim()+"'  " +
                    " and   grup_seccion='"+stkw001.txt_id_seccion.getText().toString().trim()+"'  " +
                    " and   grup_dpto='"+stkw001.txt_id_departamento.getText().toString().trim()+"'");

            listArrayGrupo.clear();
            while ( rs.next())
            {
                ArrayListContenedor h = new ArrayListContenedor();
                h.setId(Integer.parseInt(rs.getString("grup_codigo")));
                h.setidFamilia( rs.getString("grup_familia") );
                h.setDescGrupo( rs.getString("grup_desc") );
                h.setName(  rs.getString("grup_codigo")+" - "+rs.getString("grup_desc") );
                h.setLote("");

                listArrayGrupo.add(h);
            }

            stkw001.spinerGrupo.setItems(listArrayGrupo, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<ArrayListContenedor> items)
                {
                    //FORMULA PARA RECUPERAR SOLO LOS ITEMS SELECCIONADOS, SE PUEDE CREAR UNA ARRAYLIST PARA SOLO LOS SELECCIONADOS.
                    ids_grupos="";
                    grupoSeleccionados="";
                    int contGrupoSelec=0;
                    stkw001.spinerGrupo.setSearchHint("Busqueda");
                    gruposSeleccionados=0; // SE RESETEA A CERO LA CANTIDAD DE GRUPOS SELECCIONADOS
                    int i = 0;
                    for (  i = 0; i < items.size(); )
                    {
                        if (items.get(i).isSelected())
                        {
                            if(i==0)
                            {
                                ids_grupos=ids_grupos+items.get(i).getId();
                            }
                            else
                            {
                                ids_grupos=ids_grupos+","+items.get(i).getId();
                            }

                            if(!grupoSeleccionados.contains(String.valueOf(items.get(i).getId())))
                            {
                                if(contGrupoSelec==0)
                                {
                                    grupoSeleccionados=String.valueOf(items.get(i).getId());
                                }
                                else
                                {
                                    grupoSeleccionados=grupoSeleccionados+","+items.get(i).getId();

                                }
                                contGrupoSelec ++;
                            }
                            gruposSeleccionados++;
                        }
                        i++;
                    }

                    int cantidadGrupo=listArrayGrupo.size();
                    int cantidadGrupoSeleccionado=gruposSeleccionados;
                    if(cantidadGrupo==cantidadGrupoSeleccionado)
                    {   // AQUI, SI LA CANTIDAD DE GRUPO SELECCIONADOS  ES IGUAL AL TOTAL DEl GRUPO, ENTONCES EL BOLEANO ENTRA A TRUE.
                        GruposTodos=true;
                        ids_grupos="T";
                        ids_subgrupos="T";
                        stkw001.lbl_subgrupo.setVisibility(View.GONE);
                        stkw001.spinerSubGrupo.setVisibility(View.GONE);
                        listarArticulos();
                    }
                    else{
                        GruposTodos=false;
                        stkw001.lbl_subgrupo.setVisibility(View.VISIBLE);
                        stkw001.spinerSubGrupo.setVisibility(View.VISIBLE);
                        listar_SubGrupo(activity,ids_grupos,context,tipo_toma);
                    }

                }
            });
            VerificarRed(context_stkw001);

        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage()+" LISTAR GRUPO",Toast.LENGTH_LONG).show();
            VerificarRed(context_stkw001);
        }

    }

    public static void listar_SubGrupo(Activity activity, String id_grupo, Context context,int tipo_toma) {
        try
        {
            ids_subgrupos="";
            String SqlFamilia="";
            String idFamilia=stkw001.txt_id_familia.getText().toString();
            String SqlGrupo=" sugr_grupo in ("+id_grupo+") and ";
            if(idFamilia.equals("T"))//T ES IGUAL A TODOS, ENTONCES QUITA EL FILTRO POR FAMILIA.
            {
                SqlFamilia="";
            }
            else {
                SqlFamilia=  " and   sugr_flia='"+idFamilia+"'  ";
            }
            if(GruposTodos==true)
            { //SI SE SELECCIONARON TODOS LOS GRUPOS, ENTONCES QUITA EL FILTRO POR GRUPO.
                SqlGrupo="";
            }

            listArraySubgrupo.clear();
            if(id_grupo.length()>0){
                //connect = conexion.Connections();

                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("   select * from V_WEB_GRUPO  inner join V_WEB_SUBGRUPO on " +
                        "V_WEB_SUBGRUPO.sugr_grupo=V_WEB_GRUPO.GRUP_CODIGO  and " +
                        "V_WEB_SUBGRUPO.SUGR_FLIA=V_WEB_GRUPO.GRUP_FAMILIA and   " +
                        "V_WEB_SUBGRUPO.SUGR_AREA=V_WEB_GRUPO.GRUP_AREA and " +
                        "V_WEB_GRUPO.GRUP_SECCION=V_WEB_SUBGRUPO.SUGR_SECCION and " +
                        "V_WEB_SUBGRUPO.SUGR_DPTO=V_WEB_GRUPO.GRUP_DPTO  " +
                        " where " +
                        "       " +SqlGrupo+
                        "    sugr_area='"    +stkw001.txt_id_area.getText().toString().trim()+"'  " +
                        " and   sugr_seccion='" +stkw001.txt_id_seccion.getText().toString().trim()+"'  " +
                        " and   sugr_dpto='"    +stkw001.txt_id_departamento.getText().toString().trim()+"'"+
                        SqlFamilia +" order by sugr_GRUPO asc");

                int cont=0;
                while ( rs.next())
                {
                    ArrayListContenedor h = new ArrayListContenedor();
                   // h.setId(Integer.parseInt(rs.getString("sugr_codigo")));
                    h.setstringID(rs.getString("sugr_GRUPO")+"#"+rs.getString("sugr_codigo"));
                    h.setidFamilia(rs.getString("SUGR_FLIA"));
                    h.setidGrupo(rs.getString("sugr_GRUPO"));
                    h.setDescGrupo( rs.getString("grup_desc"));
                    h.setidSubgrupo( rs.getString("sugr_codigo") );
                    h.setName(rs.getString("grup_desc")+": [" +rs.getString("sugr_codigo")+" - "+rs.getString("sugr_desc")+"]");
                    h.setLote("");
                    listArraySubgrupo.add(h);
                    cont++;
                }
            }
            //EN CASO DE QUE NO HAYAN IDS, SELECCIONADOS EN EL SUBGRUPO, ENTONCES LIMPIA EL ARRAY DE ARTICULOS Y DE LOS
            //ARTICULOS SELECCIONADOS.
            listArrayArticulos.clear();
            listInsertArticulos.clear();
            listarArticulos();

            stkw001.spinerSubGrupo.setItems(listArraySubgrupo, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<ArrayListContenedor> items) {
                    //FORMULA PARA RECUPERAR SOLO LOS ITEMS SELECCIONADOS, SE PUEDE CREAR UNA ARRAYLIST PARA SOLO LOS SELECCIONADOS.
                    stkw001.spinerArticulos.setSearchHint("Busqueda");
                    ids_subgrupos="";
                    SubgrupoGruposSeleccionados="";
                    SubgrupoSeleccionadosArticulos="";
                    int conSubgrupoGrupoSelec=0;
                    int conArtGrupSub=0;
                    for (int i = 0; i < items.size();)
                    {
                        if (items.get(i).isSelected())
                        {
                            if(i==0)
                            {
                                ids_subgrupos= "'"+items.get(i).getstringID()+"'";
                            }
                            else
                            {
                                ids_subgrupos=ids_subgrupos+",'"+items.get(i).getstringID()+"'";
                            }
                            String valorIdGrupo=String.valueOf(items.get(i).getidGrupo());
                            String valorGrupoSub=String.valueOf(items.get(i).getstringID());
                            if(!SubgrupoGruposSeleccionados.contains(valorIdGrupo))
                            {
                                if(conSubgrupoGrupoSelec==0)
                                {
                                    SubgrupoGruposSeleccionados=valorIdGrupo;
                                 }
                                else
                                {
                                    SubgrupoGruposSeleccionados=SubgrupoGruposSeleccionados+","+valorIdGrupo;
                                 }
                                conSubgrupoGrupoSelec ++;
                            }
                            if(!SubgrupoSeleccionadosArticulos.contains(valorGrupoSub))
                            {
                                if(conArtGrupSub==0)
                                {
                                     SubgrupoSeleccionadosArticulos=valorGrupoSub;
                                }
                                else
                                {
                                     SubgrupoSeleccionadosArticulos=SubgrupoSeleccionadosArticulos+","+valorGrupoSub;
                                }
                                conArtGrupSub ++;
                            }
                            i++;
                        }
                    }
                    if(SubgrupoGruposSeleccionados.length()==grupoSeleccionados.length())
                          // grupoSeleccionados ES IGUAL A TODAS LAS UNIONES DEL GRUPO
                          // SubgrupoGruposSeleccionados ES IGUAL AL DISTINCT DE TODOS LOS SUBGRUPOS SELECCIONADOS, PARA EXTRAER EL ID DEL GRUPO UNICO.
                    {
                        if(tipo_toma==1)
                        {
                            try
                            {
                                listarArticulos();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else   if(tipo_toma==2)
                        {
                            listarArticulos();
                        }
                    }
                    else
                    {
                        listArrayArticulos.clear();
                        listInsertArticulos.clear();
                        limpiarListaViewArticulosSTKW001();
                        builder = new android.app.AlertDialog.Builder(context);
                        builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                        builder.setTitle("¡Atención!");
                        builder.setMessage("Grupos seleccionados de màs.");
                        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad = builder.show();
                        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                        ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                    }
                }
            });
            VerificarRed(context_stkw001);

        }
        catch (Exception e){
           Toast.makeText(context_stkw001,e.getMessage()+" LISTAR SUBGRUPO",Toast.LENGTH_LONG).show();
        }

    }

    public static void listarArticulos(){

        try {
            String inLote="";
            String inEstado="";
            String inExistenciaCero="";
            String TotalJoin="";
            if(!stkw001.BolLote){
                inLote="and arde_lote='000000' AND ARDE_FEC_VTO_LOTE='31/12/5000'";
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

            String SqlFamilia="";
            String SqlGrupo="";
            String SqlSubGrupo="";
            String idFamilia=stkw001.txt_id_familia.getText().toString();
            String id_sucursal=stkw001.txt_id_sucursal.getText().toString();
            String id_deposito=stkw001.txt_id_deposito.getText().toString();
            String id_area=stkw001.txt_id_area.getText().toString();
            String id_dpto=stkw001.txt_id_departamento.getText().toString();
            String id_seccion=stkw001.txt_id_seccion.getText().toString();

            if(idFamilia.equals("T"))
            {
                SqlFamilia="";
                SqlGrupo="";
                SqlSubGrupo="";
                ids_grupos="T";
            }
            else {
                SqlFamilia=  " and   flia_codigo='"+idFamilia+"'  ";
                SqlGrupo="      and  grup_codigo in ("+ids_grupos+")";
                SqlSubGrupo="   and CONCAT(CONCAT(grup_codigo, '#'),SUGR_CODIGO) in ("+ids_subgrupos+")";

            }
            if(GruposTodos==true){
                SqlGrupo="";
                SqlSubGrupo="";
            }


            if(ids_subgrupos.length()>0)
           {
                ResultSet rs2 = stmt2.executeQuery("" +
                "select " +
                "  CONCAT(CONCAT(grup_codigo, '#'),SUGR_CODIGO) as concatID,TO_NUMBER (arde_cant_act) as cantidad," +
                "   TO_CHAR(arde_fec_vto_lote,'DD-MM-YYYY') as vencimiento," +
                "   v_web_articulos_clasificacion.* " +
                "from " +
                "   v_web_articulos_clasificacion " +
                "where " +
                "   arde_suc="+id_sucursal+"      and " +
                "   arde_dep="+id_deposito+"      and " +
                "   area_codigo="+id_area+"   and " +
                "   dpto_codigo="+id_dpto+"   and " +
            "   secc_codigo="+id_seccion+ SqlGrupo + SqlFamilia+SqlSubGrupo+ TotalJoin+" order by flia_desc,GRUP_desc,sugr_desc,art_desc  asc");
                listArrayArticulos.clear();
                listInsertArticulos.clear();
                while ( rs2.next())
                {
                    String vencimiento=rs2.getString("vencimiento");
                    ArrayListContenedor contenedor = new ArrayListContenedor();
                    contenedor.setId(Integer.parseInt(rs2.getString("art_codigo")));
                    contenedor.setstringID( rs2.getString("concatID") );
                    contenedor.setidFamilia( rs2.getString("flia_codigo") );
                    contenedor.setidGrupo( rs2.getString("GRUP_CODIGO") );
                    contenedor.setDescGrupo( rs2.getString("GRUP_desc") );
                    contenedor.setDescFamilia( rs2.getString("flia_desc") );
                    contenedor.setName(rs2.getString("art_desc"));
                    contenedor.setLote(rs2.getString("arde_lote"));
                    contenedor.setCantidad(rs2.getString("cantidad"));
                    contenedor.setFechaVencimiento(rs2.getString("ARDE_FEC_VTO_LOTE"));
                    contenedor.setFecha_vencimientoParseado(vencimiento);
                    contenedor.setSubgrupo(rs2.getString("sugr_codigo"));
                    contenedor.setDescSubgrupo(rs2.getString("sugr_desc"));
                    listArrayArticulos.add(contenedor);
                }
            }
            else
            {
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
                    int contParcialGrupo=0;
                    int contArticulosSubgruposSeleccionados=0;
                    ArticulosSubgruposSeleccionados="";
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected())
                        {
                            ArrayListContenedor insArt = new ArrayListContenedor();
                            insArt.setstringID(items.get(i).getstringID());
                            insArt.setId(items.get(i).getId() );
                            insArt.setName(items.get(i).getName());
                            insArt.setidFamilia(items.get(i).getidFamilia());
                            insArt.setidGrupo(items.get(i).getidGrupo());
                            insArt.setLote(items.get(i).getLote());
                            insArt.setCantidad(items.get(i).getCantidad());
                            insArt.setFechaVencimiento(items.get(i).getFechaVencimiento());
                            insArt.setFecha_vencimientoParseado(items.get(i).getFecha_vencimientoParseado());
                            insArt.setSubgrupo(items.get(i).getSubgrupo());
                            insArt.setDescFamilia(items.get(i).getDescFamilia());
                            insArt.setDescGrupo(items.get(i).getDescGrupo());
                            insArt.setDescSubgrupo(items.get(i).getDescSubgrupo());
                            listInsertArticulos.add(insArt);

                            if(!ArticulosSubgruposSeleccionados.contains(String.valueOf(items.get(i).getstringID())))
                            {
                                if(contArticulosSubgruposSeleccionados==0)
                                {
                                     ArticulosSubgruposSeleccionados=String.valueOf(items.get(i).getstringID());
                                }
                                else
                                {
                                     ArticulosSubgruposSeleccionados=ArticulosSubgruposSeleccionados+","+items.get(i).getstringID();
                                }
                                contArticulosSubgruposSeleccionados ++;


                            }
                            if(!grupoParcial.contains(items.get(i).getidGrupo())){
                                if(contParcialGrupo==0){
                                    grupoParcial=grupoParcial+items.get(i).getidGrupo();
                                }
                                else{
                                    grupoParcial=grupoParcial+","+items.get(i).getidGrupo();
                                }
                                contParcialGrupo++;
                            }
                            total_articulos++;//
                        }
                    }
                    // EN ESTE CASO, EL "grupoParcial", IRA A LA CABECERA WEB_INVENTARIO, SI SE SELECCIONA TODOS O SOLO UN GRUPO, ENTONCES  EL
                    // "grupoParcial" IRA AL INSERT COMO VACIO. O SINO IRA CON LOS GRUPOS QUE FUERON SELECCIONADOS EJEMPLO: 1,2,3.
                    int total_grupo=listArrayGrupo.size();// OBTENEMOS EL TOTAL DEL GRUPO
                    int total_grupo_seleccionado=gruposSeleccionados;//OBTENEMOS EL TOTAL DEL GRUPO SELECCIONADO

                    if(total_grupo==total_grupo_seleccionado){ //SI SE SELECCIONO TODOS LOS GRUPOS, ENTONCES EL GRUPO PARCIAL DE LA CABECERA IRA EN VACIO.
                        grupoParcial="";
                    }
                    else
                    {
                        if(grupoParcial.length()==1){ //SI EL GRUPO PARCIAL, ARROJA SOLO UN GRUPO,
                            // ENTONCES EN LA CABECERA IRA EL CODIGO DEL GRUPO UNICO, PERO EN EL PARCIAL IRA VACIO
                            grupoParcial="";
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
                            TextView txt_familia = (TextView) view.findViewById(R.id.txt_familia);
                            TextView txt_grupo = (TextView) view.findViewById(R.id.txt_grupo);
                            TextView txt_subgrupo = (TextView) view.findViewById(R.id.txt_subgrupo);

                            txt_familia.setText(""+listInsertArticulos.get(position).getDescFamilia());
                            txt_subgrupo.setText(""+listInsertArticulos.get(position).getDescSubgrupo());
                            txt_grupo.setText(""+listInsertArticulos.get(position).getDescGrupo());

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
            VerificarRed(context_stkw001);

        }
        catch (Exception E){
          //  Toast.makeText(context_stkw001,E.getMessage()+" LISTAR ARTICULOS",Toast.LENGTH_LONG).show();
        }
    }

    public static void listarStkw002(){

        try {
             contador_stkw002=0;
            int cantidad=0;
            SQLiteDatabase db_consulta= conSqlite.getReadableDatabase();
            Cursor cursor=db_consulta.rawQuery("select " +
                    "winvd_nro_inv," + //0
                    "ART_DESC," +//1
                    "winvd_lote," +//2
                    "winvd_art ," +//3
                    "strftime('%d/%m/%Y',date(winvd_fec_vto)) as  winvd_fec_vto," +//4
                    "winvd_area," +//5
                    "winvd_dpto," +//6
                    "winvd_secc," +//7
                    "winvd_flia," +//8
                    "winvd_grupo," +//9
                    "winvd_cant_act," +//10
                    "winvd_cant_inv," +//11
                    "winvd_secu," +//12
                    "grup_desc," +//13
                    "flia_desc," +//14
                    "toma_registro," +//15
                    "cod_barra,caja,GRUESA "+//16
                    " from stkw002inv" +
                    " WHERE arde_suc='"+variables.ID_SUCURSAL_LOGIN+
                    "' and winvd_nro_inv="+variables.nro_registro_toma+" order by CAST(winvd_art as integer)  asc " ,null);
            int cont=0;
            ListArrayInventarioArticulos = new ArrayList();
            while (cursor.moveToNext())
            {

                contador_stkw002++;
                ListArrayInventarioArticulos.add(new Stkw002Item(  cursor.getString(1), 0,
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),
                        cursor.getString(12),cursor.getString(5),cursor.getString(13),cursor.getString(14),
                        cursor.getString(15),cursor.getString(16), cursor.getString(11),
                        "Ultimo ingresado:0", cursor.getInt(17), cursor.getInt(18),0,0,cursor.getInt(8),"Ultimo ingresado:0","Ultimo ingresado:0"));
                cont++;
                cantidad=cantidad+Integer.parseInt(cursor.getString(11));
            }
            stkw002.txtTotalArt.setText("TOTAL DE ARTICULOS:"+ contador_stkw002+"                                        CANTIDAD TOTAL:"+cantidad+"");




        }
        catch (Exception e){
            String err=e.toString();
        }

    }

    public static void listarWebViewStkw001Cancelacion(int nroToma,String consolidado) {
        try {
            String html="";
            table="";
            //connect =  conexion.Connections();
            Statement stmt =  connect.createStatement();


            if(consolidado.equals("NO")){
                ResultSet rs = stmt.executeQuery("SELECT  " +
                        " to_char(a.ARDE_FEC_VTO_LOTE) as ARDE_FEC_VTO_LOTE ,b.winvd_fec_vto,  a.ARDE_SUC, " +
                        "b.winvd_nro_inv, b.winvd_art,a.ART_DESC,b.winvd_lote,b.winvd_fec_vto,b.winvd_area,    " +
                        " b.winvd_dpto,b.winvd_secc,b.winvd_flia,b.winvd_grupo,b.winvd_cant_act,c.winve_fec, " +
                        " dpto_desc,secc_desc,flia_desc,grup_desc,area_desc ,sugr_desc  " +
                        " FROM   V_WEB_ARTICULOS_CLASIFICACION  a    " +
                        " inner join WEB_INVENTARIO_det b on a.arde_lote=b.winvd_lote    " +
                        " and a.ART_CODIGO=b.winvd_art    " +
                        "  and a.SECC_CODIGO=b.winvd_secc   " +
                        " and a.ARDE_FEC_VTO_LOTE=b.winvd_fec_vto    " +
                        "inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero   " +
                        " and c.winve_dep=a.ARDE_DEP    and c.winve_area=a.AREA_CODIGO    " +
                        " and c.winve_suc=a.ARDE_SUC    and c.winve_secc=a.SECC_CODIGO  " +
                        "  where C.WINVE_NUMERO="+nroToma+ " order by winvd_art asc" );
                int cont=0;
                while (rs.next()){
                    cont++;
                    html=html+  "<tr>" +
                            "<td>"+rs.getString("winvd_art")+"</td>" +
                            "<td>"+rs.getString("ART_DESC")+"</td>" +
                            "<td>"+rs.getString("winvd_lote")+"</td>" +
                            "<td>"+rs.getString("ARDE_FEC_VTO_LOTE")+"</td>" +
                            "<td>"+rs.getString("FLIA_DESC")+"</td>" +
                            "<td>"+rs.getString("GRUP_DESC")+"</td>" +
                            "<td>"+rs.getString("sugr_desc")+"</td>" +
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
                        "<td>FAMILIA</td>" +
                        "<td>GRUPO</td>" +
                        "<td>SUBGRUPO</td>" +
                        "</tr> </thead><tbody>"+html+" </tbody></table></div>" ;


            }

            else {
                String sql="select   " +
                        "   ART_CODIGO,ART_DESC,ART_DESC_ABREV,ART_COD_ALFANUMERICO,ART_TIPO,ART_IMPU,ART_UNID_MED,ART_EST,ART_IND_BLOQUEO," +
                        "   ART_CLASIFICACION,ART_CATEGORIA,ART_IND_LOTE,ART_IND_REG_ESPECIAL,ART_COD_PADRE,ART_CODIGO_HIJO,AREA_CODIGO," +
                        "   AREA_DESC,DPTO_CODIGO,DPTO_DESC,SECC_CODIGO,SECC_DESC,FLIA_CODIGO,FLIA_DESC,GRUP_CODIGO,GRUP_DESC,SUGR_CODIGO," +
                        "   SUGR_DESC,ARDE_SUC,ARDE_DEP,SUM(ARDE_CANT_ACT) ARDE_CANT_ACT " +
                        "   from V_WEB_ARTICULOS_CLASIFICACION t " +
                        "   INNER JOIN web_inventario c on t.ARDE_SUC=c.winve_suc " +
                        "   INNER JOIN WEB_INVENTARIO_det b ON t.ART_CODIGO=b.winvd_art and t.ARDE_DEP=c.winve_dep and t.ART_CODIGO=b.winvd_art     " +
                        "   and t.SECC_CODIGO=b.winvd_secc  and b.winvd_area=t.AREA_CODIGO   and b.winvd_secc=t.SECC_CODIGO  " +
                        "    and b.winvd_nro_inv=c.winve_numero " +
                        "   where c.winve_numero="+nroToma +
                        "   GROUP BY ART_CODIGO,ART_DESC,ART_DESC_ABREV,ART_COD_ALFANUMERICO,ART_TIPO,ART_IMPU,ART_UNID_MED,ART_EST," +
                        "   ART_IND_BLOQUEO,ART_CLASIFICACION,ART_CATEGORIA,ART_IND_LOTE,ART_IND_REG_ESPECIAL,ART_COD_PADRE,ART_CODIGO_HIJO," +
                        "   AREA_CODIGO,AREA_DESC,DPTO_CODIGO,DPTO_DESC,SECC_CODIGO,SECC_DESC,FLIA_CODIGO,FLIA_DESC,GRUP_CODIGO,GRUP_DESC," +
                        "   SUGR_CODIGO,SUGR_DESC,ARDE_SUC,ARDE_DEP,sugr_desc  ";

                ResultSet rs = stmt.executeQuery(sql);
                int cont=0;
                while (rs.next()){
                    cont++;
                    html=html+  "<tr>" +
                            "<td>"+rs.getString("ART_CODIGO")+"</td>" +
                            "<td>"+rs.getString("ART_DESC")+"</td>" +
                            "<td>"+rs.getString("FLIA_DESC")+"</td>" +
                            "<td>"+rs.getString("GRUP_DESC")+"</td>" +
                            "<td>"+rs.getString("sugr_desc")+"</td>" +
                            "</tr>";
                            }
                rs.close();

                table = "<div>TOTAL DE ARTICULOS "+cont+"<table border=1> " +
                        "<thead> " +
                        "<tr>" +
                        "<td>COD</td>" +
                        "<td>ARTICULO</td>" +
                        "<td>FAMILIA</td>" +
                        "<td>GRUPO</td>" +
                        "<td>SUBGRUPO</td>" +
                        "</tr> </thead><tbody>"+html+" </tbody></table></div>" ;


            }

        }
        catch (Exception e){
            String mens=e.toString();
    }
    }

    public static void listarWebViewConsolidados(int nroToma) {
        try {
            String html="";
            table="";
            Statement stmt =  connect.createStatement();
            String sql="SELECT               ART_DESC,  winve_fec,dpto_desc AS DEPOSITO,secc_desc,flia_desc,grup_desc,              " +
                    "   a.flia_desc   as desc_familiA ,winvd_art,WINVD_LOTE,dif.vto,              " +
                    "   dif.cant_sist_cons as cargado , dif.cant_fisi_cons as stock_sys,dif.cant_cons as diferencia                            " +
                    "    FROM                                 V_WEB_ARTICULOS_CLASIFICACION  a                         " +
                    "   inner join WEB_INVENTARIO_det b on   a.ART_CODIGO=b.winvd_art and a.SECC_CODIGO=b.winvd_secc                              " +
                    "   inner join  WEB_INVENTARIO c on b.winvd_nro_inv=c.winve_numero  And c.winve_dep=a.ARDE_DEP   and c.winve_area=a.AREA_CODIGO                             " +
                    "   and c.winve_suc=a.ARDE_SUC   and c.winve_secc=a.SECC_CODIGO INNER JOIN V_WEB_ART_CONS_DIF dif on b.WINVD_NRO_INV=dif.nro_toma and b.winvd_art=dif.articulo " +
                    "   and b.WINVD_LOTE=dif.lote          where    c.winve_empr=1                                  and a.ARDE_SUC=1  AND              " +
                    "   WINVD_NRO_INV="+nroToma+"                        GROUP BY                                 " +
                    "   ARDE_SUC,winvd_nro_inv,winvd_art, winvd_area,winvd_dpto,winvd_secc,winve_suc,winvd_flia,                              " +
                    "   winvd_grupo,winve_fec,dpto_desc,secc_desc,flia_desc,grup_desc,area_desc,sugr_codigo,winve_grupo ," +
                    "   winve_tipo_toma,winve_login,winve_grupo_parcial,winve_flia,winve_dep ,ART_DESC,  WINVD_LOTE,                               " +
                    "   dif.cant_sist_cons  , dif.cant_fisi_cons ,dif.cant_cons ,dif.vto  ";

            ResultSet rs = stmt.executeQuery(sql);
                int cont=0;
                while (rs.next()){
                    cont++;
                    html=html+  "<tr>" +
                            "<td>"+rs.getString("ART_DESC")+"</td>" +
                            "<td>"+rs.getString("winve_fec")+"</td>" +
                            "<td>"+rs.getString("DEPOSITO")+"</td>" +
                            "<td>"+rs.getString("flia_desc")+"</td>" +
                            "<td>"+rs.getString("WINVD_LOTE")+"</td>" +
                            "<td>"+rs.getString("stock_sys")+"</td>" +
                            "<td>"+rs.getString("cargado")+"</td>" +
                            "<td>"+rs.getString("diferencia")+"</td>" +
                            "</tr>";
                }
                rs.close();

                table = "<div><b>TOTAL DE ARTICULOS "+cont+"</b> <table border=1> " +
                        "<thead> " +
                        "<tr>" +
                        "<td><b> ARTICULO </b></td>" +
                        "<td><b> FECHA REGISTRO </b></td>" +
                        "<td><b> DEPOSITO </b></td>" +
                        "<td><b> FAMILIA </b></td>" +
                        "<td><b> LOTE </b></td>" +
                        "<td><b> STOCK </b></td>" +
                        "<td><b> CARGADO </b></td>" +
                        "<td><b> DIFERENCIA </b></td>" +
                        "</tr> </thead><tbody>"+html+" </tbody></table></div>" ;
        }
        catch (Exception e){
            table=e.toString();
        }
    }

    public static void ListarTomasServer(Context context) {
        try {
            int cont=0;
           // controles.connect = controles.conexion.Connections();
            Statement stmt =  connect.createStatement();

            ResultSet rs = stmt.executeQuery("" +
                    "   SELECT    " +
                    "      NVL(NVL(grup_desc,WINVE_GRUPO_PARCIAL),'TODOS') AS desc_grupos,to_char(WEB_INVENTARIO.Winve_Fec,'DD/MM/YYYY HH:SS') AS FECHAFORM," +
                    "       WEB_INVENTARIO.*,V_WEB_GRUPO.*,V_WEB_area.*,V_WEB_SECC.*,V_WEB_DPTO.*,  NVL(V_WEB_FLIA.flia_desc,'TODAS') AS flia_desc ," +
                    "       case  WEB_INVENTARIO.winve_tipo_toma when 'C' then 'CRITERIO' " +
                    "       ELSE 'MANUAL' END AS tipo_toma ,CASE winve_consolidado WHEN 'S' THEN 'SI' ELSE 'NO' END AS   consolidado," +
                    "       case when  winve_grupo IS NULL and   winve_grupo_parcial IS NULL then 'TODOS'   " +
                    "       WHEN  winve_grupo_parcial IS NOT NULL THEN 'PARCIALES' ELSE grup_desc END AS desc_grupo_parcial" +
                    "   FROM " +
                    "       WEB_INVENTARIO " +
                    "       LEFT OUTER JOIN V_WEB_FLIA on WEB_INVENTARIO.WINVE_FLIA=V_WEB_FLIA.FLIA_CODIGO " +
                    "       LEFT OUTER JOIN V_WEB_GRUPO on WEB_INVENTARIO.WINVE_GRUPO=V_WEB_GRUPO.GRUP_CODIGO AND  V_WEB_FLIA.FLIA_CODIGO=V_WEB_GRUPO.GRUP_FAMILIA " +
                    "       inner join V_WEB_area on V_WEB_area.AREA_CODIGO=WEB_INVENTARIO.Winve_Area" +
                    "       inner join V_WEB_SECC on V_WEB_SECC.SECC_CODIGO =WEB_INVENTARIO.winve_secc" +
                    "       inner join V_WEB_DPTO on V_WEB_DPTO.DPTO_CODIGO =WEB_INVENTARIO.winve_DPTO" +
                    "   WHERE " +
                    "       WINVE_ESTADO_WEB='A' AND WEB_INVENTARIO.WINVE_LOGIN=UPPER('"+variables.userdb+"') order by WINVE_NUMERO desc                                                                                                                                                                                                                                                                   " );

            Stkw002List Stkw001List=null;
            listaStkw001=new ArrayList<Stkw002List>();
            while (rs.next())
            {
                Stkw001List=new Stkw002List();

                Stkw001List.setNroToma(rs.getString("WINVE_NUMERO"));
                Stkw001List.setFechaToma(rs.getString("FECHAFORM"));
                Stkw001List.setFamilia(rs.getString("flia_desc"));
                Stkw001List.setGrupo(rs.getString("desc_grupo_parcial"));
                Stkw001List.setArea(rs.getString("area_desc"));
                Stkw001List.setDpto(rs.getString("DPTO_DESC"));
                Stkw001List.setTipoToma(rs.getString("tipo_toma"));
                Stkw001List.setSeccion(rs.getString("SECC_DESC"));
                Stkw001List.setconsolidado(rs.getString("consolidado"));

                listaStkw001.add(Stkw001List);
                cont++;
            }
            if(cont==0){
                lista_stkw001_inv.txtSinresultado.setVisibility(View.VISIBLE);
            }
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.listitem_card, R.id.text1, listaStkw001) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(R.id.text1);
                    TextView text2 = (TextView) view.findViewById(R.id.text2);
                    TextView text3 = (TextView) view.findViewById(R.id.text3);
                    TextView text4 = (TextView) view.findViewById(R.id.text4);
                    TextView text5 = (TextView) view.findViewById(R.id.text5);
                    TextView text6 = (TextView) view.findViewById(R.id.text6);
                    TextView text7 = (TextView) view.findViewById(R.id.text7);
                    TextView text8 = (TextView) view.findViewById(R.id.text8);
                    TextView text9 = (TextView) view.findViewById(R.id.text9);
                    TextView text_1 = (TextView) view.findViewById(R.id.text_1);
                    TextView text_2 = (TextView) view.findViewById(R.id.text_2);
                    TextView text_3 = (TextView) view.findViewById(R.id.text_3);
                    TextView text_4 = (TextView) view.findViewById(R.id.text_4);
                    TextView text_5 = (TextView) view.findViewById(R.id.text_5);
                    TextView text_6 = (TextView) view.findViewById(R.id.text_6);
                    TextView text_7 = (TextView) view.findViewById(R.id.text_7);
                    TextView text_8 = (TextView) view.findViewById(R.id.text_8);
                    TextView text_9 = (TextView) view.findViewById(R.id.text_9);
                    ImageView  txtimagen =   view.findViewById(R.id.txtimagen);

                    text1.setText(listaStkw001.get(position).getNroToma());
                    text2.setText(listaStkw001.get(position).getFechaToma());
                    text3.setText(listaStkw001.get(position).getArea());
                    text4.setText(listaStkw001.get(position).getDpto());
                    text5.setText(listaStkw001.get(position).getSeccion());
                    text6.setText(listaStkw001.get(position).getFamilia());
                    text7.setText(listaStkw001.get(position).getGrupo());
                    text8.setText(listaStkw001.get(position).getconsolidado());
                    text9.setText(listaStkw001.get(position).getTipoToma());

                    text_1.setText("NRO. DE TOMA:");
                    text_2.setText("FECHA TOMA:");
                    text_3.setText("AREA:");
                    text_4.setText("DEPARTAMENTO:");
                    text_5.setText("SECCION:");
                    text_6.setText("FAMILIA:");
                    text_7.setText("GRUPO:");
                    text_8.setText("CONSOLIDADO:");
                    text_9.setText("TOMA:");


                    txtimagen.setImageResource(R.drawable.ic_consulta);

                    return view;
                }
            };
            lista_stkw001_inv.listView.setAdapter(adapter);
        }
        catch (Exception e){
            lista_stkw001_inv.txtSinresultado.setText(e.getMessage()) ;
          //  if(e.getMessage().equals("Closed Connection")){
            controles. connect();
         //   }
            lista_stkw001_inv.txtSinresultado.setVisibility(View.VISIBLE);
        }
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
                GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS
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

                limpiarSubGrupo();
                // stkw001.txt_deposito.setText(arr_deposito.get(i));
                //  stkw001.txt_id_deposito.setText(arr_id_deposito.get(i));

                listar_depositos(activity,arrIdSucursales.get(i),1);
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
                GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS
                stkw001.txt_id_area.setText("");
                stkw001.txt_area.setText("");
                stkw001.txt_id_departamento.setText("");
                stkw001.txt_departamento.setText("");
                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");

                limpiarSubGrupo();
                stkw001.txt_deposito.setText(arr_deposito.get(i));
                stkw001.txt_id_deposito.setText(arr_id_deposito.get(i));

                controles.listar_areas(activity_stkw001,context_stkw001,variables.tipo_stkw001);

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
                GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS
                arr_id_departamento.clear();
                arr_departamento.clear();
                arr_id_seccion.clear();
                arr_seccion.clear();
                arr_id_familia.clear();
                arr_familia.clear();



                stkw001.txt_id_departamento.setText("");
                stkw001.txt_departamento.setText("");
                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");



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
                GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS

                arr_id_seccion.clear();
                arr_seccion.clear();
                arr_id_familia.clear();
                arr_familia.clear();



                stkw001.txt_id_seccion.setText("");
                stkw001.txt_seccion.setText("");
                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");



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
                GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS



                stkw001.txt_id_familia.setText("");
                stkw001.txt_familia.setText("");



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
                stkw001.txt_familia.setText(arr_familia.get(i));
                stkw001.txt_id_familia.setText(arr_id_familia.get(i));

                if(arr_id_familia.get(i).equals("T"))
                {
                    stkw001.spinerGrupo.setVisibility(View.GONE);
                    stkw001.spinerSubGrupo.setVisibility(View.GONE);
                    stkw001.lbl_grupo.setVisibility(View.GONE);
                    stkw001.lbl_subgrupo.setVisibility(View.GONE);
                    ids_grupos="T";
                    ids_subgrupos="T";
                    listarArticulos();
                    GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS
                }
                else
                {
                    stkw001.spinerGrupo.setVisibility(View.VISIBLE);
                    stkw001.spinerSubGrupo.setVisibility(View.VISIBLE);
                    stkw001.lbl_grupo.setVisibility(View.VISIBLE);
                    stkw001.lbl_subgrupo.setVisibility(View.VISIBLE);
                    GruposTodos=false; //VOLVEMOS A COLOCAR EN FALSE LA SELECCION TOTAL DE GRUPOS

                }
             //   stkw001.sp_familia = new SpinnerDialog(activity,arr_familia,"Listado de familias");
                listar_grupo(activity,arr_id_familia.get(i),context, tipo_toma);
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
                    ||ids_grupos.length()==0) {

                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Complete los datos requeridos.1");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);


            }

            else if (controles.ids_subgrupos.equals("")){

                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Seleccione subgrupo.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);



            }
            else if (controles.listInsertArticulos.size()==0){
                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Seleccione articulos.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            }
            else {


                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("¿Desea registrar los datos ingresado?.");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final  AsyncInsertStkw001 task = new  AsyncInsertStkw001();
                        task.execute();
                    }
                });
                builder.setNegativeButton("No",null);
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);



            }
        }
///////////////////////////////////////////////////////////////////////////////////////////////
        else if (variables.tipo_stkw001==2) {
            if( stkw001.txt_id_sucursal.getText().toString().equals("")
                    ||stkw001.txt_id_deposito.getText().toString().equals("")
                    ||stkw001.txt_id_area.getText().toString().equals("")
                    ||stkw001.txt_id_departamento.getText().toString().equals("")
                    ||stkw001.txt_id_seccion.getText().toString().equals("")
                    ||stkw001.txt_id_familia.getText().toString().equals("")
                    ||ids_grupos.equals("")) {


                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Complete los datos requeridos.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);



            }
            else if (ids_subgrupos.equals("")){


                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Seleccione subgrupo.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);


            }
            else if (listArrayArticulos.size()==0){ //SI EL FILTRO PARA LA SELECCION AUTOMATICA ES CERO, ES PORQUE NO ARROJO NINGUN ARTICULO PARA EL REGISTRO.


                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("Las combinaciónes de filtros recae en articulos inexistentes.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);



            }
            else {


                builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(context_stkw001.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage("¿Desea registrar los datos ingresados?.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final  AsyncInsertStkw001 task = new  AsyncInsertStkw001();
                        task.execute();
                    }
                });
                builder.setNegativeButton("No",null);
                ad = builder.show();
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);




            }
        }
    }

    public static void ExportarStkw002(){
        try {

            if(connect==null||connect.isClosed()){
                connect();
            }
            //SE AGREGO LA CONSULTA DE LA CONEXION, YA QUE SE NECESITABA REINICIAR LA APP PARA RECONECTAR
            SQLiteDatabase dbConsultaCont= conSqlite.getReadableDatabase();
            Cursor cursorCont=dbConsultaCont.rawQuery("select  count(*) from stkw002inv" +
                    " WHERE arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado='P' AND UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+ "')" ,null);
            if(cursorCont.moveToNext()){
                ContExportStkw002=cursorCont.getInt(0);
            }

            builder = new AlertDialog.Builder(context_menuPrincipal);
            builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("Exportación de inventarios.");
            builder.setMessage("¿Desea enviar los inventarios realizados?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
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
            });
            builder.setNegativeButton("No",null);
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);


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
                " WHERE estado in ('P','F') AND UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+"')" ,null);
        ListArrayInventarioArticulos = new ArrayList();
        if (cursor.moveToNext())
        {
            menu_principal.txt_total.setText("PENDIENTES DE ENVIO :"+cursor.getString(0));
        }
        db_consulta.close();

    }

    public static void CancelarToma(int nroToma,Context context){
        try {
            //connect = conexion.Connections();
            connect.setAutoCommit(false);
            String Cancelar="UPDATE WEB_INVENTARIO SET WINVE_ESTADO_WEB='E',WINVE_FEC_CERRADO_WEB=CURRENT_TIMESTAMP,WINVE_LOGIN_CERRADO_WEB=UPPER('"+variables.userdb+"')" +
                    " WHERE WINVE_NUMERO="+nroToma+" ";
            PreparedStatement ps = connect.prepareStatement(Cancelar);
            ps.executeUpdate();
            ps.close();
            connect.commit();
            ListarTomasServer(context);

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
          if(stkw001.txt_id_familia.getText().toString().equals("T")){
              registrarStkw001FamiliasTodos();
          }

          else{
              registrarStkw001Parcial();
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
    public static class AsyncListarConsolidados extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listado_consolidado.pgDialog = ProgressDialog.show(contextListadoConsolidado, "CONSULTANDO", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            listarWebViewConsolidados(nroTomaConsolidado);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listado_consolidado.pgDialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(contextListadoConsolidado);
            alert.setTitle("DETALLE");
            WebView wv = new WebView(contextListadoConsolidado);
            WebSettings settings = wv.getSettings();
            settings.setMinimumFontSize(20);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
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
             alert.show();
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
            listarWebViewStkw001Cancelacion(nroTomaCancelacion,consolidadoCancelacion);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lista_stkw001_inv.pgDialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(contextListaStkw001);
            alert.setTitle("ARTICULOS CARGADOS");
            WebView wv = new WebView(contextListaStkw001);
            WebSettings settings = wv.getSettings();
            settings.setMinimumFontSize(20);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
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
                int idGenCabStk=0;
                //GENERO ID PARA LA CABECERA DEL STK
                //connect = conexion.Connections();
                connect.setAutoCommit(false);
                Cursor cursorCab=db_consultaCab.rawQuery("select distinct winvd_nro_inv,WINVE_LOGIN_CERRADO_WEB,winve_dep,arde_suc,tipo_toma,toma_registro from stkw002inv " +
                        "where '"+variables.ID_SUCURSAL_LOGIN+"' AND estado IN ('P','F') and UPPER(WINVE_LOGIN_CERRADO_WEB)=UPPER('"+variables.userdb+"')",null);

                int contadorMensaje=0; // EN EL CASO DE QUE QUEDE EN CERO, ENTONCES EL MENSAJE SERA, DE QUE NO HAY DATOS PARA EXPORTAR
                while (cursorCab.moveToNext())
                {
                    int nroCabecera=cursorCab.getInt(0);
                    int winve_dep=cursorCab.getInt(2);
                    int winve_suc=cursorCab.getInt(3);
                    String WINVE_LOGIN_CERRADO_WEB=cursorCab.getString(1);
                    String tipoToma="";
                    String tomaRegistro="";
                    tomaRegistro= cursorCab.getString(5);
                    if(cursorCab.getString(4).equals("MANUAL")){
                        tipoToma="M";
                    }
                    else{
                        tipoToma="C";
                    }
                    if(!tomaRegistro.equals("R")){
                        Statement stmtStk =  connect.createStatement();
                        // ResultSet RsStk = stmtStk.executeQuery("SELECT nvl(MAX(INVE_NUMERO),0)+1  as id FROM  stk_carga_inv" );
                        ResultSet RsStk = stmtStk.executeQuery("SELECT SEQ_STK_CARGA_INV.NEXTVAL    FROM   DUAL" );

                        while (RsStk.next()){
                            idGenCabStk=RsStk.getInt(1);
                        }
                    }

                    contadorMensaje++;
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
                            "winvd_secu," +//11
                            "winve_dep, " +//12
                            "winve_suc" +  //13
                            " from stkw002inv" +
                            " WHERE " +
                            "arde_suc='"+variables.ID_SUCURSAL_LOGIN+"' AND estado in ('P','F') AND winvd_nro_inv="+nroCabecera+
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
                            int secuencia=1;
                            // ESTE CURSOR RECORRE EL DETALLE DEL SQLITE, PARA ACTUALIZAR,
                            // PRIMERO EL DETALLE DEL SERVER, LUEGO EL DETALLE DEL SQLITE
                            while (cursor.moveToNext())
                            {
                                if(tomaRegistro.equals("R")){ // SE REINVENTARIA
                                    // SE ACTUALIZA WEB_STK_CARGA_INV_DET LAS CANTIDADES REINVENTARIADAS
                                    PreparedStatement ps = connect.prepareStatement(
                                            " UPDATE WEB_STK_CARGA_INV_DET SET INVD_CANT_INV='"+cursor.getString(10)+"' " +
                                                    "WHERE INVD_NRO_INV="+cursor.getString(0)+  " AND INVD_ART='"+cursor.getString(2)+"'"
                                           );
                                    ps.executeUpdate();
                                    ps.close();
                                }
                                else{
                                    // SE INSERTA EN WEB_STK_CARGA_INV_DET, YA NO SE ACTUALIZA NADA EN WEB_INVENTARIO_DET
                                    PreparedStatement ps = connect.prepareStatement(" INSERT INTO WEB_STK_CARGA_INV_DET  " +
                                            "(          INVD_NRO_INV,       INVD_ART,                           INVD_SECU,          INVD_CANT_INV,                      " +
                                            "           INVD_FEC_VTO,       INVD_LOTE,          INVD_UM) " +

                                            "VALUES (   '"+idGenCabStk+"',  '"+cursor.getString(2)+"','"+secuencia+"',   '"+cursor.getString(10)+"'," +
                                            "           '01/01/3000',       '"+idGenCabStk+"',  '')");
                                    ps.executeUpdate();
                                    ps.close();
                                }



                            //SQLITE ACTUALIZA EL ESTADO DEL INVENTARIO A C, SE CAMBIA LA SECUENCIA POR EL CODIGO DE ARTICULO,
                            // YA QUE EL ARTICULO SERA UNICO POR CADA TOMA.
                            db_UPDATE.execSQL(" update stkw002inv set estado='C'" +
                                    " where winvd_nro_inv="  +cursor.getString(0)+"  and winvd_art="+ cursor.getString(2)+""   );
                            menu_principal.ProDialogExport.setProgress(i);
                            i++;
                            secuencia++;
                        }
                        if(tomaRegistro.equals("R")){ // SE REINVENTARIA
                            //CERRAMOS EL STK CON ESTADO A.
                            PreparedStatement pscAB = connect.prepareStatement(
                                    " UPDATE WEB_STK_CARGA_INV SET invew_est='A' " +
                                            "WHERE INVE_NUMERO="+nroCabecera+  "  "
                            );
                            pscAB.executeUpdate();
                            pscAB.close();
                        }


                        else { // EN EL CASO QUE SEA UN INVENTARIO NUEVO, ENTONCES INSERTA
                            //SERVER ACTUALIZA LA CABECERA WEB_INVENTARIO
                            PreparedStatement pscAB = connect.prepareStatement("UPDATE web_inventario SET WINVE_ESTADO_WEB='C' , " +
                                    "WINVE_FEC_CERRADO_WEB=CURRENT_TIMESTAMP," +
                                    "WINVE_LOGIN_CERRADO_WEB=UPPER('"+WINVE_LOGIN_CERRADO_WEB+"') " +
                                    "WHERE WINVE_NUMERO="+nroCabecera);

                            //HACEMOs INSERT EN LA CABECERA WEB_STK_CARGA_INV
                            PreparedStatement psCabStk = connect.prepareStatement("insert into WEB_STK_CARGA_INV " +
                                    "(INVE_NUMERO,      INVE_EMPR,  INVE_SUC,               INVE_DEP,       INVE_FEC,           INVE_CANT_TOMA, " +
                                    "INVE_LOGIN,                    INVE_TIPO_TOMA,     INVE_REF) values " +
                                    "('"+idGenCabStk+"','1',        '"+winve_suc+"',        '"+winve_dep+"',CURRENT_TIMESTAMP,  '1',             " +
                                    "UPPER('"+WINVE_LOGIN_CERRADO_WEB+"'), '"+tipoToma+"',     '"+nroCabecera+"')");
                            psCabStk.executeUpdate();
                            pscAB.executeUpdate();
                            pscAB.close();
                        }



                        db_UPDATE.setTransactionSuccessful();
                        db_UPDATE.endTransaction();

                    }
                }//FIN DEL CURSOR CABECERA

                if(contadorMensaje==0){
                    mensajeRespuestaExportStkw002="No se encontraron registros por exportar.";
                }
                else {
                    mensajeRespuestaExportStkw002="Datos exportados con exito.";

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

                builder = new AlertDialog.Builder(context_menuPrincipal);
                builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_danger));
                builder.setTitle("¡Atención!");
                builder.setMessage(mensajeRespuestaExportStkw002);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad = builder.show();
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            }
        }
    }

    public static   void limpiarListaViewArticulosSTKW001(){
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

    public static   void limpiarSubGrupo(){

        listArrayGrupo.clear();

        listArraySubgrupo.clear();
        listArrayArticulos.clear();
        listInsertArticulos.clear();
        limpiarListaViewArticulosSTKW001();
        stkw001.spinerSubGrupo.setSearchHint("Busqueda");
        stkw001.spinerArticulos.setSearchHint("Busqueda");
        stkw001.spinerGrupo.setSearchHint("Busqueda");

        stkw001.spinerGrupo.setItems(listArrayGrupo, new MultiSpinnerListener()
        {
            @Override
            public void onItemsSelected(List<ArrayListContenedor> items) {
            }
        });

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

    public static   void VerificarRed(Context context){
        //if (verificadorRed==0){
            if(connect==null){

                connect();

            builder = new android.app.AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_danger));
            builder.setTitle("¡Atención!");
            builder.setMessage("Se perdió la comunicación con el servidor, intente de nuevo.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad = builder.show();
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.azul_claro));
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            }
      //  }

    }

    private static void registrarStkw001FamiliasTodos(){
        int con=0;

        try {

            String id_cabecera="";
            if(variables.tipo_stkw001_insert.equals("M"))//SI LA TOMA ES MANUAL
            {
                    //connect = conexion.Connections();
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
                            "WINVE_ESTADO_WEB,WINVE_CONSOLIDADO,winve_grupo_parcial) values  " +
                            "('"+stkw001.txt_id_sucursal.getText().toString()+"',    " +
                            "'"+stkw001.txt_id_deposito.getText().toString()+"'," +
                            "''," +
                            "CURRENT_TIMESTAMP," +
                            "UPPER('" +variables.userdb+"')," +
                            "'"+variables.tipo_stkw001_insert+"'," +
                            "'"+stkw001.txt_id_seccion.getText().toString()+"'," +
                            "'"+stkw001.txt_id_area.getText().toString()+"'," +
                            "'"+stkw001.txt_id_departamento.getText().toString()+"'," +
                            "''," +
                            "'"+INVE_IND_LOTE+"'," +
                            "'A'," +
                            "'"+INVE_ART_EST+"'," +
                            "'"+INVE_ART_EXIST+"'," +
                            "'"+INVE_CANT_TOMA+"'," +
                            "'1', "+id_cabecera+",'A','N','')";
                    PreparedStatement ps = connect.prepareStatement(insertar);
                    ps.executeUpdate();
                    ps.close();
                    int secuencia=1;

                         for (int i = 0; i < listInsertArticulos.size(); i++) {
                            int cantidad_actual=Integer.parseInt(listInsertArticulos.get(i).getCantidad());
                            String fechaVto=listInsertArticulos.get(i).getFechaVencimiento();
                            long idArticulo=listInsertArticulos.get(i).getId();
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
                                    "WINVD_indiv,winvd_consolidado" +//19
                                    ")  VALUES ("+

                                    id_cabecera+",'"+
                                    idArticulo+"',"+
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
                                    "'"+stkw001.txt_id_area.getText().toString()+"','"+
                                    stkw001.txt_id_departamento.getText().toString()+"','"+
                                    stkw001.txt_id_seccion.getText().toString()+"'," +
                                    "'"+listInsertArticulos.get(i).getidFamilia()+"','"
                                    +listInsertArticulos.get(i).getidGrupo()+"','"
                                    +listInsertArticulos.get(i).getSubgrupo()+"','','N')";


                            PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                            ps2.executeUpdate();
                            ps2.close();
                            secuencia++;
                            con++;
                        }
                        connect.commit();
                        tipoRespuestaStkw001=1;
                        mensajeRespuestaStkw001="REGISTRADO CON EXITO.";



            }


            else if(variables.tipo_stkw001_insert.equals("C"))//SI LA TOMA ES SELECCION AUTOMATICA
            {
                //connect = conexion.Connections();
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
                        "WINVE_ESTADO_WEB,WINVE_CONSOLIDADO,winve_grupo_parcial) values  " +
                        "('"+stkw001.txt_id_sucursal.getText().toString()+"',    " +
                        "'"+stkw001.txt_id_deposito.getText().toString()+"'," +
                       "''," +
                        "CURRENT_TIMESTAMP," +
                        "UPPER('" +variables.userdb+"')," +
                        "'"+variables.tipo_stkw001_insert+"'," +
                        "'"+stkw001.txt_id_seccion.getText().toString()+"'," +
                        "'"+stkw001.txt_id_area.getText().toString()+"'," +
                        "'"+stkw001.txt_id_departamento.getText().toString()+"'," +
                        "''," +
                        "'"+INVE_IND_LOTE+"'," +
                        "'A'," +
                        "'"+INVE_ART_EST+"'," +
                        "'"+INVE_ART_EXIST+"'," +
                        "'"+INVE_CANT_TOMA+"'," +
                        "'1', "+id_cabecera+",'A','N','')";
                PreparedStatement ps = connect.prepareStatement(insertar);
                ps.executeUpdate();
                ps.close();
                int secuencia=1;


                     for (int i = 0; i < listArrayArticulos.size(); i++) {
                        int cantidad_actual=Integer.parseInt(listArrayArticulos.get(i).getCantidad());
                        String fechaVto=listArrayArticulos.get(i).getFechaVencimiento();
                        long idArticulo=listArrayArticulos.get(i).getId();
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
                                "WINVD_indiv,winvd_consolidado" +//19
                                ")  VALUES ("+

                                id_cabecera+",'"+
                                idArticulo+"',"+
                                secuencia+","+
                                cantidad_actual+"," +
                                "''," +
                                "''," +
                                "''," +
                                "''," +  "'"+
                                listArrayArticulos.get(i).getLote()+"'," +
                                "TO_DATE('"+fechaVto+"', 'yyyy/mm/dd hh24:mi:ss') ," +
                                "''," +
                                "''," +
                                "'"+stkw001.txt_id_area.getText().toString()+"','"+
                                stkw001.txt_id_departamento.getText().toString()+"','"+
                                stkw001.txt_id_seccion.getText().toString()+"'," +
                                "'"+listArrayArticulos.get(i).getidFamilia()+"','"
                                +listArrayArticulos.get(i).getidGrupo()+"','"
                                +listArrayArticulos.get(i).getSubgrupo()+"','','N')";


                        PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                        ps2.executeUpdate();
                        ps2.close();
                        secuencia++;
                        con++;
                    }
                    connect.commit();
                    tipoRespuestaStkw001=1;
                    mensajeRespuestaStkw001="REGISTRADO CON EXITO.";
             }


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
    }

    private static void registrarStkw001Parcial(){
        int con=0;

        try {


            if(variables.tipo_stkw001_insert.equals("M"))//SI LA TOMA ES MANUAL
            {
                winveSubGrupoParcial=""; //RESETEAMOS LA UNION DEL GRUPO CON LOS SUBGRUPOS PARA EL INSERT EN SUBGRUPOS PARCIALES
                String id_cabecera="";
                String idGrupoUnico="";

                if(ids_grupos.equals("T")){
                    idGrupoUnico="";//LIMPIAMOS EL ID DE GRUPO QUE VA EN CABECERA
                    grupoParcial="";//LIMPIAMOS EL GRUPO PARCIAL, YA QUE SE SELECCIONAN TODOS LOS GRUPOS.
                    winveSubGrupoParcial="";//LIMPIAMOS EL SUBGRUPO PARCIAL, YA QUE SE SELECCIONAN TODOS LOS GRUPOS.
                }
                else {
                    if(!ids_grupos.contains(",")){ //SI ID GRUPO NO CONTIENE "," ES PORQUE SOLO SE SELECCIONO UN GRUPO.
                        idGrupoUnico=ids_grupos; // EN ID UNICO COLOCA EL UNICO ID DEL GRUPO SELECCIONADO
                        grupoParcial=""; // LIMPIAMOS EL GRUPO PARCIAL, YA QUE EL GRUPO SERA UNA UNICA SELECCION
                    }
                    if(ids_subgrupos.contains(",")){ // SI ID SUBGRUPO  CONTIENE ",", ES PORQUE SE SELECCIONARON MAS DE UN SUBGRUPO
                        capturarWinveSubGrupoParcialManual(); // CAPTURAMOS EL ID DEL GRUPO SEGUIDO DE SUS SUBGRUPOS CORRESPONDIENTES.
                        if(ids_grupos.contains(",")){ // SI ID GRUPO   CONTIENE "," ES PORQUE  SE SELECCIONARON MAS DE UN GRUPO, ENTONCES LIMPIA EL ID UNICO.
                            idGrupoUnico="";
                        }
                    }
                }

                if(ArticulosSubgruposSeleccionados.length()!=SubgrupoSeleccionadosArticulos.length()&& GruposTodos==false)
                {
                    tipoRespuestaStkw001=0;
                    mensajeRespuestaStkw001="NO SE HAN SELECCIONADO ARTICULOS DE TODOS LOS SUBGRUPOS";
                }
                else
                {

                    //connect = conexion.Connections();
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
                            "WINVE_ESTADO_WEB,WINVE_CONSOLIDADO,winve_grupo_parcial,WINVE_SUBGRUPO_PARCIAL) values  " +
                            "('"+stkw001.txt_id_sucursal.getText().toString()+"',    " +
                            "'"+stkw001.txt_id_deposito.getText().toString()+"'," +
                            "'"+idGrupoUnico+"'," +
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
                            "'1', "+id_cabecera+",'A','N','"+grupoParcial+"','"+winveSubGrupoParcial+"')";
                    PreparedStatement ps = connect.prepareStatement(insertar);
                    ps.executeUpdate();
                    ps.close();
                    int secuencia=1;

                         for (int i = 0; i < listInsertArticulos.size(); i++) {
                            int cantidad_actual=Integer.parseInt(listInsertArticulos.get(i).getCantidad());
                            String fechaVto=listInsertArticulos.get(i).getFechaVencimiento();
                            long idArticulo=listInsertArticulos.get(i).getId();
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
                                    "WINVD_indiv,winvd_consolidado" +//19
                                    ")  VALUES ("+

                                    id_cabecera+",'"+
                                    idArticulo+"',"+
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
                                    "'"+stkw001.txt_id_area.getText().toString()+"','"+
                                    stkw001.txt_id_departamento.getText().toString()+"','"+
                                    stkw001.txt_id_seccion.getText().toString()+"'," +
                                    "'"+listInsertArticulos.get(i).getidFamilia()+"','"
                                    +listInsertArticulos.get(i).getidGrupo()+"','"
                                    +listInsertArticulos.get(i).getSubgrupo()+"','','N')";


                            PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                            ps2.executeUpdate();
                            ps2.close();
                            secuencia++;
                            con++;
                        }
                        connect.commit();
                        tipoRespuestaStkw001=1;
                        mensajeRespuestaStkw001="REGISTRADO CON EXITO.";


                }

            }


            else if(variables.tipo_stkw001_insert.equals("C"))//SI LA TOMA ES SELECCION AUTOMATICA
            {
                winveSubGrupoParcial=""; //RESETEAMOS LA UNION DEL GRUPO CON LOS SUBGRUPOS PARA EL INSERT EN SUBGRUPOS PARCIALES
                String id_cabecera="";
                String idGrupoUnico="";

                if(ids_grupos.equals("T")){
                    idGrupoUnico="";//LIMPIAMOS EL ID DE GRUPO QUE VA EN CABECERA
                    grupoParcial="";//LIMPIAMOS EL GRUPO PARCIAL, YA QUE SE SELECCIONAN TODOS LOS GRUPOS.
                    winveSubGrupoParcial="";//LIMPIAMOS EL SUBGRUPO PARCIAL, YA QUE SE SELECCIONAN TODOS LOS GRUPOS.
                }
                else {
                    if(!ids_grupos.contains(",")){ //SI ID GRUPO NO CONTIENE "," ES PORQUE SOLO SE SELECCIONO UN GRUPO.
                        idGrupoUnico=ids_grupos; // EN ID UNICO COLOCA EL UNICO ID DEL GRUPO SELECCIONADO
                        grupoParcial=""; // LIMPIAMOS EL GRUPO PARCIAL, YA QUE EL GRUPO SERA UNA UNICA SELECCION
                    }
                    if(ids_subgrupos.contains(",")){ // SI ID SUBGRUPO  CONTIENE ",", ES PORQUE SE SELECCIONARON MAS DE UN SUBGRUPO
                        capturarWinveSubGrupoParcialCriterio(); // CAPTURAMOS EL ID DEL GRUPO SEGUIDO DE SUS SUBGRUPOS CORRESPONDIENTES.
                        if(ids_grupos.contains(",")){ // SI ID GRUPO   CONTIENE "," ES PORQUE  SE SELECCIONARON MAS DE UN GRUPO, ENTONCES LIMPIA EL ID UNICO.
                            idGrupoUnico="";
                        }
                    }
                }


                //connect = conexion.Connections();
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
                        "WINVE_ESTADO_WEB,WINVE_CONSOLIDADO,winve_grupo_parcial,WINVE_SUBGRUPO_PARCIAL) values  " +
                        "('"+stkw001.txt_id_sucursal.getText().toString()+"',    " +
                        "'"+stkw001.txt_id_deposito.getText().toString()+"'," +
                        "'"+idGrupoUnico+"'," +
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
                        "'1', "+id_cabecera+",'A','N','"+grupoParcial+"','"+winveSubGrupoParcial+"')";
                PreparedStatement ps = connect.prepareStatement(insertar);
                ps.executeUpdate();
                ps.close();
                int secuencia=1;

                    for (int i = 0; i < listArrayArticulos.size(); i++) {
                        int cantidad_actual=Integer.parseInt(listArrayArticulos.get(i).getCantidad());
                        String fechaVto=listArrayArticulos.get(i).getFechaVencimiento();
                        long idArticulo=listArrayArticulos.get(i).getId();
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
                                "WINVD_indiv,winvd_consolidado" +//19
                                ")  VALUES ("+

                                id_cabecera+",'"+
                                idArticulo+"',"+
                                secuencia+","+
                                cantidad_actual+"," +
                                "''," +
                                "''," +
                                "''," +
                                "''," +  "'"+
                                listArrayArticulos.get(i).getLote()+"'," +
                                "TO_DATE('"+fechaVto+"', 'yyyy/mm/dd hh24:mi:ss') ," +
                                "''," +
                                "''," +
                                "'"+stkw001.txt_id_area.getText().toString()+"','"+
                                stkw001.txt_id_departamento.getText().toString()+"','"+
                                stkw001.txt_id_seccion.getText().toString()+"'," +
                                "'"+listArrayArticulos.get(i).getidFamilia()+"','"
                                +listArrayArticulos.get(i).getidGrupo()+"','"
                                +listArrayArticulos.get(i).getSubgrupo()+"','','N')";


                        PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                        ps2.executeUpdate();
                        ps2.close();
                        secuencia++;
                        con++;
                    }
                    connect.commit();
                    tipoRespuestaStkw001=1;
                    mensajeRespuestaStkw001="REGISTRADO CON EXITO.";

            }


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
    }

    private static void capturarWinveSubGrupoParcialManual()
    {
        String grupo="";
        int cont=0;
        for (int i = 0; i <  listInsertArticulos.size(); )
        {
           if(!grupo.contains(listInsertArticulos.get(i).getidGrupo()) )
           {    if(cont==0)
                {
                    grupo=listInsertArticulos.get(i).getidGrupo();
                }
                else
                    {
                    grupo=grupo+","+listInsertArticulos.get(i).getidGrupo();
                 }
               cont++;
           }
            i++;
        }
        String grupoUnidos []  =  grupo.split(",");
        String subgrupounidos="";
        winveSubGrupoParcial="";
        int conSub=0;
        for (int con = 0; con <  grupoUnidos.length; )
        {
            subgrupounidos="";
            conSub=0;
            for (int i = 0; i <  listInsertArticulos.size(); )
            {
                if(grupoUnidos[con].equals(listInsertArticulos.get(i).getidGrupo())){
                    if(conSub==0){
                        subgrupounidos=listInsertArticulos.get(i).getSubgrupo();
                    }
                    else {
                        if(!subgrupounidos.contains(listInsertArticulos.get(i).getSubgrupo()))
                        {
                            subgrupounidos=subgrupounidos+","+listInsertArticulos.get(i).getSubgrupo();
                        }
                    }
                    conSub++;
                }
                i++;
            }

            if(con==0)
            {
                winveSubGrupoParcial= grupoUnidos[con]+":"+subgrupounidos;
             }
            else {
                winveSubGrupoParcial=winveSubGrupoParcial+";"+grupoUnidos[con]+":"+subgrupounidos;
             }
            con++;
        }
     }

    private static void capturarWinveSubGrupoParcialCriterio()
    {
        String grupo="";
        int cont=0;
        for (int i = 0; i <  listArrayArticulos.size(); )
        {
            if(!grupo.contains(listArrayArticulos.get(i).getidGrupo()) )
            {    if(cont==0)
            {
                grupo=listArrayArticulos.get(i).getidGrupo();
            }
            else
            {
                grupo=grupo+","+listArrayArticulos.get(i).getidGrupo();
            }
                cont++;
            }
            i++;
        }
        String grupoUnidos []  =  grupo.split(",");
        String subgrupounidos="";
        winveSubGrupoParcial="";
        int conSub=0;
        for (int con = 0; con <  grupoUnidos.length; )
        {
            subgrupounidos="";
            conSub=0;
            for (int i = 0; i <  listArrayArticulos.size(); )
            {
                if(grupoUnidos[con].equals(listArrayArticulos.get(i).getidGrupo())){
                    if(conSub==0){
                        subgrupounidos=listArrayArticulos.get(i).getSubgrupo();
                    }
                    else {
                        if(!subgrupounidos.contains(listArrayArticulos.get(i).getSubgrupo()))
                        {
                            subgrupounidos=subgrupounidos+","+listArrayArticulos.get(i).getSubgrupo();
                        }
                    }
                    conSub++;
                }
                i++;
            }

            if(con==0)
            {
                winveSubGrupoParcial= grupoUnidos[con]+":"+subgrupounidos;
            }
            else {
                winveSubGrupoParcial=winveSubGrupoParcial+";"+grupoUnidos[con]+":"+subgrupounidos;
            }
            con++;
        }
    }

}