package com.example.sistemas.tomapedidos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListadoFormaPagoActivity extends AppCompatActivity {

    ListView lvtipopago;
    ArrayList<String> listatipopago,listaAux;
    Clientes cliente;
    String url,almacen;
    ArrayList<Productos> listaproductoselegidos;
    Usuario usuario;
    Button btnregresarformalistapago;
    String indice="0",validador = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_forma_pago);

        listaproductoselegidos = new ArrayList<>();
        cliente  = new Clientes();
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        almacen = getIntent().getStringExtra("Almacen") ;
        listatipopago =  new ArrayList<>();
        listaAux = new ArrayList<>();
        String fechaRegistro;


        Calendar fecha = Calendar.getInstance();
        final Integer dia = fecha.get(Calendar.DAY_OF_MONTH);
        final Integer mes = fecha.get(Calendar.MONTH) + 1;
        Integer year = fecha.get(Calendar.YEAR);
        final Integer hora =  fecha.get(Calendar.HOUR_OF_DAY);
        final Integer minuto = fecha.get(Calendar.MINUTE);
        final Integer segundo = fecha.get(Calendar.SECOND);


        fechaRegistro =   formatonumerico(dia) + "/" + formatonumerico(mes) +"/"+ year.toString() +
                "%20" + formatonumerico(hora)+":"+formatonumerico(minuto)+":"+formatonumerico(segundo);



        btnregresarformalistapago = findViewById(R.id.btnRegresarListaFormaPago);
        btnregresarformalistapago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(ListadoFormaPagoActivity.this,ListadoAlmacenActivity.class);
                intent.putExtra("validador",validador);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Cliente",cliente);
                intent.putExtras(bundle);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Usuario",usuario);
                intent.putExtras(bundle2);
                startActivity(intent);
                finish();

            }
        });

        Consultartipopago();
        lvtipopago = findViewById(R.id.lvtipopago);
    }

    private void Consultartipopago() {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_FPAGO";
        listatipopago = new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){
                                for(int i=0;i<jsonArray.length();i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String listado = jsonObject.getString("COD_FPAGO")+ " - "
                                            + jsonObject.getString("DES_FPAGO");
                                    listatipopago.add(listado);
                                    listaAux.add(jsonObject.getString("COD_FPAGO"));
                                }

                                ListadoAlmacenActivity.CustomListAdapter listAdapter= new ListadoAlmacenActivity.CustomListAdapter(ListadoFormaPagoActivity.this , R.layout.custom_list , listatipopago);
                                lvtipopago.setAdapter(listAdapter);
                                lvtipopago.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent =  new Intent(ListadoFormaPagoActivity.this,BuscarProductoActivity.class);
                                        intent.putExtra("Almacen",almacen);
                                        intent.putExtra("indice",indice);
                                        intent.putExtra("validador",validador);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Cliente",cliente);
                                        intent.putExtras(bundle);
                                        intent.putExtra("TipoPago",listaAux.get(position));
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putSerializable("listaproductoselegidos",listaproductoselegidos);
                                        intent.putExtras(bundle1);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putSerializable("Usuario",usuario);
                                        intent.putExtras(bundle2);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListadoFormaPagoActivity.this);
                                builder.setMessage("No se pudo encontrar los tipos de pago correspondientes");
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

    public static class CustomListAdapter extends ArrayAdapter <String> {

        private Context mContext;
        private int id;
        private List<String> items ;

        public CustomListAdapter(Context context, int textViewResourceId , List<String> list)
        {
            super(context, textViewResourceId, list);
            mContext = context;
            id = textViewResourceId;
            items = list ;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent)
        {
            View mView = v ;
            if(mView == null){
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = vi.inflate(id, null);
            }

            TextView text = (TextView) mView.findViewById(R.id.textView);

            if(items.get(position) != null)
            {
                text.setTextColor(Color.BLACK);
                text.setText(items.get(position));
                int color = Color.argb(10, 0, 20, 255);
                text.setBackgroundColor(color);
            }
            return mView;
        }
    }

    private String formatonumerico (Integer numero){

        String numeroString = numero.toString();
        if (numero <= 9){
            numeroString = "0"+ numero.toString();
        }
        return  numeroString;
    }

}
