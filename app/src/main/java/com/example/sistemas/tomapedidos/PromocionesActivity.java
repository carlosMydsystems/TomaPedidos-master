package com.example.sistemas.tomapedidos;

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
import com.example.sistemas.tomapedidos.Entidades.Promociones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PromocionesActivity extends AppCompatActivity {

    String id;
    Promociones promocion;
    ArrayList<Promociones> listaPromociones,listaPromocionesTipoT;
    Button btnRegistrarPromociones;
    Boolean validador = true;
    String url;
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<Product> products = new ArrayList<>();
    Button btnregistrarpromociones;
    ArrayList<Product> productOrders = new ArrayList<>();
    Integer Index ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);

        listView = (ListView) findViewById(R.id.customListView);
        listAdapter = new ListAdapter(this,products);
        Index = Integer.valueOf(getIntent().getStringExtra("Indice"));

        btnregistrarpromociones = (Button) findViewById(R.id.btnRegistrarPromociones);
        btnregistrarpromociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //placeOrder();
                insertaPromocion(listaPromocionesTipoT,id);
            }
        });

        id = "22011309";
        CalcularPromociones(id);

    }

    private void placeOrder()
    {
        productOrders.clear();
        for(int i=0;i<listAdapter.listProducts.size();i++)
        {
            if(listAdapter.listProducts.get(i).CartQuantity > 0)
            {
                Product products = new Product(
                        listAdapter.listProducts.get(i).ProductName
                        ,listAdapter.listProducts.get(i).ProductPrice
                        ,listAdapter.listProducts.get(i).ProductImage
                );
                products.CartQuantity = listAdapter.listProducts.get(i).CartQuantity;
                productOrders.add(products);
            }
        }
    }

    public void getProduct(ArrayList<Promociones> listaPromociones) {

        Double valorcantidad;

        for (int i = 0 ; i <listaPromociones.size() ; i++) {
            valorcantidad = Double.valueOf(listaPromociones.get(i).getEquivalencia()) * Double.valueOf(listaPromociones.get(i).getCantidadBonificada());
            products.add(new Product(listaPromociones.get(i).getNumeroPromocion(),valorcantidad,listaPromociones.get(i).getDescripcionPromocion()));
        }
    }

    private void CalcularPromociones(String id) {
        String url;
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

                                Toast.makeText(PromocionesActivity.this, "Paso", Toast.LENGTH_SHORT).show();

                                getProduct(listaPromociones);
                                listView.setAdapter(listAdapter);

                            }else{
                                AlertDialog.Builder build1 = new AlertDialog.Builder(PromocionesActivity.this);
                                build1.setTitle("Usuario  o Clave incorrecta")
                                        .setNegativeButton("Regresar",null)
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

    private void insertaPromocion(ArrayList<Promociones> listapromocioneselegidas , String id){

        if (validador){

            for (int i =0; i<listapromocioneselegidas.size();i++){

                String indice = String.valueOf(i+1);

                Double preciototal = Double.valueOf(listapromocioneselegidas.get(i).getPrecioSoles().trim())
                       * Double.valueOf(listapromocioneselegidas.get(i).getCantidadBonificada());

                Integer auxEntero = Index+i+1;

                String campoenviado = id+"|D|"+auxEntero.toString()+"|"+listapromocioneselegidas.get(i).getCantidadBonificada()+"|"+
                       listapromocioneselegidas.get(i).getCodArticulo()+"|"+ listapromocioneselegidas.get(i).
                       getPrecioSoles()+"|"+preciototal.toString() +"|";

                Toast.makeText(this, campoenviado, Toast.LENGTH_SHORT).show();

                // ActualizarProducto(campoenviado);
            }
            //validador = false;
        }
    }

}
