package pfm.android.compras;

import java.util.List;

import com.google.zxing.client.android.CaptureActivity;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import pfm.android.producto.EditProductoActivity;
import pfm.entidades.rest.ItemProducto;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Compras extends ListActivity {
	private List<ItemProducto> listaProductos = null;
	private Button btnConfirmar;
	private Button btnAgregarProducto;
	private Button btnCarro;
	private Button btnCarros;
	private Button btnEliminar;
	private TextView lblTotal;
	private TextView lblAgencia;
	private int idAgencia;
	private int idCliente;
	private int idFactura;
	private double totalFactura;
	private String nombreAgencia = "FALTA";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compras);

		// Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		this.idAgencia = parametros.getInt("idAgencia");
		this.nombreAgencia = parametros.getString("nombreAgencia");
		this.idFactura = parametros.getInt("idFactura");
		this.idCliente = parametros.getInt("idCliente");

		// llama a tarea asincrona para rellenar el spinner
		new ListarProductosTask(Compras.this).execute();

		// CONTROLES DE LA VISTA
		this.lblTotal = (TextView) findViewById(R.id.lblTotal);
		this.lblTotal.setText("Total: " + 0);

		this.lblAgencia = (TextView) findViewById(R.id.lblAgencia);
		this.lblAgencia.setText(nombreAgencia);
		
		// btnAddProducto
		this.btnAgregarProducto = (Button) findViewById(R.id.btnAddProducto);
		this.btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnAddProducto_onClick();
			}
		});

		// btnConfirmar
		this.btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
		this.btnConfirmar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnConfirmar_onClick();
			}
		});

		// btnCarro
		this.btnCarro = (Button) findViewById(R.id.btnCarro);
		this.btnCarro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarro_onClick();

			}
		});

		// btnCarros
		this.btnCarros = (Button) findViewById(R.id.btnCarros);
		this.btnCarros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarros_onClick();

			}
		});
		// btnEliminar
		this.btnEliminar = (Button) findViewById(R.id.btnEliminarCarro);
		this.btnEliminar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnEliminar_onClick();
			}
		});

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final ItemProducto item = (ItemProducto) parent
						.getItemAtPosition(position);
				Intent actividad = new Intent(Compras.this,
						EditProductoActivity.class);

				actividad.putExtra("idFacturaDetalle",
						item.getIdFacturaDetalle());
				actividad.putExtra("idAgencia", Compras.this.idAgencia);
				actividad.putExtra("nombreAgencia", Compras.this.nombreAgencia);
				actividad.putExtra("idFactura", Compras.this.idFactura);
				actividad.putExtra("idCliente", Compras.this.idCliente);
				
				startActivity(actividad);
				finish();
			}
		});

	}

	public void btnConfirmar_onClick() {
		// se abre la vista de confirmar compras
		if (totalFactura != 0) {
			Intent intento = new Intent(this, MedioDePagoActivity.class);
			intento.putExtra("idAgencia", idAgencia);
			intento.putExtra("nombreAgencia", nombreAgencia);
			intento.putExtra("idFactura", idFactura);
			intento.putExtra("idCliente", idCliente);
			// se agregan nuevos parametros
			intento.putExtra("totalFactura", totalFactura);
			startActivity(intento);
			finish();
		} else {
			Toast.makeText(this, "No existe productos", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Abrir la vista para Agregar Producto
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnAddProducto_onClick() {
		// se abre la vista del lector de codigo qr ZXing
		Intent intento = new Intent(this, CaptureActivity.class);
		intento.putExtra("idAgencia", this.idAgencia);
		intento.putExtra("nombreAgencia", this.nombreAgencia);
		intento.putExtra("idFactura", this.idFactura);
		intento.putExtra("idCliente", this.idCliente);
		startActivity(intento);
		finish();

	}

	/**
	 * Abrir la vista para Visualizar el carro de compras activo
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnCarro_onClick() {
		// TODO: Llamar a forma Carro
	}

	/**
	 * Abrir la vista para Visualizar la lista de carros de compra del usuario.
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnCarros_onClick() {
		Intent actividad = new Intent(Compras.this, CarrosCompras.class);
		actividad.putExtra("idAgencia", this.idAgencia);
		actividad.putExtra("nombreAgencia", this.nombreAgencia);
		actividad.putExtra("idFactura", this.idFactura);
		actividad.putExtra("idCliente", this.idCliente);

		startActivity(actividad);
		finish();
	}

	private class EliminarFacturaTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog pDialog;
		Context context;

		public EliminarFacturaTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Eliminando Carro de Compras...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (idFactura != 0) {

				return JPADAOFactory.getFactory().getFacturaDAO()
						.EliminarFactura(idFactura);
			} else {
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result != 0) {
				Toast.makeText(context, "Carro de compras eliminado",
						Toast.LENGTH_SHORT).show();
				Intent intento = new Intent(context, Compras.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", 0);
				intento.putExtra("idCliente", idCliente);
				startActivity(intento);
				finish();
			} else {
				Toast.makeText(context, "No existe carro de compras",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}

	/**
	 * Elimina el carro de compras, seteando la factura a eliminado = true
	 */
	public void btnEliminar_onClick() {
		new EliminarFacturaTask(this).execute();
	}

	private class ListarProductosTask extends
			AsyncTask<Void, Integer, List<ItemProducto>> {
		ProgressDialog pDialog;
		Context context;

		public ListarProductosTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Cargando Productos...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected List<ItemProducto> doInBackground(Void... params) {
			// obtiene la lista de Productos a traves del servicio REST
			listaProductos = JPADAOFactory.getFactory().getFacturaDAO()
					.getCarroActual(Compras.this.idFactura);

			if (listaProductos != null) {
				return listaProductos;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(List<ItemProducto> lista) {
			super.onPostExecute(lista);
			if (lista != null) {
				Compras.this.lblTotal.setText("Total: "
						+ lista.get(0).getTotalFactura());
				totalFactura = lista.get(0).getTotalFactura();
			} else {
				Compras.this.lblTotal.setText("Total: " + 0);
				totalFactura = 0;
			}
			// Seteamos el adaptador para llenar al ListView
			setListAdapter(new AdaptadorListaProductos(Compras.this, lista));
			pDialog.dismiss();

		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
