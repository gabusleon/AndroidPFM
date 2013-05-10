package pfm.android.dao;

import java.util.List;

import org.json.JSONObject;

import pfm.entidades.BodegaDetalle;
import pfm.entidades.Descuento;
import pfm.entidades.FacturaDetalle;

public interface FacturaDetalleDAO extends GenericDAO<FacturaDetalle, Integer> {

	/**
	 * Realiza el mapeo del objeto JSON a la entidad FacturaDetalle
	 * 
	 * @param objJSON
	 * @return FacturaDetalle
	 */
	public FacturaDetalle getJSONParserFacturaDetalle(JSONObject objJSON);

	/**
	 * Obtiene la entidad FacturaDetalle enviando como parametro el id
	 * 
	 * @param id
	 * @return FacturaDetalle
	 */
	public FacturaDetalle getFacturaDetalleById(int id);

	/**
	 * Realiza los calculos de los costos de una FacturaDetalle
	 * 
	 * @param bodegaDetalle
	 * @param descuento
	 * @param cantidad
	 * @return FacturaDetalle
	 */
	public FacturaDetalle setTotales(BodegaDetalle bodegaDetalle,
			Descuento descuento, int cantidad);

	/**
	 * Inserta un producto en la FacturaDetalle
	 * 
	 * @param idFactura
	 * @param idAgencia
	 * @param idCliente
	 * @param idBodegaDetalle
	 * @param idDescuento
	 * @param cantidad
	 * @return idFactura
	 */
	public int anadirProducto(int idFactura, int idAgencia, int idCliente,
			int idBodegaDetalle, int idDescuento, int cantidad);

	/**
	 * Actualiza la cantidad de un producto en la FacturaDetalle
	 * 
	 * @param idFacturaDetalle
	 * @param idDescuento
	 * @param cantidad
	 * @return idFactura
	 */
	public int actualizarProducto(int idFacturaDetalle, int idDescuento,
			int cantidad);

	/**
	 * Elimina fisicamente la FacturaDetalle
	 * 
	 * @param idFacturaDetalle
	 * @return idFactura
	 */
	public int eliminarProducto(int idFacturaDetalle);

	/**
	 * Verifica que el producto no este ingresado en otra FacturaDetalle
	 * 
	 * @param idFactura
	 * @param idBodegaDetalle
	 * @return true/false
	 */
	public boolean existeProductoByFacturaDetalle(int idFactura,
			int idBodegaDetalle);

	/**
	 * Obtiene la Lista de Productos ( facturaDetalle ) del carro indicado
	 * 
	 * @author Carlos Iniguez
	 * @return Lista de Detalles de Factura ( productos)
	 */
	public List<FacturaDetalle> getCarroActual(int idFactura);

}
