package pfm.android.producto;

import pfm.android.R;
import pfm.android.compras.ComprasActivity;
import pfm.android.jpa.JPADAOFactory;
import pfm.entidades.BodegaDetalle;
import pfm.entidades.Descuento;
import pfm.entidades.FacturaDetalle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class EditProductoActivity extends Activity {
	private EditText nombre;
	private EditText precio;
	private EditText subtotal;
	private EditText descuento;
	private EditText iva;
	private EditText total;
	private NumberPicker cantidad;
	private int idFacturaDetalle;
	private int idAgencia;
	private String nombreAgencia;
	private int idFactura;
	private int idCliente;
	private FacturaDetalle facturaDetalle = new FacturaDetalle();
	private BodegaDetalle bodegaDetalle = new BodegaDetalle();
	private Descuento des = new Descuento();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_producto);
		nombre = (EditText) findViewById(R.id.editProducto_nombre);
		precio = (EditText) findViewById(R.id.editProducto_precio);
		subtotal = (EditText) findViewById(R.id.editProducto_subtotal);
		descuento = (EditText) findViewById(R.id.editProducto_descuento);
		iva = (EditText) findViewById(R.id.editProducto_iva);
		total = (EditText) findViewById(R.id.editProducto_total);
		cantidad = (NumberPicker) findViewById(R.id.editProducto_cantidad);

		// setea los avkores maximos y minimos del NumberPicker
		cantidad.setMaxValue(10);
		cantidad.setMinValue(1);
		// recupera datos enviados desde actividad anterio
		// @param: idFacturaDetalle
		Bundle extra = getIntent().getExtras();
		idFacturaDetalle = extra.getInt("idFacturaDetalle");
		idAgencia = extra.getInt("idAgencia");
		nombreAgencia = extra.getString("nombreAgencia");
		idFactura = extra.getInt("idFactura");
		idCliente = extra.getInt("idCliente");

		ImageButton btn_editar = (ImageButton) findViewById(R.id.btn_editProducto_aceptar);
		ImageButton btn_cancelar = (ImageButton) findViewById(R.id.btn_editproducto_cancelar);
		ImageButton btn_eliminar = (ImageButton) findViewById(R.id.btn_editProducto_eliminar);

		btn_editar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				actualizar();
			}
		});

		btn_eliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				eliminar();
			}
		});

		btn_cancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelar();
			}
		});

		cantidad.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				calcularTotales();
			}
		});

		// carga el producto seleccionado
		new ReadFacturaDetalleTask(this).execute();
	}

	private class ReadFacturaDetalleTask extends
			AsyncTask<Void, Void, FacturaDetalle> {
		ProgressDialog pDialog;
		Context context;

		public ReadFacturaDetalleTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Cargando Producto...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected FacturaDetalle doInBackground(Void... params) {
			// devuelve facturaDetalle
			facturaDetalle = JPADAOFactory.getFactory().getFacturaDetalleDAO()
					.getFacturaDetalleById(idFacturaDetalle);
			// setea para el calculo de totales, cuando cambie el valor de la
			// cantidad
			bodegaDetalle = JPADAOFactory
					.getFactory()
					.getBodegaDetalleDAO()
					.getBodegaDetalleById(
							facturaDetalle.getBodegaDetalle().getId());
			des = JPADAOFactory
					.getFactory()
					.getDescuentoDAO()
					.getDescuentoByProducto(bodegaDetalle.getProducto().getId());

			return facturaDetalle;
		}

		@Override
		protected void onPostExecute(FacturaDetalle result) {
			super.onPostExecute(result);
			if (result != null) {

				nombre.setText(result.getBodegaDetalle().getProducto()
						.getNombre());
				precio.setText(String.valueOf(result.getPrecio()));
				subtotal.setText(String.valueOf(result.getSubtotal()));
				descuento.setText(String.valueOf(result.getDescuento()));
				iva.setText(String.valueOf(result.getIva()));
				total.setText(String.valueOf(result.getTotal()));
				cantidad.setValue(result.getCantidad());

			} else {
				Toast.makeText(context, "Producto no encontrado",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}

	public void calcularTotales() {
		facturaDetalle = JPADAOFactory.getFactory().getFacturaDetalleDAO()
				.setTotales(bodegaDetalle, des, cantidad.getValue());
		nombre.setText(facturaDetalle.getBodegaDetalle().getProducto()
				.getNombre());
		precio.setText(String.valueOf(facturaDetalle.getPrecio()));
		subtotal.setText(String.valueOf(facturaDetalle.getSubtotal()));
		descuento.setText(String.valueOf(facturaDetalle.getDescuento()));
		iva.setText(String.valueOf(facturaDetalle.getIva()));
		total.setText(String.valueOf(facturaDetalle.getTotal()));
		cantidad.setValue(facturaDetalle.getCantidad());
	}

	private class ActualizarProductoTask extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog pDialog;

		public ActualizarProductoTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Guardando Producto...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {

			if (cantidad.getValue() < bodegaDetalle.getCantidad()) {
				int descuentoId = 0;
				if (des != null) {
					descuentoId = des.getId();
				} else {
					descuentoId = 0;
				}
				return JPADAOFactory
						.getFactory()
						.getFacturaDetalleDAO()
						.actualizarProducto(idFacturaDetalle, descuentoId,
								facturaDetalle.getCantidad());

			} else {
				return -1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result > 0) {
				Toast.makeText(context, "Producto guardado", Toast.LENGTH_SHORT)
						.show();
				Intent intento = new Intent(context, ComprasActivity.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", result);
				intento.putExtra("idCliente", idCliente);
				startActivity(intento);
				finish();
			} else if (result == 0) {
				Toast.makeText(context, "Transaccion incorrecta",
						Toast.LENGTH_SHORT).show();
			} else if (result == -1) {
				Toast.makeText(context, "Stock insuficiente",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}

	public void actualizar() {
		new ActualizarProductoTask(this).execute();
	}

	private class EliminarProductoTask extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog pDialog;

		public EliminarProductoTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Genera un dialogo de espera mientras realiza la tarea asincrona
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Eliminando Producto...");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {

			return JPADAOFactory.getFactory().getFacturaDetalleDAO()
					.eliminarProducto(idFacturaDetalle);

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result > 0) {
				Toast.makeText(context, "Producto eliminado",
						Toast.LENGTH_SHORT).show();
				Intent intento = new Intent(context, ComprasActivity.class);
				intento.putExtra("idAgencia", idAgencia);
				intento.putExtra("nombreAgencia", nombreAgencia);
				intento.putExtra("idFactura", result);
				intento.putExtra("idCliente", idCliente);
				startActivity(intento);
				finish();
			} else if (result == 0) {
				Toast.makeText(context, "Transaccion incorrecta",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();

		}
	}

	public void eliminar() {
		new EliminarProductoTask(this).execute();
	}

	public void cancelar() {
		Toast.makeText(this, "Producto cancelado", Toast.LENGTH_SHORT).show();
		Intent intento = new Intent(this, ComprasActivity.class);
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
