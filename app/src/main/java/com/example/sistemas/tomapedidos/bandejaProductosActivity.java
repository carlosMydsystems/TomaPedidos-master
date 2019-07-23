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
import com.example.sistemas.tomapedidos.Entidades.ClienteSucursal;
import com.example.sistemas.tomapedidos.Entidades.Clientes;
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.Utilitarios.Utilitario;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionTestMovil;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Dolares;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.Soles;

public class bandejaProductosActivity extends AppCompatActivity {

    TextView tvtitulodinamico;
    Productos productos,producto;
    Button btnbuscarproducto, btnterminar,btnregresarbandeja, btngrabarpedido;
    ListView lvbandejaproductos;
    ArrayList<String> listabandejaproductoselegidos;
    Clientes cliente;
    String cantidad ,Precio, url, almacen, tipoformapago, fechaRegistro, validador,id,
            id_pedido,retorno,Index,check;
    View mview;
    ArrayList<Productos> listaproductoselegidos;
    Usuario  usuario;
    Double preciolista, precio = 0.0;
    ListView listView;
    Boolean valida;
    ImageButton imgbtnregresar;
    ArrayList<ClienteSucursal> listaClienteSucursal;
    String ValidarBtnTerminar, cadenaTituloAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja_productos);
        // Se captura los parametros de los otros Intent

        listaproductoselegidos = (ArrayList<Productos>) getIntent()
                .getSerializableExtra("listaProductoselegidos");
        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");
        check = getIntent().getStringExtra("Check");

        if (ValidarBtnTerminar == null){
            ValidarBtnTerminar = "true";
        }else{
            ValidarBtnTerminar = getIntent().getStringExtra("ValidarBtnTerminar");
        }

        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal");

        if (check!=null){
            EliminaPromocion();
            check = null;
        }

        valida = Boolean.valueOf(validador);
        listabandejaproductoselegidos = new ArrayList<>();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo
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

                    preciolista = Double.valueOf(listaproductoselegidos.get(i).getPrecio().replace(",",""));
                }


                if (usuario.getMoneda().equals("1")) {

                    if (listaproductoselegidos.get(i).getObservacion() == null) {
                        listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo() + " - " +
                                listaproductoselegidos.get(i).getDescripcion() + "\nCant: " + formateador.format(Double.valueOf(listaproductoselegidos.
                                get(i).getCantidad())) + "                                    Unidad: " +
                                listaproductoselegidos.get(i).getUnidad() + "\nPrecio: " + Soles + " " + formateador.format((double) preciolista) +
                                "                  Subtotal: "+Soles+" " + formateador.format((double) Aux));
                    } else {

                        if (listaproductoselegidos.get(i).getObservacion().equals("S") || listaproductoselegidos.get(i).getObservacion().equals("T")) {
                            listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo() + " - " +
                                    listaproductoselegidos.get(i).getDescripcion() + "\np - Cant: " + formateador.format(Double.valueOf(listaproductoselegidos.
                                    get(i).getCantidad())) + "                                     Unidad: " +
                                    listaproductoselegidos.get(i).getUnidad() + "\nPrecio: "+Soles+" " + formateador.format((double) preciolista) +
                                    "                  Subtotal: "+Soles+" " + formateador.format((double) Aux));
                        }
                    }
                }else{

                    if (listaproductoselegidos.get(i).getObservacion() == null) {

                        listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo() + " - " +
                                listaproductoselegidos.get(i).getDescripcion() + "\nCant: " + formateador.format(Double.valueOf(listaproductoselegidos.
                                get(i).getCantidad())) + "                                    Unidad: " +
                                listaproductoselegidos.get(i).getUnidad() + "\nPrecio: " + Dolares + " " + formateador.format((double) preciolista) +
                                "                  Subtotal: "+Dolares+" " + formateador.format((double) Aux));

                    } else {

                        if (listaproductoselegidos.get(i).getObservacion().equals("S") || listaproductoselegidos.get(i).getObservacion().equals("T")) {
                            listabandejaproductoselegidos.add(listaproductoselegidos.get(i).getCodigo() + " - " +
                                    listaproductoselegidos.get(i).getDescripcion() + "p - Cant: " + formateador.format(Double.valueOf(listaproductoselegidos.
                                    get(i).getCantidad())) + "                                     Unidad: " +
                                    listaproductoselegidos.get(i).getUnidad() + "\nPrecio: "+Dolares+" " + formateador.format((double) preciolista) +
                                    "                  Subtotal: "+Dolares+" " + formateador.format((double) Aux));
                        }
                    }
                }
            }

            cantidad = String.valueOf(listaproductoselegidos.size());
            Precio = String.valueOf(precio);
            btnbuscarproducto = findViewById(R.id.btnproducto);
            btnterminar = findViewById(R.id.btnterminar);
            tvtitulodinamico = findViewById(R.id.tvtitulodinamicoPedidos);
            btnregresarbandeja = findViewById(R.id.btnRegresarBandejaPedidos);
            btngrabarpedido = findViewById(R.id.btnValidarPromociones);
            imgbtnregresar = findViewById(R.id.imgPromocionElegida);
            if (ValidarBtnTerminar.equals("true")){

                btnterminar.setVisibility(View.VISIBLE);
                btngrabarpedido.setVisibility(View.GONE);
            }else {
                btnterminar.setVisibility(View.GONE);
                btngrabarpedido.setVisibility(View.VISIBLE);
            }
            imgbtnregresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this)
                     .setCancelable(false)
                     .setMessage("Se perderán todos los cambios, ¿Esta seguro que desea regresar?")
                     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(Utilitario.isOnline(getApplicationContext())){

                        EliminarProductoporIdpedido(id_pedido);
                        Intent intent = new Intent(bandejaProductosActivity.this, MostrarClienteActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Cliente", cliente);
                        intent.putExtras(bundle);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("Usuario", usuario);
                        intent.putExtras(bundle1);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("listaClienteSucursal",listaClienteSucursal);
                        intent.putExtras(bundle3);
                        startActivity(intent);
                        finish();

                    }else{
                        AlertDialog.Builder build = new AlertDialog.Builder(bandejaProductosActivity.this);
                        build.setTitle("Atención .. !");
                        build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
                        build.setCancelable(false);
                        build.setNegativeButton("ACEPTAR",null);
                        build.create().show();
                    }
                }
            });

            builder.create()
                    .show();
                }
            });

            if (usuario.getMoneda().equals("1")){

                cadenaTituloAux = "Productos : " + cantidad + "   |  Monto : " +Soles + "\t"+ formateador.format(precio) + "";

            }else{

                cadenaTituloAux = "Productos : " + cantidad + "   |  Monto : " +Dolares + "\t"+ formateador.format(precio) + "";

            }

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
                    intent.putExtra("Index", Index);
                    intent.putExtra("validador", "false");
                    intent.putExtra("retorno", retorno);
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
                    Bundle bundle4 = new Bundle();
                    bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                    intent.putExtras(bundle4);
                    startActivity(intent);
                    finish();
                }
            });

            // Boton para poder verificar las promociones que se encuentren

            btnterminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listaproductoselegidos.size() > 0) {

                        Intent intent = new Intent(bandejaProductosActivity.this, PromocionesActivity.class);
                        intent.putExtra("TipoPago", tipoformapago);
                        intent.putExtra("Index", Index);
                        intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                        intent.putExtra("Almacen", almacen);
                        intent.putExtra("id_pedido", id_pedido);
                        intent.putExtra("retorno", retorno);
                        intent.putExtra("Check", check);
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
                        Bundle bundle4 = new Bundle();
                        bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                        intent.putExtras(bundle4);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(bandejaProductosActivity.this, "La bandeja esta vacia", Toast.LENGTH_SHORT).show();

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
                    imgbtnregresar.setVisibility(View.GONE);
                }
            });

            btngrabarpedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(bandejaProductosActivity.this, ProveedorActivity.class);
                    intent.putExtra("TipoPago", tipoformapago);
                    intent.putExtra("Index", Index);
                    intent.putExtra("Cantidad", cantidad);
                    intent.putExtra("Precio", "" + precio);
                    intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                    intent.putExtra("Almacen", almacen);
                    intent.putExtra("id_pedido", id_pedido);
                    intent.putExtra("retorno", retorno);
                    intent.putExtra("Check", check);
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
                    Bundle bundle4 = new Bundle();
                    bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                    intent.putExtras(bundle4);
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

                if(Utilitario.isOnline(getApplicationContext())){

                    if (listaproductoselegidos.get(position).getObservacion() == null) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
                        builder.setCancelable(false);

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

                                case 1: // Eliminar el Producto

                                    EliminaPromocion();
                                    if (listaproductoselegidos.get(position).getObservacion() == null) {
                                        final String trama1 = id_pedido + "|" + listaproductoselegidos.get(position).getIndice();

                                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                                bandejaProductosActivity.this);
                                        builder1.setCancelable(false);
                                        builder1.setMessage("Esta seguro que desea eliminar el pedido?")
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        listaproductoselegidos.remove(position);
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

                                        btnterminar.setVisibility(View.VISIBLE);
                                        btngrabarpedido.setVisibility(View.GONE);
                                        valida = true;
                                        break;

                                    } else {
                                        salirlistview();
                                        break;
                                    }

                                case 2: // Cancela la accion pero elimina la promocion

                                    EliminaPromocion();
                                    salirlistview();
                                    btnterminar.setVisibility(View.VISIBLE);
                                    btngrabarpedido.setVisibility(View.GONE);
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
                        return true;
                    } else {
                        return true;
                    }

                }else{

                    AlertDialog.Builder build = new AlertDialog.Builder(bandejaProductosActivity.this);
                    build.setTitle("Atención .. !");
                    build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
                    build.setCancelable(false);
                    build.setNegativeButton("ACEPTAR",null);
                    build.create().show();

                }
                    return true;
                }
            });

            lvbandejaproductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Double Aux = Double.valueOf(listaproductoselegidos.get(position).getPrecioAcumulado().replace(",", ""));

                    AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
                    builder.setCancelable(false)
                        .setMessage(
                            "Codigo\t\t\t:\t\t" + listaproductoselegidos.get(position).getCodigo() + "\n" +
                                    "Nombre\t\t\t:\t\t" + listaproductoselegidos.get(position).getDescripcion() + "\n" +
                                    "Unidad\t\t\t:\t\t" + listaproductoselegidos.get(position).getUnidad() + "\n" +
                                    "Cantidad\t\t:\t\t" + formateador.format(Double.valueOf(listaproductoselegidos.get(position).getCantidad())) + "\n" +
                                    "Precio\t\t\t\t:\t\t" + Soles + " " + listaproductoselegidos.get(position).getPrecio() + "\n" +
                                    "Subtotal\t\t\t:\t\tS/ " + formateador.format((double) Aux))
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

                if (listaproductoselegidos.get(position1).getObservacion().equals("S")||listaproductoselegidos.get(position1).getObservacion().equals("T")){

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
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("Almacen",almacen);

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

        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
        intent.putExtras(bundle4);

        startActivity(intent);
        finish();

    }

    private void Editarproductoselecionado(Integer position) {

        producto = listaproductoselegidos.get(position);
        Intent intent =  new Intent(bandejaProductosActivity.this, ActualizarRegistroPedidosActivity.class);
        intent.putExtra("TipoPago",tipoformapago);
        intent.putExtra("id_pedido",id_pedido);
        intent.putExtra("Index",Index);
        intent.putExtra("position",position.toString());
        intent.putExtra("Almacen",almacen);

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

        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
        intent.putExtras(bundle4);
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
            url =  ejecutaFuncionTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_ELIMINA_TRAMA_MOVIL&variables='"+trama+"'";
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
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

    private void EliminarProductoporIdpedido(String idpedido) {

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        url =  ejecutaFuncionTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_ELIMINA_PEDIDO_TRAMA&variables='"+idpedido+"'";
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
            AlertDialog.Builder builder = new AlertDialog.Builder(bandejaProductosActivity.this);
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

