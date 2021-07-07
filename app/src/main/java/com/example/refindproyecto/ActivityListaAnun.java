package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Categoria;

public class ActivityListaAnun extends AppCompatActivity {

    List<Anuncio> anuncioList;
    Anuncio anuncio = null;
    Categoria categoria = new Categoria();
    String anuncioT = "";
    ImageButton btnInicio, btnFavorito, btnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anun);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
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
        categoria.setCategoriaId(Integer.valueOf(getIntent().getStringExtra("categoriaId")));
        init(categoria);
    }
    public void init(Categoria categoriaId){
        anuncioList = new ArrayList<>();
        obtenerAnuncios(categoriaId);
    }
    private void obtenerAnuncios(Categoria categoriaId){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                anuncioT = refindCliente.obtenerAnuncios(categoria);
                String[] arrayAnuncio = anuncioT.split("/");
                Integer id=0;
                for (int i = 0; i <= arrayAnuncio.length - 1; i++) {
                    anuncio = new Anuncio();
                    if (arrayAnuncio[i].equals("-")) {
                        i++;
                    }
                    id = Integer.valueOf(arrayAnuncio[i]);
                    anuncio.setAnuncioId(id);
                    i++;
                    anuncio.setTitulo(arrayAnuncio[i]);
                    i++;
                    anuncio.setDescripcion(arrayAnuncio[i]);
                    i = i + 3;
                    anuncio.setTelefono(arrayAnuncio[i]);
                    i++;
                    anuncio.setFoto(arrayAnuncio[i]);
                    anuncioList.add(anuncio);
                }
            }
        });
        thread.start();
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