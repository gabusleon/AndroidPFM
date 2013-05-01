package pfm.android.jpa;

import pfm.android.dao.AgenciaDAO;
import pfm.android.dao.BodegaDetalleDAO;
import pfm.android.dao.DAOFactory;
import pfm.android.dao.DescuentoDAO;
import pfm.android.dao.FacturaDetalleDAO;
import pfm.android.dao.UsuarioDAO;

public class JPADAOFactory extends DAOFactory {

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new JPAUsuarioDAO();
	}

	@Override
	public AgenciaDAO getAgenciaDAO() {
		return new JPAAgenciaDAO();
	}

	@Override
	public BodegaDetalleDAO getBodegaDetalleDAO() {
		return new JPABodegaDetalleDAO();
	}

	@Override
	public DescuentoDAO getDescuentoDAO() {
		return new JPADescuentoDAO();
	}

	@Override
	public FacturaDetalleDAO getFacturaDetalleDAO() {
		return new JPAFacturaDetalleDAO();
	}

}
