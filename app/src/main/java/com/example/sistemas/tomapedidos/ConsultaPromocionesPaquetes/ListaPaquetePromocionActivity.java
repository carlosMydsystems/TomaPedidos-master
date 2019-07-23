package com.example.sistemas.tomapedidos.ConsultaPromocionesPaquetes;

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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.tomapedidos.ConsultaPromociones.MostrarConsultaActivity;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.ConsultaPromocion;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.ListadoAlmacenActivity;
import com.example.sistemas.tomapedidos.R;
import com.example.sistemas.tomapedidos.Utilitarios.Utilitario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;

public class ListaPaquetePromocionActivity extends AppCompatActivity {

    Clientes clientes;
    Usuario usuario;
    ProgressDialog progressDialog;
    String url,trama;
    ArrayList<String> listaPromocionesStr,listaCodigosPromociones;
    ArrayList<ConsultaPromocion>  listaPromocionesObjetos,listaPromocionAux;
    ConsultaPromocion consultaPromocion;
    ListView lvmuestrapaquetepromocion;
    ImageButton ibRetornoMenuConsultaPromocion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paquete_promocion);

        clientes = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        ibRetornoMenuConsultaPromocion = findViewById(R.id.ibRetornoMenuConsultaPromocionPaquete);
        trama = usuario.getLugar()+"||"+clientes.getCodCliente()+"|"+usuario.getCodTienda();

        if(Utilitario.isOnline(getApplicationContext())){

            buscarpromociones(trama);
            lvmuestrapaquetepromocion = findViewById(R.id.lvListaPaquetePromocion);
            ibRetornoMenuConsultaPromocion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListaPaquetePromocionActivity.this, ConsultaPromocionesPaquetesActivity.class);
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

        }else{

            AlertDialog.Builder build = new AlertDialog.Builder(ListaPaquetePromocionActivity.this);
            build.setTitle("Atención .. !");
            build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
            build.setCancelable(false);
            build.setNegativeButton("ACEPTAR",null);
            build.create().show();

        }
    }

    private void buscarpromociones(String trama) {

        progressDialog = new ProgressDialog(ListaPaquetePromocionActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTA_PAQUETE&variables=%27"+trama+"%27";

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
                                            ListaPaquetePromocionActivity.this);
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
                                        consultaPromocion.setFechaEmision(jsonObject.getString("FECHA_INI_VIGENCIA"));
                                        consultaPromocion.setFechaFinVigencia(jsonObject.getString("FECHA_FIN_VIGENCIA"));
                                        consultaPromocion.setMoneda(jsonObject.getString("MONEDA"));
                                        consultaPromocion.setPreciopaquete(jsonObject.getString("P_PAQUETE"));
                                        consultaPromocion.setPrecioregular(jsonObject.getString("P_REGULAR"));
                                        consultaPromocion.setAhorro(jsonObject.getString("AHORRO"));
                                        listaPromocionesObjetos.add(consultaPromocion);

                                        listaPromocionesStr.add(consultaPromocion.getNroPromocion() + "  -  " + consultaPromocion.getGlosa()+"\n"
                                                +  "VALIDO DEL\t: " + consultaPromocion.getFechaEmision() +"\t\tAL\t\t"+ consultaPromocion.getFechaFinVigencia()+"\n"
                                                + "PRECIO PAQUETE : \t" + consultaPromocion.getMoneda()+" "+ formateador.format(Double.valueOf(consultaPromocion.getPreciopaquete())));
                                        listaCodigosPromociones.add(consultaPromocion.getNroPromocion());
                                    }

                                    progressDialog.dismiss();
                                    ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                            CustomListAdapter(ListaPaquetePromocionActivity.this, R.layout.custom_list, listaPromocionesStr);
                                    lvmuestrapaquetepromocion.setAdapter(listAdapter);
                                    lvmuestrapaquetepromocion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            Intent intent = new Intent(ListaPaquetePromocionActivity.this, MostrarDetallePromocionPaqueteActivity.class);
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
                                lvmuestrapaquetepromocion.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListaPaquetePromocionActivity.this);
                                builder.setMessage("No se llego a encontrar el registro")
                                        .setNegativeButton("Aceptar",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            Toast.makeText(ListaPaquetePromocionActivity.this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaPaquetePromocionActivity.this);
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

