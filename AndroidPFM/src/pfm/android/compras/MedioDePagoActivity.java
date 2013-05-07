package pfm.android.compras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class MedioDePagoActivity extends Activity {

	private int idAgencia;
	private String nombreAgencia;
	private int idFactura;
	private int idCliente;
	private double totalFactura;
	private double totalFacturaActual;
	private EditText total;
	private Spinner medioPago;
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> mapaMedioPago = new HashMap<Integer, String>();
	private List<String> listaMedioPagp = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medio_de_pago);

		// Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		this.idAgencia = parametros.getInt("idAgencia");
		this.nombreAgencia = parametros.getString("nombreAgencia");
		this.idFactura = parametros.getInt("idFactura");
		this.idCliente = parametros.getInt("idCliente");
		this.totalFactura = parametros.getDouble("totalFactura");

		new ListaMedioDePagosTask(this).execute();
		total = (EditText) findViewById(R.id.confirmarCompras_total);
		total.setText(String.valueOf(totalFactura));

		ImageButton btn_aceptar = (ImageButton) findViewById(R.id.btn_confirmarCompras_aceptar);
		ImageButton btn_cancelar = (ImageButton) findViewById(R.id.btn_confirmarCompras_cancelar);

		btn_aceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmar();

			}
		});

		btn_cancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelar();

			}
		});
	}

	private class ListaMedioDePagosTask extends
			AsyncTask<Void, Void, Map<Integer, String>> {
		ProgressDialog pDialog;
		Context context;

		public ListaMedioDePagosTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Cargando Medio de Pagos...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Map<Integer, String> doInBackground(Void... params) {
			// obtiene el id, nombre de las agencias a traves del servicio REST
			mapaMedioPago = JPADAOFactory.getFactory().getMedioPagoDAO()
					.listMedioPago();
			return mapaMedioPago;
		}

		@Override
		protected void onPostExecute(Map<Integer, String> result) {
			super.onPostExecute(result);
			// Genera el spinner a partir de las agencias obtenidas
			medioPago = (Spinner) findViewById(R.id.medioPago);

			for (String value : result.values()) {
				listaMedioPagp.add(value);
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item,
					listaMedioPagp);

			medioPago.setAdapter(adapter);
			pDialog.dismiss();

		}
	}

	private class ConfirmarTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog pDialog;
		Context context;

		public ConfirmarTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Confirmando Compra...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			totalFacturaActual = JPADAOFactory.getFactory().getFacturaDAO()
					.ConfirmaTotal(idFactura, totalFactura);
			if (totalFacturaActual == totalFactura) {
				// confirma compra
				int idMedioDePago = 0;
				Iterator<Map.Entry<Integer, String>> entries = mapaMedioPago
						.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<Integer, String> entry = entries.next();
					if (entry.getValue().equals(
							medioPago.getSelectedItem().toString())) {
						idMedioDePago = entry.getKey();
					}
				}

				int id = JPADAOFactory.getFactory().getFacturaDAO()
						.ConfirmaCompra(idFactura, idMedioDePago);
				if (id == 0) {
					// error en el servicio web
					return -1;
				} else {
					// compra confirmada
					return id;
				}

			} else {
				// cambio de valores volver a confirmar
				totalFactura = totalFacturaActual;
				return 0;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == -1) {
				Toast.makeText(context, "Error al momento de confirmar",
						Toast.LENGTH_SHORT).show();
			} else if (result == 0) {
				Toast.makeText(context,
						"El total ha cambiado, porfavor vuelva a confirmar",
						Toast.LENGTH_SHORT).show();
				total.setText(String.valueOf(totalFacturaActual));

			} else if (result > 0) {
				Toast.makeText(context,
						"Compra confirmada, acercarse a la caja mas cercana",
						Toast.LENGTH_SHORT).show();
				Intent intento = new Intent(context, Compras.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", 0);
				intento.putExtra("idCliente", idCliente);
				startActivity(intento);
				finish();
			}
			pDialog.dismiss();
		}
	}

	public void confirmar() {
		new ConfirmarTask(this).execute();
	}

	public void cancelar() {
		Toast.makeText(this, "Pago cancelado", Toast.LENGTH_SHORT).show();
		Intent intento = new Intent(this, Compras.class);
		intento.putExtra("idAgencia", idAgencia);
		intento.putExtra("nombreAgencia", nombreAgencia);
		intento.putExtra("idFactura", idFactura);
		intento.putExtra("idCliente", idCliente);
		startActivity(intento);
		finish();
	}

	@Override
	public void onBackPressed() {
		cancelar();
	}
}
