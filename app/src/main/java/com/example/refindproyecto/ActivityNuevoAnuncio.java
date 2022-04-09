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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import Cliente.ProcedimientosAnuncios;
import Cliente.ProcedimientosCategorias;
import Modelo.Anuncio;
import Modelo.Categoria;
import Modelo.Indicador;
import Modelo.Usuario;

//El bueno
public class ActivityNuevoAnuncio extends AppCompatActivity {
    EditText nombreAnuncio, telefonoAnuncio, descripcionAnuncio;
    TextView categoriaAnuncio;
    Button btnAceptar, bntCancelar, elegirImagen;
    Usuario usuario;
    Categoria categoria;
    Anuncio anuncio = new Anuncio();
    boolean fotoElegida = false;
    RequestQueue requestQueue;
    ImageView anuncioImagen;
    Bitmap bitmap;
    Intent i = null;
    int PICK_IMAGE_REQUEST = 1;

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_anuncio);
        usuario = inicializar();
        btnAceptar.setOnClickListener(v -> {
            if(fotoElegida){
                crearAnuncio(usuario);
            }
            else{
                Toast.makeText(getApplicationContext(), "Debes elegir una foto",Toast.LENGTH_SHORT).show();
            }
        });
        bntCancelar.setOnClickListener(v -> {
            startActivity(i);
        });
        elegirImagen.setOnClickListener(v -> {
            abrirGaleria();
        });
    }
    private String crearAnuncio(Usuario usuario1){
        anuncio = obtenerDatosAnuncio();
        System.out.println("Datos del anuncio el crear anuncio " + anuncio);
        System.out.println("Datos del usuario el crear anuncio " + usuario1);
        //Tengo los datos del anuncio y tengo el usuario id en el objeto usuario1
        if(anuncio.comprobarTelefono(anuncio.getTelefono())){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ProcedimientosAnuncios refindCliente = new ProcedimientosAnuncios("10.0.2.2", 30500);
                    Anuncio pruebaAnun = new Anuncio(null, anuncio.getTitulo(), anuncio.getDescripcion(), categoria, usuario1, anuncio.getTelefono(), anuncio.getFoto());
                    anuncio = refindCliente.crearAnuncio(pruebaAnun);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(), R.string.errorConexion,
                        Toast.LENGTH_SHORT).show();
            }

            if(!anuncio.getError().contains("error")){
                subirImagen(anuncio.getError());
                startActivity(i);
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.tamanoTelefonoError,
                    Toast.LENGTH_SHORT).show();
        }


        return anuncio.getError();
    }
    //No tocar

    private Anuncio obtenerDatosAnuncio(){
        Anuncio oAnuncio = new Anuncio();
        oAnuncio.setTitulo(nombreAnuncio.getText().toString());
        oAnuncio.setTelefono(telefonoAnuncio.getText().toString());
        oAnuncio.setDescripcion(descripcionAnuncio.getText().toString());
        oAnuncio.setCategoria(categoria);
        oAnuncio.setFoto("png");
        return oAnuncio;
    }
    private Usuario inicializar(){
        nombreAnuncio = findViewById(R.id.editNombreAnun);
        telefonoAnuncio = findViewById(R.id.editTelefono);
        categoriaAnuncio = findViewById(R.id.textCategoria);
        descripcionAnuncio = findViewById(R.id.editDescripcion);
        anuncioImagen = findViewById(R.id.nuevoAnuncioImagen);
        btnAceptar = findViewById(R.id.btnCrearAnuncioAceptar);
        bntCancelar = findViewById(R.id.btnCrearAnuncioCancelar);
        elegirImagen = findViewById(R.id.btnElegirImagen);
        categoria = new Categoria();
        usuario = new Usuario();
        categoria.setCategoriaId(Integer.valueOf(getIntent().getIntExtra("categoriaIdAnuncio", 0)));

        categoriaAnuncio.setText(obtenerNombreCategoria(categoria));


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ProcedimientoPreferencias pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            usuario.setUsuarioId(pF.obtenerIdentificador());
        }
        i = new Intent(ActivityNuevoAnuncio.this, ActivityListaCat.class);
        return usuario;
    }

    //No tocar Subir imagen
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void subirImagen(String identificador) {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Indicador.UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ActivityNuevoAnuncio.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivityNuevoAnuncio.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, identificador);
                params.put("tipo", "anuncio");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                anuncioImagen.setImageBitmap(bitmap);
                fotoElegida = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    private String obtenerNombreCategoria(Categoria categoria){
        final Categoria[] categoriaNombre = {new Categoria()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosCategorias pC = new ProcedimientosCategorias("10.0.2.2", 30500);
                categoriaNombre[0] = pC.obtenerNombreCategoria(categoria);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            categoriaNombre[0].setTitulo("No se encontro categoria");
            e.printStackTrace();
        }
        String resultadoFinal = categoriaNombre[0].getTitulo();
        return resultadoFinal;
    }
}
