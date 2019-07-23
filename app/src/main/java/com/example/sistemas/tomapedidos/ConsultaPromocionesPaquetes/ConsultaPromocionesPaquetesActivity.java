package com.example.sistemas.tomapedidos.ConsultaPromocionesPaquetes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.sistemas.tomapedidos.ConsultaPrecio.BuscarProductoPrecioActivity;
import com.example.sistemas.tomapedidos.ConsultaPrecio.ConsultaPrecioActivity;
import com.example.sistemas.tomapedidos.ConsultasListadoActivity;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.ListadoAlmacenActivity;
import com.example.sistemas.tomapedidos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;

public class ConsultaPromocionesPaquetesActivity extends AppCompatActivity {

    RadioGroup rggrupocliente;
    RadioButton rbnombre,rbcodigo;
    Button btnbuscar;
    ArrayList<Clientes> listaClientes;
    Clientes cliente;
    ListView lvclientes;
    ArrayList<String> listaCliente;
    EditText etcliente;
    String url, tipoConsulta = "Razon";
    ProgressDialog progressDialog;
    Usuario usuario;
    ImageButton ibregresomenuprincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_cliente);

        listaClientes = new ArrayList<>();
        listaCliente  =new ArrayList<>();
        rggrupocliente = findViewById(R.id.rgBuscar);
        rbnombre = findViewById(R.id.rbNombre);
        rbcodigo = findViewById(R.id.rbCodigo);
        btnbuscar = findViewById(R.id.btnBuscar);
        lvclientes = findViewById(R.id.lvCliente);
        etcliente = findViewById(R.id.etCliente);
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");  //Se pasa el parametro del usuario
        ibregresomenuprincipal = findViewById(R.id.ibRetornoMenuPrincipal);
        etcliente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(80)});
        etcliente.setInputType(1);
        ibregresomenuprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(ConsultaPromocionesPaquetesActivity.this, ConsultasListadoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etcliente.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaPromocionesPaquetesActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Por favor ingrese un valor valido")
                            .setNegativeButton("Aceptar",null)
                            .create()
                            .show();
                }else {
                    progressDialog = new ProgressDialog(ConsultaPromocionesPaquetesActivity.this);
                    progressDialog.setMessage("Cargando...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    btnbuscar.setVisibility(View.GONE);
                    buscarCliente(etcliente.getText().toString(),tipoConsulta,usuario);
                }
            }
        });

        lvclientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cliente = new Clientes();
                cliente =  listaClientes.get(position);
                Intent intent =  new Intent(ConsultaPromocionesPaquetesActivity.this, ListaPaquetePromocionActivity.class);
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

        rggrupocliente.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etcliente.setText("");
                switch (rggrupocliente.getCheckedRadioButtonId()){

                    case R.id.rbNombre:
                        etcliente.setInputType(2);
                        etcliente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
                        tipoConsulta = "Nombre";
                        break;

                    case R.id.rbCodigo:
                        etcliente.setInputType(2);
                        etcliente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
                        tipoConsulta = "Codigo";
                        break;
                    case R.id.rbrazon:
                        etcliente.setInputType(1);
                        etcliente.setFilters(new InputFilter[] {new InputFilter.LengthFilter(80)});
                        tipoConsulta = "Razon";
                        break;
                }
            }
        });
    }


    private void buscarCliente(String numero, String tipoConsulta,Usuario usuario) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // la Url del servicio Web // Se hace la validacion del tipo de consulta

        if (numero.length()<6){

            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaPromocionesPaquetesActivity.this);
            builder.setCancelable(false);
            builder.setNegativeButton("Aceptar",null);
            builder.setTitle("Atención...!");
            builder.setMessage("Se debe de ingresar un mínimo de 6 caracteres");
            builder.create().show();
            btnbuscar.setVisibility(View.VISIBLE);

        }else if(numero.contains("%%")) {

            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaPromocionesPaquetesActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Atención...!");
            builder.setMessage("No debe ingresar de forma consecutiva el \"%\"");
            builder.setNegativeButton("Aceptar",null);
            builder.create().show();
            btnbuscar.setVisibility(View.VISIBLE);

        }else {

        /*
            if (tipoConsulta == "Nombre") {

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='" + numero + "||'";
            } else if(tipoConsulta == "Codigo"){

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='||" + numero + "'";
            }else if(tipoConsulta == "Razon"){

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='|"+ numero +"|'";
            }

        */
            if (tipoConsulta == "Nombre") {

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='" + numero + "||'";
            } else if(tipoConsulta == "Codigo"){

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='||" + numero + "'";
            }else if(tipoConsulta == "Razon"){

                url = ejecutaFuncionCursorTestMovil +
                        "PKG_WEB_HERRAMIENTAS.FN_WS_CONSULTAR_CLIENTE&variables='|"+ numero.trim().replace("%","%25").replace(" ","%20").toUpperCase() +"|'";

            }

            listaCliente = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                btnbuscar.setVisibility(View.VISIBLE);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                                if (success) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        cliente = new Clientes();
                                        jsonObject = jsonArray.getJSONObject(i);
                                        cliente.setCodCliente(jsonObject.getString("COD_CLIENTE"));
                                        cliente.setNombre(jsonObject.getString("CLIENTE"));
                                        cliente.setDireccion(jsonObject.getString("DIRECCION"));
                                        cliente.setCodFPago(jsonObject.getString("COD_FPAGO_LIMITE"));
                                        cliente.setFormaPago(jsonObject.getString("FORMA_PAGO"));
                                        listaClientes.add(cliente);
                                        listaCliente.add(cliente.getCodCliente() + " - " + cliente.getNombre());
                                    }

                                    ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                            CustomListAdapter(ConsultaPromocionesPaquetesActivity.this, R.layout.custom_list, listaCliente);
                                    lvclientes.setAdapter(listAdapter);

                                } else {
                                    listaCliente.clear();
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                            , R.layout.support_simple_spinner_dropdown_item, listaCliente);

                                    lvclientes.setAdapter(adapter);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaPromocionesPaquetesActivity.this);
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
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaPromocionesPaquetesActivity.this);
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
}