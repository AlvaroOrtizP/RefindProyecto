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

import Cliente.RefindCliente;
import POJOS.Indicador;
import POJOS.Usuario;

public class ActivityUsuarioAjustes extends AppCompatActivity {
    Button btnBuscar, btnSubir, btnCancelar;
    ImageView iv;
    EditText et;
    EditText editNewNombre, editNewApellido, editNewBio;
    private ProcedimientoPreferencias pF = null;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    Usuario usuario = new Usuario();
    //Ruta donde se encuentra el archivo php
    String UPLOAD_URL = "http://10.0.2.2/Refind/images/upload.php";//10.0.2.2
    //Las keys de los parametros que se pasaran por POST
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    RequestQueue requestQueue;
    int comprobador = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ajustes);
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            cargarInicial();
        }
        btnCancelar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ActivityPerfil.class);
            startActivity(i);
        });
        btnBuscar.setOnClickListener(v -> showFileChooser());

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = actualizarUsuario();
                if("OK".equals(usuario.getError())){
                    if(comprobador == 1){
                        uploadImage();//todo Cuando haces los cambios sin subir una nueva foto peta
                    }
                    Intent i = new Intent(ActivityUsuarioAjustes.this, ActivityPerfil.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ActivityUsuarioAjustes.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private Usuario actualizarUsuario() {
        usuario.setNombre(editNewNombre.getText().toString());
        usuario.setApellido(editNewApellido.getText().toString());
        usuario.setBiografia(editNewBio.getText().toString());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
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
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ActivityUsuarioAjustes.this, response, Toast.LENGTH_LONG).show();
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ActivityUsuarioAjustes.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = et.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, usuario.getUsuarioId().toString());
                params.put("tipo", "usuario");
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
                comprobador = 1;
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }
    public void cargarInicial(){
        btnBuscar = findViewById(R.id.btnElegirImagen);
        btnSubir = findViewById(R.id.btnAjustesAceptar);
        btnCancelar = findViewById(R.id.btnAjustesCancelar);
        et = findViewById(R.id.editNewNombre);
        iv = findViewById(R.id.ajustesImagen);
        editNewNombre = findViewById(R.id.editNewNombre);
        editNewApellido = findViewById(R.id.editNewApellido);
        editNewBio = findViewById(R.id.editNewBio);

        usuario.setUsuarioId(pF.obtenerIdentificador());
        usuario = obtenerDatosUsuario();
        editNewNombre.setText(usuario.getNombre());
        editNewApellido.setText(usuario.getApellido());
        editNewBio.setText(usuario.getBiografia());
        cargarImagen(iv, usuario);
    }
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
    private void cargarImagen(ImageView imagenPerfil, Usuario usuario){
        System.out.println("Entra en cargar imagen");
        System.out.println("La imagen para cargar es " + Indicador.IMAGEN_USUARIO+usuario.getUsuarioId()+"."+usuario.getFoto() );
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
}
