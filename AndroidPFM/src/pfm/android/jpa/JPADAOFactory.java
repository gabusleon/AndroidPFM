package pfm.android.jpa;

import pfm.android.dao.DAOFactory;
import pfm.android.dao.UsuarioDAO;

public class JPADAOFactory extends DAOFactory {

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new JPAUsuarioDAO();
	}

}
