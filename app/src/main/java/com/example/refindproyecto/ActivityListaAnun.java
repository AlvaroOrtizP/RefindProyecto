package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.refindproyecto.Adaptador.AdaptadorAnun;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import Cliente.ProcedimientosAnuncios;
import Cliente.RefindCliente;
import Modelo.Anuncio;
import Modelo.Categoria;


/*
 * Estructura del codigo:
 *      - 1 Creacion de variables
 *      - 2 ONCREATE
 *          2.1 Vincular variables con objetos del layout
 *          2.2 Llamada a metodos
 *      - 3 OBTENER ANUNCIOS
 */
public class ActivityListaAnun extends AppCompatActivity {
    /*
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    List<Anuncio> anuncioList;
    Anuncio anuncio = null;
    Categoria categoria = new Categoria();
    String anuncioT = "";
    ImageButton btnInicio, btnFavorito, btnPerfil;
    FloatingActionButton addAnuncio;

    /*
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anun);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
        addAnuncio = findViewById(R.id.addAnuncio);
        btnInicio.setImageResource(R.drawable.ic_homeb);
        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaAnun.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaAnun.this, ActivityListaFav.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaAnun.this, ActivityPerfil.class);
            startActivity(i);
        });
        addAnuncio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaAnun.this, ActivityNuevoAnuncio.class);
            i.putExtra("categoriaIdAnuncio", categoria.getCategoriaId());
            startActivity(i);
        });
        categoria.setCategoriaId(Integer.valueOf(getIntent().getStringExtra("categoriaId")));
        anuncioList = new ArrayList<>();

        //Llamada a metodos
        obtenerAnuncios();

    }

    /*
     * -----------------------------------------------------------
     *                          3 OBTENER ANUNCIOS
     * -----------------------------------------------------------
     */
    private void obtenerAnuncios(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosAnuncios refindCliente = new ProcedimientosAnuncios("10.0.2.2", 30500);
                anuncioList = refindCliente.obtenerListaAnuncios(categoria);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), R.string.errorConexion,
                    Toast.LENGTH_SHORT).show();
        }
        setRecyclerView(anuncioList);

    }



    private void setRecyclerView(List<Anuncio> anuncioList){
        AdaptadorAnun listadapter = new AdaptadorAnun(anuncioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewAnu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}