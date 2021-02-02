package com.example.refindproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.refindproyecto.Modelo.Favorito;

import org.w3c.dom.Text;

public class ActivityFavoritos extends AppCompatActivity {
    ImageButton btnInicio, btnFavorito, btnPerfil;
    Favorito favorito = new Favorito();
    TextView tvTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);
        tvTitulo = findViewById(R.id.tvTitulo);
        TextView tvMenuContextual  = findViewById(R.id.tvTitulo);
        registerForContextMenu(tvMenuContextual);

        btnInicio.setOnClickListener(v -> {

            Intent i = new Intent(ActivityFavoritos.this, MainActivity.class);
            startActivity(i);
        });
        btnFavorito.setOnClickListener(v -> {

            Intent i = new Intent(ActivityFavoritos.this, ActivityFavoritos.class);
            startActivity(i);
        });
        btnPerfil.setOnClickListener(v -> {

            Intent i = new Intent(ActivityFavoritos.this, ActivityPerfil.class);
            startActivity(i);
        });

        favorito.setCantidadFavoritos(5);
        if(favorito.getCantidadFavoritos()>0){
            tvTitulo.setText("Ya dispones de anuncios en favortitos. /n A que esperas a por los 50?");
        }
        else{
            tvTitulo.setText("Todavia no tienes ningun Anuncio en favoritos");
        }


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opcion1:
                Toast.makeText(this, "Esperar es sabio", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcion2:
                Toast.makeText(this, "Error de capa 8", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}