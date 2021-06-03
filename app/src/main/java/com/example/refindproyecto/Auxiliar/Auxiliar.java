package com.example.refindproyecto.Auxiliar;

import android.view.View;
import com.example.refindproyecto.R;
import com.google.android.material.snackbar.Snackbar;

import POJOS.Usuario;

public class Auxiliar {

    public boolean comprobarDatosLogin(Usuario usuario, View view, String pass, String confirmarPass, Boolean isChecked){
        boolean comprobar = true;
        if(usuario != null || usuario.getEmail().equals("") || usuario.getNombre().equals("") || usuario.getApellido().equals("") || pass.equals("") || confirmarPass.equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(!isChecked){
            Snackbar snackbar = Snackbar.make(view, R.string.error18, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(!pass.equals(confirmarPass)){
            Snackbar snackbar = Snackbar.make(view, R.string.errorPassNoIguales, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        else if(pass.length()<9){
            Snackbar snackbar = Snackbar.make(view, R.string.pass9, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
            comprobar = false;
        }
        return comprobar;
    }
}
