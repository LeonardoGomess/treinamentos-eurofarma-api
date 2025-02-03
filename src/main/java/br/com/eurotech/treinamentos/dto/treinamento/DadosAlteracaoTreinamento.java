package br.com.eurotech.treinamentos.dto.treinamento;

import java.time.LocalDate;
import java.time.*;
import java.util.Set;

import br.com.eurotech.treinamentos.model.Apostila;
import br.com.eurotech.treinamentos.model.Aula;
import br.com.eurotech.treinamentos.model.Formato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAlteracaoTreinamento(

     @NotBlank(message = "Campo de nome não pode estar vazio")
     String nome,

     @NotBlank(message = "Campo de descrição não pode estar vazio")
     String descricao,

     Formato formato,
    
     @NotNull(message = "Campo de data de início não pode estar vazio")
     LocalDateTime dataInicio,
    
     @NotNull(message = "Campo de data de fim não pode estar vazio")
     LocalDateTime dataFim,

     // @NotBlank(message = "Campo de capa não pode estar vazio")
     // String capa,

     String timezone,
    
     @NotBlank(message = "Campo de nome do professor não pode estar vazio")
     String nomeProfessor,

     //@NotBlank(message = "Campo de cpf do professor não pode estar vazio")
     String cpfProfessor
      
){

    
}