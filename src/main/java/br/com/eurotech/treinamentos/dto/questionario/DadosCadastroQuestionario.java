package br.com.eurotech.treinamentos.dto.questionario;

import br.com.eurotech.treinamentos.dto.questao.DadosCadastroQuestao;
import java.util.List;

public record DadosCadastroQuestionario(
    Long id_treinamento,
    List<DadosCadastroQuestao> questoes  
) {

}