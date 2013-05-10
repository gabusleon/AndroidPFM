package pfm.android.jpa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import pfm.android.dao.FacturaDetalleDAO;
import pfm.entidades.BodegaDetalle;
import pfm.entidades.Descuento;
import pfm.entidades.FacturaDetalle;

public class JPAFacturaDetalleDAO extends
		JPAGenericDAO<FacturaDetalle, Integer> implements FacturaDetalleDAO {

	public JPAFacturaDetalleDAO() {
		super(FacturaDetalle.class, "facturaDetalle");
	}

	@Override
	public FacturaDetalle getJSONParserFacturaDetalle(JSONObject objJSON) {
		try {
			// mapea la entidad facturaDetalle a partir del JSON
			FacturaDetalle facturaDetalle = new FacturaDetalle();
			facturaDetalle.setId(objJSON.getInt("id"));
			facturaDetalle.setCantidad(objJSON.getInt("cantidad"));
			facturaDetalle.setPrecio(objJSON.getDouble("precio"));
			facturaDetalle.setSubtotal(objJSON.getDouble("subtotal"));
			facturaDetalle.setDescuento(objJSON.getDouble("descuento"));
			facturaDetalle.setIva(objJSON.getDouble("iva"));
			facturaDetalle.setTotal(objJSON.getDouble("total"));

			// genera la entidad bodegaDetalle
			JSONObject bodDetJSON = objJSON.getJSONObject("bodegaDetalle");
			facturaDetalle.setBodegaDetalle(JPADAOFactory.getFactory()
					.getBodegaDetalleDAO()
					.getJSONParserBodegaDetalle(bodDetJSON));

			// genera la entidad factura solo con su id
			JSONObject facJSON = objJSON.getJSONObject("factura");
			facturaDetalle.setFactura(JPADAOFactory.getFactory()
					.getFacturaDAO().getJSONParserFactura(facJSON));

			return facturaDetalle;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public FacturaDetalle getFacturaDetalleById(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/getFacturaDetalleById/"
				+ id);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			FacturaDetalle facturaDetalle = new FacturaDetalle();
			facturaDetalle = getJSONParserFacturaDetalle(objJSON);
			return facturaDetalle;

		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDetalleDAO <<getFacturaDetalleById>>", ex);
			return null;
		}
	}

	@Override
	public FacturaDetalle setTotales(BodegaDetalle bodegaDetalle,
			Descuento descuento, int cantidad) {
		double des = 0;
		double iva = 0;
		double sub = 0;
		double tot = 0;

		sub = Math.round((cantidad * bodegaDetalle.getPrecio()) * 100.0) / 100.0;
		if (descuento != null) {
			des = Math.round(((sub * descuento.getValor()) / 100) * 100.0) / 100.0;
		} else {
			des = 0.00;
		}
		iva = Math.round(((sub * bodegaDetalle.getBodega().getAgencia()
				.getEmpresa().getIva()) / 100) * 100.0) / 100.0;
		tot = Math.round((sub - des + iva) * 100.0) / 100.0;

		FacturaDetalle facturaDetalle = new FacturaDetalle();
		facturaDetalle.setBodegaDetalle(bodegaDetalle);
		facturaDetalle.setCantidad(cantidad);
		facturaDetalle.setDescuento(des);
		facturaDetalle.setEliminado(false);
		facturaDetalle.setIva(iva);
		facturaDetalle.setPrecio(bodegaDetalle.getPrecio());
		facturaDetalle.setSubtotal(sub);
		facturaDetalle.setTotal(tot);

		return facturaDetalle;
	}

	@Override
	public int anadirProducto(int idFactura, int idAgencia, int idCliente,
			int idBodegaDetalle, int idDescuento, int cantidad) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/create/" + idFactura + "/"
				+ idAgencia + "/" + idCliente + "/" + idBodegaDetalle + "/"
				+ idDescuento + "/" + cantidad);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDetalleDAO <<anadirProducto>>", ex);
			return 0;
		}
	}

	@Override
	public int actualizarProducto(int idFacturaDetalle, int idDescuento,
			int cantidad) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/update/" + idFacturaDetalle
				+ "/" + idDescuento + "/" + cantidad);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDetalleDAO <<actualizarProducto>>", ex);
			return 0;
		}
	}

	@Override
	public int eliminarProducto(int idFacturaDetalle) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/delete/" + idFacturaDetalle);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDetalleDAO <<eliminarProducto>>", ex);
			return 0;
		}
	}

	@Override
	public boolean existeProductoByFacturaDetalle(int idFactura,
			int idBodegaDetalle) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST
				+ "/existeProductoByFacturaDetalle/" + idFactura + "/"
				+ idBodegaDetalle);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			int id = objJSON.getInt("id");
			if (id != 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			Log.e("Error",
					"JPAFacturaDetalleDAO <<existeProductoByFacturaDetalle>>",
					ex);
			return false;
		}
	}

	/**
	 * Obtiene el listado de los productos del carro de compras actual
	 * 
	 * @param idFactura
	 *            el identificador de la factura
	 * @return listado de facturaDetalle
	 * @author Carlos Iniguez
	 */
	@Override
	public List<FacturaDetalle> getCarroActual(int idFactura) {

		List<FacturaDetalle> listaProductos = new ArrayList<FacturaDetalle>();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST
				+ "/carroCompraActual/" + idFactura);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONObject respJSON = new JSONObject(respStr);

			JSONObject pJSON = new JSONObject();
			JSONArray j = respJSON.optJSONArray("facturaDetalle");
			FacturaDetalle facturaDetalle = new FacturaDetalle();
			if (j != null) {
				JSONArray detallesJSON = respJSON
						.getJSONArray("facturaDetalle");
				for (int i = 0; i < detallesJSON.length(); i++) {
					pJSON = detallesJSON.getJSONObject(i);
					facturaDetalle = getJSONParserFacturaDetalle(pJSON);
					listaProductos.add(facturaDetalle);
				}

			} else {

				pJSON = respJSON.getJSONObject("facturaDetalle");
				facturaDetalle = getJSONParserFacturaDetalle(pJSON);
				listaProductos.add(facturaDetalle);
			}

			return listaProductos;
		} catch (Exception ex) {
			return null;
		}

	}

}
