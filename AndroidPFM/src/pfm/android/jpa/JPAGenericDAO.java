package pfm.android.jpa;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import pfm.android.dao.GenericDAO;

public class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
	private Class<T> claseREST;
	private String uri;
	private Client client;
	private WebResource wr;

	public JPAGenericDAO(Class<T> claseREST, String urlREST) {
		this.uri = "http://localhost:8080/PFM/rest/" + urlREST;
		this.client = Client.create(new DefaultClientConfig());
		this.wr = this.client.resource(UriBuilder.fromUri(this.uri).build());
		this.claseREST = claseREST;
	}

	@Override
	public boolean create(T entity) {
		try {
			ClientResponse response = wr.path("/create")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, entity);
			if (response.getStatus() == 200)
				return true;
			else
				return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage().toString());
			return false;
		}

	}

	@Override
	public T readXML(ID id) {
		try {
			wr = wr.path("query/" + id.toString());
			T responseUser = wr.accept(MediaType.APPLICATION_XML)
					.get(claseREST);
			return responseUser;
		} catch (Exception ex) {
			System.out.println(ex.getMessage().toString());
			return null;
		}
	}

	@Override
	public boolean update(T entity) {
		try {
			wr = wr.path("update");
			ClientResponse response = wr.type(MediaType.APPLICATION_XML).put(
					ClientResponse.class, entity);
			if (response.getStatus() == 200)
				return true;
			else
				return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage().toString());
			return false;
		}
	}

	@Override
	public boolean deleteById(ID id) {
		try {
			ClientResponse response = wr.path("delete").path(id.toString())
					.delete(ClientResponse.class);
			int codigoRespuesta = Integer.parseInt(response
					.getEntity(String.class));
			if (response.getStatus() == 200 && codigoRespuesta != -1)
				return true;
			else

				return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage().toString());
			return false;
		}
	}

	public Class<T> getClaseREST() {
		return claseREST;
	}

	public void setClaseREST(Class<T> claseREST) {
		this.claseREST = claseREST;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public WebResource getWr() {
		return wr;
	}

	public void setWr(WebResource wr) {
		this.wr = wr;
	}

}
