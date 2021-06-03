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
import com.example.refindproyecto.ActivityAnuncio;
import com.example.refindproyecto.R;
import java.util.List;

import POJOS.Anuncio;


public class AdaptadorAnun extends RecyclerView.Adapter<AdaptadorAnun.ViewHolder> {
    private List<Anuncio> anuncioList;
    private LayoutInflater nInflater;
    private Context context;
    RequestQueue requestImage;

    public AdaptadorAnun(List<Anuncio> anuncioList, Context context){
        this.nInflater = LayoutInflater.from(context);
        this.context = context;
        this.anuncioList = anuncioList;
        requestImage= Volley.newRequestQueue(context.getApplicationContext());
    }
    @Override
    public int getItemCount () {
        return anuncioList.size();
    }

    @Override
    public AdaptadorAnun.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view = nInflater.from(parent.getContext()).inflate(R.layout.cardviewanu, null, false);
        return new AdaptadorAnun.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.transicion_cardview));
        holder.binData(anuncioList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityAnuncio.class);
                intent.putExtra("anuncio_id", anuncioList.get(position).getAnuncioId().toString());
                context.startActivity(intent);
            }
        });
    }

    public void setItems(List<Anuncio> categoriaList){
        this.anuncioList =categoriaList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenPerfil;
        TextView titulo, descripcion;
        CardView cv;
        ViewHolder(View itemView) {
            super(itemView);
            imagenPerfil = itemView.findViewById(R.id.FotoPerfil);
            titulo = itemView.findViewById(R.id.TextoPublicacion);
            descripcion = itemView.findViewById(R.id.descripcion);
            cv = itemView.findViewById(R.id.cv);
        }

        void binData (final Anuncio itemAnuncio){
            titulo.setText(itemAnuncio.getTitulo());
            descripcion.setText(itemAnuncio.getDescripcion());
            //cargarImagen(imagenPerfil, direccion.getImagesAnuncio()+itemAnuncio.getFotoAnuncio());
            cv.setId(itemAnuncio.getAnuncioId());
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
                //Toast.makeText(getApplication(),"error", Toast.LENGTH_SHORT).show();
            }
        });
        requestImage.add(imageRequest);
    }
}
