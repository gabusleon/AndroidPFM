package pfm.android.compras;

import java.text.SimpleDateFormat;
import java.util.List;

import pfm.android.R;
import pfm.entidades.Factura;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorListaCarros extends BaseAdapter {
	private final Activity actividad;
	private final List<Factura> listaCarros;

	public AdaptadorListaCarros(Activity actividad, List<Factura> listaCarros) {
		super();
		this.actividad = actividad;
		this.listaCarros = listaCarros;
	}

	public int getCount() {
		if (listaCarros != null)
			return listaCarros.size();
		else
			return 0;
	}

	public Object getItem(int arg0) {
		if (listaCarros != null)
			return listaCarros.get(arg0);
		else
			return null;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("SimpleDateFormat")
	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.carro_lista, null, true);

		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatoFecha
				.format(listaCarros.get(position).getFecha());

		TextView textView = (TextView) view
				.findViewById(R.id.lblItemNombreCarro);
		textView.setText("Carro : " + fecha);

		TextView txtCantidad = (TextView) view
				.findViewById(R.id.lblItemAgencia);
		txtCantidad.setText("Agencia: "
				+ listaCarros.get(position).getAgencia().getNombre());

		TextView txtSubtotal = (TextView) view.findViewById(R.id.lblItemTotal);
		txtSubtotal.setText("Total: "
				+ String.valueOf(listaCarros.get(position).getTotal()));

		return view;

	}
}
