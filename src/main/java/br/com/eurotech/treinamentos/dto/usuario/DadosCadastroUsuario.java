package br.com.eurotech.treinamentos.dto.usuario;

import org.hibernate.validator.constraints.br.CPF;

import br.com.eurotech.treinamentos.model.Setor;
import br.com.eurotech.treinamentos.model.TipoUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
    @NotBlank(message = "O campo CPF não pode estar vazio")
    @CPF(message = "CPF inválido")
    String cpf,

    @NotBlank(message = "O campo de nome não pode estar vazio")
    String nome,

    @NotBlank(message = "O campo de senha não pode estar vazio")
    String senha,

    @NotNull(message = "O campo de especialidade não pode estar vazio")
    TipoUsuario tipo,

    @NotBlank(message = "O campo de re não pode estar vazio")
    String re,

    @NotNull(message = "O campo de setor não pode estar vazio")
    Setor setor
) {
    
}
