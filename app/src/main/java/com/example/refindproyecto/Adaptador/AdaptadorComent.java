package com.example.refindproyecto.Adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.example.refindproyecto.R;
import java.util.List;

import POJOS.Comentario;

public class AdaptadorComent extends RecyclerView.Adapter<AdaptadorComent.ViewHolder>{
    private List<Comentario> comentarioList;
    private LayoutInflater nInflater;
    private Context context;
    RequestQueue requestImage;

    public AdaptadorComent(List<Comentario> comentarioList, Context context){
        this.nInflater = LayoutInflater.from(context);
        this.context = context;
        this.comentarioList = comentarioList;
        requestImage= Volley.newRequestQueue(context.getApplicationContext());
    }
    @Override
    public int getItemCount () {
        return comentarioList.size();
    }

    @Override
    public AdaptadorComent.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view = nInflater.from(parent.getContext()).inflate(R.layout.cardviewcoment, null, false);
        return new AdaptadorComent.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.transicion_cardview));
        holder.binData(comentarioList.get(position));
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Perfil.class);
                intent.putExtra("categoriaId", comentarioList.get(position).getComentarioId().toString());
                context.startActivity(intent);
            }
        });*/
    }

    public void setItems(List<Comentario> comentarioList){
        this.comentarioList = comentarioList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fotoUsuario;
        TextView nombreUsuario, comentario;
        CardView cv;
        ViewHolder(View itemView) {
            super(itemView);
            fotoUsuario = itemView.findViewById(R.id.FotoPerfil);
            nombreUsuario = itemView.findViewById(R.id.TextoPublicacion);
            comentario = itemView.findViewById(R.id.descripcion);
            cv = itemView.findViewById(R.id.cvComentario);
        }

        void binData (final Comentario itemComentario){
           /* nombreUsuario.setText(itemComentario.getNombreUsuario());
            comentario.setText(itemComentario.getComentario());
            String URL=direccion.getImagesUsuario()+itemComentario.getFotoUsuario()+".png";
            cargarImagen(fotoUsuario, URL);//
            Log.i("AdaptadorComent", URL);
            cv.setId(itemComentario.getComentarioId());*/
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
