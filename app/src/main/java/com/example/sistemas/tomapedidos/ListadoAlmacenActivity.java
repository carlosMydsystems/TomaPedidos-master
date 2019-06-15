package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
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
import com.example.sistemas.tomapedidos.Entidades.ClienteSucursal;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.FormaPago;
import com.example.sistemas.tomapedidos.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionTestMovil;

public class ListadoAlmacenActivity extends AppCompatActivity {

    ListView lvAlmacenes;
    ArrayList<String> listaalmacen;
    Clientes cliente;
    Usuario usuario;
    ArrayList<FormaPago> listaFormasPago;
    Button btnretornolistadoalmacen;
    String indice;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    ArrayList<String> listaAlmacenes,listaAux;
    static ArrayList<ClienteSucursal> listaClienteSucursalBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_almacen);

        cliente  = new Clientes();
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");
        lvAlmacenes = findViewById(R.id.lvAlmacenes);

        if (listaClienteSucursal==null){

            listaClienteSucursal = listaClienteSucursalBack;

        }
        indice = getIntent().getStringExtra("Ind");
        //listaFormasPago = (ArrayList<FormaPago>) getIntent().getSerializableExtra("ListaFomasPago");

        // se hace la insercion del codigo en duro

        //**  Inicio Listado de almacenes Estaticos - Parte 1

        listaalmacen =  new ArrayList<>();
        listaalmacen.add("T02");
        listaalmacen.add("T04");
        listaalmacen.add("T10");
        listaalmacen.add("T11");
        listaalmacen.add("T12");
        listaalmacen.add("T14");
        listaalmacen.add("THI");
        listaalmacen.add("CD1");
        listaalmacen.add("CD2");
        listaalmacen.add("CD3");
        listaalmacen.add("CD4");

        //**  Fin Listado de almacenes Estaticos - Parte 1

/** Codigo para hacer dinámico los almacenes

        ProgressDialog progressDialog = new ProgressDialog(ListadoAlmacenActivity.this);
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.show();
        ConsultarAlmacen(progressDialog);

*/
        btnretornolistadoalmacen = findViewById(R.id.btnRetornoListadoAlmacen);

        btnretornolistadoalmacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(ListadoAlmacenActivity.this,MostrarClienteActivity.class);
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

        //**  Inicio Listado de almacenes Estaticos

        CustomListAdapter listAdapter= new CustomListAdapter(ListadoAlmacenActivity.this , R.layout.custom_list , listaalmacen);
        lvAlmacenes.setAdapter(listAdapter);

        lvAlmacenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =  new Intent(ListadoAlmacenActivity.this,ListadoFormaPagoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Cliente",cliente);
                intent.putExtras(bundle);
                intent.putExtra("Almacen",listaalmacen.get(position));
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

        /**  Fin Listado de almacenes Estaticos */

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

            if(items.get(position) != null )
            {
                text.setTextColor(Color.BLACK);
                text.setText(items.get(position));
                text.setTextSize(15);
                int color = Color.argb(10, 0, 20, 255);
                text.setBackgroundColor(color);
            }
            return mView;
        }
    }

    private void ConsultarAlmacen(final ProgressDialog progressdialog) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        String url1 =  ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_ALMACENES";
        listaAlmacenes = new ArrayList<>();
        listaAux = new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url1 ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressdialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            listaalmacen =  new ArrayList<>();
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            if (success){
                                for(int i=0;i<jsonArray.length();i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String listado = jsonObject.getString("COD_ALMACEN")+ " - "
                                            + jsonObject.getString("DESCRIPCION");
                                    listaalmacen.add(listado);
                                    listaAux.add(jsonObject.getString("COD_ALMACEN"));
                                }

                                ListadoAlmacenActivity.CustomListAdapter listAdapter= new ListadoAlmacenActivity.CustomListAdapter(ListadoAlmacenActivity.this , R.layout.custom_list , listaalmacen);
                                lvAlmacenes.setAdapter(listAdapter);

                                lvAlmacenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Intent intent =  new Intent(ListadoAlmacenActivity.this,ListadoFormaPagoActivity.class);

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("Cliente",cliente);
                                        intent.putExtras(bundle);
                                        intent.putExtra("Almacen",listaAux.get(position));
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
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListadoAlmacenActivity.this);
                                builder.setMessage("No se pudo encontrar los tipos de pago correspondientes");
                                builder.create().show();
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
}
