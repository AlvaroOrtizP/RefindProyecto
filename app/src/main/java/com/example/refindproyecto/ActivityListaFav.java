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
import com.example.refindproyecto.POJOS.Anuncio;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;
import com.example.refindproyecto.POJOS.Direccion;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActivityListaFav extends AppCompatActivity {
    Direccion direccion = new Direccion();
    ImageButton btnInicio, btnFavorito, btnPerfil;
    List<Anuncio> anuncioList;
    RequestQueue requestQueue;
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
        String fireId = mAuth.getUid();
        init(fireId);

    }
    public void init(String usuarioID){
        anuncioList = new ArrayList<>();
        obtenerAnuncios(direccion.getFavoritos()+usuarioID);
    }
    private  void obtenerAnuncios(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    anuncioList.add(new Anuncio(
                            jsonObject.getInt("anuncio_id"),
                            jsonObject.getString("foto"),
                            jsonObject.getString("titulo"),
                            jsonObject.getString("descripcion")));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            setRecyclerView(anuncioList);
        }, error -> Toast.makeText(getApplicationContext(), R.string.noFavoritos, Toast.LENGTH_SHORT).show()
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void setRecyclerView(List<Anuncio> anuncioList){
        AdaptadorAnun adaptadorAnuncio = new AdaptadorAnun(anuncioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewFav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorAnuncio);
    }


}