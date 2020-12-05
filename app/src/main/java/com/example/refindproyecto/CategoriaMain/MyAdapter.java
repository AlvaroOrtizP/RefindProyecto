package com.example.refindproyecto.CategoriaMain;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refindproyecto.CategoriaMain.Categoria_row;
import com.example.refindproyecto.CategoriaMain.MyHolder;
import com.example.refindproyecto.R;

import java.util.ArrayList;

public class MyAdapter  extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Categoria_row> models;
    //Este array list crea una lista de array con parametros definidos en la clase Categoria_row


    public MyAdapter(Context c, ArrayList<Categoria_row> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_row, null);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.cTitulo.setText(models.get(position).getTitleCategoria());
        holder.cDes.setText(models.get(position).getDescripCategoria());
        holder.cImagen.setImageResource(models.get(position).getImageCategoria());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
