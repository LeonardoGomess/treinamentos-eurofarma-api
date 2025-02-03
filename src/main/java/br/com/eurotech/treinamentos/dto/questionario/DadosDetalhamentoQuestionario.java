package br.com.eurotech.treinamentos.dto.questionario;

import br.com.eurotech.treinamentos.dto.questao.DadosCadastroQuestao;
import br.com.eurotech.treinamentos.dto.questao.DadosDetalhamentoQuestao;

import java.util.List;

import org.hibernate.mapping.Array;

import java.util.ArrayList;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
public record DadosDetalhamentoQuestionario(
    Long id_treinamento,
    List<DadosDetalhamentoQuestao> questoes  

) {
    public DadosDetalhamentoQuestionario(QueryDocumentSnapshot doc) {
        
        this(
            doc.getLong("id_treinamento"),
            (List<DadosDetalhamentoQuestao>)doc.get("questoes")
        );
    }
}
