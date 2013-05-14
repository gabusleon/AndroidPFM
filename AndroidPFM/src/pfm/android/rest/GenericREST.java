package pfm.android.rest;

public class GenericREST {
	protected String urlREST;
	protected String uri = "http://10.0.2.2:8080/PFM/rest/";

	public GenericREST(String urlREST) {
		this.urlREST = urlREST;
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
