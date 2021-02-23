package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.refindproyecto.POJOS.Direccion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
public class ActivityLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText correo, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mAuth = FirebaseAuth.getInstance();
        Button iniciarSesion = findViewById(R.id.IniciarSesionLogin);
        Button irRegistro = findViewById(R.id.irRegistroLogin);


        String fireId = mAuth.getUid();
        if(fireId!=null){
            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
            startActivity(i);
        }
        iniciarSesion.setOnClickListener(this::iniciarSesion);
        irRegistro.setOnClickListener(this::toRegistro);

    }
    @Override
    public void onStart() {
        super.onStart();
    }
    public void toRegistro (View view){
        Intent registro = new Intent(this, ActivityRegistro.class);
        startActivity(registro);
    }
    public void iniciarSesion (View view){
        if(correo.getText().toString().equals("")|| pass.getText().toString().equals("")){
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
                            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), R.string.bienvenida,
                                    Toast.LENGTH_SHORT).show();
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


