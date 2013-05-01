package pfm.android.compras;

import java.util.List;
import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import pfm.entidades.rest.ItemCarro;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CarrosCompras extends ListActivity {
	private List<ItemCarro> listaCarros = null;
	private Button btnCarro;
	private Button btnCarros;
	private TextView lblTitulo;
	private TextView lblAgencia;
	private int idAgencia;
	private int idUsuario;
	private int idFactura;
	private int idFacturaDetalle;
	private String nombreAgencia;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carros_compras);
		//Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		this.idAgencia = parametros.getInt("idAgencia");
		this.idFactura = parametros.getInt("idFactura");
		this.idUsuario = parametros.getInt("idUsuario");
		this.idFacturaDetalle = parametros.getInt("idFacturaDetalle");
		Log.i("CINIGUEZ","Id de Agencia: " + this.idAgencia);
		this.nombreAgencia = parametros.getString("nombreAgencia");

		// llama a tarea asincrona para rellenar el spinner
		new ListarCarrosTask(CarrosCompras.this).execute();

		//CONTROLES DE LA VISTA

		this.lblAgencia = (TextView) findViewById(R.id.lblAgencia_carros);
		this.lblAgencia.setText(this.nombreAgencia);

		this.lblTitulo = (TextView) findViewById(R.id.lblTitulo_carros);
		this.lblTitulo.setText("Mis Carros Pendientes");

		//btnCarro
		this.btnCarro = (Button) findViewById(R.id.btnCarro_carro);
		this.btnCarro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarro_onClick();

			}
		});

		//btnCarros
		this.btnCarros = (Button) findViewById(R.id.btnCarros_carro);
		this.btnCarros.setBackgroundColor(BIND_IMPORTANT);
		this.btnCarros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarros_onClick();

			}
		});

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final ItemCarro item = (ItemCarro) parent.getItemAtPosition(position);

				Intent actividad = new Intent(CarrosCompras.this, Compras.class);

				actividad.putExtra("idAgencia", CarrosCompras.this.idAgencia);
				actividad.putExtra("idFactura", item.getIdFactura());
				actividad.putExtra("idUsuario", CarrosCompras.this.idUsuario);
				actividad.putExtra("idFactura", CarrosCompras.this.idFactura);
				actividad.putExtra("nombreAgencia", CarrosCompras.this.nombreAgencia);
				startActivity(actividad);
			}
		});

	}

	/**
	 * Abrir la vista para Visualizar el carro de compras activo
	 * @author Carlos Iniguez
	 */
	public void btnCarro_onClick() {
		Intent actividad = new Intent(CarrosCompras.this, Compras.class);
		actividad.putExtra("idAgencia", this.idAgencia);
		actividad.putExtra("idFactura", this.idAgencia);
		actividad.putExtra("idUsuario", this.idUsuario);
		actividad.putExtra("idFactura", this.idFactura);
		actividad.putExtra("idFacturaDetalle", this.idFacturaDetalle);
		actividad.putExtra("nombreAgencia", this.nombreAgencia);

		startActivity(actividad);
	}

	/**
	 * Abrir la vista para Visualizar la lista de carros de compra del usuario.
	 * @author Carlos Iniguez
	 */
	public void btnCarros_onClick() {
		//TODO: Llamar Carros
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.compras, menu);
		return true;
	}

	@SuppressLint("NewApi")
	private class ListarCarrosTask extends AsyncTask<Void, Void, List<ItemCarro>> {
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
		protected List<ItemCarro> doInBackground(Void... params) {
			// obtiene la lista de Productos a traves del servicio REST
			listaCarros = JPADAOFactory.getFactory().getFacturaDAO().getListaCarros(CarrosCompras.this.idUsuario, CarrosCompras.this.idAgencia);
			if (listaCarros != null) {
				return listaCarros;
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(List<ItemCarro> lista) {
			super.onPostExecute(lista);

			//Seteamos el adaptador para llenar al ListView
			setListAdapter(new AdaptadorListaCarros(CarrosCompras.this, lista));
			pDialog.dismiss();

		}
	}

}
