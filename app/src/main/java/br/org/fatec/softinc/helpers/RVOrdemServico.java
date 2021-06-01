package br.org.fatec.softinc.helpers;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.org.fatec.softinc.R;
import br.org.fatec.softinc.models.OrdemServico;

public class RVOrdemServico extends RecyclerView.Adapter<RVOrdemServico.OrdemServicoViewHolder> {

    private List<OrdemServico> ordemServicos;
    private LayoutInflater layoutInflater;

    public RVOrdemServico(List<OrdemServico> ordemServicos){
        this.ordemServicos=ordemServicos;
    }

    @NonNull
    @Override
    public OrdemServicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordem_servico_card,parent,false);
        return new OrdemServicoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdemServicoViewHolder holder, int position) {
        holder.card_ordem_servico_descricao.setText(ordemServicos.get(position).descricao);
        holder.card_ordem_servico_status.setText(ordemServicos.get(position).status.name());
    }

    @Override
    public int getItemCount() {
        return ordemServicos.size();
    }

    public static class OrdemServicoViewHolder extends RecyclerView.ViewHolder{
        private TextView card_ordem_servico_descricao;
        private TextView card_ordem_servico_status;

        public OrdemServicoViewHolder(@NonNull View itemView) {
            super(itemView);

            card_ordem_servico_descricao = (TextView)itemView.findViewById(R.id.card_ordem_servico_descricao);
            card_ordem_servico_status = (TextView)itemView.findViewById(R.id.card_ordem_servico_status);
        }
    }
}
