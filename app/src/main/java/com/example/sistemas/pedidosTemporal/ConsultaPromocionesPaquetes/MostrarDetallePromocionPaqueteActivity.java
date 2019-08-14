package com.example.sistemas.pedidosTemporal.ConsultaPromocionesPaquetes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.sistemas.pedidosTemporal.Entidades.Clientes;
import com.example.sistemas.pedidosTemporal.Entidades.ConsultaPromocion;
import com.example.sistemas.pedidosTemporal.Entidades.Usuario;
import com.example.sistemas.pedidosTemporal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.example.sistemas.pedidosTemporal.LoginActivity.ejecutaFuncionCursorTestMovil;

public class MostrarDetallePromocionPaqueteActivity extends AppCompatActivity {

    ListView lvMuestraPromociones;
    ArrayList<ConsultaPromocion> listaPromocionAux;
    ArrayList<String> listaPromociones;
    ImageButton imgPromocionElegida;
    Usuario usuario;
    Clientes cliente;
    String nroPromocion,url,position,titulo,subtitulo,moneda;
    ProgressDialog progressDialog;
    ArrayList<ConsultaPromocion> listaPromocionesObjetos;
    ConsultaPromocion consultaPromocion;
    ArrayList<String> listaPromocionesStr;
    TextView textView30,tvTituloPromocion;

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
        textView30 = findViewById(R.id.textView30);
        tvTituloPromocion = findViewById(R.id.tvTituloPromocion);
        imgPromocionElegida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarDetallePromocionPaqueteActivity.this, ListaPaquetePromocionActivity.class);
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

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        titulo = "PAQUETE :"+listaPromocionesObjetos.get(Integer.valueOf(position)).getNroPromocion();
        textView30.setText(titulo);



        moneda = listaPromocionesObjetos.get(Integer.valueOf(position)).getMoneda();
        subtitulo = "<b>PRECIO PAQUETE : </b>"+ moneda+ formateador.format(Double.valueOf(listaPromocionesObjetos.get(Integer.valueOf(position)).
                getPreciopaquete()))+ "<br>" +"AHORRO : "+ moneda+formateador.format(Double.valueOf(listaPromocionesObjetos.get(Integer.valueOf(position)).getAhorro()));
        tvTituloPromocion.setText(Html.fromHtml(subtitulo));


        listaPromociones = new ArrayList<>();
        BuscarDetallePromocion(listaPromocionesObjetos.get(Integer.valueOf(position)).getNroPromocion(),moneda);
        MostrarDetallePromocionPaqueteActivity.CustomListAdapter1 listAdapter = new MostrarDetallePromocionPaqueteActivity.CustomListAdapter1(MostrarDetallePromocionPaqueteActivity.this, R.layout.custom_list, listaPromociones);
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
                if (String.valueOf(items.get(position).charAt(0)).equals("*")){

                    text.setTextColor(Color.RED);
                    text.setText(items.get(position));
                    text.setTextSize(15);
                    int color = Color.argb(70,255, 255, 0);
                    text.setBackgroundColor(color);
                }else {
                    text.setTextColor(Color.BLACK);
                    text.setText(items.get(position));
                    text.setTextSize(15);
                    int color = Color.argb(10, 0, 20, 255);
                    text.setBackgroundColor(color);
                }
            }
            return mView;
        }
    }

    private void BuscarDetallePromocion(String nroPromocion, final String Moneda) {

        progressDialog = new ProgressDialog(MostrarDetallePromocionPaqueteActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_DPAQUETE&variables=%27PQT|"+nroPromocion+"%27";

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
                                            MostrarDetallePromocionPaqueteActivity.this);
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
                                        consultaPromocion.setCantidad(jsonObject.getString("CANTIDAD"));
                                        consultaPromocion.setPreciopaquete(jsonObject.getString("PRECIO"));
                                        consultaPromocion.setTasadescuento(jsonObject.getString("TASA_DESCUENTO"));
                                        consultaPromocion.setSubtotal(jsonObject.getString("SUBTOTAL"));
                                        consultaPromocion.setUndVenta(jsonObject.getString("UND_MEDIDA"));

                                        listaPromocionesObjetos.add(consultaPromocion);
                                        Double Aux1 = Double.valueOf(consultaPromocion.getCantidad().toString());
                                        consultaPromocion.setCantidad(formateador.format((double) Aux1) + "");

                                        // String[] list = consultaPromocion.getDescripcion().split(" ");

                                            listaPromocionesStr.add(consultaPromocion.getCodArticulo() + "  -  " + consultaPromocion.getDescripcion() +" ( "+consultaPromocion.getUndVenta()+" ) "+ "\n"
                                                    + "MARCA\t : " + consultaPromocion.getDesMarca() + "\n"+
                                                    "CANTIDAD : " + consultaPromocion.getCantidad() + "\t\t\t\tPRECIO : "+ Moneda+" " +formateador.format(Double.valueOf(consultaPromocion.getPreciopaquete())) + "\n" +
                                                    "TASA DSCTO : " + consultaPromocion.getTasadescuento() + "\t\t\tSUBTOTAL : "+ Moneda+" " +formateador.format(Double.valueOf(consultaPromocion.getSubtotal())));

                                    }
                                    progressDialog.dismiss();
                            /*

                            ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                    CustomListAdapter(MostrarConsultaActivity.this, R.layout.custom_list, listaPromocionesStr);

                            */

                                    MostrarDetallePromocionPaqueteActivity.CustomListAdapter1 listAdapter = new MostrarDetallePromocionPaqueteActivity.CustomListAdapter1(
                                            MostrarDetallePromocionPaqueteActivity.this, R.layout.custom_list, listaPromocionesStr);

                                    lvMuestraPromociones.setAdapter(listAdapter);
                                }
                            }else {
                                progressDialog.dismiss();
                                listaPromocionesStr.clear();
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                                        , R.layout.support_simple_spinner_dropdown_item,listaPromocionesStr);
                                lvMuestraPromociones.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarDetallePromocionPaqueteActivity.this);
                                builder.setMessage("No se llego a encontrar el registro")
                                        .setNegativeButton("Aceptar",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(MostrarDetallePromocionPaqueteActivity.this, "el error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MostrarDetallePromocionPaqueteActivity.this);
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


