package pfm.android.dao;

import pfm.entidades.BodegaDetalle;

public interface BodegaDetalleDAO extends GenericDAO<BodegaDetalle, Integer> {

	public BodegaDetalle getBodegaDetalleById(int id);	

}
