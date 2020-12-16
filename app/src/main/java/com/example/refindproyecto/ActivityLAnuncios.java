package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

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
        //Anuncios 1 Hotel
        switch (idCategoria){
            case 1:
                anunciosRow.add(new Anuncio("Hotel Campomar","Este alojamiento está en primera línea de playa, en la localidad cántabra de Isla, en plena Costa Verde", R.drawable.hotel_campomar, 1, (float) 43.5020712, (float) -3.5774679));
                anunciosRow.add(new Anuncio("Hotel Olimpo","El Hotel Olimpo es emblemático en la costa de Cantabria. Se encuentra situado en la localidad costera de Isla (Cantabria) y, más en concreto, en un paraje privilegiado de impresionante belleza, probablemente el enclave más bello del norte de España", R.drawable.hotel_olimpo, 1, (float) 43.4924427, (float) -3.5476312));
                anunciosRow.add(new Anuncio("Hotel Guardeses","El Hotel está ubicado en Solares, muy cerca de Santander, Cabárceno y de todos los lugares que están buscando.", R.drawable.hotel_guardeses, 1, (float) 43.3850684, (float) -3.7417777));
                break;
            case 2:
                anunciosRow.add(new Anuncio("prueba4","Esta es la prueba 1", R.drawable.ic_launcher_background, 1, 1222, 12222));
                anunciosRow.add(new Anuncio("prueba5","Esta es la prueba 1", R.drawable.ic_launcher_background, 1, 1222, 12222));
                anunciosRow.add(new Anuncio("prueba2","Esta es la prueba 1", R.drawable.ic_launcher_background, 1, 1222, 12222));
                anunciosRow.add(new Anuncio("prueba3","Esta es la prueba 1", R.drawable.ic_launcher_background, 1, 1222, 12222));
                break;
            default:
                Toast.makeText(this,"Todavia no existen anuncios para esa categoria", Toast.LENGTH_SHORT).show();
                break;
        }



        //Anuncios 2


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