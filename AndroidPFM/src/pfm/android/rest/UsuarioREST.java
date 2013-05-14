package pfm.android.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class UsuarioREST extends GenericREST {

	public UsuarioREST() {
		super("autenticacion");
	}

	/**
	 * Verifica el username y password ingresados en la aplicacion movil
	 * 
	 * @param username
	 * @param password
	 * @return idUsuario
	 */
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

	/**
	 * Creacion de nuevo usuario
	 * 
	 * @param atributos
	 * @param values
	 * @return boolean
	 */
	public boolean create(String[] atributos, String[] values) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri + urlREST + "/create");
		post.setHeader("content-type", "application/json");

		try {

			JSONObject dato = new JSONObject();
			for (int i = 0; i < atributos.length; i++) {
				dato.put(atributos[i], values[i]);
			}

			StringEntity entity = new StringEntity(dato.toString());
			post.setEntity(entity);
			HttpResponse resp = httpClient.execute(post);
			String respStr = EntityUtils.toString(resp.getEntity());

			if (respStr.equals("1"))
				return true;
			else
				return false;

		} catch (Exception ex) {
			Log.e("Error", "JPAGenericDAO <<create>>", ex);
			return false;
		}

	}

}
