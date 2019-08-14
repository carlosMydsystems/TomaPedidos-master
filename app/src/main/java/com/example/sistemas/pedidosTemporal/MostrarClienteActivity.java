package com.example.sistemas.pedidosTemporal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemas.pedidosTemporal.Entidades.ClienteSucursal;
import com.example.sistemas.pedidosTemporal.Entidades.Clientes;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.Utilitarios.Utilitario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionCursorTestMovil;

public class MostrarClienteActivity extends AppCompatActivity {

    //private final static String[] opcionesDoc = { "Boleta", "Factura" };
    Button btnpedido,btnregresodetallecliente,btnDocumento;
    Clientes cliente;
    TextView tvcodigo,tvNombre,tvDireccion,tvGiro,tvTipoCiente,tvDeuda,tvestado,
             tvUsuarioUltPedido,tvDireccionFiscalCliente,tvRucDni,tvLineaCredito,tvCreditoDisponible,tvCanal;
    Usuario usuario;
    Spinner spopcionesdocumento, spsucursal;
    List<String> opSucursal;
    String url,clienteAcotado;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    ClienteSucursal clienteSucursal;
    List<String> opdoc;
    ImageButton ibVolverBusquedaCLiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);

        if(Utilitario.isOnline(getApplicationContext())){

            cliente  = new Clientes();
            cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
            cliente.setTipoDocumento("FAC");
            usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
            tvcodigo = findViewById(R.id.tvCofigoProducto);
            tvNombre = findViewById(R.id.tvNombreCliente);
            tvDireccion = findViewById(R.id.tvDireccion);
            tvGiro = findViewById(R.id.tvNomProdElegido);
            tvTipoCiente = findViewById(R.id.tvAlmProdElegido);
            tvDeuda = findViewById(R.id.tvStockElegido);
            tvestado = findViewById(R.id.tvPrecioElegido);
            tvLineaCredito = findViewById(R.id.tvLineaCredito);
            tvCreditoDisponible = findViewById(R.id.tvCreditoDisponible);
            tvCanal = findViewById(R.id.tvCanal);
            ibVolverBusquedaCLiente = findViewById(R.id.ibVolverBusquedaCLiente);
            tvUsuarioUltPedido = findViewById(R.id.tvTotalElegido);
            btnregresodetallecliente = findViewById(R.id.btnRetornoDetCliente);
            spopcionesdocumento = findViewById(R.id.spTipoDocumento);
            spsucursal = findViewById(R.id.spSucursal);
            tvDireccionFiscalCliente = findViewById(R.id.tvDireccionFiscalCliente);
            tvRucDni = findViewById(R.id.tvRucDni);
            btnDocumento = findViewById(R.id.btnDocumentos);
            tvcodigo.setText(cliente.getCodCliente());

            clienteAcotado = cliente.getNombre();
            Integer clienteLongitud = cliente.getNombre().length();

            if (clienteLongitud > 28){

                clienteAcotado = clienteAcotado.substring(0,27);
            }

            tvNombre.setText(clienteAcotado);
            tvDireccion.setText(cliente.getDireccion());
            opdoc = new ArrayList<>();
            tvRucDni.setText(cliente.getDocumentoCliente());
            if (cliente.getDocumentoCliente().length() == 11){
                opdoc.add("FACTURA");
                opdoc.add("BOLETA");
            }else if(cliente.getDocumentoCliente().length() == 8){
                opdoc.add("BOLETA");
            }

            spopcionesdocumento.setAdapter(new SpinnerAdapter(this,opdoc));

            ListaSucursalesClientes(cliente.getCodCliente(),cliente);
            spopcionesdocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String muestra = (String)parent.getItemAtPosition(position);
                    if (muestra.equals("FACTURA")){
                        cliente.setTipoDocumento("FAC");
                        cliente.setDocumentoSeleccionado("FACTURA");
                    }else if ( muestra.equals("BOLETA")){
                        cliente.setTipoDocumento("BOL");
                        cliente.setDocumentoSeleccionado("BOLETA");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnDocumento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MostrarClienteActivity.this,ListaDocumentosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cliente",cliente);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Usuario",usuario);
                    intent.putExtras(bundle1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("listaClienteSucursal",listaClienteSucursal);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                    finish();
                }
            });

            btnpedido = findViewById(R.id.btnPedido);
            btnpedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MostrarClienteActivity.this,ListadoAlmacenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cliente",cliente);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Usuario",usuario);
                    intent.putExtras(bundle1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("listaClienteSucursal",listaClienteSucursal);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                    finish();
                }
            });

            btnregresodetallecliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(MostrarClienteActivity.this,BusquedaClienteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Usuario",usuario);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });
            ibVolverBusquedaCLiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(MostrarClienteActivity.this,BusquedaClienteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Usuario",usuario);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            AlertDialog.Builder build = new AlertDialog.Builder(MostrarClienteActivity.this);
            build.setTitle("Atención .. !");
            build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
            build.setCancelable(false);
            build.setNegativeButton("ACEPTAR",null);
            build.create().show();
        }
    }

    private void ListaSucursalesClientes(String codCliente,final Clientes cliente) {

        final ProgressDialog progressDialog = new ProgressDialog(MostrarClienteActivity.this);
        progressDialog.setMessage("... Cargando");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_SUC_CLIENTE&variables='"+codCliente+"'";

        listaClienteSucursal = new ArrayList<ClienteSucursal>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){

                                opSucursal = new ArrayList<String>();

                                for(int i=0;i<jsonArray.length();i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    clienteSucursal = new ClienteSucursal();
                                    opSucursal.add(jsonObject.getString("NOMBRE"));
                                    clienteSucursal.setCodSucursal(jsonObject.getString("COD_SUCCLIE"));
                                    clienteSucursal.setNombreSucursal(jsonObject.getString("NOMBRE"));
                                    clienteSucursal.setDireccionSucursal(jsonObject.getString("DIRECCION"));
                                    clienteSucursal.setDepartamento(jsonObject.getString("DEPARTAMENTO"));
                                    clienteSucursal.setProvincia(jsonObject.getString("PROVINCIA"));
                                    clienteSucursal.setDistrito(jsonObject.getString("DISTRITO"));
                                    clienteSucursal.setLineaCredito(jsonObject.getString("IMPORTE_LINEA"));
                                    clienteSucursal.setCreditoDisponible(jsonObject.getString("CREDITO_DISPONIBLE"));
                                    clienteSucursal.setCanal(jsonObject.getString("CANAL"));
                                    listaClienteSucursal.add(clienteSucursal);
                                }

                                progressDialog.dismiss();

                                 spsucursal.setAdapter(new SpinnerAdapter(getApplicationContext(),opSucursal));
                                tvDireccionFiscalCliente.setText(listaClienteSucursal.get(0).getDireccionSucursal());
                                listaClienteSucursal.get(0).setCodSucursal(listaClienteSucursal.get(0).getCodSucursal());
                                 spsucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                     @Override
                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                         String texto = listaClienteSucursal.get(position).getDireccionSucursal();
                                         Integer longitud =  listaClienteSucursal.get(position).getDireccionSucursal().length();

                                         if(longitud > 36 ) {
                                             texto = listaClienteSucursal.get(position).getDireccionSucursal().substring(0,35);
                                         }

                                         tvDireccionFiscalCliente.setText(texto);
                                         tvLineaCredito.setText("S/ "+formateador.format(Double.valueOf(listaClienteSucursal.get(0).getLineaCredito())));
                                         tvCreditoDisponible.setText("S/ "+formateador.format(Double.valueOf(listaClienteSucursal.get(0).getCreditoDisponible())));
                                         tvCanal.setText(listaClienteSucursal.get(0).getCanal());
                                         listaClienteSucursal.get(0).setCodSucursal(listaClienteSucursal.get(position).getCodSucursal());
                                         listaClienteSucursal.get(0).setNombreSucursal(listaClienteSucursal.get(position).getNombreSucursal());

                                     }

                                     @Override
                                     public void onNothingSelected(AdapterView<?> parent) {

                                     }
                                 });

                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarClienteActivity.this);
                                builder.setCancelable(false)
                                        .setMessage("No se llego a encontrar el registro")
                                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(MostrarClienteActivity.this,MostrarClienteActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("Usuario",usuario);
                                                intent.putExtras(bundle);
                                                Bundle bundle1 = new Bundle();
                                                bundle1.putSerializable("Cliente",cliente);
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
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarClienteActivity.this);
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
