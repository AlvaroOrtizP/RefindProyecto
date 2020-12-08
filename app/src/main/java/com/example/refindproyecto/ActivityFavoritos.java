package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ActivityFavoritos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
    }

    /**
     * Metoto para mostrar u ocultar el menu
     * Debemos crear un metodo con este nombre exactamente al cual le pasamos un parametro de tipo MenuItem
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        //Carpeta res - menu - overflow y pasamos el objeto menu
        getMenuInflater().inflate(R.menu.navigation_items, menu);
        return true;
    }

    /**
     * Metodo para indicar a cual en cual de las opciones se hizo clic
     * Debemos crear un metodo con este nombre exactamente al cual le pasamos un parametro de tipo MenuItem
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        // Id de cada opcion del menu
        if(id == R.id.inicioId){
            Intent main_C_Menu = new Intent(this, MainActivity.class);
            startActivity(main_C_Menu);
        }else if(id == R.id.favoritoId){
            Intent main_C_Menu = new Intent(this, ActivityFavoritos.class);
            startActivity(main_C_Menu);
        }else if(id == R.id.perfilId){
            Intent main_C_Menu = new Intent(this, ActivityPerfil.class);
            startActivity(main_C_Menu);
        }
       /* else if(id == R.id.profileId){
            Intent main_C_Menu = new Intent(this, ActivityLogin.class);
            startActivity(main_C_Menu);
        }*/

        return super.onOptionsItemSelected(item);
    }
}