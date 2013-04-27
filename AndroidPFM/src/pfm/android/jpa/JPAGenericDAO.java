package pfm.android.jpa;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import pfm.android.dao.GenericDAO;

public class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
	private Class<T> claseREST;
	String urlREST;
	String uri = "http://10.0.2.2:8080/PFM/rest/";

	public JPAGenericDAO(Class<T> claseREST, String urlREST) {
		this.claseREST = claseREST;
		this.urlREST = urlREST;

	}

	@Override
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

	@Override
	public String read(ID id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/read/" + id);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			return (String) respJSON.get("id");
		} catch (Exception ex) {
			Log.e("Error", "JPAGenericDAO <<read>>", ex);
			return "error";
		}
	}

	@Override
	public boolean update(String[] atributos, String[] values) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(uri + urlREST + "/update");
		put.setHeader("content-type", "application/json");
		try {

			JSONObject dato = new JSONObject();

			for (int i = 0; i < atributos.length; i++) {
				dato.put(atributos[i], values[i]);
			}

			StringEntity entity = new StringEntity(dato.toString());
			put.setEntity(entity);

			HttpResponse resp = httpClient.execute(put);
			String respStr = EntityUtils.toString(resp.getEntity());

			if (respStr.equals("1"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			Log.e("Error", "JPAGenericDAO <<update>>", ex);
			return false;
		}
	}

	@Override
	public boolean deleteById(ID id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete del = new HttpDelete(uri + urlREST + "/delete/" + id);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());

			if (respStr.equals("true"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			Log.e("Error", "JPAGenericDAO <<deleteById>>", ex);
			return false;
		}

	}

	public Class<T> getClaseREST() {
		return claseREST;
	}

	public void setClaseREST(Class<T> claseREST) {
		this.claseREST = claseREST;
	}

	public String getUrlREST() {
		return urlREST;
	}

	public void setUrlREST(String urlREST) {
		this.urlREST = urlREST;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
