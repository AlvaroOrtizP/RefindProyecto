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
import com.example.refindproyecto.POJOS.Categoria;
import com.example.refindproyecto.Adaptador.AdaptadorCat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActivityListaCat extends AppCompatActivity {

    List<Categoria> categoriaList;
    RequestQueue requestQueue;
    ImageButton btnInicio, btnFavorito, btnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cat);
        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);

        btnFavorito.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaCat.this, ActivityListaFav.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {
            Intent i = new Intent(ActivityListaCat.this, ActivityPerfil.class);
            startActivity(i);
        });
        init();
    }
    public void init(){
        categoriaList = new ArrayList<>();
        obtenerCategoria("http://192.168.1.127:80/Android/obtener_categorias.php");
    }
    private  void obtenerCategoria(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        categoriaList.add(new Categoria(jsonObject.getInt("categoria_id"), R.drawable.hotel, jsonObject.getString("titulo"), jsonObject.getString("descripcion")));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setRecyclerView(categoriaList);
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
    private void setRecyclerView(List<Categoria> categoriaList){
        AdaptadorCat listadapter = new AdaptadorCat(categoriaList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerCat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}