package com.example.sistemas.tomapedidos.Utilitarios;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.sistemas.tomapedidos.BuildConfig;
import com.example.sistemas.tomapedidos.Entidades.DctoxVolumen;
import com.example.sistemas.tomapedidos.R;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class Utilitario {

    public static String Soles = "S/"; // Cambio de moneda en Soles
    public static String Dolares = "USD";  // Cambio de moneda en Dólares
    public static String Version = "Versión " + BuildConfig.VERSION_NAME; // Cambio de moneda en Dólares
    //public static String VersionCode = "Versión del codigo" + BuildConfig.VERSION_CODE;
    //public static String webServiceCursormovil =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=";
    //public static String webServicemovil =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=";
    public static final Integer PHONESTATS = 0x1;
    //public static final Integer puerto = 8494;

    public static String formatoFecha(Integer dateTime){

        String valor = "0";
        if (dateTime <=9){
            valor = valor + dateTime;
        }else {
            valor = dateTime + "";
        }
        return valor;
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

    }

    public static class CustomListAdapter1 extends ArrayAdapter<DctoxVolumen> {

        private Context mContext;
        private int id;
        private List<DctoxVolumen> items ;

        public CustomListAdapter1(Context context, int textViewResourceId , List<DctoxVolumen> list)
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

            DctoxVolumen dctoxVolumen = new DctoxVolumen();

            TextView tvDesdeHasta = mView.findViewById(R.id.tvDesdeHasta);
            TextView tvDscto = mView.findViewById(R.id.tvDscto);

            TextView tvPrecioDscto = mView.findViewById(R.id.tvPrecioDscto);

            if(items.get(position) != null )
            {
                tvDesdeHasta.setTextColor(Color.BLACK);
                tvDesdeHasta.setText(items.get(position).getDescuento());
                tvDesdeHasta.setTextSize(15);
                int color = Color.argb(10, 0, 20, 255);
                tvDesdeHasta.setBackgroundColor(color);

                tvDscto.setTextColor(Color.BLACK);
                tvDscto.setText(items.get(position).getDescuento());
                tvDscto.setTextSize(15);
                tvDscto.setBackgroundColor(color);

                tvPrecioDscto.setTextColor(Color.BLACK);
                tvPrecioDscto.setText(items.get(position).getDescuento());
                tvPrecioDscto.setTextSize(15);
                tvPrecioDscto.setBackgroundColor(color);

                tvDesdeHasta.setText("Desde : "+formatoDecimal(items.get(position).getDesde())+"\t\t\t\t\t\t\t\tHasta : "+formatoDecimal(items.get(position).getHasta()));
                tvPrecioDscto.setText("\t\t\t"+items.get(position).getMoneda()+formatoDecimal(items.get(position).getPrecio()));
                tvDscto.setText("Dscto : "+formatoDecimal(items.get(position).getDescuento())+"%\t\t\t\t\t - ");
            }
            return mView;
        }
    }
    public static  String formatoDecimal(String valor) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.'); // Se define el simbolo para el separador decimal
        simbolos.setGroupingSeparator(',');// Se define el simbolo para el separador de los miles
        final DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolos); // Se crea el formato del numero con los simbolo

        return formateador.format(Double.valueOf(valor.replace(",","")));

    }
}
