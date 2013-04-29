package pfm.android.dao;

import java.util.Map;

import pfm.entidades.Agencia;

public interface AgenciaDAO extends GenericDAO<Agencia, Integer> {

	public Map<Integer, String> listAgencias();

}
