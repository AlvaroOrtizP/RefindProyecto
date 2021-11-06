package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.refindproyecto.Adaptador.AdaptadorCat;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import Cliente.RefindCliente;
import POJOS.Categoria;

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
                //Al meter categorias a mano funciona pero no se pq no obtiene un array vacio 
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                categoriaT = refindCliente.obtenerCategorias();
                //NumberFormatException
                if(!categoriaT.equals("ERROR_CONEXION")){
                    //TODO: cambiar cuando se compile el REFINDCAD por el indicador
                    Integer id=0;
                    String[] arrayCat = categoriaT.split("/");
                    if(!categoriaT.equals("")){
                        for (int i = 0; i <= arrayCat.length - 1; i++) {
                            if (arrayCat[i].equals("-")) {
                                i++;
                            }
                            //Error de numero. Tiene que estar en la forma de formatear
                            id = Integer.valueOf(arrayCat[i]);
                            categoria = new Categoria();
                            categoria.setCategoriaId(id);
                            i++;
                            categoria.setTitulo(arrayCat[i]);
                            i++;
                            categoria.setDescripcion(arrayCat[i]);
                            i++;
                            categoria.setFoto(arrayCat[i]);
                            i++;
                            categoriaList.add(categoria);
                        }
                    }
                    else{
                        //TODO: añadir campo en las tablas con el tipo de imagen JPG, PNG ...
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.errorConexion,Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //TODO: añadir excepcion
            e.printStackTrace();
        }
        setRecyclerView(categoriaList);
    }

    /**
     * -----------------------------------------------------------
     *                          4 Manda datos al Recycler
     * -----------------------------------------------------------
     */
    private void setRecyclerView(List<POJOS.Categoria> categoriaList){
        AdaptadorCat listadapter = new AdaptadorCat(categoriaList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerCat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}