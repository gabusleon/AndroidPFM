package pfm.android.dao;

import pfm.android.jpa.JPADAOFactory;

public abstract class DAOFactory {
	protected static DAOFactory factory = new JPADAOFactory();

	public static DAOFactory getFactory() {
		return factory;
	}

	public abstract UsuarioDAO getUsuarioDAO();

	public abstract AgenciaDAO getAgenciaDAO();

	public abstract BodegaDetalleDAO getBodegaDetalleDAO();

	public abstract DescuentoDAO getDescuentoDAO();

	public abstract FacturaDetalleDAO getFacturaDetalleDAO();
}
