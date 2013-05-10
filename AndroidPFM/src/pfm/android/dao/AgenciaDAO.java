package pfm.android.dao;

import java.util.Map;

import org.json.JSONObject;

import pfm.entidades.Agencia;

public interface AgenciaDAO extends GenericDAO<Agencia, Integer> {

	/**
	 * Realiza el mapeo del objeto JSON a la entidad Agencia
	 * 
	 * @param objJSON
	 * @return agencia
	 */
	public Agencia getJSONParserAgencia(JSONObject objJSON);

	/**
	 * Listado de agencias
	 * 
	 * @return mapa de agencias
	 */
	public Map<Integer, String> listAgencias();

}
