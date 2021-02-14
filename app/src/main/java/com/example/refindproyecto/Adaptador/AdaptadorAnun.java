package com.example.refindproyecto.Adaptador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.refindproyecto.POJOS.Anuncio;
import com.example.refindproyecto.R;
import java.util.List;


public class AdaptadorAnun extends RecyclerView.Adapter<AdaptadorAnun.ViewHolder> {
    private List<Anuncio> anuncioList;
    private LayoutInflater nInflater;
    private Context context;

    public AdaptadorAnun(List<Anuncio> anuncioList, Context context){
        this.nInflater = LayoutInflater.from(context);
        this.context = context;
        this.anuncioList = anuncioList;
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
                //Intent intent = new Intent(context, Perfil.class);
                //intent.putExtra("categoriaId", categoriaList.get(position).getCategoria_id().toString());
                //context.startActivity(intent);
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
            cv.setId(itemAnuncio.getAnuncioId());
        }
    }
}
