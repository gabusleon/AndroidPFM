package pfm.android.dao;

import pfm.entidades.Descuento;

public interface DescuentoDAO extends GenericDAO<Descuento, Integer> {

	public Descuento getDescuentoByProducto(int idProducto);
}
