package pfm.entidades.rest;

import pfm.entidades.Agencia;
import pfm.entidades.Factura;
import pfm.entidades.Usuario;

public class SessionData {

	private static Agencia agencia;
	private static Usuario usuario;
	private static Factura factura;
	
	public static Agencia getAgencia() {
		return agencia;
	}
	public static void setAgencia(Agencia agencia) {
		SessionData.agencia = agencia;
	}
	public static Usuario getUsuario() {
		return usuario;
	}
	public static void setUsuario(Usuario usuario) {
		SessionData.usuario = usuario;
	}
	public static Factura getFactura() {
		return factura;
	}
	public static void setFactura(Factura factura) {
		SessionData.factura = factura;
	}
}
