package pfm.android.rest;

public class RESTFactory {

	public UsuarioREST getUsuarioDAO() {
		return new UsuarioREST();
	}

	public AgenciaREST getAgenciaDAO() {
		return new AgenciaREST();
	}

	public BodegaDetalleREST getBodegaDetalleDAO() {
		return new BodegaDetalleREST();
	}

	public DescuentoREST getDescuentoDAO() {
		return new DescuentoREST();
	}

	public FacturaDetalleREST getFacturaDetalleDAO() {
		return new FacturaDetalleREST();
	}

	public FacturaREST getFacturaDAO() {
		return new FacturaREST();
	}

	public MedioPagoREST getMedioPagoDAO() {
		return new MedioPagoREST();
	}

}
