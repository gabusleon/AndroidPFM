package pfm.android.jpa;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import pfm.android.dao.UsuarioDAO;
import pfm.entidades.Usuario;

public class JPAUsuarioDAO extends JPAGenericDAO<Usuario, Integer> implements
		UsuarioDAO {

	public JPAUsuarioDAO() {
		super(Usuario.class, "autenticacion");
	}

	@Override
	public String login(String username, String password) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/login/" + username
				+ "/" + password);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject respJSON = new JSONObject(respStr);

			return (String) respJSON.get("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAUsuarioDAO <<login>>", ex);
			return "error";
		}
	}

}
