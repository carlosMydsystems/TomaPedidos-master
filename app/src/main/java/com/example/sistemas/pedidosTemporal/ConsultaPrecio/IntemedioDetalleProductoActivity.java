package com.example.sistemas.pedidosTemporal.ConsultaPrecio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sistemas.pedidosTemporal.Entidades.Clientes;
import com.example.sistemas.pedidosTemporal.Entidades.Productos;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.R;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import static com.example.sistemas.pedidosTemporal.Utilitarios.Utilitario.Dolares;
import static com.example.sistemas.pedidosTemporal.Utilitarios.Utilitario.Soles;

public class IntemedioDetalleProductoActivity extends AppCompatActivity {

    TextView tvcodigoproducto,tvnombreproducto,tvalmacenproducto,tvstock,tvprecio,tvtotal,tvprecioreal,
            tvunidades,tvtasa,tvpreciorealjson,tvNroPromociones,tvPresetacion,tvEquivalencia,tv9,tv16,tv7;
    Productos productos;
    Button  btnDsctoxVolumen,btnverificar;
    Clientes cliente;
    EditText etcantidadelegida;
    Double  Aux,Aux1;
    Usuario usuario;
    ImageButton imgbtnvolverdetalleproducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        etcantidadelegida =  findViewById(R.id.etCantProdElegida);
        etcantidadelegida.setVisibility(View.GONE);
        productos  = new Productos();

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        // se recibe los datos de los productos y del que se han encontrado en el otro intent

        productos = (Productos) getIntent().getSerializableExtra("Producto");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");

        // Se referencia a todas las partes del XML asociado al ProveedorActivity
        tvcodigoproducto =  findViewById(R.id.tvCofigoProducto);
        tvnombreproducto = findViewById(R.id.tvNomProdElegido);
        tvalmacenproducto = findViewById(R.id.tvAlmProdElegido);
        tvprecioreal = findViewById(R.id.tvPrecioReal);
        tvunidades = findViewById(R.id.tvUnidad);
        tvtasa =  findViewById(R.id.tvTasa);
        imgbtnvolverdetalleproducto = findViewById(R.id.ibVolverDetalleProductoPrecio);
        tvpreciorealjson = findViewById(R.id.tvPrecioRealJson);
        tvNroPromociones = findViewById(R.id.tvNroPromociones);
        tvPresetacion = findViewById(R.id.tvPresetacion);
        tvEquivalencia = findViewById(R.id.tvEquivalencia);
        btnverificar = findViewById(R.id.btnVerificar);
        btnDsctoxVolumen = findViewById(R.id.btnDsctoxVolumen);
        tv9 = findViewById(R.id.textView9A);
        tv7 = findViewById(R.id.textView7A);
        tv16 = findViewById(R.id.textView16);
        tvtotal = findViewById(R.id.tvTotalElegido);
        tv9.setVisibility(View.GONE);
        tv16.setVisibility(View.GONE);
        tvtotal.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btnverificar.setVisibility(View.GONE);
        btnDsctoxVolumen.setVisibility(View.VISIBLE);


        // Se hace referencia a cada uno de los TextView del XML

        tvstock  =findViewById(R.id.tvStockElegido);
        tvprecio = findViewById(R.id.tvPrecioElegido);
        tvtotal = findViewById(R.id.tvTotalElegido);

        if (usuario.getMoneda().equals("1")){
            tv7.setText("Precio :  " + Soles + " ");
        }else{
            tv7.setText("Precio :  " + Dolares + " ");
        }

        if (productos.getStock()!= null) {
            tvstock.setText(productos.getStock());

            Aux1 = Double.valueOf(tvstock.getText().toString());
            tvstock.setText(formateador.format((double) Aux1) + " ");
        }

        tvunidades.setText(productos.getUnidad());

        if (tvstock.getText() == null){

            tvstock.setText("0.0");
        }

        else if (etcantidadelegida.getText()== null){

            etcantidadelegida.setText("0.0");

        }

        imgbtnvolverdetalleproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IntemedioDetalleProductoActivity.this, BuscarProductoPrecioActivity.class);
                intent.putExtra("validador","false");
                Bundle bundle = new Bundle();
                bundle.putSerializable("Cliente",cliente);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Usuario",usuario);
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();

            }
        });

        tvcodigoproducto.setText(productos.getCodigo());
        tvnombreproducto.setText(productos.getDescripcion());
        tvalmacenproducto.setText(productos.getAlmacen());
        tvprecio.setText(productos.getPrecio());
        btnDsctoxVolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntemedioDetalleProductoActivity.this, DetalleProductoPrecioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Producto",productos);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente",cliente);
                intent.putExtras(bundle1);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("Usuario",usuario);
                intent.putExtras(bundle3);
                startActivity(intent);
                finish();
            }
        });

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etcantidadelegida.getText().toString().equals("") || etcantidadelegida.getText().toString().equals("0")){

                    Aux = 0d;
                    tvtotal.setText("");
                    Toast.makeText(IntemedioDetalleProductoActivity.this, "Por favor ingrese un valor valido", Toast.LENGTH_SHORT).show();

                }else {
                }
            }
        };

        etcantidadelegida.addTextChangedListener(textWatcher);
    }
}

