package pfm.android.jpa;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;

import pfm.android.dao.UsuarioDAO;
import pfm.entidades.Usuario;

public class JPAUsuarioDAO extends JPAGenericDAO<Usuario, Integer> implements
		UsuarioDAO {

	public JPAUsuarioDAO() {
		super(Usuario.class, "autenticacion");
	}

	@Override
	public Usuario login(String username, String password) {
		try {
			this.setWr(this.getWr().path("/login/" + username + "/" + password));
			Usuario responseUser = this.getWr()
					.accept(MediaType.APPLICATION_XML).get(this.getClaseREST());
			return responseUser;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public boolean registroUsuario(Usuario usuario) {
		try {
			ClientResponse response = this.getWr().path("/registro")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, usuario);
			if (response.getStatus() == 200
					&& !response.getEntity(String.class).equals("-1"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage().toString());
			return false;
		}
	}

}
