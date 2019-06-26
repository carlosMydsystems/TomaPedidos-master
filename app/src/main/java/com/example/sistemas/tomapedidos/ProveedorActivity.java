package com.example.sistemas.tomapedidos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.sistemas.tomapedidos.Entidades.Productos;
import com.example.sistemas.tomapedidos.Entidades.Proveedor;
import com.example.sistemas.tomapedidos.Entidades.SucursalProveedor;
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.Utilitarios.Utilitario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.example.sistemas.tomapedidos.LoginActivity.ejecutaFuncionCursorTestMovil;

public class ProveedorActivity extends AppCompatActivity {

    Button btnBuscarProveedor,btnAceptarBuscarProveedor;
    String tipoformapago,Index,cantidad,precio,almacen,id_pedido,url,TipodeConsulta = "NombreProveedor",
            validador,retorno,validadorRetornoFechaPactadaProveedor,direccionProveedor,nombreProveedor,
            codProveedor,valida,NombreProveedor,SucursalProveedor;
    Usuario usuario;
    Clientes cliente;
    ArrayList<Productos> listaproductoselegidos;
    ImageButton ibretornoMenuProveedor;
    RadioGroup rgBuscarProveedor;
    RadioButton rbNombreProveedor,rbCodigoProveedor;
    EditText etProveedor;
    ArrayList<String> listaProveedorPrueba,listaSucursalesProveedorStr;
    ListView lvProveedoresPrueba;
    ConstraintLayout LLMenuInferior;
    ArrayList<Proveedor> listaProveedores;
    Proveedor proveedor;
    SucursalProveedor sucursalProveedor;
    ArrayList<SucursalProveedor> listaSucursalesProveedor;
    TextView tvnombreproveedor,tvdireccionproveedor,tvdireccionsucursalproveedor;
    Spinner spsucursalproveedor;
    LinearLayout LLlistadoproveedores;
    ArrayList<ClienteSucursal> listaClienteSucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);

        cliente = (Clientes)getIntent().getSerializableExtra("Cliente");
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        listaClienteSucursal = (ArrayList<ClienteSucursal>) getIntent().getSerializableExtra("listaClienteSucursal"); //listaSucursalesProveedor
        listaproductoselegidos = (ArrayList<Productos>) getIntent().getSerializableExtra("listaproductoselegidos"); //
        validadorRetornoFechaPactadaProveedor = getIntent().getStringExtra("validadorRetornoFechaPactadaProveedor");
        listaSucursalesProveedorStr = (ArrayList<String>) getIntent().getSerializableExtra("listaSucursalesProveedorStr"); //
        listaSucursalesProveedor = (ArrayList<SucursalProveedor>) getIntent().getSerializableExtra("listaSucursalesProveedor"); //
        listaProveedores = (ArrayList<Proveedor>) getIntent().getSerializableExtra("listaProveedores"); //

        almacen =  getIntent().getStringExtra("Almacen");
        tipoformapago =  getIntent().getStringExtra("TipoPago");
        id_pedido = getIntent().getStringExtra("id_pedido");
        validador = getIntent().getStringExtra("validador");
        retorno = getIntent().getStringExtra("retorno");
        Index = getIntent().getStringExtra("Index");
        cantidad = getIntent().getStringExtra("Cantidad");
        precio = getIntent().getStringExtra("Precio");
        SucursalProveedor = getIntent().getStringExtra("SucursalProveedor");
        codProveedor =  getIntent().getStringExtra("codProveedor");
        NombreProveedor =  getIntent().getStringExtra("NombreProveedor");

        valida = getIntent().getStringExtra("valida");

        if (codProveedor == null){

        }else {
            proveedor = new Proveedor();
            proveedor.setCodProveedor(codProveedor);
        }
        direccionProveedor = getIntent().getStringExtra("tvdireccionproveedor");
        nombreProveedor = getIntent().getStringExtra("tvnombreproveedor");
        btnBuscarProveedor = findViewById(R.id.btnBuscarProveedor);
        ibretornoMenuProveedor = findViewById(R.id.ibRetornoMenuProveedor);
        rgBuscarProveedor = findViewById(R.id.rgBuscarProveedor);
        rbNombreProveedor = findViewById(R.id.rbNombreProveedor);
        rbCodigoProveedor = findViewById(R.id.rbCodigoProveedor);
        etProveedor = findViewById(R.id.etProveedor);
        lvProveedoresPrueba = findViewById(R.id.lvProveedores);
        LLMenuInferior = findViewById(R.id.LLMenuInferior);
        tvnombreproveedor = findViewById(R.id.tvNombreProveedor);
        tvdireccionproveedor = findViewById(R.id.tvDireccionProveedor);
        spsucursalproveedor = findViewById(R.id.spSucursalProveedor);
        LLlistadoproveedores = findViewById(R.id.LLlistadoProveedores);
        LLMenuInferior.setVisibility(View.GONE);
        lvProveedoresPrueba.setVisibility(View.GONE);
        btnAceptarBuscarProveedor = findViewById(R.id.btnAceptarBuscarProveedor);
        tvdireccionsucursalproveedor = findViewById(R.id.tvDireccionSucursal);
        if (validadorRetornoFechaPactadaProveedor == null){
            validadorRetornoFechaPactadaProveedor = "true";
        }else {
            validadorRetornoFechaPactadaProveedor = getIntent().getStringExtra("validadorRetornoFechaPactadaProveedor");
            LLMenuInferior.setVisibility(View.VISIBLE);
            tvdireccionproveedor.setText(direccionProveedor);
            tvnombreproveedor.setText(nombreProveedor);
            spsucursalproveedor.setAdapter(new SpinnerAdapter(getApplicationContext(),listaSucursalesProveedorStr));
            listaSucursalesProveedor.get(0).setCodigoSucursalProveedor(listaSucursalesProveedor.get(0).getCodigoSucursalProveedor());
            listaSucursalesProveedor.get(0).setNombreSucursalProveedor(listaSucursalesProveedor.get(0).getNombreSucursalProveedor());
            tvdireccionsucursalproveedor.setText(listaSucursalesProveedor.get(0).getDireccionSucursalProveedor());
            spsucursalproveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    quitarTeclado(view);
                    SucursalProveedor =listaSucursalesProveedor.get(position).getCodigoSucursalProveedor();
                    NombreProveedor = listaSucursalesProveedor.get(position).getNombreSucursalProveedor();
                    tvdireccionsucursalproveedor.setText(listaSucursalesProveedor.get(position).getDireccionSucursalProveedor());
                    btnAceptarBuscarProveedor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(ProveedorActivity.this,FechaPactadaActivity.class);
                            intent.putExtra("TipoPago", tipoformapago);
                            intent.putExtra("Index", Index);
                            intent.putExtra("Cantidad", cantidad);
                            intent.putExtra("Precio", "" + precio);
                            intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                            intent.putExtra("Almacen", almacen);
                            intent.putExtra("id_pedido", id_pedido);
                            intent.putExtra("validador", "false");
                            if(valida.equals("valida")){
                                intent.putExtra("codProveedor", codProveedor);
                            }else {
                                intent.putExtra("codProveedor", listaProveedores.get(0).getCodProveedor());
                            }
                            intent.putExtra("SucursalProveedor",SucursalProveedor);
                            intent.putExtra("NombreProveedor",NombreProveedor);
                            intent.putExtra("tvnombreproveedor", tvnombreproveedor.getText().toString());
                            intent.putExtra("tvdireccionproveedor", tvdireccionproveedor.getText().toString());

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
                            Bundle bundle5 = new Bundle();
                            bundle5.putSerializable("listaProveedores",listaProveedores);
                            intent.putExtras(bundle5);
                            Bundle bundle6 = new Bundle();
                            bundle6.putSerializable("listaSucursalesProveedor",listaSucursalesProveedor);
                            intent.putExtras(bundle6);
                            Bundle bundle7 = new Bundle();
                            bundle7.putSerializable("listaSucursalesProveedorStr",listaSucursalesProveedorStr);
                            intent.putExtras(bundle7);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        ibretornoMenuProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProveedorActivity.this, bandejaProductosActivity.class);
                intent.putExtra("TipoPago", tipoformapago);
                intent.putExtra("validador", "true");
                intent.putExtra("Index", Index);
                intent.putExtra("id_pedido", id_pedido);
                intent.putExtra("Almacen", almacen);
                intent.putExtra("validador","false");
                intent.putExtra("Precio",precio);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listaProductoselegidos", listaproductoselegidos);
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
                bundle4.putSerializable("listaClienteSucursal",listaClienteSucursal);
                intent.putExtras(bundle4);
                startActivity(intent);
                finish();
            }
        });

        etProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LLlistadoproveedores.setVisibility(View.GONE);
                LLMenuInferior.setVisibility(View.GONE);
                tvdireccionproveedor.setText("");
                tvnombreproveedor.setText("");
                etProveedor.setText("");
                if (rbNombreProveedor.isChecked()){

                    etProveedor.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if (rbCodigoProveedor.isChecked()){
                    etProveedor.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });

        rgBuscarProveedor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rgBuscarProveedor.getCheckedRadioButtonId()){
                    case R.id.rbNombreProveedor:
                        etProveedor.setFilters(new InputFilter[] {new InputFilter.LengthFilter(80)});
                        etProveedor.setInputType(1);
                        etProveedor.setText("");
                        TipodeConsulta = "NombreProveedor";

                        break;
                    case R.id.rbCodigoProveedor:
                        etProveedor.setText("");
                        etProveedor.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
                        etProveedor.setInputType(2);
                        etProveedor.requestFocus();
                        etProveedor.append("10027462");
                        TipodeConsulta = "CodigoProveedor";
                        break;
                }
            }
        });

        btnBuscarProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvProveedoresPrueba.setVisibility(View.VISIBLE);
                LLMenuInferior.setVisibility(View.GONE);
                etProveedor.setInputType(InputType.TYPE_NULL);
                if (etProveedor.getText().toString().trim().equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorActivity.this);
                    builder.setTitle("Atención !");
                    builder.setMessage("Por favor ingrese un valor valido");
                    builder.setCancelable(false);
                    builder.setNegativeButton("Aceptar",null);
                    builder.create()
                            .show();
                    etProveedor.requestFocus();

                }else{

                    if(Utilitario.isOnline(getApplicationContext())){

                        buscarproveedor(etProveedor.getText().toString().trim(),TipodeConsulta);

                    }else{

                        AlertDialog.Builder build = new AlertDialog.Builder(ProveedorActivity.this);
                        build.setTitle("Atención .. !");
                        build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
                        build.setCancelable(false);
                        build.setNegativeButton("ACEPTAR",null);
                        build.create().show();

                    }
                }
            }
        });
    }

    public void buscarproveedor(String numero, String tipoConsulta) {

        final ProgressDialog progressDialog = new ProgressDialog(ProveedorActivity.this);
        progressDialog.setMessage("... Buscando");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        numero = numero.replace("%","%25");
        numero = numero.toUpperCase(); // se convierten los caracteres a Mayusucla

        etProveedor.setInputType(InputType.TYPE_NULL);

        listaProveedores = new ArrayList<>();
        listaProveedorPrueba = new ArrayList<>();

        if (tipoConsulta.equals("NombreProveedor")) {

            url = ejecutaFuncionCursorTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PROVEEDOR&variables='|"+numero+"'";

        }else {

            url = ejecutaFuncionCursorTestMovil +
                    "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_PROVEEDOR&variables='"+numero+"|'";
        }

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String Mensaje = "";
                        progressDialog.dismiss();

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
                                            ProveedorActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        proveedor = new Proveedor();
                                        proveedor.setCodProveedor(jsonObject.getString("COD_PROVEEDOR"));
                                        proveedor.setNombreProveedor(jsonObject.getString("NOMBRE"));
                                        proveedor.setDireccionProveedor(jsonObject.getString("DIRECCION")); //
                                        proveedor.setCodDpto(jsonObject.getString("COD_DPTO"));
                                        proveedor.setDepartamento(jsonObject.getString("DEPARTAMENTO"));
                                        proveedor.setCodProv(jsonObject.getString("COD_PROV"));
                                        proveedor.setProvincia(jsonObject.getString("PROVINCIA"));
                                        proveedor.setCodDistrito(jsonObject.getString("COD_DIST"));
                                        proveedor.setDistrito(jsonObject.getString("DISTRITO"));
                                        listaProveedorPrueba.add(proveedor.getNombreProveedor());
                                        listaProveedores.add(proveedor);
                                    }
                                    // Se hace un llamado al adaptador personalizado asociado al SML custom_list

                                    lvProveedoresPrueba.setVisibility(View.VISIBLE);
                                    LLlistadoproveedores.setVisibility(View.VISIBLE);
                                    ListadoAlmacenActivity.CustomListAdapter listAdapter= new ListadoAlmacenActivity.
                                            CustomListAdapter(ProveedorActivity.this , R.layout.custom_list , listaProveedorPrueba);
                                    lvProveedoresPrueba.setAdapter(listAdapter);
                                    lvProveedoresPrueba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            LLMenuInferior.setVisibility(View.VISIBLE);
                                            LLlistadoproveedores.setVisibility(View.GONE);
                                            lvProveedoresPrueba.setVisibility(View.GONE);
                                            tvdireccionproveedor.setText(listaProveedores.get(position).getDireccionProveedor());
                                            tvnombreproveedor.setText(listaProveedores.get(position).getNombreProveedor());
                                            etProveedor.setInputType(InputType.TYPE_NULL);
                                            listarSucursalProveedores(listaProveedores.get(position).getCodProveedor());
                                            proveedor.setCodProveedor(listaProveedores.get(position).getCodProveedor());
                                        }
                                    });
                                }
                            }else {
                                listaProveedorPrueba.clear();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorActivity.this);
                                builder.setMessage("No se llego a encontrar el proveedor")
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
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorActivity.this);
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

    public void listarSucursalProveedores(String codProveedor) {

        final ProgressDialog progressDialog = new ProgressDialog(ProveedorActivity.this);
        progressDialog.setMessage("...Cargando");
        progressDialog.setCancelable(false);
        progressDialog.show();

        LLlistadoproveedores.setVisibility(View.GONE);

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        listaSucursalesProveedor = new ArrayList<>();
        listaSucursalesProveedorStr = new ArrayList<>();

        url = ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LISTAR_SUC_PROVEEDOR&variables=%27"+codProveedor+"%27";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String Mensaje = "";

                        progressDialog.dismiss();
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
                                            ProveedorActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar", null)
                                            .create()
                                            .show();
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        sucursalProveedor = new SucursalProveedor();
                                        sucursalProveedor.setCodigoSucursalProveedor(jsonObject.getString("COD_SUCPROV"));
                                        sucursalProveedor.setDireccionSucursalProveedor(jsonObject.getString("DIRECCION"));
                                        sucursalProveedor.setNombreSucursalProveedor(jsonObject.getString("NOMBRE"));
                                        listaSucursalesProveedorStr.add(sucursalProveedor.getNombreSucursalProveedor());
                                        listaSucursalesProveedor.add(sucursalProveedor);
                                    }

                                    // Se hace un llamado al adaptador personalizado asociado al SML custom_list

                                    spsucursalproveedor.setAdapter(new SpinnerAdapter(getApplicationContext(),listaSucursalesProveedorStr));

                                    SucursalProveedor = listaSucursalesProveedor.get(0).getCodigoSucursalProveedor();

                                    tvdireccionsucursalproveedor.setText(listaSucursalesProveedor.get(0).getDireccionSucursalProveedor());
                                    spsucursalproveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                     @Override
                                     public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                                         quitarTeclado(view);
                                         SucursalProveedor = listaSucursalesProveedor.get(position).getCodigoSucursalProveedor();
                                         NombreProveedor = listaSucursalesProveedor.get(position).getNombreSucursalProveedor();
                                         tvdireccionsucursalproveedor.setText(listaSucursalesProveedor.get(position).getDireccionSucursalProveedor());

                                         btnAceptarBuscarProveedor.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 Intent intent = new Intent(ProveedorActivity.this,FechaPactadaActivity.class);
                                                 intent.putExtra("TipoPago", tipoformapago);
                                                 intent.putExtra("Index", Index);
                                                 intent.putExtra("Cantidad", cantidad);
                                                 intent.putExtra("Precio", "" + precio);
                                                 intent.putExtra("cantidadlista", listaproductoselegidos.size() + "");
                                                 intent.putExtra("Almacen", almacen);
                                                 intent.putExtra("id_pedido", id_pedido);
                                                 intent.putExtra("validador", "false");
                                                 intent.putExtra("codProveedor", proveedor.getCodProveedor());
                                                 intent.putExtra("SucursalProveedor",SucursalProveedor);
                                                 intent.putExtra("NombreProveedor",NombreProveedor);
                                                 intent.putExtra("tvnombreproveedor", tvnombreproveedor.getText().toString());
                                                 intent.putExtra("tvdireccionproveedor", tvdireccionproveedor.getText().toString());
                                                 intent.putExtra("position",position);

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
                                                 Bundle bundle5 = new Bundle();
                                                 bundle5.putSerializable("listaProveedores",listaProveedores);
                                                 intent.putExtras(bundle5);
                                                 Bundle bundle6 = new Bundle();
                                                 bundle6.putSerializable("listaSucursalesProveedor",listaSucursalesProveedor);
                                                 intent.putExtras(bundle6);
                                                 Bundle bundle7 = new Bundle();
                                                 bundle7.putSerializable("listaSucursalesProveedorStr",listaSucursalesProveedorStr);
                                                 intent.putExtras(bundle7);
                                                 startActivity(intent);
                                                 finish();
                                             }
                                         });
                                     }

                                     @Override
                                     public void onNothingSelected(AdapterView<?> parent) {
                                     }
                                 });
                                }
                            }else {
                                listaProveedorPrueba.clear();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorActivity.this);
                                builder.setMessage("No se llego a encontrar el proveedor")
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
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorActivity.this);
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
    private void quitarTeclado(View v) {
        InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        teclado.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
