package pfm.android.login;

import pfm.android.R;
import pfm.android.compras.ComprasActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class LogoutActivity extends Activity {

	private int idAgencia;
	private int idCliente;
	private int idFactura;
	private String nombreAgencia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);

		// Recepcion de Parametros
		Bundle parametros = getIntent().getExtras();
		idAgencia = parametros.getInt("idAgencia");
		idFactura = parametros.getInt("idFactura");
		idCliente = parametros.getInt("idCliente");
		nombreAgencia = parametros.getString("nombreAgencia");

		// obtiene los botones
		Button btnAceptar = (Button) findViewById(R.id.btn_logout_aceptar);
		Button btnCancelar = (Button) findViewById(R.id.btn_logout_cancelar);

		// generamos los listener

		btnAceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				aceptar();
			}
		});

		btnCancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelar();
			}
		});
	}

	public void aceptar() {
		Toast.makeText(this, "Cierre de sesion exitoso", Toast.LENGTH_SHORT)
		.show();
		Intent intento = new Intent(this, LoginActivity.class);

		// inicia la actividad
		startActivity(intento);
		finish();
	}

	public void cancelar() {
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
