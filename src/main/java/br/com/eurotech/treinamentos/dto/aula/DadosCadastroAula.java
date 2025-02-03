package br.com.eurotech.treinamentos.dto.aula;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.eurotech.treinamentos.dto.usuario.DadosDetalhamentoUsuario;
import br.com.eurotech.treinamentos.model.AlunoAula;
import br.com.eurotech.treinamentos.model.Treinamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record DadosCadastroAula(
    
    String sala,
    String nome,
    String link,
    Double duracao,
    Treinamento treinamento

){

}
