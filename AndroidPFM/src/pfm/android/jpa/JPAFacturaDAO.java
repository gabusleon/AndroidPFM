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
import pfm.android.dao.FacturaDAO;
import pfm.entidades.Factura;
import pfm.entidades.rest.ItemCarro;
import pfm.entidades.rest.ItemProducto;

public class JPAFacturaDAO extends JPAGenericDAO<Factura, Integer> implements
		FacturaDAO {

	public JPAFacturaDAO() {
		super(Factura.class, "compra");
	}

	@Override
	public List<ItemProducto> getCarroActual(int idFactura) {
		List<ItemProducto> listaProductos = new ArrayList<ItemProducto>();
		if (idFactura == -1) {
			return null;
		} else {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(this.uri + this.urlREST + "/carro/"
					+ idFactura);
			del.setHeader("content-type", "application/json");

			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONObject respJSON = new JSONObject(respStr);

				JSONObject pJSON = new JSONObject();

				if (!respStr.contains("[")) {
					pJSON = respJSON.getJSONObject("itemProducto");
					ItemProducto p = new ItemProducto();
					p.setCantidad(pJSON.getInt("cantidad"));
					p.setDescuento(pJSON.getDouble("descuento"));
					p.setDescuentoTotal(pJSON.getDouble("descuentoTotal"));
					p.setIdBodegaDetalle(pJSON.getInt("idBodegaDetalle"));
					p.setIdFacturaDetalle(pJSON.getInt("idFacturaDetalle"));
					p.setNombreProducto(pJSON.getString("nombreProducto"));
					p.setPrecio(pJSON.getDouble("precio"));
					p.setSubtotal(pJSON.getDouble("subtotal"));
					p.setTotalFactura(pJSON.getDouble("totalFactura"));
					listaProductos.add(p);

				} else {

					JSONArray detallesJSON = respJSON
							.getJSONArray("itemProducto");
					for (int i = 0; i < detallesJSON.length(); i++) {
						pJSON = detallesJSON.getJSONObject(i);

						ItemProducto p = new ItemProducto();
						p.setCantidad(pJSON.getInt("cantidad"));
						p.setDescuento(pJSON.getDouble("descuento"));
						p.setDescuentoTotal(pJSON.getDouble("descuentoTotal"));
						p.setIdBodegaDetalle(pJSON.getInt("idBodegaDetalle"));
						p.setIdFacturaDetalle(pJSON.getInt("idFacturaDetalle"));
						p.setNombreProducto(pJSON.getString("nombreProducto"));
						p.setPrecio(pJSON.getDouble("precio"));
						p.setSubtotal(pJSON.getDouble("subtotal"));
						p.setTotalFactura(pJSON.getDouble("totalFactura"));
						listaProductos.add(p);
						listaProductos.add(p);
					}
				}

				return listaProductos;
			} catch (Exception ex) {
				return null;
			}
		}
	}

	@Override
	public List<ItemCarro> getListaCarros(int idUsuario, int idAgencia) {
		List<ItemCarro> listaCarros = new ArrayList<ItemCarro>();

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/carros/"
				+ idUsuario + "/" + idAgencia);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			if (!respStr.contains("[")) {
				JSONObject pJSON = respJSON.getJSONObject("carroCompras");
				ItemCarro c = new ItemCarro();
				c.setFechaCreacion(pJSON.getString("fechaCreacion"));
				c.setIdFactura(pJSON.getInt("idFactura"));
				c.setNombreAgencia(pJSON.getString("nombreAgencia"));
				c.setTotal(pJSON.getDouble("total"));
				listaCarros.add(c);

			} else {
				JSONArray carrosJSON = respJSON.getJSONArray("carroCompras");

				for (int i = 0; i < carrosJSON.length(); i++) {
					JSONObject carroJSON = carrosJSON.getJSONObject(i);
					ItemCarro c = new ItemCarro();
					c.setFechaCreacion(carroJSON.getString("fechaCreacion"));
					c.setIdFactura(carroJSON.getInt("idFactura"));
					c.setNombreAgencia(carroJSON.getString("nombreAgencia"));
					c.setTotal(carroJSON.getDouble("total"));
					listaCarros.add(c);
				}
			}
			return listaCarros;
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
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

	@Override
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

	@Override
	public int ConfirmaCompra(int idFactura, int idMedioDePago) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/confirmaCompra/"
				+ idFactura + "/" + idMedioDePago);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);

			return objJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAFacturaDAO <<ConfirmaCompra>>", ex);
			return 0;
		}

	}

}
