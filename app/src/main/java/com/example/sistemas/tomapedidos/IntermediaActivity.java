package com.example.sistemas.tomapedidos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.sistemas.tomapedidos.Entidades.Promociones;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

import java.util.ArrayList;

public class IntermediaActivity extends AppCompatActivity {


    String id;
    Promociones promocion;
    ArrayList<Promociones> listaPromociones,listaPromocionesTipoT;
    String url,id_pedido,cantidadlista,almacen,tipoformapago,Ind,validador,trama,Index;
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<Product> products = new ArrayList<>();
    Integer indice ;
    ArrayList<String> listaTrama;    Clientes cliente;
    Usuario usuario;
    ArrayList<Productos>listaproductoselegidos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);


        listView = (ListView) findViewById(R.id.customListView);
        listAdapter = new ListAdapter(this,products);
        id_pedido = getIntent().getStringExtra("id_pedido");
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("indice");
        cantidadlista =  getIntent().getStringExtra("cantidadlista");
        Index = getIntent().getStringExtra("Index");

        listaproductoselegidos = (ArrayList<Productos>) getIntent()
                .getSerializableExtra("listaProductoselegidos");

        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");

        indice = listaproductoselegidos.size();
        //listaProductosPromociones = new ArrayList<>();

        validador = getIntent().getStringExtra("validador");
        Toast.makeText(this, validador, Toast.LENGTH_SHORT).show();


        Integer valorcantidadlista = Integer.valueOf(cantidadlista);

        for (int i = valorcantidadlista;i<listaproductoselegidos.size();i++) {

            Integer indice = i+1;
            String trama = id_pedido + "|D|" + indice + "|" + listaproductoselegidos.
                    get(i).getCantidad() + "|" + listaproductoselegidos.get(i).getNumPromocion() + "|" +
                    listaproductoselegidos.get(i).getPrecio() + "|" + listaproductoselegidos.get(i)
                    .getPrecio() + "|";

            ActualizarProducto(trama);
        }

        Intent intent = new Intent(IntermediaActivity.this,bandejaProductosActivity.class);

        intent.putExtra("id_pedido",id_pedido);
        intent.putExtra("Almacen",almacen);
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("indice",Ind);
        intent.putExtra("cantidadlista",cantidadlista);
        intent.putExtra("validador","false");
        intent.putExtra("Index",Index);
        Bundle bundle = new Bundle();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();

        bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
        bundle2.putSerializable("Usuario", usuario);
        bundle1.putSerializable("Cliente", cliente);

        intent.putExtras(bundle);
        intent.putExtras(bundle2);
        intent.putExtras(bundle1);

        startActivity(intent);
        finish();
    }


    private void ActualizarProducto(String trama) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=pkg_web_herramientas.fn_ws_registra_trama_movil&variables=

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){

                            // insertaCampos(listaproductoselegidos,id);

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
