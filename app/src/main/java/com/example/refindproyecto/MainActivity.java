package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.refindproyecto.CategoriaMain.Categoria_row;
import com.example.refindproyecto.CategoriaMain.MyAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    ArrayList<Categoria_row> arrayCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=findViewById(R.id.reciclerCategorias);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);

    }
    private ArrayList<Categoria_row> getMyList(){


        Categoria_row categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo1");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo2");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo3");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo4");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo5");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        categoria = new Categoria_row();
        categoria.setTitleCategoria("Hola mundo6");
        categoria.setDescripCategoria("Descripcion hola mundo");
        categoria.setImageCategoria(R.drawable.ic_icon_refind);
        arrayCategorias.add(categoria);

        return arrayCategorias;
    }

    public void irActivityCategoria(View view){
        //Todo: falta ver como obtener el id de la cataegoria indicada
       Toast.makeText(this, arrayCategorias.get(1).getTitleCategoria(), Toast.LENGTH_LONG).show();
        //Intent registro_login = new Intent(this, ActivityLogin.class);
        //startActivity(registro_login);
    }
}