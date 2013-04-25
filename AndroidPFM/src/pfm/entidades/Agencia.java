package pfm.entidades;

import java.io.Serializable;
import java.util.Set;

/**
 * Entity implementation class for Entity: Agencia
 * 
 */
public class Agencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String direccion;
	private String telefono;	
	private boolean eliminado;
	private Empresa empresa;
	private Set<Bodega> bodega;
	private Set<EmpleadoAgencia> empleadoAgencia;
	private Set<Factura> factura;

	public Agencia() {

	}

	public Agencia(int id, String nombre, String direccion, String telefono,
			boolean eliminado) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.eliminado = eliminado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Set<Bodega> getBodega() {
		return bodega;
	}

	public void setBodega(Set<Bodega> bodega) {
		this.bodega = bodega;
	}

	public Set<EmpleadoAgencia> getEmpleadoAgencia() {
		return empleadoAgencia;
	}

	public void setEmpleadoAgencia(Set<EmpleadoAgencia> empleadoAgencia) {
		this.empleadoAgencia = empleadoAgencia;
	}

	public Set<Factura> getFactura() {
		return factura;
	}

	public void setFactura(Set<Factura> factura) {
		this.factura = factura;
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
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + (eliminado ? 1231 : 1237);
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
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
		Agencia other = (Agencia) obj;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (eliminado != other.eliminado)
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		return true;
	}

}
