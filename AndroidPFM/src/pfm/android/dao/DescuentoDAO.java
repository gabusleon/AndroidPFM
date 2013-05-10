package pfm.android.dao;

import org.json.JSONObject;

import pfm.entidades.Descuento;

public interface DescuentoDAO extends GenericDAO<Descuento, Integer> {

	/**
	 * Realiza el mapeo del objeto JSON a la entidad Descuento
	 * 
	 * @param objJSON
	 * @return Descuento
	 */
	public Descuento getJSONParserDescuento(JSONObject objJSON);

	/**
	 * Obtiene el Descuento enviando como parametro el producto
	 * 
	 * @param idProducto
	 * @return Descuento
	 */
	public Descuento getDescuentoByProducto(int idProducto);

}
