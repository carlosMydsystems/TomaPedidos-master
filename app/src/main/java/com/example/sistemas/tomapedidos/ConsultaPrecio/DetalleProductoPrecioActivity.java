package com.example.sistemas.tomapedidos.ConsultaPrecio;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.DctoxVolumen;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.ListadoAlmacenActivity;
import com.example.sistemas.tomapedidos.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Dolares;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Soles;

public class DetalleProductoPrecioActivity extends AppCompatActivity {


    Usuario usuario;
    Clientes cliente;
    Productos producto;
    String trama,url,StrAux;
    ListView lvPrecioxVolumen;
    DctoxVolumen dctoxVolumen;
    ArrayList<DctoxVolumen> listaDctoxVolumen;
    ArrayList<String> listaDsctoxVolumenStr;
    ImageButton ibVolverDetalleProductoPrecio;
    TextView tvResumenUnidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto_precio);


        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes) getIntent().getSerializableExtra("Cliente");
        producto = (Productos) getIntent().getSerializableExtra("Producto");
        ibVolverDetalleProductoPrecio = findViewById(R.id.ibVolverDetalleProductoPrecio);
        trama = usuario.getLugar()+"|"+producto.getCodigo()+"|"+ cliente.getCodCliente()+"|"+producto.getPrecio();
        lvPrecioxVolumen = findViewById(R.id.lvPrecioxVolumen);
        tvResumenUnidades = findViewById(R.id.tvResumenUnidades);

        if (usuario.getMoneda().equals("1")){

            StrAux = "DESCRIPCION : "+ producto.getDescripcion()+"\n UNIDAD : " + producto.getUnidad()+"\t\t\t\t\t PRECIO : "+Soles+" "+producto.getPrecio();
        }else {

            StrAux = "DESCRIPCION : "+ producto.getDescripcion()+"\n UNIDAD : " + producto.getUnidad()+"\t\t\t\t\t PRECIO : "+Dolares+" "+producto.getPrecio();
        }


        tvResumenUnidades.setText(StrAux);

        ConsultarPreciosVolumen(trama);

        ibVolverDetalleProductoPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleProductoPrecioActivity.this, IntemedioDetalleProductoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Producto",producto);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente",cliente);
                intent.putExtras(bundle1);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Usuario",usuario);
                intent.putExtras(bundle2);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ConsultarPreciosVolumen(String tramaConsultaPrecioVolumen) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        listaDctoxVolumen = new ArrayList<>();
        listaDsctoxVolumenStr = new ArrayList<>();

            url = ejecutaFuncionCursorTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_DSTO_VOLUMEN&variables='" + tramaConsultaPrecioVolumen + "'";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    dctoxVolumen = new DctoxVolumen();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    dctoxVolumen.setRango(jsonObject.getString("RANGO"));
                                    dctoxVolumen.setDesde(jsonObject.getString("DESDE"));
                                    dctoxVolumen.setHasta(jsonObject.getString("HASTA"));
                                    dctoxVolumen.setDescuento(jsonObject.getString("DESCUENTO"));
                                    dctoxVolumen.setPrecio(jsonObject.getString("PRECIO"));
                                    listaDctoxVolumen.add(dctoxVolumen);
                                    if (usuario.getMoneda().equals("1")){

                                        listaDsctoxVolumenStr.add("\t"+ " Desde :\t" + formatoDecimal(dctoxVolumen.getDesde())+
                                                " \t\t\t\tHasta :\t" + formatoDecimal(dctoxVolumen.getHasta()) +"\n"+
                                                "\t"+" Dscto : "+formatoDecimal(dctoxVolumen.getDescuento()) +
                                                "%  \t\t\t - \t\t\t  Precio : "+Soles+" " + formatoDecimal(dctoxVolumen.getPrecio()));

                                    }else {
                                        listaDsctoxVolumenStr.add("\t" + " Desde :\t" + formatoDecimal(dctoxVolumen.getDesde()) +
                                                " \t\t\t\tHasta :\t" + formatoDecimal(dctoxVolumen.getHasta()) + "\n" +
                                                "\t" + " Dscto : " + formatoDecimal(dctoxVolumen.getDescuento()) +
                                                "%  \t\t\t - \t\t\t  Precio : " + Dolares + " " + formatoDecimal(dctoxVolumen.getPrecio()));
                                    }
                                }

                                ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                        CustomListAdapter(DetalleProductoPrecioActivity.this, R.layout.custom_list, listaDsctoxVolumenStr);
                                lvPrecioxVolumen.setAdapter(listAdapter);

                            } else {
                                //listaCliente.clear();
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item, listaDsctoxVolumenStr);

                                lvPrecioxVolumen.setAdapter(adapter);
                                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoPrecioActivity.this);
                                builder.setCancelable(false);
                                builder.setMessage("No se llego a encontrar el registro")
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
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

    private String formatoDecimal(String valor) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        return formateador.format(Double.valueOf(valor.replace(",","")));

    }


}