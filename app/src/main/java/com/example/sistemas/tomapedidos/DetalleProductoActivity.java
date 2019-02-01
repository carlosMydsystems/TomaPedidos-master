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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;




public class DetalleProductoActivity extends AppCompatActivity {

    TextView tvcodigoproducto,tvnombreproducto,tvalmacenproducto,tvstock,tvprecio,tvsubtotal,
            tvtotal,tvprecioreal,tvunidades;
    Productos productos;
    Button btnguardaryrevisar, btnguardaryagregar, btndverificarproducto;
    Clientes cliente;
    ArrayList<Productos> listaproductoselegidos;
    EditText etcantidadelegida;
    Double preciounitario,cantidad, Aux;
    String url,almacen,tipoPago,id_pedido;
    ProgressDialog progressDialog;
    Productos producto;
    ArrayList<Productos> listaProductos;
    ArrayList<String> listaProducto;
    Usuario usuario;
    String Ind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        etcantidadelegida =  findViewById(R.id.etCantProdElegida);
        listaproductoselegidos = new ArrayList<>();
        productos  = new Productos();
        listaProductos=new ArrayList<>();

        // se recibe los datos de los productos y del que se han encontrado en el otro intent

        productos = (Productos) getIntent().getSerializableExtra("Producto");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        almacen =  getIntent().getStringExtra("Almacen");
        tipoPago = getIntent().getStringExtra("TipoPago");
        id_pedido = getIntent().getStringExtra("id_pedido");
        Ind = getIntent().getStringExtra("indice");

        Toast.makeText(this, Ind, Toast.LENGTH_SHORT).show();
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos");

        // Se referencia a todas las partes del XML asociado al Activity
        tvcodigoproducto =  findViewById(R.id.tvCofigoProducto);
        tvnombreproducto = findViewById(R.id.tvNomProdElegido);
        tvalmacenproducto = findViewById(R.id.tvAlmProdElegido);
        btndverificarproducto = findViewById(R.id.btnVerificar);
        tvprecioreal = findViewById(R.id.tvPrecioReal);
        tvunidades = findViewById(R.id.tvUnidad);


        btndverificarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btndverificarproducto.setVisibility(View.GONE);
                progressDialog =  new ProgressDialog(DetalleProductoActivity.this);
                progressDialog.setMessage("... Por favor esperar");
                progressDialog.show();
                if(etcantidadelegida.getText().toString().equals("")|| etcantidadelegida.getText().toString().equals("0")){
                    progressDialog.dismiss();
                    Toast.makeText(DetalleProductoActivity.this, "Por favor ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                }else {
                    VerificarCantidad(etcantidadelegida.getText().toString());
                }
            }
        });

        // Se hace referencia a cada uno de los TextView del XML

        tvstock  =findViewById(R.id.tvStockElegido);
        tvprecio = findViewById(R.id.tvPrecioElegido);
        tvsubtotal = findViewById(R.id.tvSubtotal);
        tvtotal = findViewById(R.id.tvTotalElegido);


        Toast.makeText(DetalleProductoActivity.this,tvprecio.getText(), Toast.LENGTH_SHORT).show();

        btnguardaryrevisar = findViewById(R.id.btnGuardarrevisar);
        btnguardaryagregar = findViewById(R.id.btnGuardaryagregar);

        btnguardaryrevisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etcantidadelegida.getText().toString().equals("")){
                }else{


                    String trama = id_pedido+"|D|"+Ind+"|"+etcantidadelegida.getText()+"|"+
                            productos.getCodigo()+"|"+ tvprecio.getText()+"|"+ tvtotal.getText()+"|";

                    ActualizarProducto(trama);

                    productos.setCantidad(etcantidadelegida.getText().toString());
                    preciounitario = Double.valueOf(tvprecio.getText().toString());
                    cantidad = Double.valueOf(etcantidadelegida.getText().toString());
                    productos.setPrecio(tvprecio.getText().toString());
                    productos.setPrecioAcumulado(tvtotal.getText().toString()); // Se hace la definicion del precio que se va ha acumular
                    productos.setEstado(String.valueOf(cantidad)); // Se define la cantidad que se debe de tener
                    listaproductoselegidos.add(productos);

                    Intent intent = new Intent(DetalleProductoActivity.this,bandejaProductosActivity.class);
                    intent.putExtra("TipoPago",tipoPago);
                    intent.putExtra("indice",Ind);
                    intent.putExtra("id_pedido",id_pedido);
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
        btnguardaryagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DetalleProductoActivity.this,tvtotal.getText(), Toast.LENGTH_SHORT).show();

                String trama = id_pedido+"|D|"+Ind+"|"+etcantidadelegida.getText()+"|"+
                        productos.getCodigo()+"|"+ tvprecio.getText()+"|"+ tvtotal.getText()+"|";



                ActualizarProducto(trama);

                if (etcantidadelegida.getText().toString().equals("")){
                }else{


                    productos.setCantidad(etcantidadelegida.getText().toString());
                    preciounitario = Double.valueOf(tvprecio.getText().toString());
                    cantidad = Double.valueOf(etcantidadelegida.getText().toString());
                    productos.setPrecio(tvprecio.getText().toString());
                    productos.setPrecioAcumulado(tvtotal.getText().toString()); // Se hace la definicion del precio que se va ha acumular
                    productos.setEstado(String.valueOf(cantidad)); // Se define la cantidad que se debe de tener
                   // productos.setAlmacen(almacen);
                    listaproductoselegidos.add(productos);

                    Intent intent = new Intent(DetalleProductoActivity.this,BuscarProductoActivity.class);
                    intent.putExtra("TipoPago",tipoPago);
                    intent.putExtra("indice",Ind);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listaproductoselegidos", listaproductoselegidos);
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

         tvcodigoproducto.setText(productos.getCodigo());
         tvnombreproducto.setText(productos.getDescripcion());
         tvalmacenproducto.setText(productos.getAlmacen());

            tvalmacenproducto.setText(almacen);

            final TextWatcher textWatcher = new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

                 btndverificarproducto.setVisibility(View.VISIBLE);
                 btnguardaryagregar.setVisibility(View.GONE);
                 btnguardaryrevisar.setVisibility(View.GONE);
             }

             @Override
             public void afterTextChanged(Editable s) {

                 if (etcantidadelegida.getText().toString().equals("") || etcantidadelegida.getText().toString().equals("0")){

                     btndverificarproducto.setEnabled(false);
                     Aux = 0d;
                     tvtotal.setText("");
                     Toast.makeText(DetalleProductoActivity.this, "Por favor ingrese un valor valido", Toast.LENGTH_SHORT).show();

                 }else {
                     btndverificarproducto.setEnabled(true);
                 }
             }
         };

        etcantidadelegida.addTextChangedListener(textWatcher);
    }

    private void RegistroPedido() {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion = PKG_WEB_HERRAMIENTAS." +
                "FN_WS_GENERA_PEDIDO&variables="; // Se debe de encontrar el metodo
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
                                Intent intent =  new Intent(DetalleProductoActivity.this,MainActivity.class);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoActivity.this);
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
                        btnguardaryagregar.setVisibility(View.VISIBLE);
                        btnguardaryrevisar.setVisibility(View.VISIBLE);

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
                                    tvprecio.setText(formateador.format((double)Double.valueOf(producto.getPrecio())));
                                    preciounitario = Double.valueOf(producto.getPrecio());

                                    if (etcantidadelegida.getText().toString().equals("")){

                                        Toast.makeText(DetalleProductoActivity.this, "Por favor ingrese un valor valido", Toast.LENGTH_SHORT).show();

                                    }else {

                                        Double cant = Double.valueOf(etcantidadelegida.getText().toString());
                                        Double preciototal = (cant*preciounitario*100.00)/100.00; // Se hace la definicion del precio que se va ha acumular

                                        // String AuxiliarMatriz[] = preciototal.toString().split(".");

                                        tvunidades.setText(producto.getUnidad().toUpperCase());
                                        Double Aux = Double.valueOf(producto.getStock());
                                        tvstock.setText(formateador.format((double)Aux) + " ");
                                        tvtotal.setText(formateador.format((double)preciototal) + " ");
                                    }
                                }
                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoActivity.this);
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

        // http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=pkg_web_herramientas.fn_ws_registra_trama_movil&variables=

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";
        Toast.makeText(this, trama, Toast.LENGTH_LONG).show();

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
