package br.com.eurotech.treinamentos.dto.usuario;

import br.com.eurotech.treinamentos.model.Setor;

public record DadosAlunoPresenca(
    String cpf,
    String nome, 
    String re,
    Setor setor,
    Boolean aulaConcluida,
    String assinatura
) {
}
