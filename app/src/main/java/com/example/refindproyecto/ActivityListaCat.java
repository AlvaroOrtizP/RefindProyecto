package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.refindproyecto.Adaptador.AdaptadorCat;
import java.util.ArrayList;
import java.util.List;
import Cliente.ProcedimientosCategorias;
import Modelo.Categoria;

/**
 * Estructura del codigo:
 *  - 1 Creacion de variables
 *  - 2 onCreate
 *      2.1 Vincular variables con objetos del layout
 *      2.2 Funcionalidad de botones
 *      2.3 Llamadas a metodos
 *  - 3 Obtener categorias
 */
public class ActivityListaCat extends AppCompatActivity {
    /**
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */

    List<Categoria> categoriaList = new ArrayList<>();
    Categoria categoria = null;
    String categoriaT = "";
    ImageButton btnInicio, btnFavorito, btnPerfil;
    /**
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_lista_cat);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
        btnInicio.setImageResource(R.drawable.ic_homeb);
        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaCat.this, ActivityListaFav.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaCat.this, ActivityPerfil.class);
            startActivity(i);
        });

        obtenerCategorias();
    }

    /**
     * -----------------------------------------------------------
     *                          3 Obtener Categorias
     * -----------------------------------------------------------
     */
    private void obtenerCategorias(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcedimientosCategorias refindCliente = new ProcedimientosCategorias("10.0.2.2", 30500);
                    categoriaList = refindCliente.obtenerListaCategorias();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), R.string.errorConexion,Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), R.string.errorConexion,
                    Toast.LENGTH_SHORT).show();
        }

        setRecyclerView(categoriaList);
    }

    /*
     * -----------------------------------------------------------
     *                          4 Manda datos al Recycler
     * -----------------------------------------------------------
     */
    private void setRecyclerView(List<Categoria> categoriaList){
        AdaptadorCat listadapter = new AdaptadorCat(categoriaList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerCat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }

}