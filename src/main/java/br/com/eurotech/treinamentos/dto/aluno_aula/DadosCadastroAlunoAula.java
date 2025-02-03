package br.com.eurotech.treinamentos.dto.aluno_aula;

import java.util.List;

import br.com.eurotech.treinamentos.dto.aula.DadosCadastroAula;
import br.com.eurotech.treinamentos.dto.usuario.DadosIdUsuario;

public record DadosCadastroAlunoAula(
    List <DadosCadastroAula> aulas,
    List <DadosIdUsuario> alunos
) {
    
}
