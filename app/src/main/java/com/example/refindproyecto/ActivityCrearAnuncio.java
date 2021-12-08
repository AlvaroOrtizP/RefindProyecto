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
import android.view.View;
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

import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Categoria;
import POJOS.Indicador;
import POJOS.Usuario;

public class ActivityCrearAnuncio extends AppCompatActivity {
    EditText nombreAnuncio, telefonoAnuncio, descripcionAnuncio;
    TextView categoriaAnuncio;
    Button btnAceptar, bntCancelar, elegirImagen;
    Usuario usuario;
    Categoria categoria;
    Anuncio anuncio = new Anuncio();
    int comprobador = 0;
    RequestQueue requestQueue;
    ImageView anuncioImagen;
    Bitmap bitmap;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);
        inicializar();
        btnAceptar.setOnClickListener(v -> {
            //Crear anuncio
            if(crearAnuncio().equals("ok")){
                startActivity(i);
            }
            else{
                Toast.makeText(ActivityCrearAnuncio.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
        bntCancelar.setOnClickListener(v -> {
            startActivity(i);
        });
        elegirImagen.setOnClickListener(v -> {
            abrirGaleria();
        });
    }
    private void inicializar(){
        nombreAnuncio = findViewById(R.id.editNombreAnun);
        telefonoAnuncio = findViewById(R.id.editTelefono);
        categoriaAnuncio = findViewById(R.id.textCategoria);
        descripcionAnuncio = findViewById(R.id.editDescripcion);
        anuncioImagen = findViewById(R.id.nuevoAnuncioImagen);
        categoria = new Categoria();
        usuario = new Usuario();
        categoria.setCategoriaId(Integer.valueOf(getIntent().getIntExtra("categoriaIdAnuncio", 0)));
        categoriaAnuncio.setText("cambiar esto");// todo se puede hacer una consulta para obtener el nombre con el id o buscar la forma de pasar el nombre por intent tambien
        btnAceptar = findViewById(R.id.btnCrearAnuncioAceptar);
        bntCancelar = findViewById(R.id.btnCrearAnuncioCancelar);
        elegirImagen = findViewById(R.id.btnElegirImagen);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ProcedimientoPreferencias pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            usuario.setUsuarioId(pF.obtenerIdentificador());
        }
        i = new Intent(ActivityCrearAnuncio.this, ActivityListaCat.class);

    }

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
                anuncioImagen.setImageBitmap(bitmap);
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
    public void subirImagenApache() {

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Indicador.UPLOAD_URL_ANUNCIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ActivityCrearAnuncio.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivityCrearAnuncio.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    private String crearAnuncio(){
        obtenerDatosAnuncio();
        if(insertarAnuncio(usuario).equals("ok")){
            subirImagenApache();
        }
        return anuncio.getError();
    }
    private void obtenerDatosAnuncio(){
        anuncio.setUsuario(usuario);
        System.out.println(usuario);
        anuncio.setTitulo(nombreAnuncio.getText().toString());
        anuncio.setTelefono(telefonoAnuncio.getText().toString());
        anuncio.setDescripcion(descripcionAnuncio.getText().toString());
        anuncio.setCategoria(categoria);
        anuncio.setFoto("png");
    }
    private String insertarAnuncio(Usuario usu){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                Anuncio pruebaAnun = new Anuncio(null, anuncio.getTitulo(), anuncio.getDescripcion(), categoria, usu, anuncio.getTelefono(), anuncio.getFoto());
                anuncio = refindCliente.crearAnuncio(pruebaAnun);
            }
        });//todo EXCEPCION
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        System.out.println("El mensaje de errores " + anuncio.getError());
        return anuncio.getError();
    }

    //falta añadir lo de paypal
}