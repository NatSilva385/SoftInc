package br.org.fatec.softinc.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import br.org.fatec.softinc.R;
import br.org.fatec.softinc.models.Comentario;

public class RVComentario extends RecyclerView.Adapter<RVComentario.ComentarioViewHolder> {
    private final List<Comentario> comentarios;

    public RVComentario(List<Comentario> comentarios){
        this.comentarios=comentarios;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentario_card,parent,false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        holder.card_comentario_comentario.setText(comentarios.get(position).descricao);
        holder.card_comentario_data.setText(comentarios.get(position).dataEnvio);
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder{
        private final TextView card_comentario_comentario;
        private final TextView card_comentario_data;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);

            card_comentario_comentario = (TextView) itemView.findViewById(R.id.card_comentario_comentario);
            card_comentario_data = (TextView) itemView.findViewById(R.id.card_comentario_data);
        }
    }
}
