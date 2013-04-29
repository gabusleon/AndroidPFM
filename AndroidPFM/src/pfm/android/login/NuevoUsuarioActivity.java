package pfm.android.login;

import java.text.SimpleDateFormat;
import java.util.Date;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NuevoUsuarioActivity extends Activity {
	private EditText nombres;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText fechaNacimiento;
	private EditText email;
	private EditText username;
	private EditText password;
	private String fc2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_usuario);

		// recibe parametros
		nombres = (EditText) findViewById(R.id.usuario_nombres);
		apellidos = (EditText) findViewById(R.id.usuario_apellidos);
		direccion = (EditText) findViewById(R.id.usuario_direccion);
		telefono = (EditText) findViewById(R.id.usuario_telefono);
		fechaNacimiento = (EditText) findViewById(R.id.usuario_fechaNacimiento);
		email = (EditText) findViewById(R.id.usuario_email);
		username = (EditText) findViewById(R.id.usuario_username);
		password = (EditText) findViewById(R.id.usuario_password);
		// recibe botones
		ImageButton btn_aceptar = (ImageButton) findViewById(R.id.btn_nuevo_usuario_aceptar);
		ImageButton btn_cancelar = (ImageButton) findViewById(R.id.btn_nuevo_usuario_cancelar);

		btn_aceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nuevoUsuario();
			}
		});

		btn_cancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelar();
			}
		});
	}

	private class RegistroTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog pDialog;
		Context context;

		public RegistroTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Registrando...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Boolean doInBackground(Void... params) {

			String[] atributos = { "nombres", "apellidos", "direccion",
					"telefono", "fechaNacimiento", "email", "username",
					"password" };
			String[] values = { nombres.getText().toString(),
					apellidos.getText().toString(),
					direccion.getText().toString(),
					telefono.getText().toString(), fc2,
					email.getText().toString(), username.getText().toString(),
					password.getText().toString() };
			return JPADAOFactory.getFactory().getUsuarioDAO()
					.create(atributos, values);

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Intent intento = new Intent();
				intento.setData(Uri.parse("Registro Correcto"));
				setResult(RESULT_OK, intento);
				finish();

			} else {
				Toast.makeText(context, "Registro incorrecto",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	@SuppressLint("SimpleDateFormat")
	public void nuevoUsuario() {
		try {

			if (!nombres.getText().toString().isEmpty()
					&& !apellidos.getText().toString().isEmpty()
					&& !direccion.getText().toString().isEmpty()
					&& !telefono.getText().toString().isEmpty()
					&& !fechaNacimiento.getText().toString().isEmpty()
					&& !email.getText().toString().isEmpty()
					&& !username.getText().toString().isEmpty()
					&& !password.getText().toString().isEmpty()) {
				SimpleDateFormat formatoFecha1 = new SimpleDateFormat(
						"yyyy-MM-dd");
				Date fc1 = null;
				fc1 = formatoFecha1.parse(fechaNacimiento.getText().toString());
				SimpleDateFormat formatoFecha2 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				fc2 = formatoFecha2.format(fc1);

				new RegistroTask(this).execute();
			} else {
				Toast.makeText(this, "No ha ingresado los datos necesarios",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception ex) {
			Log.e("Error", "doInBackground", ex);
			Toast.makeText(this, "Formato de fecha incorrecto: (yyyy-MM-dd)",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void cancelar() {
		Intent intento = new Intent();
		intento.setData(Uri.parse("Registro Cancelado"));
		setResult(RESULT_CANCELED, intento);
		super.onBackPressed();
	}

	@Override
	public void onBackPressed() {
		cancelar();
	}
}
