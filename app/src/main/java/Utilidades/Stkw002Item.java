package Utilidades;

public class Stkw002Item {
     private String producto;
  //  private String posicion;
    private String cantidad;
    private String lote;
     private String cod_articulo;
    private String vencimiento;
    private String secuencia;

    public Stkw002Item(String producto,   String cantidad, String lote,String cod_articulo,String vencimiento ,String secuencia ) {
     //   this.mImageResource = imageResource;
        this.producto = producto;
     //   this.posicion = posicion;
        this.cantidad = cantidad;
        this.lote = lote;
        this.cod_articulo = cod_articulo;
        this.vencimiento = vencimiento;
        this.secuencia = secuencia;
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




    public String getCantidad() {
        return this.cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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