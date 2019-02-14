package com.example.sistemas.tomapedidos;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class FechaPactadaActivity extends AppCompatActivity {

    Button btnregistrafechapactada, btnregresarfechapactada;
    EditText etfechapactada;
    String trama, url;
    FloatingActionButton fabregresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha_pactada);

        btnregistrafechapactada =  findViewById(R.id.btnRegistrarFechaPactada);
        btnregresarfechapactada = findViewById(R.id.btnRegresarFechaPactada);
        fabregresar = findViewById(R.id.FActionButton);
        etfechapactada = findViewById(R.id.etFechaPactada);

        btnregresarfechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(FechaPactadaActivity.this,bandejaProductosActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnregistrafechapactada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerificaFechaPactada(trama);
            }
        });

        fabregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FechaPactadaActivity.this, "Se ha ingresado al Floating Action button", Toast.LENGTH_SHORT).show();

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


}
