package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.refindproyecto.AnuncioAdapter.RecyclerAdapterC;
import com.example.refindproyecto.POJOS.Categoria;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvLista;
    private RecyclerAdapterC adapter;
    private List<Categoria> items;

    ImageButton btnInicio, btnFavorito, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(v -> {

            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {

            Intent i = new Intent(MainActivity.this, ActivityFavoritos.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {

            Intent i = new Intent(MainActivity.this, ActivityPerfil.class);
            startActivity(i);
        });

        initViews();
        initValues();
    }
    private void initViews(){
        rvLista = findViewById(R.id.reciclerCategorias);
    }

    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);
        items = getItems();
        adapter = new RecyclerAdapterC(items, this::itemClick);
        rvLista.setAdapter(adapter);
    }
    private List<Categoria> getItems() {

        List<Categoria> categoriarows = new ArrayList<>();
        categoriarows.add(new Categoria("Hotel","Un viaje increible, con un hotel increible", 1, R.drawable.hotel));
        categoriarows.add(new Categoria("Taller","Si quieres arreglar tu vehiculo, ven a nuestros talleres unicos", 2, R.drawable.taller));
        categoriarows.add(new Categoria("Restaurante","Las increibles croquetas en los increibles restaurantes", 3, R.drawable.restaurante));
        categoriarows.add(new Categoria("Panaderias","Las increibles croquetas en los increibles restaurantes", 4, R.drawable.panaderia));
        categoriarows.add(new Categoria("Mascotas","Las increibles croquetas en los increibles restaurantes", 5, R.drawable.mascota));
        categoriarows.add(new Categoria("Peluqueria","Las increibles croquetas en los increibles restaurantes", 6, R.drawable.peluqueria));
        categoriarows.add(new Categoria("Tiendas","Las increibles croquetas en los increibles restaurantes", 7, R.drawable.tienda));
        categoriarows.add(new Categoria("Supermercado","Las increibles croquetas en los increibles restaurantes", 8, R.drawable.supermercado));

        return categoriarows;
    }


    public void itemClick(Categoria item) {
        Intent intent = new Intent(this, ActivityLAnuncios.class);
        intent.putExtra("idcat", item.getId_categoria());
        startActivity(intent);
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }


}