package pfm.android.dao;

public interface GenericDAO<T, ID> {
	public boolean create(T entity);

	public T readXML(ID id);

	public boolean update(T entity);

	public boolean deleteById(ID id);

}
