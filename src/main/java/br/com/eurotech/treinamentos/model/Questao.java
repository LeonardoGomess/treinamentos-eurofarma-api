package br.com.eurotech.treinamentos.model;

import java.util.List;

import br.com.eurotech.treinamentos.dto.questao.DadosCadastroQuestao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Questao {
    
    private String resposta;
    private List<String> alternativas;
    private String pergunta;

    public Questao(DadosCadastroQuestao dados){
        this.resposta = dados.resposta();
        this.alternativas = dados.alternativas();
        this.pergunta = dados.pergunta();
    }
}
