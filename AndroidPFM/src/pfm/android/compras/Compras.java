package pfm.android.compras;

import java.util.List;
import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import pfm.android.producto.AddProductoActivity;
import pfm.entidades.Agencia;
import pfm.entidades.Factura;
import pfm.entidades.Usuario;
import pfm.entidades.rest.ItemProducto;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Compras extends ListActivity {
	private List<ItemProducto> listaProductos = null;
	private Button btnConfirmar;
	private Button btnAgregarProducto;
	private Button btnCarro;
	private Button btnCarros;
	private Button btnEliminar;
	private TextView lblTotal;
	private TextView lblTitulo;
	private TextView lblAgencia;

	private Agencia agencia;
	private Usuario usuario;
	private Factura factura;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compras);
		
		//Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		this.agencia = (Agencia) parametros.getParcelable("agencia");
		this.factura = (Factura) parametros.getParcelable("factura");
		this.usuario = (Usuario) parametros.getParcelable("usuario");

		/*
		Agencia agencia = new Agencia(1, "Agencia Carlos", "Norte", "2341132", false);
		SessionData.setAgencia(agencia);
		Factura factura = new Factura();
		SessionData.setFactura(factura);
		Usuario usuario = new Usuario();
		usuario.setId(3);
		usuario.setNombres("Carlos");
		SessionData.setUsuario(usuario);
		*/

		// llama a tarea asincrona para rellenar el spinner
		new ListarProductosTask(Compras.this).execute();

		//CONTROLES DE LA VISTA
		this.lblTotal = (TextView) findViewById(R.id.lblTotal);
		this.lblTotal.setText("Total: " + 0);

		this.lblAgencia = (TextView) findViewById(R.id.lblAgencia);
		this.lblAgencia.setText(agencia.getNombre());

		this.lblTitulo = (TextView) findViewById(R.id.lblTitulo);
		this.lblTitulo.setText("Carro de Compras");

		//btnAddProducto
		this.btnAgregarProducto = (Button) findViewById(R.id.btnAddProducto);
		this.btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnAddProducto_onClick();
			}
		});

		//btnConfirmar
		this.btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
		this.btnConfirmar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnConfirmar_onClick();
			}
		});

		//btnCarro
		this.btnCarro = (Button) findViewById(R.id.btnCarro);
		this.btnCarro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarro_onClick();

			}
		});

		//btnCarros
		this.btnCarros = (Button) findViewById(R.id.btnCarros);
		this.btnCarros.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCarros_onClick();

			}
		});
		//btnEliminar
		this.btnEliminar = (Button) findViewById(R.id.btnEliminarCarro);
		this.btnEliminar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

	}

	public void btnConfirmar_onClick() {
		/*Intent actividad = new Intent(Compras.this, CarrosCompras.class);
		actividad.putExtra("agencia", this.agencia);
		actividad.putExtra("factura", this.factura);
		actividad.putExtra("usuario", this.usuario);
		startActivity(actividad);
		*/
		Log.i("CINIGUEZ", "Boton CONFIRMAR");
		//TODO: Confirmar Carro de Compras
	}

	/**
	 * Abrir la vista para Agregar Producto
	 * @author Carlos Iniguez
	 */
	public void btnAddProducto_onClick() {
		Intent actividad = new Intent(this, AddProductoActivity.class);
		startActivity(actividad);
	}

	/**
	 * Abrir la vista para Visualizar el carro de compras activo
	 * @author Carlos Iniguez
	 */
	public void btnCarro_onClick() {
		//TODO: Llamar a forma Carro
	}

	/**
	 * Abrir la vista para Visualizar la lista de carros de compra del usuario.
	 * @author Carlos Iniguez
	 */
	public void btnCarros_onClick() {
		Intent actividad = new Intent(Compras.this, CarrosCompras.class);
		actividad.putExtra("agencia", this.agencia);
		actividad.putExtra("factura", this.factura);
		actividad.putExtra("usuario", this.usuario);
		startActivity(actividad);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.compras, menu);
		return true;
	}

	@SuppressLint("NewApi")
	private class ListarProductosTask extends AsyncTask<Void, Integer, List<ItemProducto>> {
		ProgressDialog pDialog;
		Context context;

		public ListarProductosTask(Context context) {
			this.context = context;
		}

		@SuppressLint("NewApi")
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
			listaProductos = JPADAOFactory.getFactory().getFacturaDAO().getCarroActual(factura.getId());
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

			} else {
				Compras.this.lblTotal.setText("Total: " + 0);
			}
			//Seteamos el adaptador para llenar al ListView
			setListAdapter(new AdaptadorListaProductos(Compras.this, lista));
			pDialog.dismiss();

		}
	}

}