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

public class JPAFacturaDAO extends JPAGenericDAO<Factura, Integer> implements FacturaDAO {

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
			HttpGet del = new HttpGet(this.uri + this.urlREST + "/carro/" + idFactura);
			del.setHeader("content-type", "application/json");

			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONObject respJSON = new JSONObject(respStr);

				if (respStr.startsWith("{")) {
					JSONObject pJSON = respJSON.getJSONObject("itemProducto");
					ItemProducto p = new ItemProducto(pJSON.getInt("idBodegaDetalle"), pJSON.getString("nombreProducto"), pJSON.getDouble("subtotal"),
							pJSON.getInt("cantidad"), pJSON.getDouble("precio"), pJSON.getInt("idFacturaDetalle"));
					listaProductos.add(p);

				} else {

					JSONArray detallesJSON = respJSON.getJSONArray("itemProducto");
					for (int i = 0; i < detallesJSON.length(); i++) {
						JSONObject productoJSON = detallesJSON.getJSONObject(i);
						ItemProducto p = new ItemProducto(productoJSON.getInt("idBodegaDetalle"), productoJSON.getString("nombreProducto"),
								productoJSON.getDouble("subtotal"), productoJSON.getInt("cantidad"), productoJSON.getDouble("precio"),
								productoJSON.getInt("idFacturaDetalle"));
						listaProductos.add(p);
					}
				}

				return listaProductos;
			} catch (Exception ex) {
				Log.i("CINIGUEZ", " EXCEPTION : " + ex.getMessage().toString());
				return null;
			}
		}
	}

	@Override
	public List<ItemCarro> getListaCarros(int idUsuario, int idAgencia) {
		List<ItemCarro> listaCarros = new ArrayList<ItemCarro>();

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/carros/" + idUsuario + "/" + idAgencia);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject respJSON = new JSONObject(respStr);
			if (respStr.startsWith("{")) {
				JSONObject pJSON = respJSON.getJSONObject("itemProducto");
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

}
