package com.example.refindproyecto.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.refindproyecto.ActivityListaAnun;
import com.example.refindproyecto.R;
import java.util.List;
import POJOS.Categoria;
import POJOS.Indicador;

public class AdaptadorCat extends RecyclerView.Adapter<AdaptadorCat.ViewHolder> {
    private List<Categoria> categoriaList;
    private LayoutInflater nInflater;
    private Context context;
    private RequestQueue requestImage;

    public AdaptadorCat(List<Categoria> categoriaList, Context context){
        this.nInflater = LayoutInflater.from(context);
        this.context = context;
        this.categoriaList = categoriaList;
        requestImage= Volley.newRequestQueue(context.getApplicationContext());
    }
    @Override
    public int getItemCount () {
        return categoriaList.size();
    }

    @Override
    public AdaptadorCat.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view = nInflater.from(parent.getContext()).inflate(R.layout.cardviewcat, null, false);
        return new AdaptadorCat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.transicion_cardview));
        holder.binData(categoriaList.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityListaAnun.class);
            intent.putExtra("categoriaId", categoriaList.get(position).getCategoriaId().toString());
            context.startActivity(intent);
        });
    }

    public void setItems(List<Categoria> categoriaList){
        this.categoriaList =categoriaList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenPerfil;
        TextView titulo, descripcion;
        CardView cv;
        ViewHolder(View itemView) {
            super(itemView);
            imagenPerfil = itemView.findViewById(R.id.fotoCat);
            titulo = itemView.findViewById(R.id.tituloCat);
            descripcion = itemView.findViewById(R.id.descripcionCat);
            cv = itemView.findViewById(R.id.cvCat);
        }

        void binData (final Categoria itemCategoria){
            titulo.setText(itemCategoria.getTitulo());
            descripcion.setText(itemCategoria.getDescripcion());
            cargarImagen(imagenPerfil, Indicador.IMAGEN_CATEGORIA+itemCategoria.getFoto());
            cv.setId(itemCategoria.getCategoriaId());
        }
    }

    private void cargarImagen(ImageView imagenPerfil, String url){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagenPerfil.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Poner una imagen por defecto
            }
        });
        requestImage.add(imageRequest);
    }
}
