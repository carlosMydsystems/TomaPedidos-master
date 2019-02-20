package com.example.sistemas.tomapedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class bandejaProductosActivity extends AppCompatActivity {

    TextView tvtitulodinamico;
    Productos productos;
    Button btnbuscarproducto, btnterminar,btnregresarbandeja, btngrabarpedido;
    ListView lvbandejaproductos;
    ArrayList<String> listabandejaproductos,listabandejaproductoselegidos;
    Clientes cliente;
    String cantidad ,Precio, url, almacen, tipoformapago, fechaRegistro, documento = "Boleta",
            validador,id,Ind,id_pedido,retorno,Index;
    View mview;
    Integer cantidadProductos=0;
    ArrayList<Productos> listaproductoselegidos;
    Usuario  usuario;
    Productos producto;
    Double preciolista, precio = 0.0;
    ListView listView;
    Boolean valida;
    ImageButton imgbtnregresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja_productos);
        // Se captura los parametros de los otros Intent
        listaproductoselegidos = (ArrayList<Productos>) getIntent()
                .getSerializableExtra("listaProductoselegidos");   //

        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");   //
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");    //
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        Ind = getIntent().getStringExtra("indice");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");

        valida = Boolean.valueOf(validador);
        listabandejaproductos = new ArrayList<>();
        cantidadProductos = listabandejaproductos.size();
        listabandejaproductoselegidos = new ArrayList<>();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

        // SE llega a generar el calendario

        Calendar fecha = Calendar.getInstance();
        final Integer dia = fecha.get(Calendar.DAY_OF_MONTH);
        final Integer mes = fecha.get(Calendar.MONTH) + 1;
        Integer year = fecha.get(Calendar.YEAR);
        final Integer hora =  fecha.get(Calendar.HOUR_OF_DAY);
        final Integer minuto = fecha.get(Calendar.MINUTE);
        final Integer segundo = fecha.get(Calendar.SECOND);
        fechaRegistro =   formatonumerico(dia) + "/" + formatonumerico(mes) +"/"+ year.toString() +
                "%20" + formatonumerico(hora)+":"+formatonumerico(minuto)+":"+formatonumerico(segundo);

        // valores para el sumarizado de la bandeja

        if (listaproductoselegidos != null) {

            for (int i = 0; i < listaproductoselegidos.size(); i++) {
                // calcula numero de productos
                Double Aux = 0.0;
                if (listaproductoselegidos.get(i).getPrecioAcumulado().equals("")) {

                    precio = 0.0;

                } else {

                    precio = precio + Double.valueOf(listaproductoselegidos.get(i).getPrecioAcumulado().replace(",", ""));
                    Aux = Double.valueOf(listaproductoselegidos.get(i).getPrecioAcumulado().replace(",", ""));
                }

                if (listaproductoselegidos.get(i).getPrecio().equals("")) {

                    preciolista = 0.0;
                } else {

                    preciolista = Double.valueOf(listaproductoselegidos.get(i).getPrecio());
                }

                listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo() + " - " +
                        listaproductoselegidos.get(i).getDescripcion() + "\nCant: " + listaproductoselegidos.
                        get(i).getCantidad() + "                                             Unidad: " +
                        listaproductoselegidos.get(i).getUnidad() + "\nPrecio: S/ " + formateador.format((double) preciolista) +
                        "                  Subtotal: S/ " + formateador.format((double) Aux));
            }

            cantidad = String.valueOf(listaproductoselegidos.size());
            Precio = String.valueOf(precio);
            btnbuscarproducto = findViewById(R.id.btnproducto);
            btnterminar = findViewById(R.id.btnterminar);
            tvtitulodinamico = findViewById(R.id.tvtitulodinamico);
            btnregresarbandeja = findViewById(R.id.btnRegresarBandejaPedidos);
            btngrabarpedido = findViewById(R.id.btnValidarPromociones);
            imgbtnregresar = findViewById(R.id.imgbtnRegresaBandejaPedidos);
            imgbtnregresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this)
                    .setMessage("Esta seguro que desea regresar, Todos los cambios se perderan")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (int i = 0 ; i<listaproductoselegidos.size();i++){

                        String trama = id_pedido+"|"+listaproductoselegidos.get(i).getIndice();
                        EliminarProducto(trama);
                    }

                    Intent intent = new Intent(bandejaProductosActivity.this, MostrarClienteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cliente", cliente);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Usuario", usuario);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    finish();
                }
            });

            builder.create()
                    .show();

                }
            });

            String cadenaTituloAux = "Productos : " + cantidad + "   |  Monto : S/ " + formateador.format(precio) + "";
            tvtitulodinamico.setText(cadenaTituloAux);

            if (retorno == null) {

            } else if (retorno.equals("retorno")) {
                salirlistview();
                btnterminar.setVisibility(View.VISIBLE);
                btngrabarpedido.setVisibility(View.GONE);
            }

            if (valida) {
                btnterminar.setVisibility(View.VISIBLE);
                btngrabarpedido.setVisibility(View.GONE);
            } else {
                btnterminar.setVisibility(View.GONE);
                btngrabarpedido.setVisibility(View.VISIBLE);
            }

            mview = getLayoutInflater().inflate(R.layout.listview_dialog, null);
            btnbuscarproducto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Index = String.valueOf(Integer.valueOf(Index) + 1);
                    EliminaPromocion();
                    Intent intent = new Intent(bandejaProductosActivity.this, BuscarProductoActivity.class);
                    intent.putExtra("TipoPago", tipoformapago);
                    intent.putExtra("indice", Ind);
                    intent.putExtra("Index", Index);
                    intent.putExtra("validador", "false");
                    intent.putExtra("Almacen", almacen);
                    intent.putExtra("id_pedido", id_pedido);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Cliente", cliente);
                    intent.putExtras(bundle);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("listaproductoselegidos", listaproductoselegidos);
                    intent.putExtras(bundle1);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("Usuario", usuario);
                    intent.putExtras(bundle3);
                    startActivity(intent);
                    finish();
                }
            });

            // Boton terminar

            btnterminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listaproductoselegidos.size() > 0) {

                        Intent intent = new Intent(bandejaProductosActivity.this, PromocionesActivity.class);

                        intent.putExtra("TipoPago", tipoformapago);
                        intent.putExtra("indice", Ind);
                        intent.putExtra("Index", Index);
                        intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                        intent.putExtra("Almacen", almacen);
                        intent.putExtra("id_pedido", id_pedido);
                        intent.putExtra("validador", "false");

                        Bundle bundle = new Bundle();
                        Bundle bundle2 = new Bundle();
                        Bundle bundle3 = new Bundle();

                        bundle.putSerializable("listaproductoselegidos", listaproductoselegidos);
                        bundle2.putSerializable("Cliente", cliente);
                        bundle3.putSerializable("Usuario", usuario);

                        intent.putExtras(bundle);
                        intent.putExtras(bundle2);
                        intent.putExtras(bundle3);

                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(bandejaProductosActivity.this, "La canasta esta vacia", Toast.LENGTH_SHORT).show();

                    }
                }
            });



            btnregresarbandeja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btnbuscarproducto.setVisibility(View.VISIBLE);
                    btnterminar.setVisibility(View.VISIBLE);
                    btngrabarpedido.setVisibility(View.GONE);
                    btnregresarbandeja.setVisibility(View.GONE);
                }
            });

            btngrabarpedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(bandejaProductosActivity.this, FechaPactadaActivity.class);

                    intent.putExtra("TipoPago", tipoformapago);
                    intent.putExtra("indice", Ind);
                    intent.putExtra("Index", Index);
                    intent.putExtra("Cantidad", cantidad);
                    intent.putExtra("Precio", "" + precio);
                    intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                    intent.putExtra("Almacen", almacen);
                    intent.putExtra("id_pedido", id_pedido);
                    intent.putExtra("validador", "false");

                    Bundle bundle = new Bundle();
                    Bundle bundle2 = new Bundle();
                    Bundle bundle3 = new Bundle();

                    bundle.putSerializable("listaproductoselegidos", listaproductoselegidos);
                    bundle2.putSerializable("Cliente", cliente);
                    bundle3.putSerializable("Usuario", usuario);

                    intent.putExtras(bundle);
                    intent.putExtras(bundle2);
                    intent.putExtras(bundle3);

                    startActivity(intent);
                    finish();

                }
            });

            lvbandejaproductos = findViewById(R.id.lvbandejaProductos);
            ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                    CustomListAdapter(bandejaProductosActivity.this, R.layout.custom_list, listabandejaproductoselegidos);
            lvbandejaproductos.setAdapter(listAdapter);

            // metodo para entrar en la lista con el click largo

            lvbandejaproductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (listaproductoselegidos.get(position).getObservacion() == null) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
                        builder.setCancelable(true);

                        listView = mview.findViewById(R.id.lvopciones);
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                                bandejaProductosActivity.this, android.R.layout.simple_list_item_1,
                                getResources().getStringArray(R.array.opciones));
                        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                                usuario = (Usuario) getIntent().getSerializableExtra("Usuario");

                                switch (i) {
                                    case 0: // Editar producto

                                        EliminaPromocion();
                                        if (listaproductoselegidos.get(position).getObservacion() == null) {
                                            btnterminar.setVisibility(View.VISIBLE);
                                            btngrabarpedido.setVisibility(View.GONE);
                                            Editarproductoselecionado(position);
                                            valida = true;
                                            break;

                                        } else {
                                            salirlistview();
                                            break;
                                        }

                                    case 1:

                                        EliminaPromocion();
                                        if (listaproductoselegidos.get(position).getObservacion() == null) {
                                            final String trama1 = id_pedido + "|" + listaproductoselegidos.get(position).getIndice();

                                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                                    bandejaProductosActivity.this);
                                            builder1.setMessage("Esta seguro que desea eliminar el pedido?")
                                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            EliminarProducto(trama1);
                                                            dialogInterface.cancel();
                                                            salirlistview();
                                                        }
                                                    })
                                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            salirlistview();
                                                        }
                                                    })
                                                    .create()
                                                    .show();

                                            listaproductoselegidos.remove(position);
                                            btnterminar.setVisibility(View.VISIBLE);
                                            btngrabarpedido.setVisibility(View.GONE);
//                                        Alertsdialog("Borrar el producto");
                                            valida = true;
                                            break;

                                        } else {
                                            salirlistview();
                                            break;
                                        }

                                    case 2:

                                        EliminaPromocion();
                                        salirlistview();
                                        btnterminar.setVisibility(View.VISIBLE);
                                        btngrabarpedido.setVisibility(View.GONE);
                                        valida = true;
                                        break;
                                }
                            }
                        });  //

                        builder.setView(mview);
                        AlertDialog dialog = builder.create();
                        if (mview.getParent() != null)
                            ((ViewGroup) mview.getParent()).removeView(mview); // <- fix
                        dialog.show();
                        return true;
                    } else {
                        return true;
                    }
                }
            });

            lvbandejaproductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Double Aux = Double.valueOf(listaproductoselegidos.get(position).getPrecioAcumulado().replace(",", ""));

                    AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
                    builder.setMessage(
                            "Codigo       :   " + listaproductoselegidos.get(position).getCodigo() + "\n" +
                                    "Nombre      :   " + listaproductoselegidos.get(position).getDescripcion() + "\n" +
                                    "Unidad       :   " + listaproductoselegidos.get(position).getUnidad() + "\n" +
                                    "Cantidad    :   " + listaproductoselegidos.get(position).getCantidad() + "\n" +
                                    "Precio        : S/ " + listaproductoselegidos.get(position).getPrecio() + "\n" +
                                    "Subtotal     : S/ " + formateador.format((double) Aux))
                            .setNegativeButton("Aceptar", null)
                            .create()
                            .show();
                }
            });

        }
    }

    private void EliminaPromocion(){
        for (int position1 = 0;position1 < listaproductoselegidos.size();position1++){

            if (listaproductoselegidos.get(position1).getObservacion() == null){

            }else {

                if (listaproductoselegidos.get(position1).getObservacion().equals("Promocion")){

                    Integer indicePromocion = listaproductoselegidos.get(position1).getIndice();
                    EliminarProducto(id_pedido+"|"+indicePromocion);
                    listaproductoselegidos.remove(position1);
                    position1--;
                }
            }
        }
    }

    private void salirlistview (){

        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        almacen =  getIntent().getStringExtra("Almacen");

        Intent intent = new Intent(bandejaProductosActivity.this, bandejaProductosActivity.class);
        intent.putExtra("validador","true");
        intent.putExtra("id_pedido",id_pedido);
        intent.putExtra("Index",Index);
        intent.putExtra("indice", Ind);
        intent.putExtra("TipoPago",tipoformapago);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Producto", productos);
        intent.putExtras(bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("listaProductoselegidos", listaproductoselegidos);
        intent.putExtras(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("Usuario", usuario);
        intent.putExtras(bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("Cliente", cliente);
        intent.putExtras(bundle3);
        intent.putExtra("Almacen",almacen);
        startActivity(intent);
        finish();

    }
    private void Alertsdialog(String msg){

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(
                bandejaProductosActivity.this);
        builder1.setMessage("Esta seguro que desea " + msg + "?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        salirlistview();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        salirlistview();
                    }
                })
                .create()
                .show();
    }

    private void ActualizarProducto(String trama) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

       // http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=pkg_web_herramientas.fn_ws_registra_trama_movil&variables=

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_REGISTRA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("OK")){
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

    private void Editarproductoselecionado(Integer position) {

        producto = listaproductoselegidos.get(position);

        Intent intent =  new Intent(bandejaProductosActivity.this, ActualizarRegistroPedidosActivity.class);
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("id_pedido",id_pedido);
        intent.putExtra("Index",Index);
        intent.putExtra("position",position.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("Usuario",usuario);
        intent.putExtras(bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("listaproductoselegidos",listaproductoselegidos);
        intent.putExtras(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("Cliente",cliente);
        intent.putExtras(bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("Producto", producto);
        intent.putExtras(bundle3);
        intent.putExtra("Almacen",almacen);
        startActivity(intent);
        finish();
    }

    private String formatonumerico (Integer numero){

        String numeroString = numero.toString();
        if (numero <= 9){
            numeroString = "0"+ numero.toString();
        }
        return  numeroString;
    }

    private void EliminarProducto(String trama) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=pkg_web_herramientas.fn_ws_registra_trama_movil&variables=

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_ELIMINA_TRAMA_MOVIL&variables='"+trama+"'";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("OK")){
                            // insertaCampos(listaproductoselegidos,id);
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

