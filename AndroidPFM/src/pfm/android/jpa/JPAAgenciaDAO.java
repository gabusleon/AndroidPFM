package pfm.android.jpa;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import pfm.android.dao.AgenciaDAO;
import pfm.entidades.Agencia;

public class JPAAgenciaDAO extends JPAGenericDAO<Agencia, Integer> implements
		AgenciaDAO {

	public JPAAgenciaDAO() {
		super(Agencia.class, "autenticacion");

	}

	@Override
	public String[] listAgencias() {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/listAgencias");
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject objJSON = new JSONObject(respStr);
			JSONArray arrJSON = objJSON.getJSONArray("agencia");
			String[] agencias = new String[arrJSON.length()];

			for (int i = 0; i < arrJSON.length(); i++) {
				JSONObject obj = arrJSON.getJSONObject(i);
				String nombre = obj.getString("nombre");

				agencias[i] = nombre;
			}

			return agencias;
		} catch (Exception ex) {
			Log.e("Error", "JPAAgenciaDAO <<listAgencias>>", ex);
			return null;
		}
	}

}
