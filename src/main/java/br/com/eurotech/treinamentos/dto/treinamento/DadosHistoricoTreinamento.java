package br.com.eurotech.treinamentos.dto.treinamento;

import java.time.LocalDateTime;

public record DadosHistoricoTreinamento(
    Long id_aluno,
    Boolean aula_concluida,
    String nome,
    LocalDateTime data_inicio,
    LocalDateTime data_fim,
    String timezone,
    String sala,
    String nome_professor,
    Long id_treinamento
) {
    
}