package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class ActualizarRegistroPedidosActivity extends AppCompatActivity {

    TextView tvcodprodelegido, tvnomprodelegido, tvalmprodelegido, tvstockelegido, tvprecioelegido,
            tvtotalelegido, tvpreciorealelegido,tvunidadelegida;
    Productos productos;
    Button   btndverificarproductoelegido,btnactualizarproductoelegido;
    Clientes cliente;
    ArrayList<Productos> listaproductoselegidos;
    EditText etcantprodelegida;
    Double preciounitario,cantidad,  total, Aux;
    String url,almacen,position,tipoformapago,id_pedido;
    ProgressDialog progressDialog;
    Productos producto;
    ArrayList<Productos> listaProductos;
    ArrayList<String> listaProducto;
    Usuario usuario;
    BigDecimal redondeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_registro_pedidos);

        etcantprodelegida =  findViewById(R.id.etCantProdElegida);

        listaproductoselegidos = new ArrayList<>();
        productos  = new Productos();
        listaProductos=new ArrayList<>();

        // se recibe los datos de los productos y del que se han encontrado en el otro intent

        productos = (Productos) getIntent().getSerializableExtra("Producto");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        almacen =  getIntent().getStringExtra("Almacen");
        position =  getIntent().getStringExtra("position");
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        id_pedido = getIntent().getStringExtra("id_pedido");
        etcantprodelegida.setText(productos.getCantidad());
        // Se referencia a todas las partes del XML asociado al Activity
        tvcodprodelegido =  findViewById(R.id.tvCodProdElegido);
        tvnomprodelegido = findViewById(R.id.tvNomProdElegido);
        tvalmprodelegido = findViewById(R.id.tvAlmProdElegido);
        btndverificarproductoelegido = findViewById(R.id.btnVerificarElegido);
        tvpreciorealelegido = findViewById(R.id.tvPrecioElegido);
        btnactualizarproductoelegido = findViewById(R.id.btnActualizarElegido);
        tvunidadelegida =  findViewById(R.id.tvUnidadElegida);

        btndverificarproductoelegido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btndverificarproductoelegido.setVisibility(View.GONE);
                progressDialog =  new ProgressDialog(ActualizarRegistroPedidosActivity.this);
                progressDialog.setMessage("... Por favor esperar");
                progressDialog.show();
                if (etcantprodelegida.getText()==null || etcantprodelegida.getText().toString().equals("")){
                }else{
                    VerificarCantidad(etcantprodelegida.getText().toString());
                }
            }
        });

        // Se hace referencia a cada uno de los TextView del XML

        tvstockelegido =findViewById(R.id.tvStockElegido);
        tvprecioelegido = findViewById(R.id.tvPrecioElegido);
        tvtotalelegido = findViewById(R.id.tvTotalElegido);
        btnactualizarproductoelegido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Se genera un nuevo Progress dialog

                if (etcantprodelegida.getText().toString().equals("")){

                }else{

                    productos.setCantidad(etcantprodelegida.getText().toString());
                    preciounitario = Double.valueOf(tvprecioelegido.getText().toString());
                    cantidad = Double.valueOf(etcantprodelegida.getText().toString());
                    redondeado = new BigDecimal(cantidad).setScale(2, RoundingMode.HALF_EVEN);
                    productos.setPrecio(tvprecioelegido.getText().toString());
                    productos.setPrecioAcumulado(tvtotalelegido.getText().toString()); // Se hace la definicion del precio que se va ha acumular
                    productos.setEstado(String.valueOf(redondeado)); // Se define la cantidad que se debe de tener
                    productos.setAlmacen(almacen);
                    Integer i = Integer.valueOf(position);
                    listaproductoselegidos.get(i).setCantidad(redondeado.toString());
                    listaproductoselegidos.get(i).setPrecio(tvprecioelegido.getText().toString());
                    listaproductoselegidos.get(i).setPrecioAcumulado(tvtotalelegido.getText().toString());

                    /*
                    listaproductoselegidos.get(i).setCantidad(cantidad.toString());
                    listaproductoselegidos.get(i).setPrecio();
                    */

                    Intent intent = new Intent(ActualizarRegistroPedidosActivity.this,bandejaProductosActivity.class);
                    intent.putExtra("TipoPago",tipoformapago);
                    intent.putExtra("id_pedido",id_pedido);
                    intent.putExtra("validador","true");
                    intent.putExtra("Almacen",almacen);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Cliente",cliente);
                    intent.putExtras(bundle1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("Usuario",usuario);
                    intent.putExtras(bundle2);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("Almacen",almacen);
                    intent.putExtras(bundle3);
                    startActivity(intent);
                    finish();
                }
            }
        });

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

        tvcodprodelegido.setText(productos.getCodigo());
        tvnomprodelegido.setText(productos.getDescripcion());
        tvalmprodelegido.setText(productos.getAlmacen());

        tvalmprodelegido.setText(almacen);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btndverificarproductoelegido.setVisibility(View.VISIBLE);
                btnactualizarproductoelegido.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etcantprodelegida.getText().toString().equals("") || etcantprodelegida.getText().toString().equals("0")){

                    btndverificarproductoelegido.setEnabled(false);
                    Aux = 0d;
                    tvtotalelegido.setText("");
                    Toast.makeText(ActualizarRegistroPedidosActivity.this, "Por favor ingrese un valor valido", Toast.LENGTH_SHORT).show();

                }else {

                    btndverificarproductoelegido.setEnabled(true);
                }
            }
        };

        etcantprodelegida.addTextChangedListener(textWatcher);
    }

    private void RegistroPedido() {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion = " +
                "PKG_WEB_HERRAMIENTAS.SP_WS_GENERA_PEDIDO&variables=[TAI HEN]G Jueves 06.09.2018"; // Se debe de encontrar el metodo
        //listaProducto = new ArrayList<>();
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
                                    producto = new Productos();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    producto.setIdProducto(jsonObject.getString("IdProducto"));
                                    producto.setCodigo(jsonObject.getString("CodigoProducto"));
                                    producto.setMarca(jsonObject.getString("Marca"));
                                    producto.setDescripcion(jsonObject.getString("DescripcionProducto"));
                                    producto.setPrecio(jsonObject.getString("Precio"));
                                    producto.setStock(jsonObject.getString("Stock"));
                                    producto.setUnidad(jsonObject.getString("Unidad"));
                                    producto.setFlete(jsonObject.getString("Flete"));
                                    producto.setEstado(jsonObject.getString("Estado"));
                                    listaProductos.add(producto);
                                    listaProducto.add(producto.getCodigo()+ " - " + producto.getDescripcion());
                                }
                                Intent intent =  new Intent(ActualizarRegistroPedidosActivity.this,MainActivity.class);
                                intent.putExtra("TipoPago",tipoformapago);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
                                intent.putExtras(bundle);
                                Bundle bundle1 = new Bundle();
                                bundle1.putSerializable("Cliente",cliente);
                                intent.putExtras(bundle1);
                                Bundle bundle2 = new Bundle();
                                bundle2.putSerializable("Usuario",usuario);
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                finish();

                            }else {
                                listaProducto.clear();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarRegistroPedidosActivity.this);
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

    private void VerificarCantidad(String cantidad) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=" +
                "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_PRODUCTO&variables=%27"+almacen+"|"+usuario.
                getLugar()+"|"+productos.getCodigo()+"||"+cliente.getCodCliente()+"|||"+cantidad+"%27";

        listaProducto = new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        btnactualizarproductoelegido.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){

                                for(int i=0;i<jsonArray.length();i++) {
                                    producto = new Productos();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    producto.setCodigo(jsonObject.getString("COD_ARTICULO"));
                                    producto.setMarca(jsonObject.getString("MARCA"));
                                    producto.setDescripcion(jsonObject.getString("DESCRIPCION")); //
                                    producto.setPrecio(jsonObject.getString("PRECIO"));
                                    producto.setStock(jsonObject.getString("STOCK"));
                                    producto.setUnidad(jsonObject.getString("UND_MEDIDA"));
                                    producto.setAlmacen(almacen);
                                    listaProductos.add(producto);
                                    tvprecioelegido.setText(formateador.format((double)Double.valueOf(producto.getPrecio())));
                                    preciounitario = Double.valueOf(producto.getPrecio());
                                    if (etcantprodelegida.getText().toString().equals("")){

                                        Toast.makeText(ActualizarRegistroPedidosActivity.this,
                                                "cantidad invalida", Toast.LENGTH_SHORT).show();

                                    }else{

                                        Double cant = Double.valueOf(etcantprodelegida.getText().toString());
                                        String preciototal = String.valueOf(Math.round((cant*preciounitario*100.00))/100.00); // Se hace la definicion del precio que se va ha acumular
                                        tvunidadelegida.setText(producto.getUnidad());
                                        tvstockelegido.setText(formateador.format((double)Double.valueOf(producto.getStock()))+" "+ producto.getUnidad());
                                        tvtotalelegido.setText(formateador.format((double)Double.valueOf(preciototal)));
                                    }
                                }

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarRegistroPedidosActivity.this);
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

}
