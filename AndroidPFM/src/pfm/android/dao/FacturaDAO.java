package pfm.android.dao;

import java.util.List;

import org.json.JSONObject;

import pfm.entidades.Factura;

public interface FacturaDAO extends GenericDAO<Factura, Integer> {

	/**
	 * Realiza el mapeo del objeto JSON a la entidad Factura
	 * 
	 * @param objJSON
	 * @return Factura
	 */
	public Factura getJSONParserFactura(JSONObject objJSON);

	/**
	 * Obtiene la lista de Carros del Cliente (Facturas pendientes)
	 * 
	 * @author Carlos Iniguez
	 * @return Lista de Facturas
	 */
	public List<Factura> getCarrosCompra(int idUsuario, int idAgencia);

	/**
	 * Eliminado logico de la factura por el ID
	 * 
	 * @param id
	 * @return Id de la factura actualizada
	 */
	public int EliminarFactura(int id);

	/**
	 * Confirma que el total enviado sea igual al total generado en la fecha
	 * actual
	 * 
	 * @param id
	 * @param precioTotal
	 * @return el totalFactura generado
	 */
	public double ConfirmaTotal(int id, double totalFactura);

	/**
	 * Confirma la compra, seteando pendiente a false, pagado a true, y medio de
	 * pago
	 * 
	 * @param idFactura
	 * @param idMedioDePago
	 * @return id de la factura
	 */
	public int ConfirmaCompra(int idFactura, int idMedioDePago);

}
