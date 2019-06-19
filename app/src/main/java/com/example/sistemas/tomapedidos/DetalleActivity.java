package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.tomapedidos.Entidades.DetallePedido;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.Utilitarios.Utilitario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;

public class DetalleActivity extends AppCompatActivity {

    String numeroPedido,url,fecha,monedaPedido;
    ListView lvdetallepedidos;
    DetallePedido detallepedido;
    ArrayList<DetallePedido> listaDetallePedido;
    ArrayList<String> listadetallemostrarPedido;
    TextView tvtitulodinamicoPedidos;
    ImageButton ibretornomenuConsulta;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        numeroPedido = getIntent().getStringExtra("NroPedido");
        lvdetallepedidos = findViewById(R.id.lvDetallePedido);
        tvtitulodinamicoPedidos = findViewById(R.id.tvtitulodinamicoPedidos);
        ibretornomenuConsulta = findViewById(R.id.imgPromocionElegida);
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        fecha = getIntent().getStringExtra("fecha");
        monedaPedido = getIntent().getStringExtra("monedaPedido");

        ibretornomenuConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleActivity.this,ConsutlasActivity.class);
                intent.putExtra("fecha",fecha);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        if(Utilitario.isOnline(getApplicationContext())){

            VerificarCantidad(numeroPedido);

        }else{

            AlertDialog.Builder build = new AlertDialog.Builder(DetalleActivity.this);
            build.setTitle("Atención .. !");
            build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
            build.setCancelable(false);
            build.setNegativeButton("ACEPTAR",null);
            build.create().show();

        }
    }

    private void VerificarCantidad(String numeroPedido) {

        final ProgressDialog progressDialog = new ProgressDialog(DetalleActivity.this);
        progressDialog.setMessage("...Cargando");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        listaDetallePedido = new ArrayList<>();
        listadetallemostrarPedido = new ArrayList<>();
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_DET_PEDIDO&variables='"+numeroPedido+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Double precioAcumulado =0.0;

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){

                                for(int i=0;i<jsonArray.length();i++) {
                                    detallepedido = new DetallePedido();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    detallepedido.setNroPedido(jsonObject.getString("NRO_PEDIDO"));
                                    detallepedido.setCodArticulo(jsonObject.getString("COD_ARTICULO"));
                                    detallepedido.setArticulo(jsonObject.getString("ARTICULO"));
                                    detallepedido.setUndMedida(jsonObject.getString("UND_MEDIDA"));
                                    detallepedido.setCantidad(jsonObject.getString("CANTIDAD"));
                                    detallepedido.setPrecio(jsonObject.getString("PRECIO"));
                                    detallepedido.setSubtotal(jsonObject.getString("SUBTOTAL"));
                                    Double preciodb = Double.valueOf(detallepedido.getSubtotal().replace(",",""));
                                    precioAcumulado = precioAcumulado + preciodb;
                                    Double Aux = Double.valueOf(detallepedido.getPrecio().replace(",", ""));
                                    Double Aux1 = Double.valueOf(detallepedido.getSubtotal().replace(",", ""));
                                    listadetallemostrarPedido.add(detallepedido.getCodArticulo() + " - "
                                            + detallepedido.getArticulo() + "\n" + "Cant : " + detallepedido.getCantidad()+"\t\t\t\t\t\t\t\t\t\t\t\t\t Unidad : "+
                                            detallepedido.getUndMedida()+ "\n" + "Precio : "+monedaPedido+" " + formateador.format((double) Aux)+"\t\t\t\t\t\t Subtotal : "+monedaPedido+" "+
                                            formateador.format((double) Aux1)) ;
                                    listaDetallePedido.add(detallepedido);
                                }
                                progressDialog.dismiss();

                                String cadenaTituloAux = "Productos : " + listaDetallePedido.size() + "   |  Monto : "+monedaPedido+" " + formateador.format(precioAcumulado)+ "";
                                tvtitulodinamicoPedidos.setText(cadenaTituloAux);

                                ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                        CustomListAdapter(DetalleActivity.this, R.layout.custom_list, listadetallemostrarPedido);

                                lvdetallepedidos.setAdapter(listAdapter);
                                lvdetallepedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Double Aux = Double.valueOf(listaDetallePedido.get(position).getSubtotal().replace(",", ""));
                                    Double Aux1 = Double.valueOf(listaDetallePedido.get(position).getPrecio().replace(",", ""));

                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleActivity.this);
                                        builder.setCancelable(false)
                                            .setMessage(
                                                "Codigo\t\t\t:\t\t" + listaDetallePedido.get(position).getCodArticulo() + "\n" +
                                                "Nombre\t\t\t:\t\t" + listaDetallePedido.get(position).getArticulo() + "\n" +
                                                "Unidad\t\t\t:\t\t" + listaDetallePedido.get(position).getUndMedida() + "\n" +
                                                "Cantidad\t\t:\t\t" + listaDetallePedido.get(position).getCantidad() + "\n" +
                                                "Precio\t\t\t\t:\t\t"+monedaPedido+" " + formateador.format((double) Aux1) + "\n" +
                                                "Subtotal\t\t\t:\t\t"+monedaPedido+" " + formateador.format((double) Aux))
                                            .setNegativeButton("Aceptar", null)
                                            .create()
                                            .show();
                                    }
                                });
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleActivity.this);
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
                progressDialog.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleActivity.this);
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