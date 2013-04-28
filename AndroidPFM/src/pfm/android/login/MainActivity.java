package pfm.android.login;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText username;
	private EditText password;
	private final int REQUEST_ACTIVITY = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Spinner spinner = (Spinner) findViewById(R.id.agencias);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.this,
				android.R.layout.simple_spinner_dropdown_item, JPADAOFactory
						.getFactory().getAgenciaDAO().listAgencias());

		spinner.setAdapter(adapter);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void login() {
		String respuesta = JPADAOFactory
				.getFactory()
				.getUsuarioDAO()
				.login(username.getText().toString(),
						password.getText().toString());
		if (respuesta != "error") {
			// se ha logeado correctamente
			Toast.makeText(this,
					"Bienvenido: " + username.getText().toString(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Inicio de sesion incorrecto",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void nuevoUsuario() {

		Intent intento = new Intent(this, NuevoUsuarioActivity.class);
		startActivityForResult(intento, REQUEST_ACTIVITY);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

	/*
	 * PARA INICIALIZAR EL LECTOR DE COPDIGO QR public void onClick(View view) {
	 * Intent intent = new Intent(getApplicationContext(),
	 * CaptureActivity.class); startActivity(intent); }
	 */

	@Override
	public void onBackPressed() {
		finish();
	}

}
