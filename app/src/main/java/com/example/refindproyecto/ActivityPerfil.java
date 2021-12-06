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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import Cliente.RefindCliente;
import POJOS.Indicador;
import POJOS.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;


/*
 * Estructura del codigo:
 *  - 1 Creacion de variables
 *      1.1 Para el navbar
 *      1.2 Para el formulario emergente
 *      1.3 Para subir las imagenes
 *  - 2 onCreate
 *      2.1 Enlazar variables
 *      2.2 Funciones check
 *      2.3 Funciones botones
 *      2.4 Carga de datos del usuario
 *  - 3 OBTENER DATOS USUARIO
 *  - 4 CARGAR IMAGEN
 *  - 5 AUDIO
 *  - 6 PREFERENCIAS
 */


public class ActivityPerfil extends AppCompatActivity {
    /*
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    Usuario usuario = new Usuario();
    RequestQueue requestQueue;
    CircleImageView imagenPerfil;
    FloatingActionButton salir;
    TextView nombrePerfil, biografiaPerfil, apellidoPerfil;
    /*
     * -----------------------------------------------------------
     *                          1.1 Para el navbar
     * -----------------------------------------------------------
     */
    ImageButton btnInicio, btnFavorito, btnPerfil;
    /*
     * -----------------------------------------------------------
     *                          1.2 Para el formulario emergente
     * -----------------------------------------------------------
     */
    Button btnEditarPerfil;
    ProcedimientoPreferencias pF = null;


    /*
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        /*
         * -----------------------------------------------------------
         *                          2.1 Enlazar variables
         * -----------------------------------------------------------
         */
        imagenPerfil= findViewById(R.id.fotoUsuario);
        nombrePerfil = findViewById(R.id.nombreUsuario);
        biografiaPerfil = findViewById(R.id.tvBibliografia);
        apellidoPerfil = findViewById(R.id.apellidoUsuario);
        Switch swSonido=findViewById(R.id.swSonido);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        salir = findViewById(R.id.logOut);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnEditarPerfil = findViewById(R.id.editarPerfil);
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            usuario.setUsuarioId(pF.obtenerIdentificador());
        }
        /*
         * -----------------------------------------------------------
         *                          2.2 Funciones check
         * -----------------------------------------------------------
         */
        swSonido.setChecked(cargarPreferencias());//7 PREFERENCIAS
        swSonido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //6 AUDIO
            if(isChecked){
                pF.activarAudio();
                //procedimientoPreferencias.activarAudio();
                //activarAudio();
                Toast.makeText(getApplicationContext(), R.string.sonidoActivado, Toast.LENGTH_SHORT).show();
            }else{
                pF.desactivarAudio();
                //procedimientoPreferencias.desactivarAudio();
                //desactivarAudio();
                Toast.makeText(getApplicationContext(), R.string.sonidoDesactivado, Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * -----------------------------------------------------------
         *                          2.3 Funciones botones
         * -----------------------------------------------------------
         */
        btnPerfil.setImageResource(R.drawable.ic_perfilb);
        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityListaFav.class);
            startActivity(i);
        });
        btnEditarPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPerfil.this, ActivityAjustesUsuario.class);
            startActivity(i);
        });
        /*
         * -----------------------------------------------------------
         *                          2.4 Carga de datos del usuario
         * -----------------------------------------------------------
         */

        usuario = obtenerDatosUsuario();//4 OBTENER DATOS USUARIO

        if(!usuario.getError().equals("OK")){//TODO: probar esto
            pF.desactivarUsuario();
            pF.activarAudio();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }

        cargarImagen(imagenPerfil, usuario);//5 CARGAR IMAGEN
        nombrePerfil.setText(usuario.getNombre());
        biografiaPerfil.setText(usuario.getBiografia());
        apellidoPerfil.setText(usuario.getApellido());
        salir.setOnClickListener(view -> {

            //TODO comprobar que se eliminan preferencias
            pF.desactivarAudio();
            pF.desactivarUsuario();
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        });
        // Quitar la llamada desde la vista al popup
    }


    /*
     * -----------------------------------------------------------
     *                          4 OBTENER DATOS USUARIO
     * -----------------------------------------------------------
     */
    private Usuario obtenerDatosUsuario(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                usuario = refindCliente.obtenerUsuario(usuario);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        return usuario;
    }

    /*
     * -----------------------------------------------------------
     *                          5 CARGAR IMAGEN
     * -----------------------------------------------------------
     */
    private void cargarImagen(ImageView imagenPerfil, Usuario usuario){
        ImageRequest imageRequest = new ImageRequest(Indicador.IMAGEN_USUARIO+usuario.getUsuarioId()+"."+usuario.getFoto(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagenPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO excepcion
            }
        });
        requestQueue.add(imageRequest);
    }


    /*
     * -----------------------------------------------------------
     *                          6 AUDIO
     * -----------------------------------------------------------
     */
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

    /*
     * -----------------------------------------------------------
     *                          7 PREFERENCIAS
     * -----------------------------------------------------------
     */
    private boolean cargarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("sonido", Context.MODE_PRIVATE);
        Boolean sonidoActivado = preferences.getBoolean("sonido",true);
        return sonidoActivado;
    }



}