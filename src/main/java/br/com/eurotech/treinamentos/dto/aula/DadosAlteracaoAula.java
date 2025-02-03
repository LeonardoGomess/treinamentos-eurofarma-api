package br.com.eurotech.treinamentos.dto.aula;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.eurotech.treinamentos.model.Treinamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAlteracaoAula(
    String sala,
    String nome,
    String link,
    Double duracao

) {
    
}
