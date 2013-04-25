package pfm.android.login;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import pfm.entidades.Usuario;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText username;
	private EditText password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// obtiene los parametros de username y password
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		// obtiene los botones
		Button btnAceptar = (Button) findViewById(R.id.btn_aceptar);

		// agrega los listener de los botones
		btnAceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void login() {
		Usuario usuario = JPADAOFactory
				.getFactory()
				.getUsuarioDAO()
				.login(username.getText().toString(),
						password.getText().toString());
		if (usuario != null) {
			// se ha logeado correctamente
			Toast.makeText(this,
					"Bienvenido: " + username.getText().toString(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Inicio de sesion incorrecto",
					Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * PARA INICIALIZAR EL LECTOR DE COPDIGO QR public void onClick(View view) {
	 * Intent intent = new Intent(getApplicationContext(),
	 * CaptureActivity.class); startActivity(intent); }
	 */
}
