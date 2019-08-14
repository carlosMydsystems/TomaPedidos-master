package com.example.sistemas.pedidosTemporal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.example.sistemas.pedidosTemporal.Entidades.Pedidos;
import com.example.sistemas.pedidosTemporal.Entidades.Productos;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.Utilitarios.Utilitario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionCursorTestMovil;

public class DetallePedidoActivity extends AppCompatActivity {

    ImageButton ibregresarpendientemain;
    ListView lvPendientes;
    String url,userId,tipoPago,almacen;
    Pedidos pedidos;
    ArrayList<Pedidos> listaPedidos;
    ArrayList<String> lista;
    Usuario usuario;
    ArrayList<Productos> listaproductoselegidos;
    Productos productos;
    Integer index = 0, Index ;
    Clientes cliente;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    ClienteSucursal clienteSucursal;
    Boolean  auxBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        ibregresarpendientemain = findViewById(R.id.ibRegresarPendienteMain);
        lvPendientes = findViewById(R.id.lvPedidosPendientes);
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        userId = usuario.getUser();
        cliente = new Clientes();

        // Rutina que permite ver si el equipo tiene activo la opcion de internet

        if(Utilitario.isOnline(getApplicationContext())){

            ActualizarListView(userId);

        }else{

            AlertDialog.Builder build = new AlertDialog.Builder(DetallePedidoActivity.this);
            build.setTitle("Atención .. !");
            build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
            build.setCancelable(false);
            build.setNegativeButton("ACEPTAR",null);
            build.create().show();

        }

        ibregresarpendientemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetallePedidoActivity.this,MainActivity.class);
                intent.putExtra("userId",userId);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        });
    }

    private void ActualizarListView(String userid) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo
        final ProgressDialog progressDialog =  new ProgressDialog(DetallePedidoActivity.this);
        progressDialog.setMessage("...cargando");
        progressDialog.setCancelable(false);

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PEDIDOS_PEN&variables='"+userid.trim()+"'";

        listaPedidos = new ArrayList<>();
        lista = new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){

                                for(int i=0;i<jsonArray.length();i++) {
                                    pedidos = new Pedidos(); // Instancia cada uno de los pedidos
                                    jsonObject = jsonArray.getJSONObject(i);
                                    pedidos.setIdPedido(jsonObject.getString("ID_PEDIDO"));
                                    pedidos.setCliente(jsonObject.getString("CLIENTE"));
                                    pedidos.setFecha(jsonObject.getString("FECHA"));
                                    pedidos.setSucursalCliente(jsonObject.getString("SUCURSAL_CLI"));

                                    lista.add("Id  \t\t\t\t\t:\t" + pedidos.getIdPedido() +
                                            "\n" + "Cliente \t:\t" + pedidos.getCliente() +
                                            "\n" + "Fecha \t\t: " + pedidos.getFecha());
                                    listaPedidos.add(pedidos); // Hace la agragacion de cada uno de los objetos del pedido
                                }
                                auxBool = true;
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetallePedidoActivity.this,
                                        android.R.layout.simple_list_item_1, android.R.id.text1,lista); // Instancia el adaptador de tipo ArrayAdapter de tipo String
                                lvPendientes.setAdapter(adapter); // Adapta el objeto a ListVIew a la lista de tipo String
                                lvPendientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    if (auxBool) {
                                        auxBool = false;
                                        final ProgressDialog progressDialog = new ProgressDialog(DetallePedidoActivity.this);
                                        progressDialog.setMessage("...cargando");
                                        progressDialog.setCancelable(false);
                                        progressDialog.create();
                                        progressDialog.show();

                                        String idPedido = listaPedidos.get(position).getIdPedido();
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                        url = ejecutaFuncionCursorTestMovil +
                                                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PEDIDOS_TRAMA&variables='" + idPedido + "'";

                                        listaproductoselegidos = new ArrayList<>();

                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        boolean success = jsonObject.getBoolean("success");
                                                        JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");

                                                        listaPedidos = new ArrayList<>();
                                                        if (success) {

                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                productos = new Productos();
                                                                jsonObject = jsonArray.getJSONObject(i);
                                                                String trama = jsonObject.getString("TRAMA");
                                                                trama = trama.trim();
                                                                trama = trama.replace("|", " ");
                                                                String[] partesTrama = trama.split(" ");

                                                                productos.setIdPedido(partesTrama[0]);
                                                                productos.setTipoTupla(partesTrama[1]);

                                                                //-------------------------------------------

                                                                if (productos.getTipoTupla().equals("D")) {

                                                                    productos.setIndice(Integer.valueOf(partesTrama[2]));
                                                                    productos.setCantidad(partesTrama[3]);
                                                                    productos.setCodigo(partesTrama[4]);
                                                                    productos.setPrecio(partesTrama[5]);
                                                                    productos.setTasaDescuento(partesTrama[6]);
                                                                    productos.setNumPromocion(partesTrama[7]);
                                                                    productos.setPresentacion(partesTrama[8]);
                                                                    productos.setEquivalencia(partesTrama[9]);
                                                                    if (partesTrama[10].equals("N")) {

                                                                    } else {
                                                                        productos.setObservacion(partesTrama[10]);
                                                                    }

                                                                    String articulo = jsonObject.getString("ARTICULO");
                                                                    articulo = articulo.trim();
                                                                    String[] partesArticulo = articulo.split(" - ");

                                                                    productos.setDescripcion(partesArticulo[0]);
                                                                    productos.setUnidad(partesArticulo[2]);
                                                                    Double precioAcumulado = Double.valueOf(productos.
                                                                            getPrecio()) * Double.valueOf(productos.getCantidad());
                                                                    if (partesTrama[10].equals("N")) {

                                                                        BigDecimal precioBig1 = new BigDecimal(productos.getPrecio());
                                                                        precioBig1 = precioBig1.setScale(2, RoundingMode.HALF_EVEN);
                                                                        Double Descuento = (1 - Double.valueOf(jsonObject.getString("TASA_DESCUENTO")) / 100);
                                                                        Double precioDouble = Double.valueOf(precioBig1.toString())
                                                                                * Descuento * Double.valueOf(productos.getCantidad().toString());
                                                                        BigDecimal precioBigTotal = new BigDecimal(precioDouble.toString());
                                                                        precioBigTotal = precioBigTotal.setScale(2, RoundingMode.HALF_EVEN);
                                                                        productos.setPrecioAcumulado("" + precioBigTotal);

                                                                    } else {

                                                                        productos.setPrecioAcumulado("0.0");

                                                                    }

                                                                    listaproductoselegidos.add(productos);

                                                                    if (index > productos.getIndice()) {

                                                                    } else {

                                                                        index = productos.getIndice();

                                                                    }

                                                                    Index = index;

                                                                } else {

                                                                    listaClienteSucursal = new ArrayList<>();
                                                                    clienteSucursal = new ClienteSucursal();
                                                                    clienteSucursal.setCodSucursal(partesTrama[11]);
                                                                    clienteSucursal.setNombreSucursal(pedidos.getSucursalCliente().toString());
                                                                    listaClienteSucursal.add(clienteSucursal);
                                                                    tipoPago = partesTrama[6];
                                                                    almacen = partesTrama[3];
                                                                    cliente.setCodCliente(partesTrama[4]);
                                                                    cliente.setFormaPago(jsonObject.getString("FORMA_PAGO"));
                                                                    cliente.setNombre(jsonObject.getString("CLIENTE"));
                                                                    cliente.setCodFPago(jsonObject.getString("COD_FPAGO"));
                                                                    cliente.setDireccion(jsonObject.getString("DIRECCION"));
                                                                    cliente.setTipoDocumento(partesTrama[7]);
                                                                    cliente.setDocumentoCliente(jsonObject.getString("DOC_CLIENTE"));

                                                                    if (cliente.getTipoDocumento().equals("FAC")) {
                                                                        cliente.setDocumentoSeleccionado("FACTURA");
                                                                    } else if (cliente.getTipoDocumento().equals("BOL")) {
                                                                        cliente.setDocumentoSeleccionado("BOLETA");
                                                                    }

                                                                    usuario.setNombre(jsonObject.getString("VENDEDOR"));
                                                                    Index = index;

                                                                }
                                                            }

                                                            Intent intent = new Intent(DetallePedidoActivity.this, bandejaProductosActivity.class);
                                                            intent.putExtra("TipoPago", tipoPago);
                                                            intent.putExtra("validador", "true");
                                                            intent.putExtra("Check", "Ok");
                                                            intent.putExtra("Index", Index + "");
                                                            intent.putExtra("id_pedido", listaproductoselegidos.get(0).getIdPedido());

                                                            Bundle bundle = new Bundle();
                                                            bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
                                                            intent.putExtras(bundle);

                                                            Bundle bundle1 = new Bundle();
                                                            bundle1.putSerializable("Cliente", cliente);
                                                            intent.putExtras(bundle1);

                                                            Bundle bundle2 = new Bundle();
                                                            bundle2.putSerializable("Usuario", usuario);
                                                            intent.putExtras(bundle2);

                                                            Bundle bundle3 = new Bundle();
                                                            bundle3.putSerializable("Almacen", almacen);
                                                            intent.putExtras(bundle3);

                                                            Bundle bundle4 = new Bundle();
                                                            bundle4.putSerializable("listaClienteSucursal", listaClienteSucursal);
                                                            intent.putExtras(bundle4);

                                                            startActivity(intent);
                                                            finish();

                                                        } else {
                                                            progressDialog.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoActivity.this);
                                                            builder.setCancelable(false)
                                                                    .setMessage("No se llego a encontrar el registro")
                                                                    .setNegativeButton("Aceptar", null)
                                                                    .create()
                                                                    .show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                progressDialog.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoActivity.this);
                                                builder.setTitle("Atención ...!");
                                                builder.setMessage("EL servicio no se encuentra disponible en estos momentos");
                                                builder.setCancelable(false);
                                                builder.setNegativeButton("Aceptar",null);
                                                builder.create().show();

                                            }
                                        });

                                        int socketTimeout = 30000;

                                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                        stringRequest.setRetryPolicy(policy);
                                        requestQueue.add(stringRequest);

                                    }
                                    }
                                });

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoActivity.this);

                                builder.setCancelable(false)
                                        .setMessage("No se llego a encontrar el registro")
                                        .setNegativeButton("Aceptar",null)
                                        .create()
                                        .show();

                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoActivity.this);
                builder.setTitle("Atención ...!");
                builder.setMessage("EL servicio no se encuentra disponible en estos momentos");
                builder.setCancelable(false);
                builder.setNegativeButton("Aceptar",null);
                builder.create().show();

            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}