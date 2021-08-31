package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import Cliente.RefindCliente;
import POJOS.Anuncio;
import POJOS.Usuario;

/**
 * Estructura del codigo:
 *  - 1 Creacion de variables
 *  - 2 onCreate
 *      2.1 Vincular variables con objetos del layout
 *      2.2 Funcionalidad de botones
 *      2.3 Llamadas a metodos
 *  - 3 Obtener favoritos
 */
public class ActivityListaFav extends AppCompatActivity {
    /**
     * -----------------------------------------------------------
     *                          1 CREACION DE VARIABLES
     * -----------------------------------------------------------
     */
    ImageButton btnInicio, btnFavorito, btnPerfil;
    Usuario usuario = new Usuario();
    ArrayList<Anuncio> anuncioList = new ArrayList<>();
    String anuncioT = "";
    /**
     * -----------------------------------------------------------
     *                          2 ONCREATE
     * -----------------------------------------------------------
     */
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
        anuncioList = new ArrayList<>();

        //Llamadas a metodos
        obtenerFavortios(usuario);


    }

    /**
     * -----------------------------------------------------------
     *                          3 Obtener Favoritos
     * -----------------------------------------------------------
     */
    private void obtenerFavortios(Usuario usuario){
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RefindCliente refindCliente = new RefindCliente("10.0.2.2", 30500);
                //anuncioT = refindCliente.obtenerFavoritos(usuario);
                String[] anuncioArray = anuncioT.split("/");
                Anuncio anuncio = null;
                Integer id=0;
                if(!anuncioT.equals("")){
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
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), R.string.errorConexion, Toast.LENGTH_SHORT).show();
        }
        if(anuncioT.equals("")){
            Toast.makeText(getApplicationContext(), R.string.noFavoritos, Toast.LENGTH_SHORT).show();
        }
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