package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.refindproyecto.Modelo.Anuncio_row;
import com.example.refindproyecto.AnuncioAdapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityLAnuncios extends AppCompatActivity {

    ImageButton btnInicio, btnFavorito, btnPerfil;
    private RecyclerView rvLista;
    private RecyclerAdapter adapter;
    private List<Anuncio_row> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_anuncios);


        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(v -> {

            Intent i = new Intent(ActivityLAnuncios.this, MainActivity.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {

            Intent i = new Intent(ActivityLAnuncios.this, ActivityFavoritos.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {

            Intent i = new Intent(ActivityLAnuncios.this, ActivityPerfil.class);
            startActivity(i);
        });

        initViews();
        initValues();
    }
    private void initViews(){
        rvLista = findViewById(R.id.rvLista);
    }

    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);

        items = getItems();
        adapter = new RecyclerAdapter(items, this::itemClick);
        rvLista.setAdapter(adapter);
    }
    private List<Anuncio_row> getItems() {
        List<Anuncio_row> categoriarows = new ArrayList<>();
        categoriarows.add(new Anuncio_row("Hola mundo1", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo2", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo3", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo4", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo5", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo6", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo7", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo8", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo9", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo10", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo11", "Una", R.drawable.ic_launcher_background));
        categoriarows.add(new Anuncio_row("Hola mundo12", "Una", R.drawable.ic_launcher_background));

        return categoriarows;
    }


    public void itemClick(Anuncio_row item) {
        Intent intent = new Intent(this, ActivityAnuncio.class);
        intent.putExtra("datosAnuncio", item);
        startActivity(intent);
        //Toast.makeText(this, item.getTitulo(), Toast.LENGTH_LONG).show();
    }


    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}