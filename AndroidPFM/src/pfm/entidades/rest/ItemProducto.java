package pfm.entidades.rest;

import java.io.Serializable;


public class ItemProducto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idBodegaDetalle;
	private int idFacturaDetalle;
	private String nombreProducto;
	private double subtotal;
	private int cantidad;
	private double precio;
	private double descuento;
	private double descuentoTotal;
	private double totalFactura;
	
	public ItemProducto(int idBodegaDetalle, int idFacturaDetalle, String nombreProducto, double subtotal, int cantidad, double precio, double descuento,
			double descuentoTotal, double totalFactura) {
		super();
		this.idBodegaDetalle = idBodegaDetalle;
		this.idFacturaDetalle = idFacturaDetalle;
		this.nombreProducto = nombreProducto;
		this.subtotal = subtotal;
		this.cantidad = cantidad;
		this.precio = precio;
		this.descuento = descuento;
		this.descuentoTotal = descuentoTotal;
		this.totalFactura = totalFactura;
	}

	public ItemProducto(){
		
	}
	
	
	
	public int getIdBodegaDetalle() {
		return idBodegaDetalle;
	}
	public void setIdBodegaDetalle(int idBodegaDetalle) {
		this.idBodegaDetalle = idBodegaDetalle;
	}
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getIdFacturaDetalle() {
		return idFacturaDetalle;
	}

	public void setIdFacturaDetalle(int idFacturaDetalle) {
		this.idFacturaDetalle = idFacturaDetalle;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getDescuentoTotal() {
		return descuentoTotal;
	}

	public void setDescuentoTotal(double descuentoTotal) {
		this.descuentoTotal = descuentoTotal;
	}

	public double getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}

}
