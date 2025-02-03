package br.com.eurotech.treinamentos.dto.aula;

import java.time.LocalDateTime;

import br.com.eurotech.treinamentos.model.Aula;
import br.com.eurotech.treinamentos.model.Treinamento;



public record DadosDetalhamentoAula(
    Long id,
    String sala,
    String nome,
    String link,
    Double duracao
    // Treinamento treinamento

){
    public DadosDetalhamentoAula(Aula aula){
        this(aula.getId(),aula.getSala(),aula.getLink(),aula.getNome(),aula.getDuracao());
    }
}
