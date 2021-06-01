package br.org.fatec.softinc.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    public String descricao;
    public double preco;
    public String dataAbertura;
    public String dataFinalizacao;
    public StatusOrdemServico status;

    public List<Comentario> comentarios = new ArrayList<>();
}
