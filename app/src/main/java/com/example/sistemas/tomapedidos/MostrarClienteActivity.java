package com.example.sistemas.tomapedidos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MostrarClienteActivity extends AppCompatActivity {

    Button btnpedido,btnregresodetallecliente;
    Clientes cliente;
    TextView tvcodigo,tvNombre,tvDireccion,tvGiro,tvTipoCiente,tvDeuda,tvestado,
             tvUsuarioUltPedido;
    Usuario usuario;
    Spinner spopcionesdocumento;
    private final static String[] opcionesDoc = { "Boleta", "Factura" };
    List<String> opdoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);


        cliente  = new Clientes();
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        tvcodigo = findViewById(R.id.tvCofigoProducto);
        tvNombre = findViewById(R.id.tvNombreCliente);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvGiro = findViewById(R.id.tvNomProdElegido);
        tvTipoCiente = findViewById(R.id.tvAlmProdElegido);
        tvDeuda = findViewById(R.id.tvStockElegido);
        tvestado = findViewById(R.id.tvPrecioElegido);
        tvUsuarioUltPedido = findViewById(R.id.tvTotalElegido);
        btnregresodetallecliente = findViewById(R.id.btnRetornoDetCliente);
        spopcionesdocumento = findViewById(R.id.spTipoDocumento);

        tvcodigo.setText(cliente.getCodCliente());
        tvNombre.setText(cliente.getNombre());
        tvDireccion.setText(cliente.getDireccion());

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,opcionesDoc);


        opdoc = new ArrayList<String>();
        opdoc.add("Factura");
        opdoc.add("Boleta");
        Toast.makeText(this, opdoc.get(0), Toast.LENGTH_SHORT).show();

        spopcionesdocumento.setAdapter(new SpinnerAdapter(this,opdoc));

        spopcionesdocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String muestra = (String)parent.getItemAtPosition(position);
                cliente.setTipoDocumento(muestra);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnpedido = findViewById(R.id.btnPedido);
        btnpedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MostrarClienteActivity.this,ListadoAlmacenActivity.class);
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

        btnregresodetallecliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MostrarClienteActivity.this,BusquedaClienteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
