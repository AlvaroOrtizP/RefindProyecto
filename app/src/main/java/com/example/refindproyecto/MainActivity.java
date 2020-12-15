package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.refindproyecto.AnuncioAdapter.RecyclerAdapterC;
import com.example.refindproyecto.Modelo.Categoria;
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
        categoriarows.add(new Categoria("TIutulo1","Descrf", 1, R.drawable.ic_launcher_background));
        categoriarows.add(new Categoria("TIutulo2","Descrf", 2, R.drawable.ic_launcher_background));
        categoriarows.add(new Categoria("TIutulo3","Descrf", 3, R.drawable.ic_launcher_background));

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