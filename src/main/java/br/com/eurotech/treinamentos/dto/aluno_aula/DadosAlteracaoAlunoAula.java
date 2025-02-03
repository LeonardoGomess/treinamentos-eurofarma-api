package br.com.eurotech.treinamentos.dto.aluno_aula;

import java.util.List;

import br.com.eurotech.treinamentos.dto.aula.DadosIdAula;
import br.com.eurotech.treinamentos.dto.usuario.DadosIdUsuario;

public record DadosAlteracaoAlunoAula(
    List <Long> alunos_deletados,
    List <Long> alunos_adicionados,
    Long id_treinamento
) {
    
}
