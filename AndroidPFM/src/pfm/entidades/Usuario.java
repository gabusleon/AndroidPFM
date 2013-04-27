package pfm.entidades;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Usuario
 * 
 */

@XmlRootElement
public class Usuario implements Serializable {

	private int id;
	private String nombres;
	private String apellidos;
	private Date fechaNacimiento;
	private String direccion;
	private String telefono;
	private String email;
	private String username;
	private String password;
	private boolean eliminado;
	private Rol rol;
	private Set<Factura> facturaCliente;
	private Set<EmpleadoAgencia> empleadoAgencia;
	private static final long serialVersionUID = 1L;

	public Usuario() {

	}

	public boolean isLogeado() {
		return true;
	}

	public Usuario(int id, String nombres, String apellidos,
			Date fechaNacimiento, String direccion, String telefono,
			String email, String username, String password, boolean eliminado) {
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.telefono = telefono;
		this.email = email;
		this.username = username;
		this.password = password;
		this.eliminado = eliminado;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Set<Factura> getFacturaCliente() {
		return facturaCliente;
	}

	public void setFacturaCliente(Set<Factura> facturaCliente) {
		this.facturaCliente = facturaCliente;
	}

	public Set<EmpleadoAgencia> getEmpleadoAgencia() {
		return empleadoAgencia;
	}

	public void setEmpleadoAgencia(Set<EmpleadoAgencia> empleadoAgencia) {
		this.empleadoAgencia = empleadoAgencia;
	}

	@Override
	public String toString() {
		return nombres + " " + apellidos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + (eliminado ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((fechaNacimiento == null) ? 0 : fechaNacimiento.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombres == null) ? 0 : nombres.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		Usuario other = (Usuario) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (eliminado != other.eliminado)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fechaNacimiento == null) {
			if (other.fechaNacimiento != null)
				return false;
		} else if (!fechaNacimiento.equals(other.fechaNacimiento))
			return false;
		if (id != other.id)
			return false;
		if (nombres == null) {
			if (other.nombres != null)
				return false;
		} else if (!nombres.equals(other.nombres))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (rol == null) {
			if (other.rol != null)
				return false;
		} else if (!rol.equals(other.rol))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
