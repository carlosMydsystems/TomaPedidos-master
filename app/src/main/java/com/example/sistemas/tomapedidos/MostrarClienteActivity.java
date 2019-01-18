package com.example.sistemas.tomapedidos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

public class MostrarClienteActivity extends AppCompatActivity {

    Button btnpedido,btnregresodetallecliente;
    Clientes cliente;
    TextView tvcodigo,tvNombre,tvDireccion,tvGiro,tvTipoCiente,tvDeuda,tvestado,tvFechaUltPedido,
             tvUsuarioUltPedido;
    Usuario usuario;

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
        tvFechaUltPedido = findViewById(R.id.tvSubtotal);
        tvUsuarioUltPedido = findViewById(R.id.tvTotalElegido);
        btnregresodetallecliente = findViewById(R.id.btnRetornoDetCliente);

        tvcodigo.setText(cliente.getCodCliente());
        tvNombre.setText(cliente.getNombre());
        tvDireccion.setText(cliente.getDireccion());
        tvGiro.setText(cliente.getGiro());
        tvTipoCiente.setText(cliente.getTipoCliente());
        tvDeuda.setText(cliente.getDeuda());
        tvestado.setText(cliente.getEstado());
        tvFechaUltPedido.setText(cliente.getFechaultpedido());
        tvUsuarioUltPedido.setText(cliente.getUsuarioultpedido());

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
