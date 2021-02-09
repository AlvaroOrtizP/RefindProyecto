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


import com.example.refindproyecto.POJOS.Anuncio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ActivityFavoritos extends AppCompatActivity {
    private int puerto = 8000;
    private Socket socket;
    ImageButton btnInicio, btnFavorito, btnPerfil;
    TextView tvTitulo;
    int cantidad=-1;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.1.127", puerto);
                    OutputStream os = socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeUTF("favoritos-1");



                    //Pedir Array de Anuncio
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ArrayList<Anuncio> listaFavoritos = ( ArrayList<Anuncio>) ois.readObject();
                    ois.close();
                    cantidad = listaFavoritos.size();

                    dos.close();
                    os.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        tvTitulo.setText(cantidad);
    }/*
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

    }*/
}