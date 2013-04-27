package pfm.android.login;

import java.text.SimpleDateFormat;
import java.util.Date;

import pfm.android.R;
import pfm.android.jpa.JPADAOFactory;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
		Button btn_aceptar = (Button) findViewById(R.id.btn_nuevo_usuario_aceptar);

		btn_aceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nuevoUsuario();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	public void nuevoUsuario() {
		try {
			SimpleDateFormat formatoFecha1 = new SimpleDateFormat("yyyy-MM-dd");
			Date fc1 = null;
			fc1 = formatoFecha1.parse(fechaNacimiento.getText().toString());
			SimpleDateFormat formatoFecha2 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			String fc2 = formatoFecha2.format(fc1);
			String[] atributos = { "nombres", "apellidos", "direccion",
					"telefono", "fechaNacimiento", "email", "username",
					"password" };
			String[] values = { nombres.getText().toString(),
					apellidos.getText().toString(),
					direccion.getText().toString(),
					telefono.getText().toString(), fc2,
					email.getText().toString(), username.getText().toString(),
					password.getText().toString() };

			if (JPADAOFactory.getFactory().getUsuarioDAO()
					.create(atributos, values)) {
				Intent intento = new Intent();
				intento.setData(Uri.parse("Registro Correcto"));
				setResult(RESULT_OK, intento);
				super.onBackPressed();

			} else {
				Toast.makeText(this, "Registro incorrecto", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception ex) {
			Toast.makeText(this, "Error: registro de nuevo usuario",
					Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuevo_usuario, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intento = new Intent();
		intento.setData(Uri.parse("Registro Cancelado"));
		setResult(RESULT_CANCELED, intento);
		super.onBackPressed();
	}
}
