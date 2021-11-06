package com.example.refindproyecto.Procedimientos;

import android.content.Context;
import android.content.SharedPreferences;

public class ProcedimientoPreferencias {

    private Context context;

    public ProcedimientoPreferencias(Context context) {
        this.context = context;
    }

    public static boolean cargarPreferenciasSonido(Context context){
        SharedPreferences preferences=context.getSharedPreferences("sonido", Context.MODE_PRIVATE);
        Boolean sonidoActivado = preferences.getBoolean("sonido",true);
        return sonidoActivado;
    }
    public void activarAudio(){
        SharedPreferences preferences=context.getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",true);
        //editor.commit();
        editor.apply();
    }
    public void desactivarAudio(){
        SharedPreferences preferences=context.getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",false);
        //editor.commit();
        editor.apply();
    }
}
