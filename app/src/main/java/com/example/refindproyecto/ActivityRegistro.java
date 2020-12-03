package com.example.refindproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityRegistro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText correo;
   // private EditText nombre;
    //private EditText apellido;
    private EditText pass;
    private EditText confirmacionpass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.emailRegistro);
        //nombre = findViewById(R.id.nombreRegistro);
        //apellido = findViewById(R.id.apellidoRegistro);
        pass = findViewById(R.id.passRegistro);
        confirmacionpass = findViewById(R.id.passComRegistro);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void registrarUsuario (View view){

        if(pass.getText().toString().trim().equals(confirmacionpass.getText().toString().trim())){
            mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent registro = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(registro);
                            } else {
                                //Si no pone como minimo 9 digitos en la contraseña falla
                                Toast.makeText(getApplicationContext(), "Fallo al registrarse", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this,"Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
        }


    }

}