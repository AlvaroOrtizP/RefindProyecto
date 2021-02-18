package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.POJOS.Anuncio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityAnuncio extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RequestQueue requestQueue;
    TextView tvTitulo, tvDescripcion;
    Boolean onFav;
    FloatingActionButton fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        fav = findViewById(R.id.floatingActionButton);
        tvTitulo=findViewById(R.id.tvTituloAnuncio);
        tvDescripcion=findViewById(R.id.tvDescripcionDetail);
        mAuth = FirebaseAuth.getInstance();
        String fireId = mAuth.getUid();
        String anuncioId=getIntent().getStringExtra("anuncio_id").toString();
        obtenerAnuncio("http://192.168.1.127:80/Android/obtener_anuncio.php?anuncio_id="+anuncioId);
        saberFav("http://192.168.1.127:80/Android/saberFav.php?usuario_firebase="+fireId+"&anuncio_id="+anuncioId);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFav){
                    eliminarFav(fireId,anuncioId);
                    Toast.makeText(getApplicationContext(), "Se ha quitado de favoritos", Toast.LENGTH_SHORT).show();
                }else{
                    addFav(fireId,anuncioId);
                    Toast.makeText(getApplicationContext(), "Se ha añadido a favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //saberFav("http://192.168.1.127:80/Android/saberFav.php?usuario_firebase="+ fireId +"&anuncio_id="+anuncioId);
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
                        //anuncio.setFotoPerfil(R.drawable.hotel_campomar);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                tvTitulo.setText(anuncio.getTitulo());
                tvDescripcion.setText(anuncio.getDescripcion());
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
        requestQueue= Volley.newRequestQueue(this);
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
                        int miColor = getResources().getColor(R.color.favOn);
                        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{miColor});
                        //fav.setRippleColor(csl);
                        onFav=true;
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

}
