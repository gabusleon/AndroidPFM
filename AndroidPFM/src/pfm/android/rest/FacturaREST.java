package pfm.android.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import pfm.entidades.Factura;

public class FacturaREST extends GenericREST {

	public FacturaREST() {
		super("factura");
	}

	/**
	 * Realiza el mapeo del objeto JSON a la entidad Factura
	 * 
	 * @param objJSON
	 * @return Factura
	 */
	@SuppressLint("SimpleDateFormat")
	public Factura getJSONParserFactura(JSONObject objJSON) {
		try {
			// mapea la entidad factura a partir del JSON
			Factura factura = new Factura();
			factura.setId(objJSON.getInt("id"));
			factura.setSubtotal(objJSON.getDouble("subtotal"));
			factura.setDescuento(objJSON.getDouble("descuento"));
			factura.setIva(objJSON.getDouble("iva"));
			factura.setTotal(objJSON.getDouble("total"));
			factura.setEliminado(objJSON.getBoolean("eliminado"));
			factura.setPagado(objJSON.getBoolean("pagado"));
			factura.setPendiente(objJSON.getBoolean("pendiente"));
			// obtiene fecha
			String fecha = objJSON.getString("fecha");
			SimpleDateFormat formatoFecha1 = new SimpleDateFormat("yyyy-MM-dd");
			Date fc1 = null;
			fc1 = formatoFecha1.parse(fecha);
			factura.setFecha(fc1);
			// obtiene la entidad agencia del objeto JSON
			JSONObject ageJSON = objJSON.getJSONObject("agencia");
			factura.setAgencia(new RESTFactory().getAgenciaDAO()
					.getJSONParserAgencia(ageJSON));
			return factura;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtiene la lista de Carros del Cliente (Facturas pendientes)
	 * 
	 * @author Carlos Iniguez
	 * @return Lista de Facturas
	 */
	public List<Factura> getCarrosCompra(int idUsuario, int idAgencia) {
		List<Factura> listaCarros = new ArrayList<Factura>();

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/carrosCompra/"
				+ idUsuario + "/" + idAgencia);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			JSONArray j = respJSON.optJSONArray("factura");
			Factura factura = new Factura();
			if (j != null) {
				JSONArray carrosJSON = respJSON.getJSONArray("factura");

				for (int i = 0; i < carrosJSON.length(); i++) {
					JSONObject carroJSON = carrosJSON.getJSONObject(i);
					factura = new RESTFactory().getFacturaDAO()
							.getJSONParserFactura(carroJSON);
					listaCarros.add(factura);
				}

			} else {
				JSONObject pJSON = respJSON.getJSONObject("factura");
				factura = new RESTFactory().getFacturaDAO()
						.getJSONParserFactura(pJSON);
				listaCarros.add(factura);

			}
			return listaCarros;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Eliminado logico del carro de compras
	 * 
	 * @param id
	 *            identificador de la factura
	 * @return id de la factura eliminada
	 * @author Gabus
	 * 
	 */
	public int EliminarFactura(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/delete/" + id);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDAO <<EliminarFactura>>", ex);
			return 0;
		}
	}

	/**
	 * Confirma que el total enviado sea igual al total generado en la fecha
	 * actual
	 * 
	 * @param id
	 * @param precioTotal
	 * @return el totalFactura generado
	 */
	public double ConfirmaTotal(int id, double totalFatura) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/confirmaTotal/" + id + "/"
				+ totalFatura);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getDouble("total");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDAO <<ConfirmaTotal>>", ex);
			return 0;
		}
	}

	/**
	 * Confirma la compra, seteando pendiente a false, pagado a true, y medio de
	 * pago
	 * 
	 * @param idFactura
	 * @param idMedioDePago
	 * @return id de la factura
	 */
	public int ConfirmaCompra(int idFactura, int idMedioDePago) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/confirmaCompra/"
				+ idFactura + "/" + idMedioDePago);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			// comprobar si el JSON es FacturaDetalle o Factura
			JSONArray arrFacDetJSON = objJSON.optJSONArray("facturaDetalle");
			if (arrFacDetJSON == null) {
				JSONObject objfacDetJSON = objJSON
						.optJSONObject("facturaDetalle");
				if (objfacDetJSON == null) {
					// confirmacion correcta
					return objJSON.getInt("id");
				} else {
					// no hay stock para un producto
					return -2;
				}
			} else {
				// no hay stock para varios productos
				return -2;
			}

		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDAO <<ConfirmaCompra>>", ex);
			return 0;
		}

	}
}
