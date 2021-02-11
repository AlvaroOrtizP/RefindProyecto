package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityPerfil extends AppCompatActivity {
    //Para el navbar
    ImageButton btnInicio, btnFavorito, btnPerfil;
    RequestQueue requestQueue;
    //Para el formulario emergente
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newNombre, newBiografia;
    private TextView nombrePerfil, descripcionPerfil, apellidoPerfil;
    private ImageButton btnNewfoto;
    private Button btnGuardar, btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        nombrePerfil = findViewById(R.id.nombreUsuario);
        //descripcionPerfil = findViewById(R.id.tvBibliografia);
        apellidoPerfil = findViewById(R.id.apellidoUsuario);

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
        String URL = "http://192.168.1.127:80/Android/buscar_usuario.php?usuario_id="+2;
        obtenerDatosPerfil(URL);
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
    private void obtenerDatosPerfil(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Toast.makeText(getApplicationContext(), "Entra"+i, Toast.LENGTH_SHORT).show();
                        jsonObject = response.getJSONObject(i);
                        nombrePerfil.setText(jsonObject.getString("nombre"));
                        apellidoPerfil.setText(jsonObject.getString("apellido"));
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
}