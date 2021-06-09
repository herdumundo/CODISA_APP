package Utilidades;

public class Stkw002List {
     private String nroToma;
    private String fechaToma;
    private String familia;
    private String grupo;

    public Stkw002List(String nroToma, String fechaToma, String familia, String grupo) {

        this.nroToma = nroToma;
        this.fechaToma = fechaToma;
        this.familia = familia;
        this.grupo = grupo;
    }
    public Stkw002List(){

    }
    public String getNroToma() {
        return this.nroToma;
    }
    public void setNroToma(String nroToma) {
        this.nroToma = nroToma;
    }


    public String getFechaToma() {
        return this.fechaToma;
    }
    public void setFechaToma(String fechaToma) {
        this.fechaToma = fechaToma;
    }

    public String getFamilia() {
        return this.familia;
    }
    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getGrupo() {
        return this.grupo;
    }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

}