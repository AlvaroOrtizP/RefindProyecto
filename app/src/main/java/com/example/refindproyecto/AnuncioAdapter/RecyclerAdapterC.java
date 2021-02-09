package com.example.refindproyecto.AnuncioAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refindproyecto.POJOS.Categoria;
import com.example.refindproyecto.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterC extends RecyclerView.Adapter<RecyclerAdapterC.RecyclerHolder> {
    private List<Categoria> items;
    private List<Categoria> originalItems;
    private RecyclerItemClick itemClick;

    public RecyclerAdapterC(List<Categoria> items, RecyclerItemClick itemClick) {
        this.items = items;
        this.itemClick = itemClick;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(items);
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_row, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerHolder holder, final int position) {
        final Categoria item = items.get(position);
        holder.imgItem.setImageResource(item.getImageCategoria());
        holder.tvTitulo.setText(item.getTitleCategoria());
        holder.tvDescripcion.setText(item.getDescripCategoria());

        holder.itemView.setOnClickListener(v -> itemClick.itemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filter(final String strSearch) {
        if (strSearch.length() == 0) {
            items.clear();
            items.addAll(originalItems);
        }
        notifyDataSetChanged();
    }


    public class RecyclerHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvTitulo;
        private TextView tvDescripcion;


        public RecyclerHolder(@NonNull View itemView_1) {
            super(itemView_1);

            imgItem = itemView.findViewById(R.id.imageCategoria);
            tvTitulo = itemView.findViewById(R.id.titleCategoria);
            tvDescripcion = itemView.findViewById(R.id.descripCategoria);
        }
    }

    public interface RecyclerItemClick {
        void itemClick(Categoria item);
    }
}
