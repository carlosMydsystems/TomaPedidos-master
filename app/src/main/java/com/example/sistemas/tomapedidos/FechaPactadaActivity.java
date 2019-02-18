package com.example.sistemas.tomapedidos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FechaPactadaActivity extends AppCompatActivity {

    Button btnregistrafechapactada, btnregresarfechapactada;
    EditText etfechapactada;
    String url;
    ArrayList<Productos> listaproductoselegidos;
    Clientes cliente;
    Usuario usuario;
    String almacen,tipoformapago,Ind,id_pedido,validador,retorno,Index,precio,cantidad;
    TextView tvCantidad,tvPrecio;

    DatePickerDialog datePickerDialog;
    int year,month,dayOfMonth;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_pactada);

        btnregistrafechapactada =  findViewById(R.id.btnRegistrarFechaPactada);
        btnregresarfechapactada = findViewById(R.id.btnRegresarFechaPactada);
        etfechapactada = findViewById(R.id.etFechaPactada);
        tvCantidad = findViewById(R.id.tvNumeroItem);
        tvPrecio = findViewById(R.id.tvMontoTotal);
        listaproductoselegidos = (ArrayList<Productos>) getIntent()
                .getSerializableExtra("listaproductoselegidos");   //
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");   //
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");    //
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("indice");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");
        cantidad = getIntent().getStringExtra("Cantidad");
        precio = getIntent().getStringExtra("Precio");
        tvCantidad.setText(cantidad);
        tvPrecio.setText(precio);

        btnregresarfechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FechaPactadaActivity.this,bandejaProductosActivity.class);

                intent.putExtra("TipoPago",tipoformapago);
                intent.putExtra("indice",Ind);
                intent.putExtra("Index",Index);
                intent.putExtra("Cantidad",cantidad);
                intent.putExtra("Precio",precio);
                intent.putExtra("cantidadlista",listaproductoselegidos.size()+"");
                intent.putExtra("Almacen",almacen);
                intent.putExtra("id_pedido",id_pedido);
                intent.putExtra("validador","false");

                Bundle bundle = new Bundle();
                Bundle bundle2 = new Bundle();
                Bundle bundle3 = new Bundle();

                bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
                bundle2.putSerializable("Cliente",cliente);
                bundle3.putSerializable("Usuario",usuario);

                intent.putExtras(bundle);
                intent.putExtras(bundle2);
                intent.putExtras(bundle3);

                startActivity(intent);
                finish();

            }
        });

        etfechapactada.setOnClickListener(new View.OnClickListener() {
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

                                etfechapactada.setText(FormatoDiaMes(day) + "/" + FormatoDiaMes(month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btnregistrafechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // VerificaFechaPactada(trama);

                RegistrarPedido(id_pedido);


            }
        });
    }



    private void VerificaFechaPactada(String trama ) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // Se hace el ingreso de la URL para la verificacion de la fecha que se ha pactado por medio del peso (El factor limitante)

        //url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("OK")){
                            Toast.makeText(FechaPactadaActivity.this, "Se hizo el registro", Toast.LENGTH_SHORT).show();
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
    private String FormatoDiaMes(Integer valor) {

        String ValorString;

        if (valor<=9){
            ValorString = "0"+valor.toString();
        }else{
            ValorString = valor.toString();
        }
        return ValorString;
    }

    private void RegistrarPedido(String id_pedido) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_GENERA_PEDIDO&variables='"+id_pedido+"'";

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
                                }else{

                                    Toast.makeText(FechaPactadaActivity.this, "Entro al Ok", Toast.LENGTH_SHORT).show();
                                    /*
                                    Intent intent = new Intent(FechaPactadaActivity.this,MainActivity.class);

                                    Bundle bundle2 = new Bundle();
                                    Bundle bundle3 = new Bundle();

                                    bundle2.putSerializable("Cliente",cliente);
                                    bundle3.putSerializable("Usuario",usuario);

                                    intent.putExtras(bundle2);
                                    intent.putExtras(bundle3);

                                    startActivity(intent);
                                    finish();
                                    */
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
        requestQueue.add(stringRequest);
    }
}
