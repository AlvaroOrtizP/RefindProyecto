package com.example.refindproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegistro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText correo;
    private EditText nombre;
    private EditText apellido;
    private EditText pass;
    private EditText confirmacionPass;
    CheckBox checkBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.emailRegistro);
        nombre = findViewById(R.id.nombreRegistro);
        apellido = findViewById(R.id.apellidoRegistro);
        pass = findViewById(R.id.passRegistro);
        confirmacionPass = findViewById(R.id.passComRegistro);
        checkBox = findViewById(R.id.checkBox);
    }

    /**@Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }*/
    public void registrarUsuario (View view){
        if(correo.getText().toString().equals("") || nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || pass.getText().toString().equals("") || confirmacionPass.getText().toString().equals("")){
            Snackbar snackbar = Snackbar.make(view, "Debes rellenar todos los campos", Snackbar.LENGTH_LONG);
            //snackbar.setTextColor(Color.parseColor("#FF0000"));
            //snackbar.setBackgroundTint(Color.parseColor("#FFB0D9B9"));
            snackbar.show();
        }
        else if(!checkBox.isChecked()){
            Snackbar snackbar = Snackbar.make(view, "Debes tener mas de 18 para usar esta aplicación", Snackbar.LENGTH_LONG);
            snackbar.show();//TODO LOS mesnajes tienen que ir traducidos
        }
        else if(!pass.getText().toString().equals(confirmacionPass.getText().toString())){
            Snackbar snackbar = Snackbar.make(view, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG);
            snackbar.show();//TODO LOS mesnajes tienen que ir traducidos
        }
        else if(pass.length()<9){
            Snackbar snackbar = Snackbar.make(view, "La contraseña tiene que tener como minimo 9 caracteres", Snackbar.LENGTH_LONG);
            snackbar.show();//TODO LOS mesnajes tienen que ir traducidos
        }
        else{
            if(pass.getText().toString().trim().equals(confirmacionPass.getText().toString().trim())){
                mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String fireId = mAuth.getUid();
                                    crearUsuario("http://192.168.1.127:80/Android/insertar_usuario.php",nombre.getText().toString(), apellido.getText().toString(), correo.getText().toString(),fireId);
                                    Intent registro = new Intent(getApplicationContext(), ActivityListaCat.class);
                                    startActivity(registro);
                                } else {
                                    //Si no pone como minimo 9 digitos en la contraseña falla
                                    Snackbar snackbar = Snackbar.make(view, "Fallo al registrarse", Snackbar.LENGTH_LONG);
                                    snackbar.show();//TODO LOS mesnajes tienen que ir traducidos
                                }
                            }
                        });
            }
            else{
                Snackbar snackbar = Snackbar.make(view, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG);
                snackbar.show();//TODO LOS mesnajes tienen que ir traducidos
            }
        }

    }
    public void irLogin(View view){
        Intent registro_login = new Intent(this, ActivityLogin.class);
        startActivity(registro_login);
    }
    private void crearUsuario(String URL, String nombre, String apellido, String email, String fire){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Registro correcto", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombre",nombre);
                parametros.put("email",email);
                parametros.put("apellido",apellido);
                parametros.put("usuario_firebase",fire);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}