package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Categoria;
import POJOS.Usuario;

public class ActivityNewAnuncio extends AppCompatActivity {
    EditText nombreAnuncio, telefonoAnuncio, descripcionAnuncio;
    Button aceptar, cancelar;
    Spinner categoriaAnuncio;
    Anuncio anuncio = new Anuncio();
    Categoria categoria = new Categoria();
    Usuario usuario = new Usuario();
    private FirebaseAuth mAuth;
    //Falta la imagen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_anuncio);
        aceptar.findViewById(R.id.aceptarNewAnuncio);//Error de runtime exception
        cancelar.findViewById(R.id.cancelarrNewAnuncio);
        mAuth = FirebaseAuth.getInstance();
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos();
                //compruebo que la imagen es nueva
                //Creo anuncio
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                        
                       // usuario.setError(refindCliente.crearAnuncio(anuncio));
                    }
                });
                thread.start();
                //Subo imagen
            }
        });
    }
    private Anuncio cargarDatos(){
        nombreAnuncio.findViewById(R.id.nombreNewAnuncio);
        telefonoAnuncio.findViewById(R.id.telefonoNewAnuncio);
        descripcionAnuncio.findViewById(R.id.descripNewAnuncio);
        categoriaAnuncio.findViewById(R.id.categoriaNewAnuncio);
        anuncio.setTitulo(nombreAnuncio.getText().toString());
        anuncio.setDescripcion(descripcionAnuncio.getText().toString());
        usuario.setUsuarioFirebase(mAuth.getUid());
        categoria.setCategoriaId(categoriaAnuncio.getId());
        anuncio.setCategoria(categoria);
        anuncio.setUsuario(usuario);
        return anuncio;
    }
}