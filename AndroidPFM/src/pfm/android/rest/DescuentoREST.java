package pfm.android.rest;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;
import pfm.entidades.Descuento;

public class DescuentoREST extends GenericREST {

	public DescuentoREST() {
		super("descuento");

	}

	/**
	 * Realiza el mapeo del objeto JSON a la entidad Descuento
	 * 
	 * @param objJSON
	 * @return Descuento
	 */
	public Descuento getJSONParserDescuento(JSONObject objJSON) {
		try {
			// mapea la entidad descuento a partir del JSON
			Descuento descuento = new Descuento();
			descuento.setEliminado(objJSON.getBoolean("eliminado"));
			descuento.setFechaFin((Date) objJSON.get("fechaFin"));
			descuento.setFechaInicio((Date) objJSON.get("fechaFin"));
			descuento.setId(objJSON.getInt("id"));
			descuento.setNombre(objJSON.getString("nombre"));
			descuento.setValor(objJSON.getDouble("valor"));

			return descuento;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtiene el Descuento enviando como parametro el producto
	 * 
	 * @param idProducto
	 * @return Descuento
	 */
	public Descuento getDescuentoByProducto(int idProducto) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/getDescuentoByProducto/"
				+ idProducto);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			Descuento descuento = new Descuento();
			descuento = getJSONParserDescuento(objJSON);
			return descuento;
		} catch (Exception ex) {
			Log.e("Error", "JPADescuentoDAO <<getDescuentoByProducto>>", ex);
			return null;
		}
	}
}