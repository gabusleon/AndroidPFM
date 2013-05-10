package pfm.android.dao;

import pfm.entidades.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {

	/**
	 * Verifica el username y password ingresados en la aplicacion movil
	 * 
	 * @param username
	 * @param password
	 * @return idUsuario
	 */
	public int login(String username, String password);

}
