package pfm.entidades;

import java.io.Serializable;
import java.lang.String;
import java.util.Set;

/**
 * Entity implementation class for Entity: Producto
 * 
 */
public class Producto implements Serializable {

	private int id;
	private String nombre;
	private boolean eliminado;
	private Marca marca;
	private Categoria categoria;
	private Set<BodegaDetalle> bodegaDetalle;
	private Set<DescuentoProducto> descuentoProducto;
	private static final long serialVersionUID = 1L;

	public Producto() {

	}

	public Producto(int id, String nombre, boolean eliminado) {
		this.id = id;
		this.nombre = nombre;
		this.eliminado = eliminado;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Set<BodegaDetalle> getBodegaDetalle() {
		return bodegaDetalle;
	}

	public void setBodegaDetalle(Set<BodegaDetalle> bodegaDetalle) {
		this.bodegaDetalle = bodegaDetalle;
	}

	public Set<DescuentoProducto> getDescuentoProducto() {
		return descuentoProducto;
	}

	public void setDescuentoProducto(Set<DescuentoProducto> descuentoProducto) {
		this.descuentoProducto = descuentoProducto;
	}

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + (eliminado ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((marca == null) ? 0 : marca.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Producto other = (Producto) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (eliminado != other.eliminado)
			return false;
		if (id != other.id)
			return false;
		if (marca == null) {
			if (other.marca != null)
				return false;
		} else if (!marca.equals(other.marca))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

}
