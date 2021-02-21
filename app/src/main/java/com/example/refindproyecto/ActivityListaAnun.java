package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.Anuncio;
import com.example.refindproyecto.Adaptador.AdaptadorAnun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListaAnun extends AppCompatActivity {

    List<Anuncio> anuncioList;
    RequestQueue requestQueue;
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

        String s=getIntent().getStringExtra("categoriaId").toString();
        init(s);
    }
    public void init(String categoriaId){
        anuncioList = new ArrayList<>();
        obtenerAnuncios("http://192.168.1.127:80/Android/obtener_anuncios.php?categoria_id="+categoriaId);
    }
    private  void obtenerAnuncios(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        anuncioList.add(new
                                Anuncio(jsonObject.getInt("anuncio_id"),
                                jsonObject.getString("foto"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("descripcion")));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setRecyclerView(anuncioList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Esta categoria no tiene anuncios por ahora", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ActivityListaAnun.this, ActivityListaCat.class);
                startActivity(i);
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void setRecyclerView(List<Anuncio> anuncioList){
        AdaptadorAnun listadapter = new AdaptadorAnun(anuncioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewAnu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}