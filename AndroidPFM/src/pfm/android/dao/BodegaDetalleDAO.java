package pfm.android.dao;

import org.json.JSONObject;

import pfm.entidades.BodegaDetalle;

public interface BodegaDetalleDAO extends GenericDAO<BodegaDetalle, Integer> {

	/**
	 * Realiza el mapeo del objeto JSON a la entidad BodegaDetalle
	 * 
	 * @param objJSON
	 * @return BodegaDetalle
	 */
	public BodegaDetalle getJSONParserBodegaDetalle(JSONObject objJSON);

	/**
	 * busca la entidad bodegaDetalle
	 * 
	 * @param id
	 * @return BodegaDetalle
	 */
	public BodegaDetalle getBodegaDetalleById(int id);

}
