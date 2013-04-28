package pfm.android.dao;

import pfm.entidades.Agencia;

public interface AgenciaDAO extends GenericDAO<Agencia, Integer> {

	public String[] listAgencias();

}
