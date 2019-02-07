package com.example.sistemas.tomapedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PromocionesActivity extends AppCompatActivity {

    String id;
    Promociones promocion;
    ArrayList<Promociones> listaPromociones,listaPromocionesTipoT;
    String url,id_pedido,cantidadlista,almacen,tipoformapago,Ind,validador,trama,Index;
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<Product> products = new ArrayList<>();
    Button btnregistrarpromociones;
    ArrayList<Product> productOrders = new ArrayList<>();
    Integer indice ;
    ArrayList<String> listaTrama;
    Productos productopromocion;
    Clientes cliente;
    Usuario usuario;

    ArrayList<Productos> listaProductosPromociones,listaproductoselegidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);

        listView = (ListView) findViewById(R.id.customListView);
        listAdapter = new ListAdapter(this,products);
        id_pedido = getIntent().getStringExtra("id_pedido");
        Index = getIntent().getStringExtra("Index");
        Toast.makeText(this, id_pedido, Toast.LENGTH_SHORT).show();
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos");

        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("Ind");
        cantidadlista =  getIntent().getStringExtra("cantidadlista");
        Toast.makeText(this, cantidadlista, Toast.LENGTH_SHORT).show();
        indice = listaproductoselegidos.size();
        listaProductosPromociones = new ArrayList<>();
        btnregistrarpromociones = (Button) findViewById(R.id.btnRegistrarPromociones);
        btnregistrarpromociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //placeOrder();
            }
        });
        validador = getIntent().getStringExtra("validador");

        cantidadlista = ""+listaproductoselegidos.size();

        //id = id_pedido;

        id = "30011659";

        CalcularPromociones(id);
    }

    private void placeOrder(ArrayList<Productos> listaproductoselegidos) // Captura el listado
    {
        productOrders.clear();
        listaTrama =  new ArrayList<>();


        for(int i=0;i<listAdapter.listProducts.size();i++)
        {
            productopromocion = new Productos();
            if(listAdapter.listProducts.get(i).CartQuantity > 0)
            {
                Product products = new Product(
                        listAdapter.listProducts.get(i).ProductName
                        ,listAdapter.listProducts.get(i).ProductPrice
                        ,listAdapter.listProducts.get(i).ProductImage
                        ,listAdapter.listProducts.get(i).ProductIdArticulo
                        ,listAdapter.listProducts.get(i).UnidadProducto
                );

                productopromocion.setCodigo(listAdapter.listProducts.get(i).ProductIdArticulo);
                productopromocion.setDescripcion(listAdapter.listProducts.get(i).ProductName);
                productopromocion.setUnidad(listAdapter.listProducts.get(i).UnidadProducto);
                productopromocion.setCantidad(String.valueOf(listAdapter.listProducts.get(i).CartQuantity));
                productopromocion.setPrecio(listAdapter.listProducts.get(i).ProductPrice.toString());
                productopromocion.setPrecioAcumulado("0.0");
                productopromocion.setNumPromocion(listAdapter.listProducts.get(i).ProductName);
                productopromocion.setObservacion("Promocion");

                listaproductoselegidos.add(productopromocion);

                String Subtrama = listAdapter.listProducts.get(i).CartQuantity+"|"+listAdapter.listProducts.get(i).UnidadProducto + "|||";
                Toast.makeText(this, Subtrama, Toast.LENGTH_SHORT).show();
               // listaTrama.add(Subtrama);
                //products.CartQuantity = listAdapter.listProducts.get(i).CartQuantity;
                //productOrders.add(products);
            }
        }

        Intent intent = new Intent(PromocionesActivity.this,bandejaProductosActivity.class);
        intent.putExtra("cantidadlista",cantidadlista);
        intent.putExtra("Almacen",almacen);
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("Ind",Ind);
        intent.putExtra("id_pedido",id_pedido);

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

    public void getProduct(ArrayList<Promociones> listaPromociones) {

        Double valorcantidad;

        for (int i = 0 ; i <listaPromociones.size() ; i++) {
            valorcantidad = Double.valueOf(listaPromociones.get(i).getEquivalencia()) * Double.valueOf(listaPromociones.get(i).getCantidadBonificada());

            products.add(new Product(listaPromociones.get(i).getNumeroPromocion(),
                    valorcantidad,
                    listaPromociones.get(i).getDescripcionPromocion(),
                    listaPromociones.get(i).getCodArticulo(),
                    listaPromociones.get(i).getUnidad()
                   ));
            //Toast.makeText(this, "Este es el precio "+ listaPromociones.get(i).getPrecioSoles() , Toast.LENGTH_SHORT).show();
        }
    }

    public void CalcularPromociones(String identificador) {

        final String Id = identificador;


        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=" +
                "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTA_PROMOCION&variables=%27"+id+"%27"; // se debe actalizar la URL

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject jsonObject=new JSONObject(response1);
                            boolean success = jsonObject.getBoolean("success");
                            listaPromociones = new ArrayList<>();
                            listaPromocionesTipoT = new ArrayList<>();

                            if (success){
                                JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");

                                for(int i=0;i<jsonArray.length();i++) {

                                    promocion = new Promociones();
                                    jsonObject = jsonArray.getJSONObject(i);

                                    if (jsonObject.getString("OPCION_SELECCION").equals("S")) {

                                        promocion.setNumeroPromocion(jsonObject.getString("NRO_PROMOCION"));
                                        promocion.setCodArticulo(jsonObject.getString("COD_ARTICULO"));
                                        promocion.setDescripcionPromocion(jsonObject.getString("DESCRIPCION"));
                                        promocion.setMarcaPromocion(jsonObject.getString("DES_MARCA"));
                                        promocion.setUnidad(jsonObject.getString("UNIDAD"));
                                        promocion.setCantidadPedida(jsonObject.getString("CANTIDAD_PEDIDA"));
                                        promocion.setFlgGraba(jsonObject.getString("FLG_GRABA"));
                                        promocion.setTasaDescuento(jsonObject.getString("TASA_DESCUENTO"));
                                        promocion.setCodPresentacion(jsonObject.getString("COD_PRESENTACION"));
                                        promocion.setPrecioSoles(jsonObject.getString("PRECIO_SOLES"));
                                        promocion.setPrecioDolares(jsonObject.getString("PRECIO_DOLARES"));
                                        promocion.setStkDisponible(jsonObject.getString("STK_DISPONIBLE"));
                                        promocion.setStkFisico(jsonObject.getString("STK_FISICO"));
                                        promocion.setCantidadBonificada(jsonObject.getString("CANTIDAD_BONIFICADA"));
                                        promocion.setFactor(jsonObject.getString("FACTOR"));
                                        promocion.setFormaPromocion(jsonObject.getString("FORMA_PROMOCION"));
                                        promocion.setCodDocumento(jsonObject.getString("COD_DOCUMENTO"));
                                        promocion.setOpcionSeleccion(jsonObject.getString("OPCION_SELECCION"));
                                        promocion.setPrecioRegularSoles(jsonObject.getString("PRECIO_REGULAR_SOLES"));
                                        promocion.setPrecioRegularDolares(jsonObject.getString("PRECIO_REGULAR_DOLARES"));
                                        promocion.setValido(jsonObject.getString("VALIDO"));
                                        promocion.setEquivalencia(jsonObject.getString("EQUIVALENCIA"));
                                        listaPromociones.add(promocion);

                                    }else if(jsonObject.getString("OPCION_SELECCION").equals("T")){

                                        promocion.setNumeroPromocion(jsonObject.getString("NRO_PROMOCION"));
                                        promocion.setCodArticulo(jsonObject.getString("COD_ARTICULO"));
                                        promocion.setDescripcionPromocion(jsonObject.getString("DESCRIPCION"));
                                        promocion.setMarcaPromocion(jsonObject.getString("DES_MARCA"));
                                        promocion.setUnidad(jsonObject.getString("UNIDAD"));
                                        promocion.setCantidadPedida(jsonObject.getString("CANTIDAD_PEDIDA"));
                                        promocion.setFlgGraba(jsonObject.getString("FLG_GRABA"));
                                        promocion.setTasaDescuento(jsonObject.getString("TASA_DESCUENTO"));
                                        promocion.setCodPresentacion(jsonObject.getString("COD_PRESENTACION"));
                                        promocion.setPrecioSoles(jsonObject.getString("PRECIO_SOLES"));
                                        promocion.setPrecioDolares(jsonObject.getString("PRECIO_DOLARES"));
                                        promocion.setStkDisponible(jsonObject.getString("STK_DISPONIBLE"));
                                        promocion.setStkFisico(jsonObject.getString("STK_FISICO"));
                                        promocion.setCantidadBonificada(jsonObject.getString("CANTIDAD_BONIFICADA"));
                                        promocion.setFactor(jsonObject.getString("FACTOR"));
                                        promocion.setFormaPromocion(jsonObject.getString("FORMA_PROMOCION"));
                                        promocion.setCodDocumento(jsonObject.getString("COD_DOCUMENTO"));
                                        promocion.setOpcionSeleccion(jsonObject.getString("OPCION_SELECCION"));
                                        promocion.setPrecioRegularSoles(jsonObject.getString("PRECIO_REGULAR_SOLES"));
                                        promocion.setPrecioRegularDolares(jsonObject.getString("PRECIO_REGULAR_DOLARES"));
                                        promocion.setValido(jsonObject.getString("VALIDO"));
                                        promocion.setEquivalencia(jsonObject.getString("EQUIVALENCIA"));
                                        listaPromocionesTipoT.add(promocion);
                                    }
                                }

                                if (listaPromocionesTipoT.size() == 0){

                                    Toast.makeText(PromocionesActivity.this, "No existe promociones T", Toast.LENGTH_SHORT).show();

                                }else {

                                    for (int i = 0; i < listaPromocionesTipoT.size();i++){

                                        productopromocion = new Productos();
                                        productopromocion.setNumPromocion(listaPromocionesTipoT.get(i).getNumeroPromocion());
                                        productopromocion.setCodigo(id_pedido);
                                        productopromocion.setIdProducto(listaPromocionesTipoT.get(i).getCodArticulo());
                                        productopromocion.setDescripcion(listaPromocionesTipoT.get(i).getDescripcionPromocion());
                                        productopromocion.setUnidad(listaPromocionesTipoT.get(i).getUnidad());
                                        productopromocion.setCantidad(listaPromocionesTipoT.get(i).getCantidadBonificada());
                                        productopromocion.setPrecio(listaPromocionesTipoT.get(i).getPrecioSoles());
                                        productopromocion.setPrecioAcumulado("0.0");
                                        productopromocion.setObservacion("Promocion");
                                        listaproductoselegidos.add(productopromocion);
                                        listaProductosPromociones.add(productopromocion);
                                    }
                                }

                                getProduct(listaPromociones);
                                listView.setAdapter(listAdapter);

                                btnregistrarpromociones.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        placeOrder(listaproductoselegidos);

                                        // ActualizarProducto(trama);

                                        Intent intent = new Intent(PromocionesActivity.this,IntermediaActivity.class);

                                        intent.putExtra("cantidadlista",cantidadlista);
                                        intent.putExtra("Almacen",almacen);
                                        intent.putExtra("TipoPago",tipoformapago);
                                        intent.putExtra("Ind",Ind);
                                        intent.putExtra("Index",Index);
                                        intent.putExtra("id_pedido",id_pedido);
                                        intent.putExtra("validador","false");

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
                                });
                            }else{
                                AlertDialog.Builder build1 = new AlertDialog.Builder(PromocionesActivity.this);
                                build1.setTitle("No se han encontrado Promociones")
                                        .setNegativeButton("Regresar",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(PromocionesActivity.this,bandejaProductosActivity.class);

                                                intent.putExtra("cantidadlista",cantidadlista);
                                                intent.putExtra("Almacen",almacen);
                                                intent.putExtra("TipoPago",tipoformapago);
                                                intent.putExtra("Ind",Ind);
                                                intent.putExtra("Index",Index);
                                                intent.putExtra("id_pedido",id_pedido);
                                                intent.putExtra("validador","false");
                                                //intent.putExtra("retorno", "retorno");

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
                                        })

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
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public interface OnRefreshViewListner{

        public void refreshView();

    }
}
