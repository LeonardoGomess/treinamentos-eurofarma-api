package br.com.eurotech.treinamentos.dto.questao;

import java.util.List;

public record DadosCadastroQuestao(
    String resposta,
    List<String> alternativas,
    String pergunta
) {
    
}
