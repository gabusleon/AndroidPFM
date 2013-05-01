package pfm.android.compras;

import java.util.List;

import pfm.android.R;
import pfm.entidades.rest.ItemProducto;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorListaProductos extends BaseAdapter {
	private final Activity actividad;
	private final List<ItemProducto> listaProductos;

	public AdaptadorListaProductos(Activity actividad, List<ItemProducto> listaProductos) {
		super();
		this.actividad = actividad;
		this.listaProductos = listaProductos;
	}

	public int getCount() {
		if (listaProductos != null)
			return listaProductos.size();
		else
			return 0;
	}

	public Object getItem(int arg0) {
		if (listaProductos != null)
			return listaProductos.get(arg0);
		else
			return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.producto_lista, null, true);

		TextView textView = (TextView) view.findViewById(R.id.lblItemNombre);
		textView.setText(listaProductos.get(position).getNombreProducto());

		TextView txtCantidad = (TextView) view.findViewById(R.id.lblItemCantidad);
		txtCantidad.setText("Cantidad: " + String.valueOf(listaProductos.get(position).getCantidad()));

		TextView txtSubtotal = (TextView) view.findViewById(R.id.lblItemSubtotal);
		txtSubtotal.setText(String.valueOf(listaProductos.get(position).getSubtotal()));

		TextView txtPrecio = (TextView) view.findViewById(R.id.lblItemPrecio);
		txtPrecio.setText("Valor Unitario: "+String.valueOf(listaProductos.get(position).getPrecio()));

		return view;

	}

}
