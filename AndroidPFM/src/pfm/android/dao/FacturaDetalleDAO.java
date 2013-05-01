package pfm.android.dao;

import pfm.entidades.BodegaDetalle;
import pfm.entidades.Descuento;
import pfm.entidades.FacturaDetalle;

public interface FacturaDetalleDAO extends GenericDAO<FacturaDetalle, Integer> {

	public FacturaDetalle getFacturaDetalleById(int id);

	public FacturaDetalle setTotales(BodegaDetalle bodegaDetalle,
			Descuento descuento, int cantidad);

	public int anadirProducto(int idFactura, int idAgencia, int idCliente,
			int idBodegaDetalle, int idDescuento, int cantidad);

	public int actualizarProducto(int idFacturaDetalle, int idDescuento,
			int cantidad);
	
	public int eliminarProducto(int idFacturaDetalle); 

	public boolean existeProductoByFacturaDetalle(int idFactura,
			int idBodegaDetalle);
}
