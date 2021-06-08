package Utilidades;

public class Stkw002Item {
     private String producto;
    private String posicion;
    private String cantidad;
    private String lote;

    public Stkw002Item(String producto, String posicion, String cantidad, String lote) {
     //   this.mImageResource = imageResource;
        this.producto = producto;
        this.posicion = posicion;
        this.cantidad = cantidad;
        this.lote = lote;
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

    public String getLote() {
        return this.lote;
    }

}