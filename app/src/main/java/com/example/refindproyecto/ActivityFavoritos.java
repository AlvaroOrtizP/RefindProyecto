package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.refindproyecto.Modelo.Favorito;

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


        btnInicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityFavoritos.this, MainActivity.class);
                startActivity(i);
            }
        });
        btnFavorito.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityFavoritos.this, ActivityFavoritos.class);
                startActivity(i);
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityFavoritos.this, ActivityPerfil.class);
                startActivity(i);
            }
        });

        favorito.setCantidadFavoritos(5);
        if(favorito.getCantidadFavoritos()>0){
            tvTitulo.setText("Ya dispones de anuncios en favortitos. /n A que esperas a por los 50?");
        }
        else{
            tvTitulo.setText("Todavia no tienes ningun Anuncio en favoritos");
        }
    }

}