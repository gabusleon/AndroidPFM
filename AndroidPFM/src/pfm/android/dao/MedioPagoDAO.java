package pfm.android.dao;

import java.util.Map;

import pfm.entidades.MedioDePago;

public interface MedioPagoDAO extends GenericDAO<MedioDePago, Integer> {

	public Map<Integer, String> listMedioPago();
}
