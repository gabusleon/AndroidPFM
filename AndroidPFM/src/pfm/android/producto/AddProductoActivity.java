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

public class AddProductoActivity extends Activity {

	private EditText nombre;
	private EditText precio;
	private EditText subtotal;
	private EditText descuento;
	private EditText iva;
	private EditText total;
	private NumberPicker cantidad;
	private int idBodegaDetalle;
	private int idAgencia;
	private String nombreAgencia;
	private int idFactura;
	private int idCliente;
	private BodegaDetalle bodegaDetalle = new BodegaDetalle();
	private Descuento des = new Descuento();
	private FacturaDetalle facturaDetalle = new FacturaDetalle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_producto);

		nombre = (EditText) findViewById(R.id.addProducto_nombre);
		precio = (EditText) findViewById(R.id.addProducto_precio);
		subtotal = (EditText) findViewById(R.id.addProducto_subtotal);
		descuento = (EditText) findViewById(R.id.addProducto_descuento);
		iva = (EditText) findViewById(R.id.addProducto_iva);
		total = (EditText) findViewById(R.id.addProducto_total);
		cantidad = (NumberPicker) findViewById(R.id.addProducto_cantidad);

		// setea los avkores maximos y minimos del NumberPicker
		cantidad.setMaxValue(10);
		cantidad.setMinValue(1);
		// recupera datos enviados desde actividad anterio
		// @param: idBodegaDetalle -- bodegaDetalle de captura de codigo QR
		// @param: idAgencia
		// @param: idFactura
		Bundle extra = getIntent().getExtras();
		idBodegaDetalle = extra.getInt("idBodegaDetalle");
		idAgencia = extra.getInt("idAgencia");
		nombreAgencia = extra.getString("nombreAgencia");
		idFactura = extra.getInt("idFactura");
		idCliente = extra.getInt("idCliente");

		// obtiene botones de la pantalla
		ImageButton btn_aceptar = (ImageButton) findViewById(R.id.btn_addProducto_aceptar);
		ImageButton btn_cancelar = (ImageButton) findViewById(R.id.btn_addproducto_cancelar);

		btn_aceptar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				anadirProducto();

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

		// ejecuta la lectura del producto
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

			bodegaDetalle = JPADAOFactory.getFactory().getBodegaDetalleDAO()
					.getBodegaDetalleById(idBodegaDetalle);
			des = JPADAOFactory
					.getFactory()
					.getDescuentoDAO()
					.getDescuentoByProducto(bodegaDetalle.getProducto().getId());
			facturaDetalle = JPADAOFactory.getFactory().getFacturaDetalleDAO()
					.setTotales(bodegaDetalle, des, 1);
			return facturaDetalle;
		}

		@Override
		protected void onPostExecute(FacturaDetalle result) {
			super.onPostExecute(result);
			if (result != null) {
				if (idAgencia == result.getBodegaDetalle().getBodega()
						.getAgencia().getId()) {
					nombre.setText(result.getBodegaDetalle().getProducto()
							.getNombre());
					precio.setText(String.valueOf(result.getPrecio()));
					subtotal.setText(String.valueOf(result.getSubtotal()));
					descuento.setText(String.valueOf(result.getDescuento()));
					iva.setText(String.valueOf(result.getIva()));
					total.setText(String.valueOf(result.getTotal()));
					cantidad.setValue(result.getCantidad());
				} else {
					Toast.makeText(context,
							"Producto no pertenece a la agencia seleccionada",
							Toast.LENGTH_SHORT).show();
				}
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

	private class AgregarProductoTask extends AsyncTask<Void, Void, Integer> {
		Context context;
		ProgressDialog pDialog;

		public AgregarProductoTask(Context context) {
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
			boolean existeProducto = false;
			if (idFactura != 0) {
				// validar que no exista el mismo producto en el carro de
				existeProducto = JPADAOFactory
						.getFactory()
						.getFacturaDetalleDAO()
						.existeProductoByFacturaDetalle(idFactura,
								idBodegaDetalle);
			}

			if (existeProducto == false) {
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
							.anadirProducto(idFactura, idAgencia, idCliente,
									idBodegaDetalle, descuentoId,
									facturaDetalle.getCantidad());
				} else {
					return -1;
				}
			} else {
				return -2;
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
			} else if (result == -2) {
				Toast.makeText(context,
						"El producto se encuentra en su carro de compras",
						Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}

	public void anadirProducto() {
		new AgregarProductoTask(this).execute();
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
