package com.example.sistemas.tomapedidos.ConsultaStock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.tomapedidos.ConsultasListadoActivity;
import com.example.sistemas.tomapedidos.Entidades.ClienteSucursal;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.ListadoAlmacenActivity;
import com.example.sistemas.tomapedidos.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionTestMovil;

public class BuscarProductoStockActivity extends AppCompatActivity {

    RadioGroup rggrupoproducto;
    RadioButton rbnombreproducto, rbcodigoproducto;
    Button btnbuscarProducto,btnregresarproducto;
    ArrayList<Productos> listaProductos,listaproductoselegidos;
    Productos producto;
    ListView lvProducto;
    ArrayList<String> listaProducto;
    Clientes cliente;
    EditText etproducto,etglosa;
    String url,Tipobusqueda = "Nombre",tipoPago,almacen,validador,id_pedido,Index;
    ProgressDialog progressDialog;
    Usuario usuario;
    ArrayList<ClienteSucursal> listaClienteSucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        cliente  = new Clientes();
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");
        tipoPago = getIntent().getStringExtra("TipoPago");
        almacen = getIntent().getStringExtra("Almacen");
        validador = getIntent().getStringExtra("validador");
        id_pedido = getIntent().getStringExtra("id_pedido");
        Index = getIntent().getStringExtra("Index");
        listaProductos = new ArrayList<>();
        listaProducto = new ArrayList<>();
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos");
        rggrupoproducto = findViewById(R.id.rgBuscarProducto);
        rbnombreproducto = findViewById(R.id.rbNombreProducto);
        rbcodigoproducto = findViewById(R.id.rbCodigoProducto);
        btnbuscarProducto = findViewById(R.id.btnBuscarProducto);
        lvProducto = findViewById(R.id.lvProducto);
        etproducto  = findViewById(R.id.etPrducto);
        etglosa = findViewById(R.id.etGlosa);
        btnregresarproducto = findViewById(R.id.btnRegresarProducto);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnbuscarProducto.getWindowToken(), 0);

        btnregresarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent =  new Intent(BuscarProductoStockActivity.this, ConsultasListadoActivity.class);
            intent.putExtra("Almacen",almacen);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Cliente",cliente);
            intent.putExtras(bundle);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("Usuario",usuario);
            intent.putExtras(bundle1);
            Bundle bundle3 = new Bundle();
            bundle3.putSerializable("listaClienteSucursal",listaClienteSucursal);
            intent.putExtras(bundle3);
            startActivity(intent);
            finish();

            }
        });

        btnbuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(etproducto.getWindowToken(), 0);

            btnbuscarProducto.setVisibility(View.GONE);
            btnregresarproducto.setVisibility(View.GONE);
            Boolean verficador = false;
            Integer posicion=0;

            if (etproducto.getText().toString().trim().equals("")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoStockActivity.this);
                builder.setTitle("Atención !");
                builder.setMessage("Por favor ingrese una cantidad valida");
                builder.setCancelable(false);
                builder.setNegativeButton("Aceptar",null);
                builder.create()
                        .show();
                btnregresarproducto.setVisibility(View.VISIBLE);
                btnbuscarProducto.setVisibility(View.VISIBLE);
            }else {
                buscarproducto(etproducto.getText().toString().replace(" ", ""), Tipobusqueda, usuario);
            }
            }
        });

        lvProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                producto = new Productos();
                cliente = new Clientes();
                cliente = (Clientes)getIntent().getSerializableExtra("Cliente");

                Intent intent =  new Intent(BuscarProductoStockActivity.this, ConsultaStockActivity.class);
                intent.putExtra("TipoPago",tipoPago);
                intent.putExtra("Almacen",almacen);
                intent.putExtra("id_pedido",id_pedido);
                intent.putExtra("Index",Index);
                Bundle bundle = new Bundle();
                producto =  listaProductos.get(position);
                bundle.putSerializable("Producto",producto);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente",cliente);
                intent.putExtras(bundle1);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("listaproductoselegidos",listaproductoselegidos);
                intent.putExtras(bundle2);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("Usuario",usuario);
                intent.putExtras(bundle3);
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                intent.putExtras(bundle4);
                startActivity(intent);
                finish();
            }
        });

        rbcodigoproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etproducto.setInputType(2);   // envia el teclado de tipo numerico
                Tipobusqueda = "Codigo";
                etproducto.setText("");
            }
        });
        rbnombreproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etproducto.setInputType(16384 );  // envia el teclado de tipo alfanumerico
                Tipobusqueda = "Nombre";
                etproducto.setText("");
            }
        });
    }

    private void buscarproducto(String numero, String tipoConsulta,Usuario user) {
        progressDialog = new ProgressDialog(BuscarProductoStockActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        numero = numero.replace("%","%25");
        numero = numero.toUpperCase(); // se convierten los caracteres a Mayusucla
        if (tipoConsulta.equals("Nombre")) {
            url = ejecutaFuncionCursorTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_PRODUCTO_S&variables='"+user.getCodAlmacen()+"||"+numero.toUpperCase()+"'";
        }else {
            url = ejecutaFuncionCursorTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_PRODUCTO_S&variables='"+user.getCodAlmacen()+"|"+numero+"|'";
        }

        listaProducto = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String Mensaje = "";


                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            Boolean condicion = false,error = false;
                            if (success) {
                                String Aux = response.replace("{", "|");
                                Aux = Aux.replace("}", "|");
                                Aux = Aux.replace("[", "|");
                                Aux = Aux.replace("]", "|");
                                Aux = Aux.replace("\"", "|");
                                Aux = Aux.replace(",", " ");
                                Aux = Aux.replace("|", "");
                                Aux = Aux.replace(":", " ");
                                String partes[] = Aux.split(" ");
                                for (String palabras : partes) {
                                    if (condicion) {
                                        Mensaje += palabras + " ";
                                    }
                                    if (palabras.equals("ERROR")) {
                                        condicion = true;
                                        error = true;
                                    }
                                }
                                if (error) {

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                                            BuscarProductoStockActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                    progressDialog.dismiss();
                                    btnregresarproducto.setVisibility(View.VISIBLE);
                                    btnbuscarProducto.setVisibility(View.VISIBLE);
                                } else {

                                    listaProducto.clear();
                                    listaProductos.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        producto = new Productos();
                                        producto.setCodigo(jsonObject.getString("COD_ARTICULO"));
                                        producto.setMarca(jsonObject.getString("DES_MARCA"));
                                        producto.setDescripcion(jsonObject.getString("DES_ARTICULO")); //
                                        producto.setUnidad(jsonObject.getString("UND_MEDIDA"));
                                        producto.setAlmacen(almacen);
                                        listaProductos.add(producto);
                                        listaProducto.add(producto.getCodigo() + " - " + producto.getDescripcion());
                                    }

                                    // Se hace un llamado al adaptador personalizado asociado al SML custom_list

                                    if (listaProductos.size()<=1){

                                        producto = new Productos();
                                        cliente = new Clientes();
                                        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");

                                        Intent intent =  new Intent(BuscarProductoStockActivity.this,ConsultaStockActivity.class);
                                        intent.putExtra("TipoPago",tipoPago);
                                        intent.putExtra("Almacen",almacen);
                                        intent.putExtra("id_pedido",id_pedido);
                                        intent.putExtra("Index",Index);

                                        Bundle bundle = new Bundle();
                                        producto =  listaProductos.get(0);
                                        bundle.putSerializable("Producto",producto);
                                        intent.putExtras(bundle);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putSerializable("Cliente",cliente);
                                        intent.putExtras(bundle1);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putSerializable("listaproductoselegidos",listaproductoselegidos);
                                        intent.putExtras(bundle2);
                                        Bundle bundle3 = new Bundle();
                                        bundle3.putSerializable("Usuario",usuario);
                                        intent.putExtras(bundle3);
                                        Bundle bundle4 = new Bundle();
                                        bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                                        intent.putExtras(bundle4);

                                        startActivity(intent);
                                        finish();

                                    }else {
                                        progressDialog.dismiss();
                                        btnbuscarProducto.setVisibility(View.VISIBLE);
                                        btnregresarproducto.setVisibility(View.VISIBLE);
                                        ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                                CustomListAdapter(BuscarProductoStockActivity.this, R.layout.custom_list, listaProducto);
                                        lvProducto.setAdapter(listAdapter);
                                    }
                                }

                            }else {
                                progressDialog.dismiss();
                                listaProducto.clear();
                                btnbuscarProducto.setVisibility(View.VISIBLE);
                                btnregresarproducto.setVisibility(View.VISIBLE);
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item,listaProducto);
                                lvProducto.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoStockActivity.this);
                                builder.setMessage("No se llego a encontrar el registro")
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
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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

    private void EliminarProductoporIdpedido(String idpedido) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  ejecutaFuncionTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_ELIMINA_PEDIDO_TRAMA&variables='"+idpedido+"'";
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