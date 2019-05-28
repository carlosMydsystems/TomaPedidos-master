package com.example.sistemas.tomapedidos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.sistemas.tomapedidos.Entidades.ClienteSucursal;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.DiasPactados;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Proveedor;
import com.example.sistemas.tomapedidos.Entidades.SucursalProveedor;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.Request.EnvioRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionTestMovil;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Dolares;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Soles;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.formatoFecha;

public class FechaPactadaActivity extends AppCompatActivity {

    Button btnregistrafechapactada, btnfechapactada;
    ArrayList<Productos> listaproductoselegidos;
    Clientes cliente;
    Usuario usuario;
    String almacen,tipoformapago,Ind,id_pedido,validador,retorno,Index,precio,cantidad,url,SucursalProveedor,
            NombreProveedor,position,fechahabil,codProveedor,tvnombreproveedor,tvdireccionproveedor,
            latitud, longitud, direccion, ubicacion;
    TextView tvCantidad,tvPrecio,tv22,tvmuestra,tvlatitud,tvlongitud,tvdireccion;
    BigDecimal redondeado;
    DatePickerDialog datePickerDialog;
    int year,month,dayOfMonth;
    Calendar calendar;
    DiasPactados diasPactados;
    ArrayList<DiasPactados> listadiaspactados;
    ArrayList<String> listadiasvalidos,listaSucursalesProveedorStr ;
    Spinner spfechashabiles;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    EditText etComentario;
    ArrayList<Proveedor> listaProveedores;
    ArrayList<SucursalProveedor> listaSucursalesProveedor;
    ImageButton ibRetornoFechaPactadaProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_pactada);

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(); // Procedimiento que pone dm
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

        btnregistrafechapactada =  findViewById(R.id.btnRegistrarFechaPactada);
        ibRetornoFechaPactadaProveedores = findViewById(R.id.ibRetornoFechaPactadaProveedores);
        tvCantidad = findViewById(R.id.tvNumeroItem);
        tvPrecio = findViewById(R.id.tvMontoTotal);
        btnfechapactada = findViewById(R.id.btnfechaPactada);
        spfechashabiles = findViewById(R.id.spFechasHabiles);
        etComentario = findViewById(R.id.etComentario);
        tv22 = findViewById(R.id.textView22);
        tvmuestra = findViewById(R.id.tvmuestra);
        tvlatitud =  findViewById(R.id.tvlatitud);
        tvlongitud =  findViewById(R.id.tvlongitud);;
        tvdireccion = findViewById(R.id.tvdireccion);
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos"); //
        listaSucursalesProveedor = (ArrayList<SucursalProveedor>)getIntent().getSerializableExtra("listaSucursalesProveedor");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        listaProveedores = (ArrayList<Proveedor>)getIntent().getSerializableExtra("listaProveedores");
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("indice");
        position = getIntent().getStringExtra("position");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");
        cantidad = getIntent().getStringExtra("Cantidad");
        precio = getIntent().getStringExtra("Precio");
        tvnombreproveedor = getIntent().getStringExtra("tvnombreproveedor");
        tvdireccionproveedor = getIntent().getStringExtra("tvdireccionproveedor");
        codProveedor = getIntent().getStringExtra("codProveedor");
        SucursalProveedor = getIntent().getStringExtra("SucursalProveedor");
        NombreProveedor = getIntent().getStringExtra("NombreProveedor");
        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");
        listaSucursalesProveedorStr  = (ArrayList<String>) getIntent().getSerializableExtra("listaSucursalesProveedorStr");
        tvCantidad.setText(cantidad);
        redondeado = new BigDecimal(precio).setScale(2, RoundingMode.HALF_EVEN);

        if (usuario.getMoneda().equals("1")){
            tvPrecio.setText(Soles+" "+formateador.format(redondeado));
        }else{
            tvPrecio.setText(Dolares+" "+formateador.format(redondeado));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationStart();
        }

        ibRetornoFechaPactadaProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FechaPactadaActivity.this, ProveedorActivity.class);
                intent.putExtra("TipoPago", tipoformapago);
                intent.putExtra("validador", "true");
                intent.putExtra("Index", Index);
                intent.putExtra("Precio", precio);
                intent.putExtra("validadorRetornoFechaPactadaProveedor", "false");
                intent.putExtra("id_pedido", id_pedido);
                intent.putExtra("Almacen", almacen);
                intent.putExtra("Cantidad", cantidad);
                intent.putExtra("tvnombreproveedor", tvnombreproveedor);
                intent.putExtra("tvdireccionproveedor", tvdireccionproveedor);
                intent.putExtra("valida", "valida");
                intent.putExtra("codProveedor", codProveedor);
                intent.putExtra("NombreProveedor", NombreProveedor);
                intent.putExtra("SucursalProveedor", SucursalProveedor);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listaproductoselegidos", listaproductoselegidos);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente", cliente);
                intent.putExtras(bundle1);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Usuario", usuario);
                intent.putExtras(bundle2);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("listaClienteSucursal",listaClienteSucursal);
                intent.putExtras(bundle3);
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("listaSucursalesProveedorStr",listaSucursalesProveedorStr);
                intent.putExtras(bundle4);
                Bundle bundle5 = new Bundle();
                bundle5.putSerializable("listaSucursalesProveedor",listaSucursalesProveedor);
                intent.putExtras(bundle5);
                Bundle bundle6 = new Bundle();
                bundle6.putSerializable("listaProveedores",listaProveedores);
                intent.putExtras(bundle6);
                startActivity(intent);
                finish();
            }
        });

        btnfechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(FechaPactadaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String trama = id_pedido+"|"+ formatoFecha(day) + "/" + formatoFecha(month + 1) + "/" + year;
                                String fecha = formatoFecha(day) + "/" + formatoFecha(month + 1) + "/" + year;
                                String fechaActual = formatoFecha(dayOfMonth) + "/" + formatoFecha(month+1) + "/"+formatoFecha(year);
/*
                                if(fecha.equals(fechaActual)){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(FechaPactadaActivity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Atencion !");
                                    builder.setMessage("Debe elegir una fecha posterior al dia de Hoy");
                                    builder.setNegativeButton("Aceptar",null);
                                    builder.create().show();

                                }else {*/
                                    VerificaFecha(trama);
                                //}
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btnregistrafechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String moneda;
                if (usuario.getMoneda().equals("1")){
                    moneda = " S/ ";
                }else{
                    moneda = " USD ";
                }

                Registro();

                ubicacion = tvlatitud.getText().toString() + "|" + tvlongitud.getText()
                        .toString() + "|"+ tvdireccion.getText().toString().replace(" ","%20");

                AlertDialog.Builder builder = new AlertDialog.Builder(FechaPactadaActivity.this)
                        .setTitle("\t\t\t\t\t\t\tFin del Pedido")
                        .setMessage(Html.fromHtml("<b>Cliente \t: </b>" + cliente.getCodCliente() + " - " + cliente.getNombre() +  "<br>" +
                            "<b>Suc Cliente \t: </b>"+listaClienteSucursal.get(0).getCodSucursal()+"-"+listaClienteSucursal.get(0).getNombreSucursal()+"<br><br>" +
                            "<b>Transportista \t: </b>" + tvnombreproveedor + "<br>" +
                            "<b>Suc Transportista\t: </b>"  + SucursalProveedor+"-" + NombreProveedor+"<br><br>" +
                            "<b>Fecha Pactada \t: </b>" + fechahabil + "<br>" +
                            "<b>Almacen \t: </b>" + almacen + "<br><br>" +
                            "<b>Tipo Documento \t: </b>" + cliente.getDocumentoSeleccionado() + "<br>" +
                            "<b>Importe \t: </b>"+ moneda + redondeado + "<br>" +
                            "<b>Items  \t\t\t: </b>" + cantidad))
                        .setCancelable(false);
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int aux = 0;
                        if (aux==0){

                            String Trama =  id_pedido+"|C|0|"+almacen +"|" +cliente.getCodCliente()+"|" +usuario.
                                    getCodVendedor() + "|"+tipoformapago+"|"+cliente.getTipoDocumento()
                                    +"|"+usuario.getMoneda()+"|"+usuario.getUser().trim()+"|"+usuario.getCodTienda()+"|"+
                                    listaClienteSucursal.get(0).getCodSucursal()+"|"+codProveedor+"|"+
                                    SucursalProveedor+"|"+fechahabil+"|"+etComentario.getText().toString().replace(" ","%20")+"|"+ubicacion;
/*
                            String Trama =  id_pedido + "|C|0|" + almacen + "|" + cliente.getCodCliente() +
                                    "|" + usuario.getCodVendedor() + "|" + tipoformapago + "|" +
                                    cliente.getTipoDocumento() + "|" + usuario.getMoneda() + "|" +
                                    usuario.getUser().trim() + "|" + usuario.getCodTienda() + "|" +
                                    listaClienteSucursal.get(0).getCodSucursal()+"|"+codProveedor+"|"+
                                    SucursalProveedor + "|" + fechahabil + "|" + ubicacion;
*/
                            ActualizarProducto(Trama);
                            aux++;
                        }
                    }
                });
                builder.create()
                        .show();
            }
        });
    }

    private void VerificaFecha(String trama) {
        Integer cola = 0;
        final ProgressDialog progressDialog = new ProgressDialog(FechaPactadaActivity.this);
        progressDialog.setMessage("... Cargando");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        listadiaspactados = new ArrayList<DiasPactados>();

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_VAL_FECHA_PACTADA&variables='"+trama+"'";

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

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(
                                                FechaPactadaActivity.this);
                                        dialog.setMessage(Mensaje)
                                                .setNegativeButton("Regresar", null)
                                                .create()
                                                .show();
                                    } else {

                                    listadiaspactados.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        diasPactados = new DiasPactados();
                                        jsonObject = jsonArray.getJSONObject(i);

                                            diasPactados.setDia1(jsonObject.getString("DIA1"));
                                            diasPactados.setDia2(jsonObject.getString("DIA2"));
                                            diasPactados.setDia3(jsonObject.getString("DIA3"));
                                            diasPactados.setDia4(jsonObject.getString("DIA4"));
                                            diasPactados.setDia5(jsonObject.getString("DIA5"));
                                            diasPactados.setDia6(jsonObject.getString("DIA6"));
                                            diasPactados.setDia7(jsonObject.getString("DIA7"));
                                            diasPactados.setDia8(jsonObject.getString("DIA8"));
                                            diasPactados.setDia9(jsonObject.getString("DIA9"));
                                            diasPactados.setDia10(jsonObject.getString("DIA10"));
                                            listadiaspactados.add(diasPactados);
                                    }
                                    progressDialog.dismiss();

                                    verificaPesosporfecha(listadiaspactados);
                                }
                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(FechaPactadaActivity.this);
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
        if (cola<1) {

            cola++;
            requestQueue.add(stringRequest);
        }
    }

    private void verificaPesosporfecha(ArrayList<DiasPactados> listadiaspactados) {

        listadiasvalidos = new ArrayList<>();

        Double fecha1= Double.valueOf(listadiaspactados.get(1).getDia1()) - Double.valueOf(listadiaspactados.get(2).getDia1());
        Double fecha2= Double.valueOf(listadiaspactados.get(1).getDia2()) - Double.valueOf(listadiaspactados.get(2).getDia2());
        Double fecha3= Double.valueOf(listadiaspactados.get(1).getDia3()) - Double.valueOf(listadiaspactados.get(2).getDia3());
        Double fecha4= Double.valueOf(listadiaspactados.get(1).getDia4()) - Double.valueOf(listadiaspactados.get(2).getDia4());
        Double fecha5= Double.valueOf(listadiaspactados.get(1).getDia5()) - Double.valueOf(listadiaspactados.get(2).getDia5());
        Double fecha6= Double.valueOf(listadiaspactados.get(1).getDia6()) - Double.valueOf(listadiaspactados.get(2).getDia6());
        Double fecha7= Double.valueOf(listadiaspactados.get(1).getDia7()) - Double.valueOf(listadiaspactados.get(2).getDia7());
        Double fecha8= Double.valueOf(listadiaspactados.get(1).getDia8()) - Double.valueOf(listadiaspactados.get(2).getDia8());
        Double fecha9= Double.valueOf(listadiaspactados.get(1).getDia9()) - Double.valueOf(listadiaspactados.get(2).getDia9());
        Double fecha10= Double.valueOf(listadiaspactados.get(1).getDia10()) - Double.valueOf(listadiaspactados.get(2).getDia10());

        if (fecha1>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia1());}
        if (fecha2>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia2());}
        if (fecha3>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia3());}
        if (fecha4>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia4());}
        if (fecha5>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia5());}
        if (fecha6>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia6());}
        if (fecha7>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia7());}
        if (fecha8>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia8());}
        if (fecha9>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia9());}
        if (fecha10>0){ listadiasvalidos.add(listadiaspactados.get(0).getDia10());}

        spfechashabiles.setAdapter(new SpinnerAdapter(getApplicationContext(),listadiasvalidos));
        spfechashabiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fechahabil = listadiasvalidos.get(position);
                tv22.setVisibility(View.VISIBLE);
                etComentario.setVisibility(View.VISIBLE);
                btnregistrafechapactada.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void RegistrarPedido(String id_pedido) {

        Integer cola=0;

        final ProgressDialog progressDialog = new ProgressDialog(FechaPactadaActivity.this);
        progressDialog.setMessage("... Enviando");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_GENERA_PEDIDO&variables='"+id_pedido+"'";

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

                            progressDialog.dismiss();

                            if (success){

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
                                    }else if (palabras.equals("OK")){
                                        condicion = true;
                                        error = false;
                                    }
                                }
                                if (error) {

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                                            FechaPactadaActivity.this);
                                    dialog.setCancelable(false);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                }else{

                                    AlertDialog.Builder builder = new AlertDialog.Builder(FechaPactadaActivity.this)
                                            .setCancelable(false)
                                            .setMessage("Se ha generado de forma correcta el pedido N° " + Mensaje);

                                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(FechaPactadaActivity.this,MostrarClienteActivity.class);
                                                    Bundle bundle2 = new Bundle();
                                                    Bundle bundle3 = new Bundle();
                                                    bundle2.putSerializable("Cliente",cliente);
                                                    bundle3.putSerializable("Usuario",usuario);
                                                    intent.putExtras(bundle2);
                                                    intent.putExtras(bundle3);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder.create()
                                                    .show();
                                }
                            }else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(FechaPactadaActivity.this);
                                builder.setMessage("No se llego a encontrar el registro")
                                        .setCancelable(false)
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

    private void ActualizarProducto(String trama) {

        Integer cola=0;

        final ProgressDialog progressDialog = new ProgressDialog(FechaPactadaActivity.this);
        progressDialog.setMessage("... Enviando");
        progressDialog.setCancelable(false);
        progressDialog.show();


        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  ejecutaFuncionTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){
                            //progressDialog.dismiss();
                            RegistrarPedido(id_pedido);
                        }else{

                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new  AlertDialog.Builder(FechaPactadaActivity.this);
                            builder.setTitle("Atención !");
                            builder.setMessage("Error"+response);
                            builder.setNegativeButton("Aceptar",null);
                            builder.create().show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void Registro() {

        Integer cola=0;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonresponse = new JSONObject(response);
                    boolean success = jsonresponse.getBoolean("success");

                    if (success){

                    }else{
                        AlertDialog.Builder  builder = new AlertDialog.Builder(
                                FechaPactadaActivity.this);
                        builder.setMessage("No se encontraron registros")
                                .setNegativeButton("Regresar",null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        latitud = tvlatitud.getText().toString();
        longitud = tvlongitud.getText().toString();
        direccion = tvdireccion.getText().toString();
        String fechaRegistro="Hoy";
        String horaRegistro="Ahora";
        //placa = tvplaca.getText().toString();
        String placa = "ABCDE";
        EnvioRequest envioRequest = new EnvioRequest(latitud,longitud,direccion, fechaRegistro,horaRegistro,placa, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FechaPactadaActivity.this);
        queue.add(envioRequest);
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setFechaPactadaActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {


            /**  Se hace la habilitacion del GPS, si se descomenta esta parte del codigo */

            /*
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            */

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;

        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, (LocationListener) Local);
        tvlatitud.setText("ND");
        tvdireccion.setText("ND");
        tvlongitud.setText("ND");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    tvdireccion.setText("" + DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        FechaPactadaActivity fechaPactadaActivity;
        public FechaPactadaActivity getFechaPactadaActivity() {

            return fechaPactadaActivity;
        }
        public void setFechaPactadaActivity(FechaPactadaActivity fechaPactadaActivity) {
            this.fechaPactadaActivity = fechaPactadaActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            String Text = loc.getLatitude()+"";

            String longitudTxt = loc.getLongitude()+"";
            tvlatitud.setText(Text);
            tvlongitud.setText(longitudTxt);
            this.fechaPactadaActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            tvlatitud.setText("ND");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            tvlatitud.setText("GPS%20Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}