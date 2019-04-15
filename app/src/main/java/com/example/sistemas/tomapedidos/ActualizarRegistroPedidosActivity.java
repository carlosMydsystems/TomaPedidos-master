package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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
import com.example.sistemas.tomapedidos.Entidades.ClienteSucursal;
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
             tvtotalelegido, tvpreciorealelegido,tvunidadelegida,tvpreciorealjsonelegido,tvtasaelegida;
    Productos productos;
    Button   btndverificarproductoelegido,btnactualizarproductoelegido;
    Clientes cliente;
    ArrayList<Productos> listaproductoselegidos,listaProductos;
    EditText etcantprodelegida;
    Double preciounitario,cantidad, Aux,precioDouble;
    String url,almacen,position,tipoformapago,id_pedido,Index,Mensaje;
    ProgressDialog progressDialog;
    Productos producto;
    ArrayList<String> listaProducto;
    Usuario usuario;
    BigDecimal redondeado,precioBigTotal;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    Double Descuento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_registro_pedidos);

        etcantprodelegida =  findViewById(R.id.etCantProdElegida);
        listaproductoselegidos = new ArrayList<>();
        productos  = new Productos();
        listaProductos=new ArrayList<>();

        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");
        productos = (Productos) getIntent().getSerializableExtra("Producto");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        almacen =  getIntent().getStringExtra("Almacen");
        position =  getIntent().getStringExtra("position");
        Index =  getIntent().getStringExtra("Index");
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos");

        tipoformapago =  getIntent().getStringExtra("TipoPago");
        id_pedido = getIntent().getStringExtra("id_pedido");
        etcantprodelegida.setText(productos.getCantidad());
        etcantprodelegida.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        tvcodprodelegido =  findViewById(R.id.tvCodProdElegido);
        tvnomprodelegido = findViewById(R.id.tvNomProdElegido);
        tvalmprodelegido = findViewById(R.id.tvAlmProdElegido);
        btndverificarproductoelegido = findViewById(R.id.btnVerificarElegido);
        tvpreciorealelegido = findViewById(R.id.tvPrecioElegido);
        btnactualizarproductoelegido = findViewById(R.id.btnActualizarElegido);
        tvunidadelegida =  findViewById(R.id.tvUnidadElegida);
        tvpreciorealjsonelegido = findViewById(R.id.tvPrecioRealJsonElegido);
        tvtasaelegida = findViewById(R.id.tvTasaElegida);

        btndverificarproductoelegido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btndverificarproductoelegido.setVisibility(View.GONE);
                progressDialog =  new ProgressDialog(ActualizarRegistroPedidosActivity.this);
                progressDialog.setCancelable(false);
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
                Double validarStock;
                Double stockDouble = Double.valueOf(tvstockelegido.getText().toString().replace(",",""));
                Double cantidadElegida = Double.valueOf(etcantprodelegida.getText().toString());

                    validarStock = stockDouble - cantidadElegida;

                if (validarStock < 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarRegistroPedidosActivity.this)
                            .setMessage("El Stock es insuficiente, por favor ingrese una cantidad menor");
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create()
                           .show();

                }else {

                    if (etcantprodelegida.getText().toString().equals("")) {

                    } else {

                        String trama = id_pedido + "|D|" + listaproductoselegidos.get(Integer.valueOf(position)).getIndice() + "|" + etcantprodelegida.getText() + "|" +
                                    productos.getCodigo() + "|" + tvpreciorealjsonelegido.getText().toString().replace(",", "") +
                                    "|" + tvtasaelegida.getText().toString().trim() + "|" + productos.getNumPromocion().trim() + "|" + productos.getPresentacion() +
                                    "|" + productos.getEquivalencia() + "|N";  // Tasas

                        ActualizarProducto(trama);
                        productos.setCantidad(etcantprodelegida.getText().toString());
                        preciounitario = Double.valueOf(tvprecioelegido.getText().toString().replace(",",""));
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

                        Intent intent = new Intent(ActualizarRegistroPedidosActivity.this, bandejaProductosActivity.class);
                        intent.putExtra("TipoPago", tipoformapago);
                        intent.putExtra("id_pedido", id_pedido);
                        intent.putExtra("validador", "true");
                        intent.putExtra("Almacen", almacen);
                        intent.putExtra("Index", Index);

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
                        bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                        intent.putExtras(bundle4);

                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

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

                                response = response.trim();
                                Boolean condicion = false,error = false;

                                String Aux = response.replace("{","|");
                                Aux = Aux.replace("}","|");
                                Aux = Aux.replace("[","|");
                                Aux = Aux.replace("]","|");
                                Aux = Aux.replace("\"","|");
                                Aux = Aux.replace(","," ");
                                Aux = Aux.replace("|","");
                                Aux = Aux.replace(":"," ");
                                String partes[] = Aux.split(" ");

                                for (String palabras : partes){
                                    if (condicion){ Mensaje += palabras+" "; }
                                    if (palabras.equals("ERROR")){
                                        condicion = true;
                                        error = true;
                                    }
                                }

                                if (error) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarRegistroPedidosActivity.this);
                                    builder.setTitle("Alerta !");
                                    builder.setMessage(Mensaje);
                                    builder.setNegativeButton("Aceptar",null);
                                    builder.create().show();

                                    Mensaje = "";

                                }else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        producto = new Productos();
                                        jsonObject = jsonArray.getJSONObject(i);
                                        producto.setCodigo(jsonObject.getString("COD_ARTICULO"));//
                                        producto.setMarca(jsonObject.getString("DES_MARCA"));//
                                        producto.setDescripcion(jsonObject.getString("DES_ARTICULO"));//
                                        producto.setPrecio(jsonObject.getString("PRECIO_SOLES"));

                                        precioDouble = Double.valueOf(jsonObject.getString("PRECIO_SOLES")) * (1 - Double.valueOf(jsonObject.getString("TASA_DESCUENTO")) / 100);
                                        BigDecimal precioBig = new BigDecimal(precioDouble.toString());
                                        precioBig = precioBig.setScale(4, RoundingMode.HALF_EVEN);


                                        BigDecimal precioBig1 = new BigDecimal(producto.getPrecio());
                                        precioBig1 = precioBig1.setScale(4, RoundingMode.HALF_EVEN);

                                        Descuento = (1 - Double.valueOf(jsonObject.getString("TASA_DESCUENTO")) / 100);
                                        precioDouble = Double.valueOf(precioBig1.toString()) * Descuento * Double.valueOf(etcantprodelegida.getText().toString());
                                        precioBigTotal = new BigDecimal(precioDouble.toString());
                                        precioBigTotal = precioBigTotal.setScale(2, RoundingMode.HALF_EVEN);
                                        tvprecioelegido.setText(precioBig.toString());
                                        tvpreciorealjsonelegido.setText(jsonObject.getString("PRECIO_SOLES"));
                                        precioDouble = Double.valueOf(precioBig.toString()) * Double.valueOf(etcantprodelegida.getText().toString());
                                        producto.setStock(jsonObject.getString("STOCK_DISPONIBLE"));
                                        producto.setUnidad(jsonObject.getString("UND_MEDIDA"));
                                        producto.setEquivalencia(jsonObject.getString("EQUIVALENCIA"));
                                        producto.setTasaDescuento(jsonObject.getString("TASA_DESCUENTO"));
                                        tvtasaelegida.setText(jsonObject.getString("TASA_DESCUENTO"));
                                        producto.setPresentacion(jsonObject.getString("COD_PRESENTACION"));
                                        producto.setAlmacen(almacen);

//--------------------------------------------------------------------------------
/*
                                    producto.setPrecio(jsonObject.getString("PRECIO_SOLES"));

                                    BigDecimal precioBig1 = new BigDecimal(producto.getPrecio());
                                    precioBig1 = precioBig1.setScale(2,RoundingMode.HALF_EVEN);
                                    Descuento = (1 - Double.valueOf(jsonObject.getString("TASA_DESCUENTO"))/100  );
                                    precioDouble = Double.valueOf(precioBig1.toString()) * Descuento *  Double.valueOf(etcantidadelegida.getText().toString());
                                    precioBigTotal = new BigDecimal(precioDouble.toString());
                                    precioBigTotal = precioBigTotal.setScale(2,RoundingMode.HALF_EVEN);
                                    tvtotal.setText(""+precioBigTotal);
                                    precioUnitarioDouble = Double.valueOf(producto.getPrecio()) * Descuento;
                                    precioBigUnitario = new BigDecimal(precioUnitarioDouble.toString());
                                    precioBigUnitario = precioBigUnitario.setScale(4,RoundingMode.HALF_EVEN);
                                    tvprecio.setText(precioBigUnitario.toString());
                                    tvpreciorealjson.setText(jsonObject.getString("PRECIO_SOLES"));
                                    producto.setStock(jsonObject.getString("STOCK_DISPONIBLE"));
                                    producto.setUnidad(jsonObject.getString("UND_MEDIDA"));
                                    producto.setEquivalencia(jsonObject.getString("EQUIVALENCIA"));
                                    producto.setTasaDescuento(jsonObject.getString("TASA_DESCUENTO"));
                                    producto.setPresentacion(jsonObject.getString("COD_PRESENTACION"));
                                    producto.setAlmacen(almacen);
                                    tvtasa.setText(producto.getTasaDescuento());

*/

                                        if (etcantprodelegida.getText().toString().equals("")) {

                                        } else {

                                            BigDecimal precioTotalBig = new BigDecimal(precioDouble.toString());
                                            precioTotalBig = precioTotalBig.setScale(2, RoundingMode.HALF_UP);
                                            tvunidadelegida.setText(producto.getUnidad().toUpperCase());
                                            Double Aux1 = Double.valueOf(producto.getStock());
                                            tvstockelegido.setText(formateador.format((double) Aux1) + " ");
                                            tvtotalelegido.setText(precioTotalBig.toString());
                                        }
                                    }
                                }

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarRegistroPedidosActivity.this);
                                builder.setMessage("No se llegaron a encontrar registros")
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

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        response = response.toString().trim();

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
