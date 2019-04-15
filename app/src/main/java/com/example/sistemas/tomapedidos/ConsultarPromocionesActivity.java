package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.ConsultaPromocion;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class ConsultarPromocionesActivity extends AppCompatActivity {

    Clientes clientes;
    Usuario usuario;
    ProgressDialog progressDialog;
    String url,trama;
    ArrayList<String> listaPromocionesStr,listaCodigosPromociones;
    ArrayList<ConsultaPromocion>  listaPromocionesObjetos,listaPromocionAux;
    ConsultaPromocion consultaPromocion;
    ListView lvmuestrapromociones;
    ImageButton ibRetornoMenuConsultaPromocion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_promociones);

        clientes = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        ibRetornoMenuConsultaPromocion = findViewById(R.id.ibRetornoMenuConsultaPromocion);
        trama = usuario.getLugar()+"|"+clientes.getCodCliente()+"|"+usuario.getCodTienda();
        buscarpromociones(trama);
        lvmuestrapromociones = findViewById(R.id.lvMuestraPromociones);

        ibRetornoMenuConsultaPromocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultarPromocionesActivity.this,BusquedaClienteActivity.class);
                intent.putExtra("consultaPromociones","true");
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente",clientes);
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void buscarpromociones(String trama) {

        progressDialog = new ProgressDialog(ConsultarPromocionesActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=" +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PROMOCIONES_CAB&variables=%27"+trama+"%27";

        listaPromocionesStr = new ArrayList<>();
        listaPromocionesObjetos = new ArrayList<>();
        listaCodigosPromociones = new ArrayList<>();
        listaPromocionAux = new ArrayList<>();

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

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                                            ConsultarPromocionesActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                } else {

                                    listaPromocionesStr.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        consultaPromocion = new ConsultaPromocion();

                                        consultaPromocion.setNroPromocion(jsonObject.getString("NRO_PROMOCION"));
                                        consultaPromocion.setGlosa(jsonObject.getString("GLOSA"));
                                        consultaPromocion.setFechaEmision(jsonObject.getString("FECHA_EMISION"));
                                        consultaPromocion.setFechaFinVigencia(jsonObject.getString("FECHA_FIN_VIGENCIA"));
                                        consultaPromocion.setFormaPromocion(jsonObject.getString("FORMA_PROMOCION"));
                                        consultaPromocion.setImportecantidad(jsonObject.getString("IMPORTE_CANTIDAD_MINIMO"));

                                        listaPromocionesObjetos.add(consultaPromocion);

                                            listaPromocionesStr.add(consultaPromocion.getNroPromocion() + "  -  " + consultaPromocion.getGlosa()+"\n"
                                            +  "VALIDO DEL\t: " + consultaPromocion.getFechaEmision() +"\t\tAL\t\t"+ consultaPromocion.getFechaFinVigencia()+"\n"
                                            + "PROMOCION POR\t" + consultaPromocion.getFormaPromocion()+"\t\t\t\t\tMINIMO "+ consultaPromocion.getImportecantidad());
                                            listaCodigosPromociones.add(consultaPromocion.getNroPromocion());
                                    }

                                    progressDialog.dismiss();

                                    ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                            CustomListAdapter(ConsultarPromocionesActivity.this, R.layout.custom_list, listaPromocionesStr);
                                    lvmuestrapromociones.setAdapter(listAdapter);
                                    lvmuestrapromociones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            Intent intent = new Intent(ConsultarPromocionesActivity.this,MostrarConsultaActivity.class);
                                            Bundle bundle = new Bundle();
                                            intent.putExtra("position",""+position);
                                            bundle.putSerializable("listaPromocionesObjetos",listaPromocionesObjetos);
                                            intent.putExtras(bundle);
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putSerializable("Cliente",clientes);
                                            intent.putExtras(bundle1);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putSerializable("Usuario",usuario);
                                            intent.putExtras(bundle2);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });
                                }
                            }else {
                                progressDialog.dismiss();
                                listaPromocionesStr.clear();

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item,listaPromocionesStr);
                                lvmuestrapromociones.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConsultarPromocionesActivity.this);
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
