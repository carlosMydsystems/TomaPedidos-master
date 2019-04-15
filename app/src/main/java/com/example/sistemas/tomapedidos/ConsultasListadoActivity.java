package com.example.sistemas.tomapedidos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sistemas.tomapedidos.Entidades.Usuario;
import java.util.ArrayList;
import java.util.Calendar;

public class ConsultasListadoActivity extends AppCompatActivity {

    Calendar calendar;
    Integer year,month,dayOfMonth;
    DatePickerDialog datePickerDialog;
    Usuario usuario;
    ListView lvConsultaTipo;
    ArrayList<String> listaTipoConsulta;
    ImageButton ibRetornoMenu;
    String menuConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_listado);

        listaTipoConsulta = new ArrayList<>();
        usuario = (Usuario)getIntent().getSerializableExtra("Usuario");
        lvConsultaTipo = findViewById(R.id.lvConsultasTipo);
        listaTipoConsulta.add("Consulta Pedido");
        listaTipoConsulta.add("Consulta Promociones");
        listaTipoConsulta.add("Consulta Stock");
        listaTipoConsulta.add("Consulta Precios");
        ibRetornoMenu = findViewById(R.id.ibRetornoMenu);
        ibRetornoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultasListadoActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario",usuario);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        ListadoAlmacenActivity.CustomListAdapter listAdapter = new ListadoAlmacenActivity.
                                        CustomListAdapter(ConsultasListadoActivity.this, R.layout.custom_list, listaTipoConsulta);
            lvConsultaTipo.setAdapter(listAdapter);

            lvConsultaTipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: // Editar producto

                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(ConsultasListadoActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String fecha = FormatoDiaMes(day) + "/" + FormatoDiaMes(month + 1) + "/" + year;
                                Intent intent = new Intent(ConsultasListadoActivity.this,ConsutlasActivity.class);
                                intent.putExtra("fecha",fecha);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Usuario",usuario);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();

                                }
                            }, year, month, dayOfMonth);
                        datePickerDialog.show();
                        break;

                    case 1:

                        Intent intent1 = new Intent(ConsultasListadoActivity.this,BusquedaClienteActivity.class);
                        intent1.putExtra("consultaPromociones","true");
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("Usuario",usuario);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        finish();
                        break;

                    case 2:

                        Intent intent = new Intent(ConsultasListadoActivity.this,BuscarProductoStockActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Usuario",usuario);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;

                    case 3:

                        Intent intent2 = new Intent(ConsultasListadoActivity.this,ConsultaPrecioActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("Usuario",usuario);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(ConsultasListadoActivity.this, "ingreso", Toast.LENGTH_SHORT).show();
                        break;

                }
                }
            });
        }

    private String FormatoDiaMes(Integer valor) {

        String ValorString;

        if (valor<=9){
            ValorString = "0"+valor.toString();
        }else{
            ValorString = valor.toString();
        }
        return ValorString;
    }
}
