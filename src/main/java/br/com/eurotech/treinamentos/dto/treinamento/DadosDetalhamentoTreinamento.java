package br.com.eurotech.treinamentos.dto.treinamento;

import java.time.LocalDateTime;
import br.com.eurotech.treinamentos.model.Formato;
import br.com.eurotech.treinamentos.model.Treinamento;

public record DadosDetalhamentoTreinamento(
     Long id,
     
     String nome,
     
     String descricao,

     Formato formato,

     Boolean ativo,
    
     LocalDateTime dataInicio,
       
     LocalDateTime dataFim,

     String nomeProfessor,

     String timezone,

     String cpfProfessor
){

     public DadosDetalhamentoTreinamento(Treinamento treinamento) {
          this(treinamento.getId(), treinamento.getNome(), treinamento.getDescricao(), treinamento.getFormato(), treinamento.getAtivo(), treinamento.getDataInicio(), treinamento.getDataFim(),treinamento.getNomeProfessor(), treinamento.getTimezone() ,treinamento.getCpfProfessor());
     }

    
}