package Utilidades;

public class ArrayListContenedor {
	private long id;
	private String name;
	private String lote;
	private String cantidad;
	private String fecha_vencimiento;
	private String subgrupo;
	private boolean isSelected;
	private Object object;

	public ArrayListContenedor() {
	}

	public ArrayListContenedor(String name, boolean isSelected, String lote, String cantidad,String fecha_vencimiento,String subgrupo) {
		this.name = name;
		this.isSelected = isSelected;
		this.lote = lote;
		this.cantidad = cantidad;
		this.fecha_vencimiento = fecha_vencimiento;
		this.subgrupo = subgrupo;
 	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}


	public String getFechaVencimiento() {
		return fecha_vencimiento;
	}
	public void setFechaVencimiento(String fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}


	public boolean isSelected() {
		return isSelected;
	}
 	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
