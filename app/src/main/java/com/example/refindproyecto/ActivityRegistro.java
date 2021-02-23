package com.example.refindproyecto;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.Direccion;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegistro extends AppCompatActivity {
    Direccion direccion = new Direccion();
    private FirebaseAuth mAuth;
    private EditText correo;
    private EditText nombre;
    private EditText apellido;
    private EditText pass;
    private EditText confirmacionPass;
    //CheckBox checkBox;
    Boolean isChecked=false;
    MaterialCheckBox checkBox;



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
        checkBox.setOnClickListener(v -> isChecked= !isChecked);

    }

    public void registrarUsuario (View view){
        if(correo.getText().toString().equals("") || nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || pass.getText().toString().equals("") || confirmacionPass.getText().toString().equals("")){
            Snackbar snackbar = Snackbar.make(view, R.string.todosCamposOk, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else if(!isChecked){
            Snackbar snackbar = Snackbar.make(view, R.string.error18, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else if(!pass.getText().toString().equals(confirmacionPass.getText().toString())){
            Snackbar snackbar = Snackbar.make(view, R.string.errorPassNoIguales, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else if(pass.length()<9){
            Snackbar snackbar = Snackbar.make(view, R.string.pass9, Snackbar.LENGTH_LONG);
            snackbar.setDuration(10000);
            snackbar.setAction("Ok", v -> {
            });
            snackbar.show();
        }
        else{
            if(pass.getText().toString().trim().equals(confirmacionPass.getText().toString().trim())){
                mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(), pass.getText().toString().trim())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                //FirebaseUser user = mAuth.getCurrentUser();
                                String fireId = mAuth.getUid();
                                crearUsuario(nombre.getText().toString(), apellido.getText().toString(), correo.getText().toString(),fireId);
                                Intent registro = new Intent(getApplicationContext(), ActivityListaCat.class);
                                startActivity(registro);
                            } else {
                                Snackbar snackbar = Snackbar.make(view, R.string.errorRegistro, Snackbar.LENGTH_LONG);
                                snackbar.setDuration(10000);
                                snackbar.show();
                            }
                        });
            }
        }

    }
    public void irLogin(View view){
        Intent registro_login = new Intent(this, ActivityLogin.class);
        startActivity(registro_login);
    }
    private void crearUsuario(String nombre, String apellido, String email, String fire){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direccion.addUsuario(), response ->
                Toast.makeText(getApplicationContext(), R.string.bienvenida+" "+nombre, Toast.LENGTH_SHORT).show(), error -> Toast.makeText(getApplicationContext(), R.string.errorRegistro, Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
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