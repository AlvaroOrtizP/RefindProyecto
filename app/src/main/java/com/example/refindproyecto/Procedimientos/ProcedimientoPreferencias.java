package com.example.refindproyecto.Procedimientos;

import android.content.Context;
import android.content.SharedPreferences;
import Modelo.Usuario;


public class ProcedimientoPreferencias {

    private Context context;

    public ProcedimientoPreferencias(Context context) {
        this.context = context;
    }


    public static boolean cargarPreferenciasSonid(Context context){
        SharedPreferences preferences=context.getSharedPreferences("sonido", Context.MODE_PRIVATE);
        Boolean sonidoActivado = preferences.getBoolean("sonido",true);
        return sonidoActivado;
    }
    public static int cargarPreferenciasIdentificacion(Context context){
        SharedPreferences preferences=context.getSharedPreferences("identificacion", Context.MODE_PRIVATE);
        int identificacion = preferences.getInt("identificacion",0);
        return identificacion;
    }



    public void guardarIdentificador(Usuario usuario){
        SharedPreferences preferences=context.getSharedPreferences("identificacion",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("identificacion",usuario.getUsuarioId());
        //editor.commit();
        editor.apply();
    }
    public Integer obtenerIdentificador(){
        SharedPreferences preferences=context.getSharedPreferences("identificacion",Context.MODE_PRIVATE);
        return preferences.getInt("identificacion", 0);
    }
    public void activarAudio(){
        SharedPreferences preferences=context.getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",true);
        //editor.commit();
        editor.apply();
    }
    public void desactivarUsuario(){
        SharedPreferences preferences=context.getSharedPreferences("identificacion",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.remove("identificacion");

        editor.apply();

    }
    public void desactivarAudio(){
        SharedPreferences preferences=context.getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",false);
        editor.clear();
        //editor.commit();
        editor.apply();
    }
}
