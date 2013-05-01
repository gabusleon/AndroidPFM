package pfm.android.dao;

import pfm.entidades.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {

	public int login(String username, String password);	

}
