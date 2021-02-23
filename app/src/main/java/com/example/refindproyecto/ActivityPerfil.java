package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityPerfil extends AppCompatActivity {
    Direccion direccion = new Direccion();
    RequestQueue requestQueue;
    FirebaseAuth mAuth;
    CircleImageView imagenPerfil;
    //Para el navbar
    ImageButton btnInicio, btnFavorito, btnPerfil;
    FloatingActionButton salir;
    //Para el formulario emergente
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText newNombre, newBiografia, newApellido;
    TextView nombrePerfil, biografiaPerfil, apellidoPerfil;
    ImageView btnNewfoto;
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
        Switch swSonido=findViewById(R.id.swSonido);
        salir = findViewById(R.id.logOut);
        requestQueue=Volley.newRequestQueue(getApplicationContext());

        swSonido.setChecked(cargarPreferencias());

        swSonido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                activarAudio();
                Toast.makeText(getApplicationContext(), R.string.sonidoActivado, Toast.LENGTH_SHORT).show();
            }else{
                desactivarAudio();
                Toast.makeText(getApplicationContext(), R.string.sonidoDesactivado, Toast.LENGTH_SHORT).show();
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
        salir.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            //editor.commit();
            editor.apply();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        });
    }

    /**
     * Metodod creado para usar con el boton editarPerfil
     * Este metodo usa el layout popup para crear un formulario emergente
     */
    public void createNewContactDialog(View view){
        Usuario usuario = new Usuario();
        usuario.setFoto(0);
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        newNombre = (EditText)contactPopupView.findViewById(R.id.popupNombre);
        newBiografia = (EditText)contactPopupView.findViewById(R.id.popupBiografia);
        newApellido = (EditText)contactPopupView.findViewById(R.id.popupApellido);
        btnGuardar = (Button)contactPopupView.findViewById(R.id.btnCGuardar);
        btnCancelar = (Button)contactPopupView.findViewById(R.id.btnCCancelar);
        btnNewfoto = (ImageView)contactPopupView.findViewById(R.id.imgPopup);
        //Visionado del popup
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        Spinner spinner = (Spinner)contactPopupView.findViewById(R.id.spinner);
        String [] opciones = {"Customizada 1", "Customizada 2", "Customizada 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageRequest imageRequest = new ImageRequest(direccion.getImagesUsuario()+position+".png", response -> {
                    btnNewfoto.setImageBitmap(response);
                    imagenPerfil.setImageBitmap(response);
                    usuario.setFoto(position);
                }, 0, 0, ImageView.ScaleType.CENTER, null,
                        error -> Toast.makeText(getApplication(),R.string.errorCargarImagen, Toast.LENGTH_SHORT).show());
                requestQueue.add(imageRequest);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), R.string.errorConexion, Toast.LENGTH_SHORT).show();
            }
        });
        btnGuardar.setOnClickListener(v -> {
            usuario.setNombre(newNombre.getText().toString());
            usuario.setApellido(newApellido.getText().toString());
            usuario.setBiografia(newBiografia.getText().toString());
            usuario.setUsuFire(mAuth.getUid());
            actualizarUsuario(usuario);
        });
        btnCancelar.setOnClickListener(v -> dialog.cancel());
    }
    private void obtenerUsuario(String fireId){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(direccion.getUsuario()+fireId, response -> {
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
                    usuario.setFoto(jsonObject.getInt("foto"));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            nombrePerfil.setText(usuario.getNombre());
            if(usuario.getBiografia().equals("")){
                biografiaPerfil.setText(R.string.biografiaEjemplo);
            }
            else{
                biografiaPerfil.setText(usuario.getBiografia());
            }
            apellidoPerfil.setText(usuario.getApellido());
            cargarImagen(usuario.getFoto());
        }, error ->{
            Toast.makeText(getApplicationContext(), R.string.errorObtenerDatosUsuario, Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }
        );
        requestQueue.add(jsonArrayRequest);

    }
    private void cargarImagen(int fotoUsuario){
        String url=direccion.getImagesUsuario()+fotoUsuario+".png";
        ImageRequest imageRequest = new ImageRequest(url, response -> {//6.40
            imagenPerfil.setImageBitmap(response);
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),R.string.errorConexion, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }
    private void desactivarAudio(){
        SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",false);
        //editor.commit();
        editor.apply();
    }
    private void activarAudio(){
        SharedPreferences preferences=getSharedPreferences("sonido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("sonido",true);
        //editor.commit();
        editor.apply();
    }
    private boolean cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("sonido", Context.MODE_PRIVATE);
        Boolean sonidoActivado = preferences.getBoolean("sonido",true);
        return sonidoActivado;
    }
    private void actualizarUsuario(Usuario usuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direccion.updateUsuario(), response ->{
            Toast.makeText(getApplicationContext(), R.string.actualizacionCorrecta, Toast.LENGTH_SHORT).show();
            nombrePerfil.setText(usuario.getNombre());
            apellidoPerfil.setText(usuario.getApellido());
            biografiaPerfil.setText(usuario.getBiografia());
        }
                , error -> Toast.makeText(getApplicationContext(), R.string.errorConexion, Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario_firebase",usuario.getUsuFire());
                parametros.put("nombre",usuario.getNombre());
                parametros.put("apellido",usuario.getApellido());
                parametros.put("biografia",usuario.getBiografia());
                parametros.put("foto",""+usuario.getFoto());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}