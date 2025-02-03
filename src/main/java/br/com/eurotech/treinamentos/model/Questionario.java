package br.com.eurotech.treinamentos.model;

import java.util.List;

import br.com.eurotech.treinamentos.dto.questao.DadosCadastroQuestao;
import br.com.eurotech.treinamentos.dto.questionario.DadosCadastroQuestionario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Questionario {
    private String id;
    private Long id_treinamento;
    private List<Questao> questoes;

    public Questionario(DadosCadastroQuestionario dto){
        this.id_treinamento = dto.id_treinamento();
        this.questoes = dto.questoes().stream().map(Questao :: new).toList();
    }
}
