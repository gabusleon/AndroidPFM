package pfm.android.dao;

public interface GenericDAO<T, ID> {
	public boolean create(String[] atributos, String[] values);

	public int read(ID id);

	public boolean update(String[] atributos, String[] values);

	public boolean deleteById(ID id);

}
