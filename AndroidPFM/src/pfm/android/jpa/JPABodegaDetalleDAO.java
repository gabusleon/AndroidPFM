package pfm.android.jpa;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;
import pfm.android.dao.BodegaDetalleDAO;
import pfm.entidades.Agencia;
import pfm.entidades.Bodega;
import pfm.entidades.BodegaDetalle;
import pfm.entidades.Categoria;
import pfm.entidades.Empresa;
import pfm.entidades.Marca;
import pfm.entidades.Producto;

public class JPABodegaDetalleDAO extends JPAGenericDAO<BodegaDetalle, Integer>
		implements BodegaDetalleDAO {

	public JPABodegaDetalleDAO() {
		super(BodegaDetalle.class, "bodegaDetalle");
	}

	@Override
	public BodegaDetalle getBodegaDetalleById(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet(uri + urlREST + "/getBodegaDetalleById/" + id);
		del.setHeader("content-type", "application/json");

		try {
			HttpResponse resp = httpClient.execute(del);
			String respStr = EntityUtils.toString(resp.getEntity());
			
			JSONObject objJSON = new JSONObject(respStr);

			BodegaDetalle bodegaDetalle = new BodegaDetalle();
			bodegaDetalle.setId(objJSON.getInt("id"));
			bodegaDetalle.setCantidad(objJSON.getInt("cantidad"));
			bodegaDetalle.setPrecio(objJSON.getDouble("precio"));
			bodegaDetalle.setEliminado(objJSON.getBoolean("eliminado"));
			
			JSONObject bod = objJSON.getJSONObject("bodega");
			JSONObject pro = objJSON.getJSONObject("producto");

			// genera la entidad bodega
			Bodega bodega = new Bodega();
			bodega.setDireccion(bod.getString("direccion"));
			bodega.setEliminado(bod.getBoolean("eliminado"));
			bodega.setId(bod.getInt("id"));
			bodega.setNombre(bod.getString("nombre"));
			bodega.setTelefono(bod.getString("telefono"));

			// genera la entidad agencia para agregarla a la bodega	
			JSONObject age = bod.getJSONObject("agencia");
			Agencia agencia = new Agencia();
			agencia.setDireccion(age.getString("direccion"));
			agencia.setEliminado(age.getBoolean("eliminado"));
			agencia.setId(age.getInt("id"));
			agencia.setNombre(age.getString("nombre"));
			agencia.setTelefono(age.getString("telefono"));

			// genera la entidad empresa para agregarla a la agencia			
			JSONObject emp = age.getJSONObject("empresa");
			Empresa empresa = new Empresa();
			empresa.setDireccion(emp.getString("direccion"));
			empresa.setEliminado(emp.getBoolean("eliminado"));
			empresa.setId(emp.getInt("id"));
			empresa.setIva(emp.getDouble("iva"));
			empresa.setRazonSocial(emp.getString("razonSocial"));
			empresa.setRuc(emp.getString("ruc"));
			empresa.setTelefono(emp.getString("telefono"));

			// agrega la empresa a la agencia
			agencia.setEmpresa(empresa);
			// agrega la agencia a la bodega
			bodega.setAgencia(agencia);
			// agrega la bodega a bodegaDetalle
			bodegaDetalle.setBodega(bodega);

			// genera la entidad producto
			Producto producto = new Producto();
			producto.setEliminado(pro.getBoolean("eliminado"));
			producto.setId(pro.getInt("id"));
			producto.setNombre(pro.getString("nombre"));

			// genera la entidad categora para agregarla al producto			
			JSONObject cat = pro.getJSONObject("categoria");
			Categoria categoria = new Categoria();
			categoria.setEliminado(cat.getBoolean("eliminado"));
			categoria.setId(cat.getInt("id"));
			categoria.setNombre("nombre");

			// genera la entidad marca para agregara al producto			
			JSONObject mar = pro.getJSONObject("marca");
			Marca marca = new Marca();
			marca.setEliminado(mar.getBoolean("eliminado"));
			marca.setId(mar.getInt("id"));
			marca.setNombre(mar.getString("nombre"));

			// agrega la categoria al producto
			producto.setCategoria(categoria);
			// agrega la marca al producto
			producto.setMarca(marca);
			// agrega el producto a bodegaDetalle
			bodegaDetalle.setProducto(producto);

			return bodegaDetalle;

		} catch (Exception ex) {
			Log.e("Error", "JPABodegaDetalleDAO <<getBodegaDetalleById>>", ex);
			return null;
		}

	}

}
