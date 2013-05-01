package pfm.android.compras;

import java.util.List;
import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import pfm.entidades.Agencia;
import pfm.entidades.Factura;
import pfm.entidades.Usuario;
import pfm.entidades.rest.ItemCarro;
import pfm.entidades.rest.SessionData;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarrosCompras extends ListActivity {
	private List<ItemCarro> listaCarros = null;
	private Button btnCarro;
	private Button btnCarros;
	private TextView lblTitulo;
	private TextView lblAgencia;
	@SuppressWarnings("unused")
	private Agencia agencia;
	@SuppressWarnings("unused")
	private Usuario usuario;
	@SuppressWarnings("unused")
	private Factura factura;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carros_compras);

		//Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		this.agencia = (Agencia) parametros.getParcelable("agencia");
		this.factura = (Factura) parametros.getParcelable("factura");
		this.usuario = (Usuario) parametros.getParcelable("usuario");

		// llama a tarea asincrona para rellenar el spinner
		new ListarCarrosTask(CarrosCompras.this).execute();

		//CONTROLES DE LA VISTA

		this.lblAgencia = (TextView) findViewById(R.id.lblAgencia_carros);
		this.lblAgencia.setText(SessionData.getAgencia().getNombre());

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

	}

	/**
	 * Abrir la vista para Visualizar el carro de compras activo
	 * @author Carlos Iniguez
	 */
	public void btnCarro_onClick() {
		Intent intento = new Intent(this, Compras.class);
		startActivity(intento);
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
			listaCarros = JPADAOFactory.getFactory().getFacturaDAO().getListaCarros(SessionData.getUsuario().getId(), SessionData.getAgencia().getId());
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
