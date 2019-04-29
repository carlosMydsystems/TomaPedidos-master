package com.example.sistemas.tomapedidos.ConsultaPromociones;

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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.example.sistemas.tomapedidos.Entidades.ConsultaPromocion;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.ListadoAlmacenActivity;
import com.example.sistemas.tomapedidos.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;

public class MostrarConsultaActivity extends AppCompatActivity {

    ListView lvMuestraPromociones;
    ArrayList<ConsultaPromocion> listaPromocionAux;
    ArrayList<String> listaPromociones;
    ImageButton imgPromocionElegida;
    Usuario usuario;
    Clientes cliente;
    String nroPromocion,url,position;
    ProgressDialog progressDialog;
    ArrayList<ConsultaPromocion> listaPromocionesObjetos;
    ConsultaPromocion consultaPromocion;
    ArrayList<String> listaPromocionesStr;
    Clientes clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_consulta);

        listaPromocionesObjetos = (ArrayList<ConsultaPromocion>)getIntent().getSerializableExtra("listaPromocionesObjetos");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        cliente = (Clientes) getIntent().getSerializableExtra("Cliente");
        nroPromocion = getIntent().getStringExtra("nroPromocion");
        position = getIntent().getStringExtra("position");
        lvMuestraPromociones = findViewById(R.id.lvMuestraPromociones);
        imgPromocionElegida = findViewById(R.id.imgPromocionElegida);
        imgPromocionElegida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarConsultaActivity.this, ConsultarPromocionesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listaPromocionAux",listaPromocionAux);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Cliente",cliente);
                intent.putExtras(bundle1);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("Usuario",usuario);
                intent.putExtras(bundle2);
                startActivity(intent);
                finish();
            }
        });

        listaPromociones = new ArrayList<>();
        BuscarDetallePromocion(listaPromocionesObjetos.get(Integer.valueOf(position)).getNroPromocion());
        CustomListAdapter1 listAdapter = new CustomListAdapter1(MostrarConsultaActivity.this, R.layout.custom_list, listaPromociones);
        lvMuestraPromociones.setAdapter(listAdapter);
    }
    public class CustomListAdapter1 extends ArrayAdapter<String> {

        private Context mContext;
        private int id;
        private List<String> items ;

        public CustomListAdapter1(Context context, int textViewResourceId , List<String> list)
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

    private void BuscarDetallePromocion(String nroPromocion) {

        progressDialog = new ProgressDialog(MostrarConsultaActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PROMOCIONES_DET&variables=%27"+nroPromocion+"%27";

        listaPromocionesObjetos = new ArrayList<>();
        listaPromocionAux = new ArrayList<>();
        listaPromocionesStr = new ArrayList<>();


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
                                            MostrarConsultaActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        consultaPromocion = new ConsultaPromocion();
                                        consultaPromocion.setCodArticulo(jsonObject.getString("COD_ARTICULO"));
                                        consultaPromocion.setDescripcion(jsonObject.getString("DESCRIPCION"));
                                        consultaPromocion.setDesMarca(jsonObject.getString("DES_MARCA"));
                                        consultaPromocion.setUndVenta(jsonObject.getString("UND_VENTA"));
                                        consultaPromocion.setCantidad(jsonObject.getString("CANTIDAD"));
                                        consultaPromocion.setFlgRegalo(jsonObject.getString("FLG_REGALO"));
                                        listaPromocionesObjetos.add(consultaPromocion);
                                        Double Aux1 = Double.valueOf(consultaPromocion.getCantidad().toString());
                                        consultaPromocion.setCantidad(formateador.format((double) Aux1) + "");

                                        // String[] list = consultaPromocion.getDescripcion().split(" ");

                                        if (consultaPromocion.getFlgRegalo().trim().toString().equals("S")) {

                                            listaPromocionesStr.add(consultaPromocion.getCodArticulo() + "  -  " + consultaPromocion.getDescripcion() + "\n"
                                                    + "p - MARCA\t: " + consultaPromocion.getDesMarca() + "\t\t\t\tUNIDAD : " + consultaPromocion.getUndVenta() + "\n"
                                                    + "CANTIDAD \t : " + consultaPromocion.getCantidad());
                                        }else{

                                            listaPromocionesStr.add(consultaPromocion.getCodArticulo() + "  -  " + consultaPromocion.getDescripcion() + "\n"
                                                    + "MARCA\t: " + consultaPromocion.getDesMarca() + "\t\t\t\tUNIDAD : " + consultaPromocion.getUndVenta() + "\n"
                                                    + "CANTIDAD \t : " + consultaPromocion.getCantidad());
                                        }
                                    }
                                    progressDialog.dismiss();
                                    ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                            CustomListAdapter(MostrarConsultaActivity.this, R.layout.custom_list, listaPromocionesStr);
                                    lvMuestraPromociones.setAdapter(listAdapter);
                                }
                            }else {
                                progressDialog.dismiss();
                                listaPromocionesStr.clear();
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item,listaPromocionesStr);
                                lvMuestraPromociones.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarConsultaActivity.this);
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
