package com.example.sistemas.tomapedidos.Utilitarios;

import android.app.AlertDialog;
import android.widget.Toast;

import com.example.sistemas.tomapedidos.BuildConfig;

public class Utilitario {

    public static String Soles = "S/"; // Cambio de moneda en Soles
    public static String Dolares = "USD";  // Cambio de moneda en Dólares
    public static String Version = "Versión " + BuildConfig.VERSION_NAME; // Cambio de moneda en Dólares
    public static String VersionCode = "Versión del codigo" + BuildConfig.VERSION_CODE;

    public static String webServiceCursormovil =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionCursorTestMovil.php?funcion=";
    public static String webServicemovil =  "http://www.taiheng.com.pe:8494/oracle/ejecutaFuncionTestMovil.php?funcion=";
    public static final Integer PHONESTATS = 0x1;

    public static String formatoFecha(Integer dateTime){

        String valor = "0";
        if (dateTime <=9){
            valor = valor + dateTime;
        }else {
            valor = dateTime + "";
        }
        return valor;
    }

}
