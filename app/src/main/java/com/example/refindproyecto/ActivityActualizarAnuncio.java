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

import Cliente.ProcedimientosAnuncios;
import Cliente.ProcedimientosUsuarios;
import Modelo.Anuncio;
import Modelo.Indicador;
import Modelo.Usuario;

public class ActivityActualizarAnuncio extends AppCompatActivity {
    Anuncio anuncio = new Anuncio();
    Bitmap bitmap;//Esta no se remplaz
    int comprobador = 0;
    //Usuario usuario = new Usuario();
    ProcedimientoPreferencias pF = null;
    EditText editNuevoTitulo, editNewTel, editDescrp;
    Button btnElegirImagen, btnAceptar, bntCancelar;
    ImageView ajustesImagen;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_anuncio);

        String anuncioId = getIntent().getStringExtra("anuncio_id");

        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        inicializar();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            cargarInicial(anuncioId);
        }
        btnElegirImagen.setOnClickListener(v -> {
            abrirGaleria();
        });
        bntCancelar.setOnClickListener(v -> {
            Intent i = new Intent(ActivityActualizarAnuncio.this, ActivityPerfil.class);
            startActivity(i);
        });
        btnAceptar.setOnClickListener(v -> {
           anuncio = actualizarAnuncio();
            if(anuncio.getError()!= null && "OK".equals(anuncio.getError())){
                if(comprobador == 1){
                    subirImagenApache();
                }
                Intent i = new Intent(ActivityActualizarAnuncio.this, ActivityPerfil.class);
                startActivity(i);
            }
            else{
                Toast.makeText(ActivityActualizarAnuncio.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Anuncio actualizarAnuncio() {
        anuncio.setTitulo((editNuevoTitulo.getText()==null)?"":editNuevoTitulo.getText().toString());
        anuncio.setDescripcion((editDescrp.getText()==null)?"":editDescrp.getText().toString());
        anuncio.setTelefono((editNewTel.getText()==null)?"":editNewTel.getText().toString());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosAnuncios refindCliente = new ProcedimientosAnuncios("10.0.2.2", 30500);
                anuncio = refindCliente.actualizarAnuncio(anuncio);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return anuncio;
    }

    private void cargarInicial(String anuncioId) {
        try{
            anuncio.setAnuncioId(Integer.parseInt(anuncioId));
            anuncio = obtenerDatosAnuncio();
            editNuevoTitulo.setText(anuncio.getTitulo());
            editNewTel.setText(anuncio.getTelefono());
            editDescrp.setText(anuncio.getDescripcion());
            cargarImagen(ajustesImagen, anuncio);
        }
        catch (NumberFormatException ex){
            Intent i = new Intent(getApplicationContext(), ActivityAnuncio.class);
            startActivity(i);
        }

    }
    private Anuncio obtenerDatosAnuncio(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosAnuncios refindCliente = new ProcedimientosAnuncios("10.0.2.2", 30500);
                anuncio = refindCliente.obtenerAnuncio(anuncio);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        return anuncio;
    }

    private void inicializar() {
        editNuevoTitulo = findViewById(R.id.editNuevoTitulo);
        editNewTel = findViewById(R.id.editNewTel);
        editDescrp = findViewById(R.id.editDescrp);
        ajustesImagen = findViewById(R.id.ajustesImagen);
        btnAceptar = findViewById(R.id.btnAjustesAceptar);
        bntCancelar = findViewById(R.id.btnAjustesCancelar);
        btnElegirImagen = findViewById(R.id.btnElegirImagen);
    }

    private void cargarImagen(ImageView imagenPerfil, Anuncio anuncio){
        ImageRequest imageRequest = new ImageRequest(Indicador.IMAGEN_USUARIO+anuncio.getAnuncioId()+"."+anuncio.getFoto(), new Response.Listener<Bitmap>() {
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
                        Toast.makeText(ActivityActualizarAnuncio.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivityActualizarAnuncio.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String imagen = getStringImagen(bitmap);
                String nombre = anuncio.getAnuncioId().toString();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(Indicador.KEY_IMAGE, imagen);
                params.put(Indicador.KEY_NOMBRE, nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}