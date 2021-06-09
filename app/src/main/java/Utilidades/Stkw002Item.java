package Utilidades;

public class Stkw002Item {
     private String producto;
    private String posicion;
    private String cantidad;
    private String lote;
     private String cod_articulo;
    private String vencimiento;

    public Stkw002Item(String producto, String posicion, String cantidad, String lote,String cod_articulo,String vencimiento ) {
     //   this.mImageResource = imageResource;
        this.producto = producto;
        this.posicion = posicion;
        this.cantidad = cantidad;
        this.lote = lote;
        this.cod_articulo = cod_articulo;
        this.vencimiento = vencimiento;
    }

    public String getProducto() {
        return this.producto;
    }

    public String getPosicion() {
        return this.posicion;
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