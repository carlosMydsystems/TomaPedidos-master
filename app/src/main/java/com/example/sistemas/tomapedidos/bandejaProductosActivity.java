package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class bandejaProductosActivity extends AppCompatActivity {

    TextView tvtitulodinamico;
    Productos productos;
    Button btnbuscarproducto, btnterminar,btnregresarbandeja, btnvalidarpromociones;
    ListView lvbandejaproductos;
    ArrayList<String> listabandejaproductos,listabandejaproductoselegidos;
    Clientes cliente;
    String cantidad ,Precio, url, almacen, tipoformapago, fechaRegistro, documento = "Boleta",
            validador;  // se define el documento en el caso que se use en la trama
    View mview;
    Integer cantidadProductos=0;
    ArrayList<Productos> listaproductoselegidos;
    Usuario  usuario;
    Productos producto;
    Double preciolista, precio = 0.0,Aux;
    String id,Ind,id_pedido,cantidadlista,retorno;
    ListView listView;
    Boolean valida;

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
        valida = Boolean.valueOf(validador);
        listabandejaproductos = new ArrayList<>();
        cantidadProductos = listabandejaproductos.size();
        listabandejaproductoselegidos = new ArrayList<>();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,###.00",simbolos); // Se crea el formato del numero con los simbolo

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

        for (int i=0;i<listaproductoselegidos.size();i++){
           // calcula numero de productos
            Double Aux = 0.0;
            if (listaproductoselegidos.get(i).getPrecioAcumulado().equals("")){

                precio = 0.0;

            }else {

                precio = precio + Double.valueOf(listaproductoselegidos.get(i).getPrecioAcumulado().replace(",",""));
                Aux = Double.valueOf(listaproductoselegidos.get(i).getPrecioAcumulado().replace(",",""));
            }

            if (listaproductoselegidos.get(i).getPrecio().equals("")){

                preciolista = 0.0;
            }else {

                preciolista = Double.valueOf(listaproductoselegidos.get(i).getPrecio());
            }

            listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo()+ " - " +
            listaproductoselegidos.get(i).getDescripcion()+"\nCant: "+listaproductoselegidos.
            get(i).getCantidad()+ "                                             Unidad: "+
            listaproductoselegidos.get(i).getUnidad() + "\nPrecio: S/ "+formateador.format((double)preciolista) +
            "                  Subtotal: S/ "+formateador.format((double)Aux));

        }
        cantidad = String.valueOf(listaproductoselegidos.size());
        Precio = String.valueOf(precio);
        btnbuscarproducto =  findViewById(R.id.btnproducto);
        btnterminar = findViewById(R.id.btnterminar);
        tvtitulodinamico  = findViewById(R.id.tvtitulodinamico);
        btnregresarbandeja = findViewById(R.id.btnRegresarBandejaPedidos);
        btnvalidarpromociones = findViewById(R.id.btnValidarPromociones);
        String cadenaTituloAux = "Productos : "+ cantidad+"   |  Monto : S/ "+formateador.format(precio)+"";
        tvtitulodinamico.setText(cadenaTituloAux);

        if (retorno == null){

        }else if (retorno.equals("retorno")){

            salirlistview();
            btnterminar.setVisibility(View.VISIBLE);
            btnvalidarpromociones.setVisibility(View.GONE);

        }

        if (valida){

            btnterminar.setVisibility(View.VISIBLE);
            btnvalidarpromociones.setVisibility(View.GONE);

        }else{

            btnterminar.setVisibility(View.GONE);
            btnvalidarpromociones.setVisibility(View.VISIBLE);

        }

        mview = getLayoutInflater().inflate(R.layout.listview_dialog,null);
        btnbuscarproducto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(bandejaProductosActivity.this,BuscarProductoActivity.class);
                intent.putExtra("TipoPago",tipoformapago);
                intent.putExtra("indice",Ind);
                intent.putExtra("validador","false");
                intent.putExtra("Almacen",almacen);
                intent.putExtra("id_pedido",id_pedido);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Cliente",cliente);
                intent.putExtras(bundle);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("listaproductoselegidos",listaproductoselegidos);
                intent.putExtras(bundle1);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("Usuario",usuario);
                intent.putExtras(bundle3);
                startActivity(intent);
                finish();
            }
        });

        // Boton terminar

        btnterminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listaproductoselegidos.size() > 0){

                    Intent intent = new Intent(bandejaProductosActivity.this,PromocionesActivity.class);

                    intent.putExtra("TipoPago",tipoformapago);
                    intent.putExtra("indice",Ind);
                    intent.putExtra("cantidadlista",listaproductoselegidos.size()+"");
                    intent.putExtra("Almacen",almacen);
                    intent.putExtra("id_pedido",id_pedido);
                    intent.putExtra("validador","false");

                    Bundle bundle = new Bundle();
                    Bundle bundle2 = new Bundle();
                    Bundle bundle3 = new Bundle();

                    bundle.putSerializable("listaproductoselegidos", listaproductoselegidos);
                    bundle2.putSerializable("Cliente",cliente);
                    bundle3.putSerializable("Usuario",usuario);

                    intent.putExtras(bundle);
                    intent.putExtras(bundle2);
                    intent.putExtras(bundle3);

                    startActivity(intent);
                    finish();

                }else {

                    Toast.makeText(bandejaProductosActivity.this, "La canasta esta vacia", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnregresarbandeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnbuscarproducto.setVisibility(View.VISIBLE);
                btnterminar.setVisibility(View.VISIBLE);
                btnvalidarpromociones.setVisibility(View.GONE);
                btnregresarbandeja.setVisibility(View.GONE);
            }
        });

        btnvalidarpromociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        lvbandejaproductos =  findViewById(R.id.lvbandejaProductos);
        ListadoAlmacenActivity.CustomListAdapter listAdapter= new ListadoAlmacenActivity.
                CustomListAdapter(bandejaProductosActivity.this , R.layout.custom_list ,listabandejaproductoselegidos );
        lvbandejaproductos.setAdapter(listAdapter);

        // metodo para entrar en la lista con el click largo

        lvbandejaproductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (listaproductoselegidos.get(position).getObservacion()!=null){

                }else {
                    //Toast.makeText(bandejaProductosActivity.this, listaproductoselegidos.get(position).getObservacion(), Toast.LENGTH_SHORT).show();

                    for (int position1 = 0; position1 < listaproductoselegidos.size(); position1++) {

                        if (listaproductoselegidos.get(position1).getObservacion() == null) {

                        } else {

                            if (listaproductoselegidos.get(position1).getObservacion().equals("Promocion")) {
                                listaproductoselegidos.remove(position1);
                                position1--;
                            }
                        }
                    }

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

                                    if (listaproductoselegidos.get(position).getObservacion() == null) {
                                        btnterminar.setVisibility(View.VISIBLE);
                                        btnvalidarpromociones.setVisibility(View.GONE);
                                        Editarproductoselecionado(position);
                                        valida = true;
                                        break;

                                    } else {
                                        salirlistview();
                                        break;
                                    }

                                case 1:

                                    if (listaproductoselegidos.get(position).getObservacion() == null) {

                                        listaproductoselegidos.remove(position);
                                        String tramaEliminaSeleccion;
                                        int posfinal = position + 1;
                                        tramaEliminaSeleccion = id_pedido + "|" + posfinal;
                                        EliminarProducto(tramaEliminaSeleccion);
                                        btnterminar.setVisibility(View.VISIBLE);
                                        btnvalidarpromociones.setVisibility(View.GONE);
                                        Alertsdialog("Borrar el producto");
                                        valida = true;
                                        break;

                                    } else {
                                        salirlistview();
                                        break;
                                    }

                                case 2:

                                    salirlistview();
                                    btnterminar.setVisibility(View.VISIBLE);
                                    btnvalidarpromociones.setVisibility(View.GONE);
                                    valida = true;
                                    break;
                            }
                        }
                    });

                    builder.setView(mview);
                    AlertDialog dialog = builder.create();
                    if (mview.getParent() != null)
                        ((ViewGroup) mview.getParent()).removeView(mview); // <- fix
                    dialog.show();
                }
                    return true;

            }


        });

        lvbandejaproductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Double Aux = Double.valueOf(listaproductoselegidos.get(position).getPrecioAcumulado().replace(",",""));

                AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
                builder.setMessage(
                        "Codigo       :   "  + listaproductoselegidos.get(position).getCodigo() + "\n" +
                        "Nombre      :   " + listaproductoselegidos.get(position).getDescripcion()+ "\n"+
                        "Unidad       :   " + listaproductoselegidos.get(position).getUnidad()+ "\n"+
                        "Cantidad    :   " + listaproductoselegidos.get(position).getCantidad()+ "\n"+
                        "Precio        : S/ " + listaproductoselegidos.get(position).getPrecio()+ "\n"+
                        "Subtotal     : S/ " +formateador.format((double)Aux))
                        .setNegativeButton("Aceptar",null)
                        .create()
                        .show();
            }
        });
    }

    private void salirlistview (){

        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        almacen =  getIntent().getStringExtra("Almacen");

        Intent intent = new Intent(bandejaProductosActivity.this, bandejaProductosActivity.class);
        intent.putExtra("validador","true");
        intent.putExtra("id_pedido",id_pedido);
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


    private void Editarproductoselecionado(Integer position) {

        producto = listaproductoselegidos.get(position);

        Intent intent =  new Intent(bandejaProductosActivity.this, ActualizarRegistroPedidosActivity.class);
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("id_pedido",id_pedido);
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
    private void EliminarProducto(String tramaElimina) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        // http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=pkg_web_herramientas.fn_ws_registra_trama_movil&variables=

        url =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=PKG_WEB_HERRAMIENTAS.FN_WS_ELIMINA_TRAMA_MOVIL&variables='"+tramaElimina+"'";

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

