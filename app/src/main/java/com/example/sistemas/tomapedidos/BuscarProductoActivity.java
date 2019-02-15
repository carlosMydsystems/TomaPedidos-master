package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class BuscarProductoActivity extends AppCompatActivity {

    RadioGroup rggrupoproducto;
    RadioButton rbnombreproducto, rbcodigoproducto;
    Button btnbuscarProducto,btnregresarproducto;
    ArrayList<Productos> listaProductos,listaproductoselegidos;
    Productos producto;
    ListView lvProducto;
    ArrayList<String> listaProducto;
    Clientes cliente;
    EditText etproducto,etglosa;
    String url,Tipobusqueda = "Nombre",tipoPago,almacen,Ind= "1";
    ProgressDialog progressDialog;
    Usuario usuario;
    String id_Pedido,fechaRegistro,precio = "0.0";
    String indice,validador,Glosa,id_pedido,Index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        cliente  = new Clientes();
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        tipoPago = getIntent().getStringExtra("TipoPago");
        almacen = getIntent().getStringExtra("Almacen");
        indice = getIntent().getStringExtra("indice");
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

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

        btnregresarproducto = findViewById(R.id.btnRegresarProducto);

        // Calendario

        Calendar fecha = Calendar.getInstance();
        final Integer dia = fecha.get(Calendar.DAY_OF_MONTH);
        final Integer mes = fecha.get(Calendar.MONTH) + 1;
        Integer year = fecha.get(Calendar.YEAR);
        final Integer hora =  fecha.get(Calendar.HOUR_OF_DAY);
        final Integer minuto = fecha.get(Calendar.MINUTE);
        final Integer segundo = fecha.get(Calendar.SECOND);

        fechaRegistro =   formatonumerico(dia) + "/" + formatonumerico(mes) +"/"+ year.toString() +
                "%20" + formatonumerico(hora)+":"+formatonumerico(minuto)+":"+formatonumerico(segundo);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnbuscarProducto.getWindowToken(), 0);

if (listaproductoselegidos.size() != 0) {
    btnregresarproducto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoActivity.this)
                    .setMessage("Esta seguro que desea regresar, los cambios actuales serán eliminados")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(BuscarProductoActivity.this, bandejaProductosActivity.class);

                    intent.putExtra("TipoPago", tipoPago);
                    intent.putExtra("indice", indice);
                    intent.putExtra("id_pedido", id_Pedido);
                    intent.putExtra("Almacen", almacen);
                    intent.putExtra("Index", Index);
                    intent.putExtra("retorno", "retorno");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cliente", cliente);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Usuario", usuario);
                    intent.putExtras(bundle1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("listaProductoselegidos", listaproductoselegidos);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                    finish();

                }
            });

            builder.create()
                    .show();
        }
    });
}else {

    btnregresarproducto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoActivity.this)
                    .setMessage("Esta seguro que desea regresar, se perderan todos los pedidos ")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent =  new Intent(BuscarProductoActivity.this,ListadoFormaPagoActivity.class);
                    intent.putExtra("Almacen",almacen);
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

            builder.create()
                    .show();
        }
    });
}


        if (listaproductoselegidos.size()==0 && validador.equals("true")){

            String Trama =  id_pedido+"|C|0|"+almacen +"|" +cliente.getCodCliente()+"|" +usuario.
                    getCodVendedor() + "|"+tipoPago+"|"+fechaRegistro+"|"+fechaRegistro +"|0.00|"+etglosa.getText().toString()+"|";

            ActualizarProducto(Trama);

        }

        btnbuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etproducto.getWindowToken(), 0);

                btnbuscarProducto.setVisibility(View.GONE);
                btnregresarproducto.setVisibility(View.GONE);
                progressDialog = new ProgressDialog(BuscarProductoActivity.this);
                progressDialog.setMessage("Cargando...");
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();

                Boolean verficador = false;
                Integer posicion=0;

                if (listaproductoselegidos.size()!=0){

                    for (int i =0 ; i < listaproductoselegidos.size();i++){

                        if( etproducto.getText().toString().equals(listaproductoselegidos.get(i).getCodigo())){
                            verficador = true;
                            posicion = i;
                        }
                    }
                    if( verficador){
                        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoActivity.
                                this).setMessage("EL Producto "+listaproductoselegidos.get(posicion).getCodigo()+" ya se encuentra registrado");
                        builder
                                .setPositiveButton("Aceptar",null)
                                .create()
                                .show();

                        progressDialog.dismiss();
                        btnbuscarProducto.setVisibility(View.VISIBLE);
                        btnregresarproducto.setVisibility(View.VISIBLE);
                        etproducto.setText("");
                        ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                CustomListAdapter(BuscarProductoActivity.this, R.layout.custom_list, listaProducto);
                        listAdapter.clear();
                        lvProducto.setAdapter(listAdapter);

                    }else{
                        buscarproducto(etproducto.getText().toString().replace(" ",""),Tipobusqueda);
                    }
                }else{
                    buscarproducto(etproducto.getText().toString().replace(" ",""),Tipobusqueda);
                }
            }
        });

        lvProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.dismiss();
                producto = new Productos();
                cliente = new Clientes();
                cliente = (Clientes)getIntent().getSerializableExtra("Cliente");

                Intent intent =  new Intent(BuscarProductoActivity.this,DetalleProductoActivity.class);
                intent.putExtra("TipoPago",tipoPago);
                intent.putExtra("Almacen",almacen);
                intent.putExtra("id_pedido",id_pedido);
                intent.putExtra("Index",Index);

                Integer  aux = listaproductoselegidos.size() + 1;
                intent.putExtra("indice",aux.toString());
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

    private void buscarproducto(String numero, String tipoConsulta) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // Este reemplazo se da debido al simbolo % la cual cuando no esta asociado a un caracter se vuelve una llamada ilegal
        numero = numero.replace("%","%25");  
        numero = numero.toUpperCase(); // se convierten los caracteres a Mayusucla
        // se llega a definir la forma en la que se va a acceder a cada uno de los metodos
        if (tipoConsulta.equals("Nombre")) {

            url = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=" +
                 "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_PRODUCTO&variables='"+almacen+"|"+usuario.getLugar()+"|"+numero+"||"+cliente.getCodCliente()+"|||1'";

        }else {

            url = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=" +
                  "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_PRODUCTO&variables='"+almacen+"|"+usuario.getLugar()+"|"+numero+"||"+cliente.getCodCliente()+"|||1'";
        }

        listaProducto = new ArrayList<>();

        // se usa un metodo de acceso por medio del GET
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String Mensaje = "";
                        progressDialog.dismiss();
                        btnbuscarProducto.setVisibility(View.VISIBLE);
                        btnregresarproducto.setVisibility(View.VISIBLE);

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
                                                BuscarProductoActivity.this);
                                        dialog.setMessage(Mensaje)
                                                .setNegativeButton("Regresar", null)
                                                .create()
                                                .show();
                                    } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
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
                                        listaProducto.add(producto.getCodigo() + " - " + producto.getDescripcion());
                                    }

                                    // Se hace un llamado al adaptador personalizado asociado al SML custom_list

                                    ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                            CustomListAdapter(BuscarProductoActivity.this, R.layout.custom_list, listaProducto);
                                    lvProducto.setAdapter(listAdapter);
                                }
                            }else {
                                listaProducto.clear();
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item,listaProducto);
                                lvProducto.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuscarProductoActivity.this);
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

    private String formatonumerico (Integer numero){

        String numeroString = numero.toString();
        if (numero <= 9){
            numeroString = "0"+ numero.toString();
        }
        return  numeroString;
    }

    private void ActualizarProducto(String trama) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){
                            Toast.makeText(BuscarProductoActivity.this, "Se hizo el registro", Toast.LENGTH_SHORT).show();
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
