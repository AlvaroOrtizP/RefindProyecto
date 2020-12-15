package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.refindproyecto.Modelo.Anuncio;
import com.example.refindproyecto.AnuncioAdapter.RecyclerAdapterA;
import java.util.ArrayList;
import java.util.List;

public class ActivityLAnuncios extends AppCompatActivity {

    ImageButton btnInicio, btnFavorito, btnPerfil;
    private RecyclerView rvLista;
    private RecyclerAdapterA adapter;
    private List<Anuncio> items;
    private int idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_anuncios);
        idCategoria = (int) getIntent().getExtras().getSerializable("idcat");


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
        adapter = new RecyclerAdapterA(items, this::itemClick);
        rvLista.setAdapter(adapter);
    }
    private List<Anuncio> getItems() {
        List<Anuncio> anunciosRow = new ArrayList<>();

        anunciosRow.add(new Anuncio("prueba1","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba2","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba3","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba4","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba5","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba2","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));
        anunciosRow.add(new Anuncio("prueba3","Esta es la prueba 1", R.drawable.ic_launcher_background, 1));


        return anunciosRow;
    }


    public void itemClick(Anuncio item) {
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