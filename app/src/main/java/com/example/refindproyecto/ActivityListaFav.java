package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Categoria;
import POJOS.Usuario;

public class ActivityListaFav extends AppCompatActivity {
    ImageButton btnInicio, btnFavorito, btnPerfil;
    Usuario usuario = new Usuario();
    ArrayList<Anuncio> anuncioList = new ArrayList<>();
    String anuncioT = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fav);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
        btnFavorito.setImageResource(R.drawable.ic_favoritob_on);
        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaFav.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaFav.this, ActivityPerfil.class);
            startActivity(i);
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        usuario.setUsuarioFirebase(mAuth.getUid());
        init(usuario);

    }
    public void init(Usuario usuario){
        anuncioList = new ArrayList<>();
        obtenerAnuncios(usuario);
    }

    private void obtenerAnuncios(Usuario usuario){
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                anuncioT = refindCliente.obtenerFavoritos(usuario);
                String[] anuncioArray = anuncioT.split("/");
                Anuncio anuncio = null;
                Integer id=0;
                for (int i = 0; i <= anuncioArray.length - 1; i++) {
                    anuncio = new Anuncio();
                    if (anuncioArray[i].equals("-")) {
                        i++;
                    }
                    id = Integer.valueOf(anuncioArray[i]);
                    anuncio.setAnuncioId(id);
                    i++;
                    anuncio.setTitulo(anuncioArray[i]);
                    i++;
                    anuncio.setDescripcion(anuncioArray[i]);
                    i=i+3;
                    anuncio.setTelefono(anuncioArray[i]);
                    i++;
                    anuncio.setFoto(anuncioArray[i]);
                    anuncioList.add(anuncio);
                }
            }
        });
        thread.start();
        setRecyclerView(anuncioList);
    }

    private void setRecyclerView(List<Anuncio> anuncioList){
        AdaptadorAnun adaptadorAnuncio = new AdaptadorAnun(anuncioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewFav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorAnuncio);
    }


}