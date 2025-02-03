package br.com.eurotech.treinamentos.dto.questao;

import java.util.List;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

public record DadosDetalhamentoQuestao(
    String resposta,
    List<String> alternativas,
    String pergunta
) {
    public DadosDetalhamentoQuestao(QueryDocumentSnapshot doc){
        this(
            doc.getString("resposta"),
            (List<String>)doc.get("alternativas"),
            doc.getString("pergunta")
        );
    } 

    
}

