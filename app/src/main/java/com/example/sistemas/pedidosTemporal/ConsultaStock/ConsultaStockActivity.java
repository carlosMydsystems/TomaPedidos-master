package com.example.sistemas.pedidosTemporal.ConsultaStock;

import android.app.ProgressDialog;
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
import com.example.sistemas.pedidosTemporal.Entidades.Productos;
import com.example.sistemas.pedidosTemporal.Entidades.Stock;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.ListadoAlmacenActivity;
import com.example.sistemas.pedidosTemporal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionCursorTestMovil;

public class ConsultaStockActivity extends AppCompatActivity {

    ListView lvStock;
    ProgressDialog progressDialog;
    String url;
    ArrayList<String> listaStock;
    Stock stock;
    ArrayList<Stock> listaStockDisponible;
    TextView tvTituloCodAlamcen,tvTituloStock,tvCodigoStock,tvNombreStock,tvMarcaStock,tvUnidadStock;
    Productos productos;
    ImageButton imgConsultaStockMenu;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_stock);

        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        productos = (Productos)getIntent().getSerializableExtra("Producto");
        lvStock = findViewById(R.id.lvStock);
        tvTituloCodAlamcen = findViewById(R.id.tvTitulocodalmacen);
        tvTituloStock = findViewById(R.id.tvTituloStock);
        tvCodigoStock = findViewById(R.id.tvCodigoStock);
        tvNombreStock = findViewById(R.id.tvNombreStock);
        tvMarcaStock = findViewById(R.id.tvMarcaStock);
        tvUnidadStock = findViewById(R.id.tvUnidadStock);
        tvCodigoStock.setText(productos.getCodigo());
        tvMarcaStock.setText(productos.getMarca());
        tvUnidadStock.setText(productos.getUnidad());
        tvNombreStock.setText(productos.getDescripcion());
        buscarproducto(productos.getCodigo().toString());
        imgConsultaStockMenu = findViewById(R.id.imgPromocionElegida);
        imgConsultaStockMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultaStockActivity.this, BuscarProductoStockActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Usuario",usuario);
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void buscarproducto(String numero) {

        progressDialog = new ProgressDialog(ConsultaStockActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        numero = numero.trim();

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_STOCK&variables=%27"+numero+"%27";

        listaStock = new ArrayList<>();
        listaStockDisponible = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String Mensaje = "";

                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                    simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
                    simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
                    final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo
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

                            progressDialog.dismiss();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    ConsultaStockActivity.this);
                            dialog.setMessage(Mensaje)
                                    .setNegativeButton("Regresar", null)
                                    .create()
                                    .show();
                        } else {

                            listaStock.clear();
                            listaStock.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                stock = new Stock();
                                stock.setCodalmacen(jsonObject.getString("COD_ALMACEN"));
                                stock.setStockDisponible(jsonObject.getString("STK_DISPONIBLE"));
                                listaStockDisponible.add(stock);
                                listaStock.add("\t\t\t\t\t"+stock.getCodalmacen() +
                                        "\t\t\t\t\t\t\t- \t\t\t\t\t\t\t\t\t" + formateador.format(Double.valueOf(stock.getStockDisponible() )));
                            }

                            tvTituloCodAlamcen.setVisibility(View.VISIBLE);
                            tvTituloStock.setVisibility(View.VISIBLE);
                            ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                    CustomListAdapter(ConsultaStockActivity.this, R.layout.custom_list, listaStock);
                            lvStock.setAdapter(listAdapter);
                            progressDialog.dismiss();
                        }

                    }else {
                        progressDialog.dismiss();
                        listaStock.clear();

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                , R.layout.support_simple_spinner_dropdown_item,listaStock);
                        lvStock.setAdapter(adapter);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaStockActivity.this);
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
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaStockActivity.this);
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
