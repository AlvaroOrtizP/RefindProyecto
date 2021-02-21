package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityPerfil extends AppCompatActivity {
    RequestQueue requestQueue;
    FirebaseAuth mAuth;
    CircleImageView imagenPerfil;
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
        mAuth = FirebaseAuth.getInstance();
        imagenPerfil= findViewById(R.id.fotoUsuario);
        nombrePerfil = findViewById(R.id.nombreUsuario);
        biografiaPerfil = findViewById(R.id.tvBibliografia);
        apellidoPerfil = findViewById(R.id.apellidoUsuario);
        seguidores = findViewById(R.id.tvNSeguidores);
        seguidos = findViewById(R.id.tvNSiguiendo);
        comentarios = findViewById(R.id.tvNComentario);
        fab = findViewById(R.id.logOut);
        Switch swSonido=findViewById(R.id.swSonido);
        requestQueue=Volley.newRequestQueue(getApplicationContext());

        if(cargarPreferencias()){
            swSonido.setChecked(true);
        }
        else{
            swSonido.setChecked(false);
        }

        swSonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    activarAudio();
                }else{
                    desactivarAudio();
                }
            }
        });

        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setImageResource(R.drawable.ic_perfilb);
        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaFav.class);
            startActivity(i);
        });

        String fireId = mAuth.getUid();
        obtenerUsuario(fireId);
        fab.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
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
    private void obtenerUsuario(String fireId){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://192.168.1.127:80/Android/buscar_usuario.php?usuario_firebase="+fireId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                Usuario usuario =null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        usuario = new Usuario(
                                jsonObject.getString("nombre"),
                                jsonObject.getString("apellido"),
                                jsonObject.getString("email"),
                                jsonObject.getString("bibliografia"),
                                0,
                                0,
                                0);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                nombrePerfil.setText(usuario.getNombre());
                biografiaPerfil.setText(usuario.getBiografia());
                apellidoPerfil.setText(usuario.getApellido());
                seguidores.setText(usuario.getSeguidores().toString());
                seguidos.setText(usuario.getSiguiendo().toString());
                comentarios.setText(usuario.getComentarios().toString());
                cargarImagen();
            }
        }, error -> Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);

    }

    private void cargarImagen(){
        String url="https://www.nintenderos.com/wp-content/uploads/2020/11/shiny-jigglypuff-pokemon.jpg";
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {//6.40
                imagenPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }

    private void desactivarAudio(){
        SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",false);
        editor.commit();
    }
    private void activarAudio(){
        SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",true);
        editor.commit();
    }
    private boolean cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("sonido", Context.MODE_PRIVATE);
        Boolean sonidoActivado = preferences.getBoolean("sonido",true);
        return sonidoActivado;
    }
}