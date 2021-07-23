package Utilidades;

public class Stkw002Item {
     private String producto;
  //  private String posicion;

    private String cantidad;
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

    public Stkw002Item(String producto,   String cantidad, String lote,String cod_articulo,String vencimiento ,String secuencia,String area ,String grupo,String familia ,String tomaRegistro) {
     //   this.mImageResource = imageResource;
        this.producto = producto;
     //   this.posicion = posicion;
        this.cantidad = cantidad;
        this.lote = lote;
        this.cod_articulo = cod_articulo;
        this.vencimiento = vencimiento;
        this.secuencia = secuencia;
        this.area = area;
        this.grupo = grupo;
        this.familia = familia;
        this.tomaRegistro = tomaRegistro;

    }

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

    public String getLote() {
        return this.lote;
    }
    public String getVencimiento() {
        return this.vencimiento;
    }
   public String getCodArticulo() {
        return this.cod_articulo;
    }

}