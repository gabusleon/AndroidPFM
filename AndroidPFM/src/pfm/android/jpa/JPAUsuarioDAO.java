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

	/**
	 * Envia el username y password al servicio REST devuelve la entidad usuario
	 * y el metodo devuelve el id del usuario encontrado caso contrario, un
	 * valor 0
	 */
	@Override
	public int login(String username, String password) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(this.uri + this.urlREST + "/login/"
				+ username + "/" + password);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			JSONObject respJSON = new JSONObject(respStr);

			return respJSON.getInt("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAUsuarioDAO <<login>>", ex);
			return 0;
		}
	}

}
