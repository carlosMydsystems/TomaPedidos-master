package com.example.sistemas.pedidosTemporal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.sistemas.pedidosTemporal.Adaptadores.ConsultaPromociones.CustomListAdapter;
import com.example.sistemas.pedidosTemporal.Entidades.ClienteSucursal;
import com.example.sistemas.pedidosTemporal.Entidades.Clientes;
import com.example.sistemas.pedidosTemporal.Entidades.CtaCte;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionCursorTestMovil;

public class ListaDocumentosActivity extends AppCompatActivity {

    ListView lvListaDocumentos;
    ImageButton ibRetornoDetalleCliente;
    Clientes cliente;
    Usuario usuario;
    ProgressDialog progressDialog;
    String url;
    ArrayList<CtaCte> listaDocumentosCtaCte;
    ArrayList<String> listaDocumentosCtaCteString;
    CtaCte ctaCte;
    TextView tvTituloListaDocumentos;
    ArrayList<ClienteSucursal> listaClienteSucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_documentos);

        lvListaDocumentos = findViewById(R.id.lvListaDocumentos);
        ibRetornoDetalleCliente = findViewById(R.id.ibRetornoDetalleCliente);
        tvTituloListaDocumentos = findViewById(R.id.tvTituloListaDocumentos);

        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        Double utilizado = Double.parseDouble(listaClienteSucursal.get(0).getLineaCredito()) - Double.parseDouble(listaClienteSucursal.get(0).getCreditoDisponible());

        Toast.makeText(this, ""+ listaClienteSucursal.get(0).getCreditoDisponible(), Toast.LENGTH_SHORT).show();

        // Double cantidad = Double.parseDouble(clienteSucursal.getLineaCredito()) - Double.parseDouble(clienteSucursal.getCreditoDisponible());

        String tituloListaDoc = "TOTAL USADO S/ " + formateador.format(utilizado);
        tvTituloListaDocumentos.setText(tituloListaDoc);

        CargaDocumentos(cliente.getCodCliente());

        ibRetornoDetalleCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListaDocumentosActivity.this,MostrarClienteActivity.class);
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
    }

    private void CargaDocumentos(String codCliente) {

        progressDialog = new ProgressDialog(ListaDocumentosActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_CTA_CTE_CLI&variables='"+codCliente +"'";

        listaDocumentosCtaCte = new ArrayList<>();
        listaDocumentosCtaCteString = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String Mensaje = "";

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
                                            ListaDocumentosActivity.this);
                                    dialog.setCancelable(false);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();

                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        ctaCte = new CtaCte();
                                        ctaCte.setTipoDocumento(jsonObject.getString("TIPO_DOCUMENTO"));
                                        ctaCte.setNumeroDocumento(jsonObject.getString("NRO_DOCUMENTO"));
                                        ctaCte.setFecha(jsonObject.getString("FECHA")); //
                                        ctaCte.setMoneda(jsonObject.getString("MONEDA"));
                                        ctaCte.setImporte(jsonObject.getString("IMPORTE"));
                                        ctaCte.setTienda(jsonObject.getString("TIENDA"));
                                        listaDocumentosCtaCte.add(ctaCte);
                                        String cadena = "<b>TIPO DOC : </b>" + ctaCte.getTipoDocumento() + " \t\t\t " + "<b>NRO DOC : </b>" + ctaCte.getNumeroDocumento()+ "<br>"+
                                                "<b>FECHA : </b>" + ctaCte.getFecha() +" \t\t\t\t <b>TIENDA : </b>"+ ctaCte.getTienda()+ "<br>" + "<b>IMPORTE : S/ </b>"+ ctaCte.getImporte();
                                        listaDocumentosCtaCteString.add(Html.fromHtml(cadena).toString());
                                    }

                                    // Se hace un llamado al adaptador personalizado asociado al SML custom_list

                                        progressDialog.dismiss();

                                        CustomListAdapter.CustomListAdapter1 listAdapter1 = new CustomListAdapter.CustomListAdapter1(ListaDocumentosActivity.
                                                this,R.layout.custom_list,listaDocumentosCtaCteString);

                                        lvListaDocumentos.setAdapter(listAdapter1);
                                }

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ListaDocumentosActivity.this);
                                builder.setMessage("No se llego a encontrar el registro")
                                        .setNegativeButton("Aceptar",null)
                                        .create()
                                        .show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaDocumentosActivity.this);
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




