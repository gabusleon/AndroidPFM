package pfm.entidades;

import java.io.Serializable;

/**
 * Entity implementation class for Entity: DescuentoProducto
 * 
 */
public class DescuentoProducto implements Serializable {

	private int id;
	private boolean eliminado;
	private Producto producto;
	private Descuento descuento;
	private static final long serialVersionUID = 1L;

	public DescuentoProducto() {
	}

	public DescuentoProducto(int id, boolean eliminado) {
		this.id = id;
		this.eliminado = eliminado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Descuento getDescuento() {
		return descuento;
	}

	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}

	@Override
	public String toString() {
		return "DescuentoProducto [id=" + id + ", eliminado=" + eliminado
				+ ", producto=" + producto + ", descuento=" + descuento + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descuento == null) ? 0 : descuento.hashCode());
		result = prime * result + (eliminado ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result
				+ ((producto == null) ? 0 : producto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DescuentoProducto other = (DescuentoProducto) obj;
		if (descuento == null) {
			if (other.descuento != null)
				return false;
		} else if (!descuento.equals(other.descuento))
			return false;
		if (eliminado != other.eliminado)
			return false;
		if (id != other.id)
			return false;
		if (producto == null) {
			if (other.producto != null)
				return false;
		} else if (!producto.equals(other.producto))
			return false;
		return true;
	}

}
