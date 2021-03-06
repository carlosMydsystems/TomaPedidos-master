package com.example.sistemas.pedidosTemporal;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.pedidosTemporal.Entidades.ClienteSucursal;
import com.example.sistemas.pedidosTemporal.Entidades.Clientes;
import com.example.sistemas.pedidosTemporal.Entidades.Productos;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.Utilitarios.Utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionTestMovil;

public class IntermediaActivity extends AppCompatActivity {

    String url,id_pedido,cantidadlista,almacen,tipoformapago,validador,Index,id;
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<Product> products = new ArrayList<>();
    Integer indice ;
    Clientes cliente;
    Usuario usuario;
    ArrayList<Productos>listaproductoselegidos;
    BigDecimal precio;
    ArrayList<ClienteSucursal> listaClienteSucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);

        if(Utilitario.isOnline(getApplicationContext())){


            listView = (ListView) findViewById(R.id.customListView);
            listAdapter = new ListAdapter(this,products);
            id_pedido = getIntent().getStringExtra("id_pedido");
            almacen =  getIntent().getStringExtra("Almacen");
            tipoformapago =  getIntent().getStringExtra("TipoPago");
            cantidadlista =  getIntent().getStringExtra("cantidadlista");
            Index = getIntent().getStringExtra("Index");
            listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");

            listaproductoselegidos = (ArrayList<Productos>) getIntent()
                    .getSerializableExtra("listaProductoselegidos");

            cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
            usuario = (Usuario)getIntent().getSerializableExtra("Usuario");

            indice = listaproductoselegidos.size();
            validador = getIntent().getStringExtra("validador");
            Integer valorcantidadlista = Integer.valueOf(cantidadlista);

            for (int i = valorcantidadlista;i<listaproductoselegidos.size();i++) {

                Integer indice = Integer.valueOf(Index)+1;
                listaproductoselegidos.get(i).setIndice(indice);


                Double cantidad = Double.valueOf(listaproductoselegidos.get(i).getPrecio());
                precio = new BigDecimal(cantidad).setScale(2, RoundingMode.HALF_EVEN);

                if (listaproductoselegidos.get(i).getNumPromocion().trim().equals("null")) {


                    String trama = id_pedido + "|D|" + indice + "|" + listaproductoselegidos.
                            get(i).getCantidad() + "|" + listaproductoselegidos.get(i).getCodigo() + "|" +
                            listaproductoselegidos.get(i).getPrecio() + "||" + listaproductoselegidos.get(i).getNumPromocion() + "|" +
                            listaproductoselegidos.get(i).getPresentacion() + "|" + listaproductoselegidos.get(i)
                            .getEquivalencia() + "|S";
                    Index = indice.toString();
                    ActualizarProducto(trama);
                }else {

                    String trama = id_pedido + "|D|" + indice + "|" + listaproductoselegidos.
                            get(i).getCantidad() + "|" + listaproductoselegidos.get(i).getCodigo() + "|" +
                            listaproductoselegidos.get(i).getPrecio() + "|" + listaproductoselegidos.get(i)
                            .getTasaDescuento() + "|" + listaproductoselegidos.get(i).getNumPromocion() + "|" +
                            listaproductoselegidos.get(i).getPresentacion() + "|" + listaproductoselegidos.get(i)
                            .getEquivalencia() + "|S";
                    Index = indice.toString();
                    ActualizarProducto(trama);

                }
            }

            Intent intent = new Intent(IntermediaActivity.this,bandejaProductosActivity.class);

            intent.putExtra("id_pedido",id_pedido);
            intent.putExtra("Almacen",almacen);
            intent.putExtra("TipoPago",tipoformapago);
            intent.putExtra("cantidadlista",cantidadlista);
            intent.putExtra("validador","false");
            intent.putExtra("Index",Index);
            Bundle bundle = new Bundle();
            Bundle bundle1 = new Bundle();
            Bundle bundle2 = new Bundle();
            Bundle bundle3 = new Bundle();

            bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
            bundle2.putSerializable("Usuario", usuario);
            bundle1.putSerializable("Cliente", cliente);
            bundle3.putSerializable("listaClienteSucursal", listaClienteSucursal);

            intent.putExtras(bundle);
            intent.putExtras(bundle2);
            intent.putExtras(bundle1);
            intent.putExtras(bundle3);

            startActivity(intent);
            finish();

        }else{

            AlertDialog.Builder build = new AlertDialog.Builder(IntermediaActivity.this);
            build.setTitle("Atención .. !");
            build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
            build.setCancelable(false);
            build.setNegativeButton("ACEPTAR",null);
            build.create().show();

        }
    }


    private void ActualizarProducto(String trama) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  ejecutaFuncionTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){
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
