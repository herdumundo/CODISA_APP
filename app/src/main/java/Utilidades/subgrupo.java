package Utilidades;

public class subgrupo {
     private String id;
    private String valor;


    public subgrupo(String id, String valor ) {
        this.id = id;
        this.valor = valor;
    }

    public subgrupo(){

    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return this.valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }

}