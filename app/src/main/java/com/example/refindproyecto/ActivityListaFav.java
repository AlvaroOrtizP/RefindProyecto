package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;
import com.example.refindproyecto.Procedimientos.ProcedimientoPreferencias;
import java.util.ArrayList;
import java.util.List;
import Cliente.ProcedimientosFavoritos;
import Modelo.Anuncio;
import Modelo.Usuario;

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
    List<Anuncio> anuncioList = new ArrayList<>();
    ProcedimientoPreferencias pF = null;
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
        pF = new ProcedimientoPreferencias(this.getApplicationContext());
        if(pF.obtenerIdentificador() == 0){
            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
            startActivity(i);
        }else{
            usuario.setUsuarioId(pF.obtenerIdentificador());
        }
        //Llamadas a metodos
        obtenerFavortios(usuario);
    }

    /*
     * -----------------------------------------------------------
     *                          3 Obtener Favoritos
     * -----------------------------------------------------------
     */
    private void obtenerFavortios(Usuario usuario){
       Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcedimientosFavoritos refindCliente = new ProcedimientosFavoritos("10.0.2.2", 30500);
                try{
                    anuncioList = refindCliente.obtenerListaFavoritos(usuario);

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "No tienes", Toast.LENGTH_SHORT).show();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), R.string.errorConexion,
                    Toast.LENGTH_SHORT).show();
        }
        if(anuncioList.size()==0){
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