package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ActivityPerfil extends AppCompatActivity {
    ImageButton btnInicio, btnFavorito, btnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btnInicio =findViewById(R.id.btnInicio);
        btnFavorito =findViewById(R.id.btnFavorito);
        btnPerfil =findViewById(R.id.btnPerfil);

        btnInicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityPerfil.this, MainActivity.class);
                startActivity(i);
            }
        });
        btnFavorito.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityPerfil.this, ActivityFavoritos.class);
                startActivity(i);
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(ActivityPerfil.this, ActivityPerfil.class);
                startActivity(i);
            }
        });


    }

}