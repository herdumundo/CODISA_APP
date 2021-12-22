package Utilidades;

public class Stkw002Item {
     private String producto;
    private Integer CantidadUnitaria;
    private String cantidadTotal;
    private String contador;
    private String lote;
    private String cod_articulo;
    private String vencimiento;
    private String secuencia;
    private String seccion;
    private String grupo;
    private String area;
    private String familia;
    private String tomaRegistro;
    private String codBarra;
    private String ultimo_unidades;
    private Integer cantidad_cajas;
    private Integer cantidad_gruesa;
    private Integer cantidad_cajasBD;
    private Integer cantidad_gruesaBD;
    private Integer id_familia;
    private String ultimo_cajas;
    private String ultimo_gruesa;


    public Stkw002Item(String producto, Integer CantidadUnitaria, String lote, String cod_articulo, String vencimiento
            , String secuencia, String area , String grupo, String familia , String tomaRegistro, String codBarra, String cantidadTotal, String ultimo_unidades,
                       Integer cantidad_cajasBD, Integer cantidad_gruesaBD, Integer cantidad_cajas, Integer cantidad_gruesa, Integer id_familia, String ultimo_cajas, String ultimo_gruesa) {
        this.producto = producto;
        this.CantidadUnitaria = CantidadUnitaria;
        this.cantidadTotal = cantidadTotal;
        this.contador = contador;
        this.lote = lote;
        this.cod_articulo = cod_articulo;
        this.vencimiento = vencimiento;
        this.secuencia = secuencia;
        this.seccion = seccion;
        this.grupo = grupo;
        this.area = area;
        this.familia = familia;
        this.tomaRegistro = tomaRegistro;
        this.codBarra = codBarra;
        this.ultimo_unidades = ultimo_unidades;
        this.cantidad_cajas = cantidad_cajas;
        this.cantidad_gruesa = cantidad_gruesa;
        this.cantidad_gruesaBD = cantidad_gruesaBD;
        this.cantidad_cajasBD = cantidad_cajasBD;
        this.id_familia = id_familia;
        this.ultimo_cajas = ultimo_cajas;
        this.ultimo_gruesa = ultimo_gruesa;


    }


    public Integer getId_familia() {
        return id_familia;
    }

    public void setId_familia(Integer id_familia) {
        this.id_familia = id_familia;
    }

    public Integer getCantidad_cajas() {
        return cantidad_cajas;
    }

    public void setCantidad_cajas(Integer cantidad_cajas) {
        this.cantidad_cajas = cantidad_cajas;
    }


    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidadUnitaria() {
        return CantidadUnitaria;
    }

    public void setCantidadUnitaria(Integer CantidadUnitaria) {
        this.CantidadUnitaria = CantidadUnitaria;
    }





    public Integer getCantidad_cajasBD() {
        return cantidad_cajasBD;
    }

    public void setCantidad_cajasBD(Integer cantidad_cajasBD) {
        this.cantidad_cajasBD = cantidad_cajasBD;
    }






    public String getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(String cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getCod_articulo() {
        return cod_articulo;
    }

    public void setCod_articulo(String cod_articulo) {
        this.cod_articulo = cod_articulo;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getTomaRegistro() {
        return tomaRegistro;
    }

    public void setTomaRegistro(String tomaRegistro) {
        this.tomaRegistro = tomaRegistro;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getUltimo_unidades() {
        return ultimo_unidades;
    }

    public void setUltimo_unidades(String ultimo_unidades) {
        this.ultimo_unidades = ultimo_unidades;
    }

    public String getUltimo_cajas() {
        return ultimo_cajas;
    }

    public void setUltimo_cajas(String ultimo_cajas) {
        this.ultimo_cajas = ultimo_cajas;
    }

    public String getUltimo_gruesa() {
        return ultimo_gruesa;
    }

    public void setUltimo_gruesa(String ultimo_gruesa) {
        this.ultimo_gruesa = ultimo_gruesa;
    }

    public Integer getCantidad_gruesa() {
        return cantidad_gruesa;
    }

    public void setCantidad_gruesa(Integer cantidad_gruesa) {
        this.cantidad_gruesa = cantidad_gruesa;
    }

    public Integer getCantidad_gruesaBD() {
        return cantidad_gruesaBD;
    }

    public void setCantidad_gruesaBD(Integer cantidad_gruesaBD) {
        this.cantidad_gruesaBD = cantidad_gruesaBD;
    }
}