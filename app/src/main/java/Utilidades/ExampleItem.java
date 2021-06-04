package Utilidades;

public class ExampleItem {
    private int mImageResource;
    private String producto;
    private String posicion;
    private String cantidad;

    public ExampleItem(int imageResource, String producto, String posicion, String cantidad) {
        this.mImageResource = imageResource;
        this.producto = producto;
        this.posicion = posicion;
        this.cantidad = cantidad;
    }

    public int getImageResource() {
        return this.mImageResource;
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

}