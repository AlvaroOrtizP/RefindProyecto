package com.example.refindproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;


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
        ArrayList<Categoria_row> arrayCategorias = new ArrayList<>();

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
}