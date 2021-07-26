package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import POJOS.Usuario;


/**
 * Estructura del codigo:
 *  - 1 Creacion de variables
 *  - 2 onCreate
 *      2.1 Vincular variables con objetos del layout
 *      2.2 Funcionalidad de botones
 *      2.3 Llamadas a metodos
 *  - 3 Iniciar aplicacion
 *  - 4 Funcionalidad de botones
 *      4.1 Ir al registro
 *      4.2 Iniciar sesion
 */
public class ActivityLogin extends AppCompatActivity {
    /**
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    private FirebaseAuth mAuth;
    private EditText correo, pass;
    Usuario usuario = null;

    /**
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = new Usuario();
        correo = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mAuth = FirebaseAuth.getInstance();
        Button iniciarSesion = findViewById(R.id.IniciarSesionLogin);
        Button irRegistro = findViewById(R.id.irRegistroLogin);

        if(mAuth.getUid()!=null){
            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
            startActivity(i);
        }
        //Llamada a metodos
        iniciarSesion.setOnClickListener(this::iniciarSesion);
        irRegistro.setOnClickListener(this::toRegistro);

    }
    //Probar a eliminar este metodo
    /**
     * -----------------------------------------------------------
     *                          3 Inciar aplicacion
     * -----------------------------------------------------------
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * -----------------------------------------------------------
     *                          4 Funcionalidades de botones
     * -----------------------------------------------------------
     */
    //Ir al registro
    public void toRegistro (View view){
        Intent registro = new Intent(this, ActivityRegistro.class);
        startActivity(registro);
    }


    //Iniciar sesion
    public void iniciarSesion (View view){
        usuario.setEmail(correo.getText().toString());
        usuario.setPass(pass.getText().toString());
        if(usuario.getEmail() == null || usuario.getPass() == null || usuario.getEmail().equals("")  || usuario.getPass().equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else{
            mAuth.signInWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            MediaPlayer mp = MediaPlayer.create(this, R.raw.correcto);
                            mp.start();

                            Toast.makeText(getApplicationContext(), R.string.bienvenida,
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
                            startActivity(i);

                        } else {
                            MediaPlayer mp = MediaPlayer.create(this, R.raw.error);
                            mp.start();
                            Snackbar snackbar = Snackbar.make(view, R.string.errorLogin, Snackbar.LENGTH_LONG);
                            snackbar.setDuration(10000);
                            snackbar.setAction("Ok", v -> {
                            });
                            snackbar.show();
                        }
                    });
        }
    }

}


