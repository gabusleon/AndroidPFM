package pfm.android.dao;

import java.util.Map;

import pfm.entidades.MedioDePago;

public interface MedioPagoDAO extends GenericDAO<MedioDePago, Integer> {

	/**
	 * Listado de Medio de pagos
	 * 
	 * @return mapa de medio de pagos
	 */
	public Map<Integer, String> listMedioPago();
}
