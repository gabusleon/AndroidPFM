package pfm.android.dao;

import java.util.List;

import pfm.entidades.Factura;
import pfm.entidades.rest.ItemCarro;
import pfm.entidades.rest.ItemProducto;

public interface FacturaDAO extends GenericDAO<Factura, Integer> {

	/**
	 * Obtiene la Lista de Productos ( facturaDetalle ) del carro indicado
	 * @author Carlos Iniguez
	 * @return Lista de Detalles de Factura ( productos)
	 */
	public List<ItemProducto> getCarroActual(int idFactura);
	/**
	 * Obtiene la lista de Carros del Cliente (Facturas pendientes)
	 * @author Carlos Iniguez
	 * @return Lista de Facturas
	 */
	public List<ItemCarro> getListaCarros(int idUsuario, int idAgencia);

}
