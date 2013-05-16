package pfm.android.compras;

import java.util.List;

import pfm.android.R;
import pfm.android.rest.RESTFactory;
import pfm.entidades.Factura;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CarrosComprasActivity extends ListActivity {
	private List<Factura> listaCarros = null;
	private Button btnCarro;
	private Button btnCarros;
	private TextView lblTitulo;
	private TextView lblAgencia;
	private int idAgencia;
	private int idCliente;
	private int idFactura;
	private String nombreAgencia;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carros_compras);
		// Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		idAgencia = parametros.getInt("idAgencia");
		idFactura = parametros.getInt("idFactura");
		idCliente = parametros.getInt("idCliente");
		nombreAgencia = parametros.getString("nombreAgencia");

		// llama a tarea asincrona para rellenar la lista de carros
		new ListarCarrosTask(this).execute();

		// CONTROLES DE LA VISTA

		lblAgencia = (TextView) findViewById(R.id.lblAgencia_carros);
		lblAgencia.setText(nombreAgencia);

		lblTitulo = (TextView) findViewById(R.id.lblTitulo_carros);
		lblTitulo.setText("Mis Carros Pendientes");

		// btnCarro
		btnCarro = (Button) findViewById(R.id.btnCarro_carro);
		btnCarro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarro_onClick();

			}
		});

		// btnCarros
		btnCarros = (Button) findViewById(R.id.btnCarros_carro);
		btnCarros.setBackgroundColor(BIND_IMPORTANT);
		btnCarros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarros_onClick();

			}
		});

		// lista de carros
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Factura item = (Factura) parent
						.getItemAtPosition(position);

				Intent intento = new Intent(CarrosComprasActivity.this,
						ComprasActivity.class);
				intento.putExtra("idAgencia",
						CarrosComprasActivity.this.idAgencia);
				intento.putExtra("nombreAgencia",
						CarrosComprasActivity.this.nombreAgencia);
				intento.putExtra("idFactura", item.getId());
				intento.putExtra("idCliente",
						CarrosComprasActivity.this.idCliente);

				// inicia la actividad
				startActivity(intento);
				finish();
			}
		});

	}

	/**
	 * Abrir la vista para Visualizar el carro de compras activo
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnCarro_onClick() {
		Intent intento = new Intent(this, ComprasActivity.class);
		intento.putExtra("idAgencia", idAgencia);
		intento.putExtra("nombreAgencia", nombreAgencia);
		intento.putExtra("idFactura", idFactura);
		intento.putExtra("idCliente", idCliente);

		// inicia la actividad
		startActivity(intento);
		finish();
	}

	/**
	 * Abrir la vista para Visualizar la lista de carros de compra del usuario.
	 * 
	 * @author Carlos Iniguez
	 */
	public void btnCarros_onClick() {
		// se encuntra en el listado de carros de compras por lo que el boton no
		// debe realizar ninguna accion
	}

	private class ListarCarrosTask extends AsyncTask<Void, Void, List<Factura>> {
		ProgressDialog pDialog;
		Context context;

		public ListarCarrosTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Cargando Carros de Compra...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected List<Factura> doInBackground(Void... params) {
			// obtiene la lista de Productos a traves del servicio REST
			listaCarros = new RESTFactory().getFacturaDAO().getCarrosCompra(
					idCliente, idAgencia);
			if (listaCarros != null) {
				return listaCarros;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(List<Factura> lista) {
			super.onPostExecute(lista);

			// Seteamos el adaptador para llenar al ListView
			setListAdapter(new AdaptadorListaCarros(CarrosComprasActivity.this,
					lista));
			pDialog.dismiss();

		}
	}

	public void cancelar() {
		Toast.makeText(this, "Compras pendientes canceladas",
				Toast.LENGTH_SHORT).show();
		Intent intento = new Intent(this, ComprasActivity.class);
		intento.putExtra("idAgencia", idAgencia);
		intento.putExtra("nombreAgencia", nombreAgencia);
		intento.putExtra("idFactura", idFactura);
		intento.putExtra("idCliente", idCliente);

		// inicia la actividad
		startActivity(intento);
		finish();
	}

	@Override
	public void onBackPressed() {
		cancelar();
	}
	
}
