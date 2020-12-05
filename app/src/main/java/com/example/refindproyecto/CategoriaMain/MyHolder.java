package com.example.refindproyecto.CategoriaMain;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refindproyecto.R;

public class MyHolder extends RecyclerView.ViewHolder {
    ImageView cImagen;
    TextView cTitulo, cDes;
    public MyHolder(@NonNull View itemView) {
        super(itemView);
        this.cImagen = itemView.findViewById(R.id.imageCategoria);
        this.cTitulo = itemView.findViewById(R.id.titleCategoria);
        this.cDes = itemView.findViewById(R.id.descripCategoria);
    }
}
