package com.example.sistemas.tomapedidos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
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
import com.example.sistemas.tomapedidos.Entidades.Usuario;
import com.example.sistemas.tomapedidos.Utilitarios.Utilitario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.example.sistemas.tomapedidos.Utilitarios.Utilitario.PHONESTATS;

public class LoginActivity extends AppCompatActivity {

    EditText etusuario, etclave;
    Button btnlogeo;
    Usuario usuario;
    String url, Mensaje = "",imei = "",puerto = "8494", versionName = "1.0.12";
    boolean validador = true;
    TextView tvVersion;
    public static String ejecutaFuncionCursorTestMovil;
    public static String ejecutaFuncionTestMovil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (validador) {

            ejecutaFuncionCursorTestMovil = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorDesaMovil.php?funcion=";
            ejecutaFuncionTestMovil = "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionDesaMovil.php?funcion=";

        }else{

            ejecutaFuncionCursorTestMovil = "http://www.taiheng.com.pe:"+puerto+"/oracle/ejecutaFuncionCursorTestMovil.php?funcion=";
            ejecutaFuncionTestMovil = "http://www.taiheng.com.pe:"+puerto+"/oracle/ejecutaFuncionTestMovil.php?funcion=";
        }

        usuario = new Usuario();
        etusuario = findViewById(R.id.etUsuario);
        etclave = findViewById(R.id.etClave);
        btnlogeo = findViewById(R.id.btnLogin);
        tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(Utilitario.Version);
        consultarPermiso(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        btnlogeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utilitario.isOnline(getApplicationContext())){

                    if (etusuario.getText().equals("") || etclave.getText().equals("")) {
                    } else {

                        verificarUsuario(etusuario.getText().toString().replace(" ", "").toUpperCase()
                            , etclave.getText().toString().replace(" ", "").toUpperCase(),imei);
                    }

                }else{

                    AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                    build.setTitle("Atención .. !");
                    build.setMessage("El Servicio de Internet no esta Activo, por favor revisar");
                    build.setCancelable(false);
                    build.setNegativeButton("ACEPTAR",null);
                    build.create().show();

                }
            }
        });
    }

    public void verificarUsuario(String Codigo_usuario,String Contraseña_usuario,String Imei){

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("... Validando");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Mensaje = "";
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        url =  ejecutaFuncionCursorTestMovil +
                "PKG_WEB_HERRAMIENTAS.FN_WS_LOGIN&variables='7|"+Codigo_usuario.toUpperCase()+"|"
                +Contraseña_usuario.toUpperCase()+"|"+Imei+"'"; // se debe actalizar la URL

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response1);
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("hojaruta");
                            Boolean condicion = false,error = false;
                            if (success) {
                                String Aux = response1.replace("{","|");
                                Aux = Aux.replace("}","|");
                                Aux = Aux.replace("[","|");
                                Aux = Aux.replace("]","|");
                                Aux = Aux.replace("\"","|");
                                Aux = Aux.replace(","," ");
                                Aux = Aux.replace("|","");
                                Aux = Aux.replace(":"," ");
                                String partes[] = Aux.split(" ");

                                for (String palabras : partes){
                                    if (condicion){ Mensaje += palabras+" "; }
                                    if (palabras.equals("ERROR")||(palabras.equals("ERROR "))){
                                        condicion = true;
                                        error = true;
                                    }
                                }

                                if (error) {
                                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                                            LoginActivity.this);
                                    dialog.setMessage(Mensaje)
                                            .setNegativeButton("Regresar",null)
                                            .create()
                                            .show();
                                }else {

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        usuario = new Usuario();
                                        jsonObject = jsonArray.getJSONObject(i);
                                        usuario.setCodAlmacen(jsonObject.getString("COD_ALMACEN"));
                                        usuario.setNombre(jsonObject.getString("NOMBRE"));
                                        usuario.setMoneda(jsonObject.getString("MONEDA"));
                                        usuario.setCodTienda(jsonObject.getString("COD_TIENDA"));
                                        usuario.setCodVendedor(jsonObject.getString("COD_VENDEDOR"));
                                        usuario.setFechaActual(jsonObject.getString("FECHA_ACTUAL"));
                                        usuario.setTipoCambio(jsonObject.getString("TIPO_CAMBIO"));
                                        usuario.setLugar(jsonObject.getString("COD_TIPO_LISTAPRE")); // Se usa en la busqueda de producto
                                        usuario.setUser(etusuario.getText().toString().toUpperCase().trim());

                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userId", etusuario.getText().toString());
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Usuario", usuario);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }

                            }else{

                                AlertDialog.Builder build1 = new AlertDialog.Builder(LoginActivity.this);
                                build1.setTitle("Usuario  o Clave incorrecta")
                                        .setNegativeButton("Regresar",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "El error es : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void consultarPermiso(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permission)) {

                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{permission}, requestCode);

            } else { ActivityCompat.requestPermissions(LoginActivity.this, new String[]{permission}, requestCode); }

        } else { imei = obtenerIMEI(); }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1: {

                // Validamos si el usuario acepta el permiso para que la aplicación acceda a los datos internos del equipo, si no denegamos el acceso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imei = obtenerIMEI();
                } else {
                    Toast.makeText(LoginActivity.this, "Has negado el permiso a la aplicación", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private String obtenerIMEI() {
        final TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
            return telephonyManager.getImei();

        } else {

            return telephonyManager.getDeviceId();

        }
    }
}
