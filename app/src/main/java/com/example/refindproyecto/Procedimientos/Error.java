package com.example.refindproyecto.Procedimientos;

import android.view.View;

import com.example.refindproyecto.R;
import com.google.android.material.snackbar.Snackbar;

import POJOS.Usuario;

public class Error {
    public void prueba(View view, Usuario usuario){
        Snackbar snackbar = null;
        switch (usuario.getError()){
            case "sad":
                snackbar= Snackbar.make(view, R.string.errorRegistroCorreoDupli, Snackbar.LENGTH_LONG);
                break;
            case "asdad":
                snackbar = Snackbar.make(view, R.string.errorRegistroCorreoDupli, Snackbar.LENGTH_LONG);
                break;
        }

        //Snackbar snackbar = Snackbar.make(view, R.string.errorRegistroCorreoDupli, Snackbar.LENGTH_LONG);
        snackbar.setDuration(10000);
        snackbar.show();
    }
}
