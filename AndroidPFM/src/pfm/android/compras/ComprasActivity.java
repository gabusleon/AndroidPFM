package pfm.android.compras;

import java.util.List;

import com.google.zxing.client.android.CaptureActivity;

import pfm.android.R;
import pfm.android.login.LogoutActivity;
import pfm.android.producto.EditProductoActivity;
import pfm.android.rest.RESTFactory;
import pfm.entidades.FacturaDetalle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ComprasActivity extends ListActivity {
	private List<FacturaDetalle> listaProductos = null;
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
		setContentView(R.layout.activity_compras);

		// Recepcion de Parametros
		Bundle extra = getIntent().getExtras();
		idAgencia = extra.getInt("idAgencia");
		nombreAgencia = extra.getString("nombreAgencia");
		idFactura = extra.getInt("idFactura");
		idCliente = extra.getInt("idCliente");

		// llama a tarea asincrona para rellenar el listado de productos si el
		// idFactura es mayor a 0
		if (idFactura > 0) {
			new ListarProductosTask(this).execute();
		}

		// CONTROLES DE LA VISTA
		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblTotal.setText("Total: " + 0);

		lblAgencia = (TextView) findViewById(R.id.lblAgencia);
		lblAgencia.setText(nombreAgencia);

		// Creacion de botones con sus listener
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

		// creacion de listas
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final FacturaDetalle item = (FacturaDetalle) parent
						.getItemAtPosition(position);
				Intent intento = new Intent(ComprasActivity.this,
						EditProductoActivity.class);

				intento.putExtra("idFacturaDetalle", item.getId());
				intento.putExtra("idAgencia", ComprasActivity.this.idAgencia);
				intento.putExtra("nombreAgencia",
						ComprasActivity.this.nombreAgencia);
				intento.putExtra("idFactura", ComprasActivity.this.idFactura);
				intento.putExtra("idCliente", ComprasActivity.this.idCliente);

				// inicia la actividad
				startActivity(intento);
				finish();
			}
		});

	}

	/**
	 * Abrir la vista para confirmar la compra
	 * 
	 * @author Gabus
	 */
	public void btnConfirmar_onClick() {
		// valida que exista prodictos en la lista de compras
		if (totalFactura != 0) {
			Intent intento = new Intent(this, ConfirmarComprasActivity.class);
			intento.putExtra("idAgencia", idAgencia);
			intento.putExtra("nombreAgencia", nombreAgencia);
			intento.putExtra("idFactura", idFactura);
			intento.putExtra("idCliente", idCliente);
			intento.putExtra("totalFactura", totalFactura);

			// inicia la actividad
			startActivity(intento);
			finish();
		} else {
			Toast.makeText(this, "No existe productos", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Abrir la vista para anadir nuevo producto
	 * 
	 * @author Gabus
	 */
	public void btnAddProducto_onClick() {
		// se abre la vista del lector de codigo QR de la libreria ZXing
		Intent intento = new Intent(this, CaptureActivity.class);
		intento.putExtra("idAgencia", this.idAgencia);
		intento.putExtra("nombreAgencia", this.nombreAgencia);
		intento.putExtra("idFactura", this.idFactura);
		intento.putExtra("idCliente", this.idCliente);

		// inicia la actividad
		startActivity(intento);
		finish();

	}

	public void btnCarro_onClick() {
		// se encuntra en el carro de compras por lo que el boton no debe
		// realizar ninguna accion
	}

	/**
	 * Abrir la vista para Visualizar la lista de carros de compra del usuario.
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnCarros_onClick() {
		Intent intento = new Intent(this, CarrosComprasActivity.class);
		intento.putExtra("idAgencia", idAgencia);
		intento.putExtra("nombreAgencia", nombreAgencia);
		intento.putExtra("idFactura", idFactura);
		intento.putExtra("idCliente", idCliente);

		// inicia la actividad
		startActivity(intento);
		finish();
	}

	/**
	 * Eliminado logico del carro de compras
	 * 
	 * @author Gabus
	 */
	public void btnEliminar_onClick() {
		new EliminarCarroTask(this).execute();
	}

	/**
	 * Tarea asincrona para eliminar el carro de compras
	 * 
	 * @author Gabus
	 * 
	 */
	private class EliminarCarroTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog pDialog;
		Context context;

		public EliminarCarroTask(Context context) {
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
			// valida si existe un carro de compras
			if (idFactura > 0) {
				// devuelve el id de la factura eliminada
				return new RESTFactory().getFacturaDAO().EliminarFactura(
						idFactura);
			} else {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result > 0) {
				Toast.makeText(context, "Carro de compras eliminado",
						Toast.LENGTH_SHORT).show();
				Intent intento = new Intent(context, ComprasActivity.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", 0);
				intento.putExtra("idCliente", idCliente);

				// inicia la actividad
				startActivity(intento);
				finish();
			} else if (result == -1) {
				Toast.makeText(context, "No existe carro de compras",
						Toast.LENGTH_SHORT).show();
			} else if (result == 0) {
				Toast.makeText(context, "Existe un error en el servidor",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}

	/**
	 * Tarea asincrona para listar los productos del carro de compras
	 * 
	 * @author Carlos Iniguez
	 * 
	 */
	private class ListarProductosTask extends
			AsyncTask<Void, Integer, List<FacturaDetalle>> {
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
		protected List<FacturaDetalle> doInBackground(Void... params) {
			// obtiene la lista de Productos a traves del servicio REST
			listaProductos = new RESTFactory().getFacturaDetalleDAO()
					.getCarroActual(idFactura);

			if (listaProductos != null) {
				return listaProductos;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(List<FacturaDetalle> lista) {
			super.onPostExecute(lista);
			if (lista != null) {
				lblTotal.setText("Total: "
						+ lista.get(0).getFactura().getTotal());
				totalFactura = lista.get(0).getFactura().getTotal();
			} else {
				lblTotal.setText("Total: " + 0);
				totalFactura = 0;
			}
			// Seteamos el adaptador para llenar al ListView
			setListAdapter(new AdaptadorListaProductos(ComprasActivity.this,
					lista));
			pDialog.dismiss();

		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.carros_compras, menu);
		return true;

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case R.id.menu_logout:
			Intent intento = new Intent(this, LogoutActivity.class);
			intento.putExtra("idAgencia", idAgencia);
			intento.putExtra("nombreAgencia", nombreAgencia);
			intento.putExtra("idFactura", idFactura);
			intento.putExtra("idCliente", idCliente);

			// inicia la actividad
			startActivity(intento);
			finish();
			break;
		}
		return true;
	}
}
