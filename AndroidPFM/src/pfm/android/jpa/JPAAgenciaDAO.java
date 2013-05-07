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
import pfm.android.dao.AgenciaDAO;
import pfm.entidades.Agencia;

public class JPAAgenciaDAO extends JPAGenericDAO<Agencia, Integer> implements
		AgenciaDAO {

	public JPAAgenciaDAO() {
		super(Agencia.class, "agencia");

	}

	@SuppressLint("UseSparseArrays")
	@Override
	public Map<Integer, String> listAgencias() {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/listAgencias");
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			Map<Integer, String> lista = new HashMap<Integer, String>();
			JSONArray j = objJSON.optJSONArray("agencia");
			if (j != null) {
				JSONArray arrJSON = objJSON.getJSONArray("agencia");

				for (int i = 0; i < arrJSON.length(); i++) {
					JSONObject obj = arrJSON.getJSONObject(i);
					int key = obj.getInt("id");
					String value = obj.getString("nombre");
					lista.put(key, value);
				}
			} else {
				JSONObject obj = objJSON.getJSONObject("agencia");
				lista.put(obj.getInt("id"), obj.getString("nombre"));
			}
			return lista;
		} catch (Exception ex) {
			Log.e("Error", "JPAAgenciaDAO <<listAgencias>>", ex);
			return null;
		}
	}

}
