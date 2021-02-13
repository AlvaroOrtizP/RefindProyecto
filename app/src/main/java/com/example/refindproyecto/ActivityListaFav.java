package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.Anuncio;
import com.example.refindproyecto.adaptador.AdaptadorAnun;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListaFav extends AppCompatActivity {
    private FirebaseAuth mAuth;
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

        btnInicio.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaFav.this, ActivityListaCat.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaFav.this, ActivityPerfil.class);
            startActivity(i);
        });
        mAuth = FirebaseAuth.getInstance();
        String fireId = mAuth.getUid();
        init(fireId);

    }
    public void init(String usuarioID){
        anuncioList = new ArrayList<>();//http://192.168.1.127/Android/obtener_favoritos.php?usuario_firebase=ihCQe2i9jtaStefSlU2sCLZnwH33
        obtenerAnuncios("http://192.168.1.127:80/Android/obtener_favoritos.php?usuario_firebase="+usuarioID);
    }
    private  void obtenerAnuncios(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        anuncioList.add(new Anuncio(
                                jsonObject.getInt("anuncio_id"),
                                R.drawable.hotel_campomar,
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
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void setRecyclerView(List<Anuncio> anuncioList){
        AdaptadorAnun listadapter = new AdaptadorAnun(anuncioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewFav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }

}