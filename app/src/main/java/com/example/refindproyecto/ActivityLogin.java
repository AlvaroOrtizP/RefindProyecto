package com.example.refindproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correo, pass;
    private Button iniciarSesion, irRegistro;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mAuth = FirebaseAuth.getInstance();
        iniciarSesion= findViewById(R.id.IniciarSesionLogin);
        irRegistro= findViewById(R.id.irRegistroLogin);
        String fireId = mAuth.getUid();
        if(fireId!=null){
            obtenerUsuario(fireId);
            Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
            startActivity(i);
        }
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion(v);
            }
        });
        irRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toRegistro(v);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void toRegistro (View view){
        Intent registro = new Intent(this, ActivityRegistro.class);
        startActivity(registro);
    }
    public void iniciarSesion (View view){
        if(correo.getText().toString().equals("")|| pass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe rellenar los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String fireId = mAuth.getUid();
                                obtenerUsuario(fireId);

                                Intent i = new Intent(getApplicationContext(), ActivityListaCat.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), "Inicio de sesion correcto.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Fallo al iniciar sesion.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void obtenerUsuario(String fireId){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://192.168.1.127:80/Android/buscar_usuario.php?usuario_firebase="+fireId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Usuario usuario = new Usuario(
                                jsonObject.getInt("usuario_id"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("apellido"),
                                jsonObject.getString("email"),
                                jsonObject.getString("bibliografia"),
                                jsonObject.getInt("seguidor"),
                                jsonObject.getInt("seguidos"),
                                jsonObject.getInt("comentario"));
                        guardarPreferencias(usuario);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void guardarPreferencias(Usuario usuario){
        //Creamos el repositor donde guardaremos las preferencias
        SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        //Creamos el editor de preferencias
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nombre",usuario.getNombre());
        editor.putString("apellido",usuario.getApellido());
        editor.putString("biografia",usuario.getBiografia());
        editor.putInt("comentarios",usuario.getComentarios());
        editor.putInt("seguidores",usuario.getSeguidores());
        editor.putInt("siguiendo",usuario.getSiguiendo());

        editor.commit();
    }
}


