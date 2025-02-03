package br.com.eurotech.treinamentos.dto.treinamento;

import java.time.LocalDateTime;
import br.com.eurotech.treinamentos.model.Formato;
import br.com.eurotech.treinamentos.model.Treinamento;

public record DadosListagemTreinamento(
     Long id,
     
     String nome,
     
     String descricao,

     Formato formato,

     Boolean ativo,
    
     LocalDateTime dataInicio,
       
     LocalDateTime dataFim,

     String timezone,

     String nomeProfessor,

     String cpfProfessor
      
     // Set<DadosDetalhamentoAula> aulas,

     // Set<DadosDelhamentoApostila> apostilas
){

     public DadosListagemTreinamento(Treinamento treinamento) {
          this(treinamento.getId(), treinamento.getNome(), treinamento.getDescricao(), treinamento.getFormato(), treinamento.getAtivo(), treinamento.getDataInicio(), treinamento.getDataFim(), treinamento.getTimezone(),treinamento.getNomeProfessor(), treinamento.getCpfProfessor());
     }

    
}