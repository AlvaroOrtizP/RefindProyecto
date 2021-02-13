package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class ActivityPerfil extends AppCompatActivity {
   // private FirebaseAuth mAuth;
    //Para el navbar
    ImageButton btnInicio, btnFavorito, btnPerfil;
    FloatingActionButton fab;
    //Para el formulario emergente
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    //
    EditText newNombre, newBiografia;
    TextView nombrePerfil, biografiaPerfil, apellidoPerfil, seguidores, seguidos, comentarios;
    ImageButton btnNewfoto;
    Button btnGuardar, btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        nombrePerfil = findViewById(R.id.nombreUsuario);
        biografiaPerfil = findViewById(R.id.tvBibliografia);
        apellidoPerfil = findViewById(R.id.apellidoUsuario);
        seguidores = findViewById(R.id.tvNSeguidores);
        seguidos = findViewById(R.id.tvNSiguiendo);
        comentarios = findViewById(R.id.tvNComentario);
        fab = findViewById(R.id.logOut);

        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        //btnPerfil =findViewById(R.id.btnPerfil);//Esta desactivado ya que logicamente no te interesa ir del perfil al perfil
        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaFav.class);
            startActivity(i);
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(i);
            }
        });

        getPreferences();
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

    /**
     * Metodo para cargar el archivo de preferencias en los datos del perfil
     * El archivo usado es "datosUsuario" y se crea en el Login
     */
    private void getPreferences(){
        SharedPreferences preferences =getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        nombrePerfil.setText(preferences.getString("nombre",""));
        apellidoPerfil.setText(preferences.getString("apellido",""));
        biografiaPerfil.setText(preferences.getString("biografia",""));
        seguidores.setText(""+preferences.getInt("seguidores",0));
        seguidos.setText(""+preferences.getInt("siguiendo",0));
        comentarios.setText(""+preferences.getInt("comentarios",0));
    }

}