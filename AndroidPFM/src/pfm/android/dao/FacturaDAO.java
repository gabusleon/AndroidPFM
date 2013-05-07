package pfm.android.dao;

import java.util.List;

import pfm.entidades.Factura;
import pfm.entidades.rest.ItemCarro;
import pfm.entidades.rest.ItemProducto;

public interface FacturaDAO extends GenericDAO<Factura, Integer> {

	/**
	 * Obtiene la Lista de Productos ( facturaDetalle ) del carro indicado
	 * 
	 * @author Carlos Iniguez
	 * @return Lista de Detalles de Factura ( productos)
	 */
	public List<ItemProducto> getCarroActual(int idFactura);

	/**
	 * Obtiene la lista de Carros del Cliente (Facturas pendientes)
	 * 
	 * @author Carlos Iniguez
	 * @return Lista de Facturas
	 */
	public List<ItemCarro> getListaCarros(int idUsuario, int idAgencia);

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
