package pfm.android.jpa;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import pfm.android.dao.MedioPagoDAO;
import pfm.entidades.MedioDePago;

public class JPAMedioPagoDAO extends JPAGenericDAO<MedioDePago, Integer>
		implements MedioPagoDAO {

	public JPAMedioPagoDAO() {
		super(MedioDePago.class, "medioDePago");
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public Map<Integer, String> listMedioPago() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/listMedioPago");
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			JSONArray arrJSON = objJSON.getJSONArray("medioDePago");
			Map<Integer, String> lista = new HashMap<Integer, String>();

			for (int i = 0; i < arrJSON.length(); i++) {
				JSONObject obj = arrJSON.getJSONObject(i);
				int key = obj.getInt("id");
				String value = obj.getString("nombre");
				lista.put(key, value);
			}

			return lista;
		} catch (Exception ex) {
			Log.e("Error", "JPAMedioPagoDAO <<listMedioPago>>", ex);
			return null;
		}

	}
}
