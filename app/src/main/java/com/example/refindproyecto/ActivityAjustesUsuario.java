package com.example.refindproyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import Cliente.ProcedimientosUsuarios;
import Modelo.Indicador;
import Modelo.Usuario;

//todo eliminar
public class ActivityAjustesUsuario extends AppCompatActivity {
    /*
     * -----------------------------------------------------------
     *                          1.3 Para subir las imagenes
     * -----------------------------------------------------------
     */

    Bitmap bitmap;//Esta no se remplaz
    int comprobador = 0;
    Usuario usuario = new Usuario();
    ProcedimientoPreferencias pF = null;
    EditText editNewNombre, editNewApellido, editNewBio;
    Button btnElegirImagen, btnAceptar, bntCancelar;
    ImageView ajustesImagen;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_usuario);
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        inicializar();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            cargarInicial();
        }
        btnElegirImagen.setOnClickListener(v -> {
            abrirGaleria();
        });
        bntCancelar.setOnClickListener(v -> {
            Intent i = new Intent(ActivityAjustesUsuario.this, ActivityPerfil.class);
            startActivity(i);
        });

        // falta cambiar el nombre de la base de datos
        btnAceptar.setOnClickListener(v -> {
            usuario = actualizarUsuario();
            if("OK".equals(usuario.getError())){
                if(comprobador == 1){
                    subirImagenApache();//todo Cuando haces los cambios sin subir una nueva foto peta
                }
                Intent i = new Intent(ActivityAjustesUsuario.this, ActivityPerfil.class);
                startActivity(i);
            }
            else{
                Toast.makeText(ActivityAjustesUsuario.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
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
                ProcedimientosUsuarios refindCliente = new ProcedimientosUsuarios("10.0.2.2", 30500);
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
    private void cargarImagen(ImageView imagenPerfil, Usuario usuario){
        ImageRequest imageRequest = new ImageRequest(Indicador.IMAGEN_USUARIO+usuario.getUsuarioId()+"."+usuario.getFoto(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagenPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        requestQueue.add(imageRequest);
    }
    private Usuario actualizarUsuario() {
        usuario.setNombre(editNewNombre.getText().toString());
        usuario.setApellido(editNewApellido.getText().toString());
        usuario.setBiografia(editNewBio.getText().toString());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosUsuarios refindCliente = new ProcedimientosUsuarios("10.0.2.2", 30500);
                System.out.println("antes");
                usuario.setNombre("paco");
                usuario = refindCliente.actualizarUsuario(usuario);
                System.out.println("Despues");
            }
        });//todo EXCEPCION
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
     *                          9 SUBIR IMAGEN
     * -----------------------------------------------------------
     */
    /*
     * -----------------------------------------------------------
     *                          9.1 ABRIR LAS FOTOS DEL TELEFONO
     * -----------------------------------------------------------
     */
    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), Indicador.PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Indicador.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                comprobador = 1;
                ajustesImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    /*
     * -----------------------------------------------------------
     *                          9.2 SUBIR LA IMAGEN
     * -----------------------------------------------------------
     */
    public void subirImagenApache() {

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ActivityAjustesUsuario.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivityAjustesUsuario.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String imagen = getStringImagen(bitmap);
                String nombre = usuario.getUsuarioId().toString();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(Indicador.KEY_IMAGE, imagen);
                params.put(Indicador.KEY_NOMBRE, nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    /*
     * -----------------------------------------------------------
     *                          9.1 INICIZALIZA
     * -----------------------------------------------------------
     */
    private void inicializar(){
        editNewNombre = findViewById(R.id.editNewNombre);
        editNewApellido = findViewById(R.id.editNewApellido);
        editNewBio = findViewById(R.id.editNewBio);
        ajustesImagen = findViewById(R.id.ajustesImagen);
        btnAceptar = findViewById(R.id.btnAjustesAceptar);
        bntCancelar = findViewById(R.id.btnAjustesCancelar);
        btnElegirImagen = findViewById(R.id.btnElegirImagen);
    }
    public void cargarInicial(){
        usuario.setUsuarioId(pF.obtenerIdentificador());
        usuario = obtenerDatosUsuario();
        editNewNombre.setText(usuario.getNombre());
        editNewApellido.setText(usuario.getApellido());
        editNewBio.setText(usuario.getBiografia());
        cargarImagen(ajustesImagen, usuario);
    }
}