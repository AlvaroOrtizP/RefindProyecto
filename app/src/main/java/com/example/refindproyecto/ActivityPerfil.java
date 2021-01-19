package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ActivityPerfil extends AppCompatActivity {
    //Para el navbar
    ImageButton btnInicio, btnFavorito, btnPerfil;

    //Para el formulario emergente
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText newNombre, newBiografia;
    private ImageButton btnNewfoto;
    private Button btnGuardar, btnCancelar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //Botones del bottonNav
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
        btnInicio.setOnClickListener(v -> {

            Intent i = new Intent(ActivityPerfil.this, MainActivity.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {

            Intent i = new Intent(ActivityPerfil.this, ActivityFavoritos.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {

            Intent i = new Intent(ActivityPerfil.this, ActivityPerfil.class);
            startActivity(i);
        });


    }

    /**
     * Metodod creado para usar con el boton editarPerfil
     * Este metodo usa el layout popup para crear un formulario emergente
     * @param view
     */
    public void createNewContactDialog(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        //Relacionar con los ID
        newNombre = (EditText)contactPopupView.findViewById(R.id.popupNombre);
        newBiografia = (EditText)contactPopupView.findViewById(R.id.popupBiografia);
        btnGuardar = (Button)contactPopupView.findViewById(R.id.btnGuardar);
        btnCancelar = (Button)contactPopupView.findViewById(R.id.btnCancelar);
        btnNewfoto = (ImageButton)contactPopupView.findViewById(R.id.imgPopup);
        //Visionado del popup
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //Funciones de los botonoes
        btnNewfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fallo al iniciar sesion.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fallo al iniciar sesion.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fallo al iniciar sesion.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}