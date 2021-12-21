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
    private String ultimo;
    private Integer cantidad_cajas;
    private Integer cantidad_cajetillas;
    private Integer cantidad_cajasBD;
    private Integer cantidad_cajetillasBD;



    public Stkw002Item(String producto, Integer CantidadUnitaria, String lote, String cod_articulo, String vencimiento
            , String secuencia, String area , String grupo, String familia , String tomaRegistro, String codBarra, String cantidadTotal, String ultimo,
                       Integer cantidad_cajasBD, Integer cantidad_cajetillasBD, Integer cantidad_cajas, Integer cantidad_cajetillas) {
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
        this.ultimo = ultimo;
        this.cantidad_cajas = cantidad_cajas;
        this.cantidad_cajetillas = cantidad_cajetillas;
        this.cantidad_cajetillasBD = cantidad_cajetillasBD;
        this.cantidad_cajasBD = cantidad_cajasBD;
    }


    public Integer getCantidad_cajas() {
        return cantidad_cajas;
    }

    public void setCantidad_cajas(Integer cantidad_cajas) {
        this.cantidad_cajas = cantidad_cajas;
    }

    public Integer getCantidad_cajetillas() {
        return cantidad_cajetillas;
    }

    public void setCantidad_cajetillas(Integer cantidad_cajetillas) {
        this.cantidad_cajetillas = cantidad_cajetillas;
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

    public Integer getCantidad_cajetillasBD() {
        return cantidad_cajetillasBD;
    }

    public void setCantidad_cajetillasBD(Integer cantidad_cajetillasBD) {
        this.cantidad_cajetillasBD = cantidad_cajetillasBD;
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

    public String getUltimo() {
        return ultimo;
    }

    public void setUltimo(String ultimo) {
        this.ultimo = ultimo;
    }
/*
    public String getProducto() {
        return this.producto;
    }

    public String getSecuencia() {
        return this.secuencia;
    }
    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getArea() {
        return this.area;
    }



    public String getCantidadTotal() {
        return this.cantidadTotal;
    }
    public void setCantidadTotal(String cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getUltimo() {
        return this.ultimo;
    }
    public void setUltimo(String ultimo) {
        this.ultimo = ultimo;
    }



    public String getCantidad() {
        return this.cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getgrupo() {
        return this.grupo;
    }
    public void setgrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getfamilia() {
        return this.familia;
    }
    public void setfamilia(String familia) {
        this.familia = familia;
    }

    public String getTomaRegistro() {
        return this.tomaRegistro;
    }

    public String getCodBarra() {
        return this.codBarra;
    }

    public String getLote() {
        return this.lote;
    }
    public String getVencimiento() {
        return this.vencimiento;
    }
   public String getCodArticulo() {
        return this.cod_articulo;
    }*/

}