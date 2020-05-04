package com.belval.alcoolougasolina;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PrefsUtil {

    private SharedPreferences prefs;

    public PrefsUtil(Context context) {
        //Obtem uma instancia de SharedPreferences que dá
        //acesso ao aquivo de preferencias
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getStr(String chave, String valorPadrao) {
        //Recupera uma String para uma propriedade defina por "chave"(key)
        return prefs.getString(chave, valorPadrao);
    }
    public String getStr(String chave) {
        return getStr(chave, "");
    }

    public int getInt(String chave) {
        return getInt(chave, 0);
    }

    public int getInt(String chave, int valorPadrao) {
        return prefs.getInt(chave, valorPadrao);
    }

    public double getDouble(String chave, double valorPadrao) {
        return Double.parseDouble(
                prefs.getString(chave, String.format("%f", valorPadrao)));
    }

    public double getDouble(String chave) {
        return getDouble(chave, 0.0000001);
    }
}
