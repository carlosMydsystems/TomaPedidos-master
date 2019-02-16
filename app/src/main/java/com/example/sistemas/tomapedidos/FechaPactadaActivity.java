package com.example.sistemas.tomapedidos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

import java.util.ArrayList;

public class FechaPactadaActivity extends AppCompatActivity {

    Button btnregistrafechapactada, btnregresarfechapactada;
    EditText etfechapactada;
    String url;
    ArrayList<Productos> listaproductoselegidos;
    Clientes cliente;
    Usuario usuario;
    String almacen,tipoformapago,Ind,id_pedido,validador,retorno,Index,precio,cantidad;
    TextView tvCantidad,tvPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_pactada);

        btnregistrafechapactada =  findViewById(R.id.btnRegistrarFechaPactada);
        btnregresarfechapactada = findViewById(R.id.btnRegresarFechaPactada);
        etfechapactada = findViewById(R.id.etFechaPactada);
        tvCantidad = findViewById(R.id.tvNumeroItem);
        tvPrecio = findViewById(R.id.tvMontoTotal);

        listaproductoselegidos = (ArrayList<Productos>) getIntent()
                .getSerializableExtra("listaproductoselegidos");   //
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");   //
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");    //
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("indice");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");
        cantidad = getIntent().getStringExtra("Cantidad");
        precio = getIntent().getStringExtra("Precio");
        tvCantidad.setText(cantidad);
        tvPrecio.setText(precio);

        btnregresarfechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FechaPactadaActivity.this,bandejaProductosActivity.class);

                intent.putExtra("TipoPago",tipoformapago);
                intent.putExtra("indice",Ind);
                intent.putExtra("Index",Index);
                intent.putExtra("Cantidad",cantidad);
                intent.putExtra("Precio",precio);
                intent.putExtra("cantidadlista",listaproductoselegidos.size()+"");
                intent.putExtra("Almacen",almacen);
                intent.putExtra("id_pedido",id_pedido);
                intent.putExtra("validador","false");

                Bundle bundle = new Bundle();
                Bundle bundle2 = new Bundle();
                Bundle bundle3 = new Bundle();

                bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
                bundle2.putSerializable("Cliente",cliente);
                bundle3.putSerializable("Usuario",usuario);

                intent.putExtras(bundle);
                intent.putExtras(bundle2);
                intent.putExtras(bundle3);

                startActivity(intent);
                finish();

            }
        });

        btnregistrafechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // VerificaFechaPactada(trama);

                Intent intent = new Intent(FechaPactadaActivity.this,MainActivity.class);

                Bundle bundle2 = new Bundle();
                Bundle bundle3 = new Bundle();

                bundle2.putSerializable("Cliente",cliente);
                bundle3.putSerializable("Usuario",usuario);

                intent.putExtras(bundle2);
                intent.putExtras(bundle3);

                startActivity(intent);
                finish();
            }
        });
    }

    private void VerificaFechaPactada(String trama ) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // Se hace el ingreso de la URL para la verificacion de la fecha que se ha pactado por medio del peso (El factor limitante)

        //url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){
                            Toast.makeText(FechaPactadaActivity.this, "Se hizo el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }


}
