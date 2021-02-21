package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.Adaptador.AdaptadorComent;
import com.example.refindproyecto.POJOS.Anuncio;
import com.example.refindproyecto.POJOS.Comentario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAnuncio extends AppCompatActivity {
    List<Comentario> comentarioList;
    private FirebaseAuth mAuth;
    ImageView imageView;
    RequestQueue requestQueue;
    TextView tvTitulo, tvDescripcion;
    Boolean onFav;
    ImageButton fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        fav = findViewById(R.id.imBFav);
        tvTitulo=findViewById(R.id.tvTituloAnuncio);
        tvDescripcion=findViewById(R.id.tvDescripcionDetail);
        imageView=findViewById(R.id.anuncioFoto);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        String fireId = mAuth.getUid();
        String anuncioId=getIntent().getStringExtra("anuncio_id").toString();
        init(anuncioId);
        obtenerAnuncio("http://192.168.1.127:80/Android/obtener_anuncio.php?anuncio_id="+anuncioId);
        saberFav("http://192.168.1.127:80/Android/saberFav.php?usuario_firebase="+fireId+"&anuncio_id="+anuncioId);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFav){
                    eliminarFav(fireId,anuncioId);
                    fav.setImageResource(R.drawable.ic_favorito_off);
                    Toast.makeText(getApplicationContext(), "Se ha quitado de favoritos", Toast.LENGTH_SHORT).show();
                }else{
                    addFav(fireId,anuncioId);
                    fav.setImageResource(R.drawable.ic_favorito_on);
                    Toast.makeText(getApplicationContext(), "Se ha añadido a favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(String anuncioId){
        comentarioList = new ArrayList<>();
        obtenerComentarios("http://192.168.1.127:80/Android/obtener_comentarios.php?anuncio_id="+anuncioId);
    }

    private  void obtenerComentarios(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        comentarioList.add(new Comentario(jsonObject.getInt("comentario_id"), jsonObject.getString("foto"), jsonObject.getString("nombre"), jsonObject.getString("comentario")));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setRecyclerView(comentarioList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
                comentarioList.add(new Comentario(1, "usuario.png", "No existen comentarios", "Danos tu opinion"));
                setRecyclerView(comentarioList);
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private  void obtenerAnuncio(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            Anuncio anuncio = new Anuncio(1,"", "");
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        anuncio.setTitulo(jsonObject.getString("titulo"));
                        anuncio.setDescripcion(jsonObject.getString("descripcion"));
                        anuncio.setFotoAnuncio(jsonObject.getString("foto"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                tvTitulo.setText(anuncio.getTitulo());
                tvDescripcion.setText(anuncio.getDescripcion());
                cargarImagen(anuncio.getFotoAnuncio());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ActivityAnuncio.this, ActivityListaCat.class);
                startActivity(i);
            }
        }
        );

        requestQueue.add(jsonArrayRequest);
    }
    private  void saberFav(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if(response.length()>0)
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        onFav=true;
                        fav.setImageResource(R.drawable.ic_favorito_on);
                        Toast.makeText(getApplicationContext(), "Lo tiene en FAV", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onFav=false;
                fav.setImageResource(R.drawable.ic_favorito_off);
                Toast.makeText(getApplicationContext(), "No lo tiene en fav", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void addFav(String usuarioFire, String anuncioId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.127:80/Android/insertar_fav.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Añadido a Fav", Toast.LENGTH_SHORT).show();
                onFav=true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo añadir a favoritos", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario_firebase",usuarioFire.toString());
                parametros.put("anuncio_id",anuncioId.toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void eliminarFav(String fireId, String anuncioId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.127:80/Android/eliminar_fav.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onFav=false;
                Toast.makeText(getApplicationContext(), "Se ha quitado de favoritos", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo quitar de favoritos", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario_firebase",fireId);
                parametros.put("anuncio_id",anuncioId);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void cargarImagen(String url){
        ImageRequest imageRequest = new ImageRequest("http://192.168.1.127/Android/images/anuncio/"+url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(),"error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }
    private void setRecyclerView(List<Comentario> comentarioList){
        AdaptadorComent listadapter = new AdaptadorComent(comentarioList, this);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewComen);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listadapter);
    }
}
//Todo si le pones al toas igual funciona
//context.getApplicationContext()