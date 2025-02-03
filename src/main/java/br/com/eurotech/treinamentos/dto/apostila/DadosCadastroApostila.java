package br.com.eurotech.treinamentos.dto.apostila;

import br.com.eurotech.treinamentos.model.Treinamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroApostila(
    
    String link,

    Treinamento treinamento

) {

}