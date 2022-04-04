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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import Cliente.ProcedimientosUsuarios;
import Cliente.RefindCliente;
import Modelo.Usuario;

public class ActivityUsuarioAjustes extends AppCompatActivity {
    Button btnBuscar, btnSubir;
    ImageView imagen;
    EditText nombre;
    EditText apellido;
    EditText biografia;
    ProcedimientoPreferencias pF = null;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    Usuario usuario;
    private boolean imagenSeleccionada=false;
    //Ruta donde se encuentra el archivo php
    String UPLOAD_URL = "http://10.0.2.2/Refind/images/upload.php";//10.0.2.2
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ajustes);
        this.usuario = new Usuario();
        btnBuscar = findViewById(R.id.btnElegirImagen);
        btnSubir = findViewById(R.id.btnAjustesAceptar);

        nombre = findViewById(R.id.editNewNombre);
        imagen = findViewById(R.id.ajustesImagen);
        apellido = findViewById(R.id.editNewApellido);
        biografia = findViewById(R.id.editNewBio);
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            this.usuario.setUsuarioId(pF.obtenerIdentificador());
        }
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarUsuario();
                if(imagenSeleccionada){
                    uploadImage();
                }

            }
        });
        System.out.println("El usuario con el que entra es "+ this.usuario.getUsuarioId());
        usuario = obtenerDatosUsuario();
        nombre.setText(usuario.getNombre() == null ? "": usuario.getNombre());
        apellido.setText(usuario.getApellido() == null ? "": usuario.getApellido());
        biografia.setText(usuario.getBiografia() == null ? "": usuario.getBiografia());

    }
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
    private Usuario actualizarUsuario(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosUsuarios refindCliente = new ProcedimientosUsuarios("10.0.2.2", 30500);

                usuario.setNombre(nombre.getText().toString());
                usuario.setApellido(apellido.getText().toString());
                usuario.setBiografia(biografia.getText().toString());
                usuario = refindCliente.actualizarUsuario(usuario);
                System.out.println("El usaurio para actualizar es "+ usuario);
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
                String nombre = ActivityUsuarioAjustes.this.nombre.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);
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
                imagen.setImageBitmap(bitmap);
                imagenSeleccionada=true;
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
}
