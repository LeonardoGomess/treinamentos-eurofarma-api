package br.com.eurotech.treinamentos.dto.presenca;

import java.time.LocalDateTime;

public record DadosPresenca(
    String assinaturaFile,
    Long idTreinamento,
    Long idAluno,
    String timezone,
    LocalDateTime dataEHoraAparelhoAluno
) {
    
}
