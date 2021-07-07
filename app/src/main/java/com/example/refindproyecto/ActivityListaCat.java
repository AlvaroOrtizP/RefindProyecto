package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.refindproyecto.Adaptador.AdaptadorCat;
import java.util.ArrayList;
import java.util.List;
import Cliente.RefindCliente;
import POJOS.Categoria;


public class ActivityListaCat extends AppCompatActivity {
    List<Categoria> categoriaList = null;
    Categoria categoria = null;
    String categoriaT = "";
    ImageButton btnInicio, btnFavorito, btnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        init();
    }
    public void init(){
        categoriaList = new ArrayList<>();
        obtenerCategorias();
    }
    private void obtenerCategorias(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Al meter categorias a mano funciona pero no se pq no obtiene un array vacio 
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                categoriaT = refindCliente.obtenerCategoria();
                //NumberFormatException
                Integer id=0;
                String[] arrayCat = categoriaT.split("/");
                for (int i = 0; i <= arrayCat.length - 1; i++) {
                    if (arrayCat[i].equals("-")) {
                        i++;
                    }
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
        });
        thread.start();
        setRecyclerView(categoriaList);
    }
    private void setRecyclerView(List<POJOS.Categoria> categoriaList){
        AdaptadorCat listadapter = new AdaptadorCat(categoriaList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerCat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}