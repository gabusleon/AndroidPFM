package pfm.android.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

import pfm.android.R;
import pfm.android.compras.ComprasActivity;
import pfm.android.rest.RESTFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class MainActivity extends Activity {

	private EditText username;
	private EditText password;
	private Spinner agencia;
	private Map<Integer, String> mapaAgencias = new HashMap<Integer, String>();
	private List<String> listaAgencias = new ArrayList<String>();
	private final int REQUEST_ACTIVITY = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// llama a tarea asincrona para rellenar el spinner de agencuias
		new ListaAgenciasTask(MainActivity.this).execute();

		// obtiene los parametros de username y password
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		// obtiene los botones
		Button btnAceptar = (Button) findViewById(R.id.btn_aceptar);
		Button btnNuevoUsuario = (Button) findViewById(R.id.btn_nuevo_usuario);

		// agrega los listener de los botones
		btnAceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login();

			}
		});

		btnNuevoUsuario.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nuevoUsuario();
			}
		});

	}

	/**
	 * Tarea asincrona para llenar el spinner con el listado de agencias
	 * 
	 * @author Gabus
	 * 
	 */
	private class ListaAgenciasTask extends
			AsyncTask<Void, Void, Map<Integer, String>> {
		ProgressDialog pDialog;
		Context context;

		public ListaAgenciasTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Cargando Agencias...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Map<Integer, String> doInBackground(Void... params) {
			// obtiene el id, nombre de las agencias a traves del servicio REST
			mapaAgencias = new RESTFactory().getAgenciaDAO().listAgencias();
			return mapaAgencias;
		}

		@Override
		protected void onPostExecute(Map<Integer, String> result) {
			super.onPostExecute(result);
			// Genera el spinner a partir de las agencias obtenidas
			if (result != null) {
				agencia = (Spinner) findViewById(R.id.agencias);

				for (String value : result.values()) {
					listaAgencias.add(value);
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						context, android.R.layout.simple_spinner_dropdown_item,
						listaAgencias);

				agencia.setAdapter(adapter);
			} else {
				Toast.makeText(context, "Existe un error en el servidor",
						Toast.LENGTH_SHORT).show();

			}
			pDialog.dismiss();

		}
	}

	/**
	 * Tarea asincrona para comprobar el username y password ingresados
	 * 
	 * @author Gabus
	 * 
	 */
	private class LoginTask extends AsyncTask<Void, Void, Integer> {
		ProgressDialog pDialog;
		Context context;
		int idAgencia;
		String nombreAgencia;

		public LoginTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Comprobando Credenciales...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// envia como resultado el id del usuario
			int id = new RESTFactory().getUsuarioDAO().login(
					username.getText().toString(),
					password.getText().toString());
			return id;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result != 0) {
				// se ha logeado correctamente

				// obtiene el id y nombre de la agencia seleccionada
				Iterator<Map.Entry<Integer, String>> entries = mapaAgencias
						.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<Integer, String> entry = entries.next();
					if (entry.getValue().equals(
							agencia.getSelectedItem().toString())) {
						idAgencia = entry.getKey();
						nombreAgencia = entry.getValue();
					}
				}

				Toast.makeText(context,
						"Bienvenido: " + username.getText().toString(),
						Toast.LENGTH_SHORT).show();

				Intent intento = new Intent(context, ComprasActivity.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", 0);
				intento.putExtra("idCliente", result);

				// inicia la actividad
				startActivity(intento);
				finish();
			} else {
				// si existe un error en el servidor o los datos son erroneos
				Toast.makeText(context, "Inicio de sesion incorrecto",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();

		}
	}

	public void login() {

		// validar que ingresa los datos necesarios
		if (!password.getText().toString().isEmpty()
				&& !username.getText().toString().isEmpty() && agencia != null) {

			new LoginTask(this).execute();

		} else {
			Toast.makeText(this, "No ha ingresado los datos necesarios",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void nuevoUsuario() {

		// crea la nueva actividad espernado un resultado de la actividad creada
		Intent intento = new Intent(this, NuevoUsuarioActivity.class);
		startActivityForResult(intento, REQUEST_ACTIVITY);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// la respuesta de la actividad NuevoUsuarioActivity.class
		if (requestCode == REQUEST_ACTIVITY) {
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT)
						.show();

			} else if (resultCode == RESULT_OK) {
				Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
