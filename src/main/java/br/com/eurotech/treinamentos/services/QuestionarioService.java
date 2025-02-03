package br.com.eurotech.treinamentos.services;

import java.util.Map;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.google.api.core.ApiFuture;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import br.com.eurotech.treinamentos.dto.questao.DadosDetalhamentoQuestao;
import br.com.eurotech.treinamentos.dto.questionario.DadosDetalhamentoQuestionario;
import br.com.eurotech.treinamentos.model.Questionario;


@Service
public class QuestionarioService {
    public void criarQuestionario(Questionario questionario){
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection("questionario").document();
        questionario.setId(documentReference.getId());
        ApiFuture<WriteResult> apiFuture= documentReference.set(questionario);        
    }

    public List<DadosDetalhamentoQuestionario> listarQuestionariosByTreinamento(Long id_treinamento) throws JsonProcessingException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> apiFuture = firestore.collection("questionario").get();
        List<DadosDetalhamentoQuestionario> listaQuestionarios = new ArrayList<>();
        
        try {
            List<QueryDocumentSnapshot> documents = apiFuture.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                DadosDetalhamentoQuestionario questionario = convertToDadosDetalhamentoQuestionario(document);
                if(questionario.id_treinamento() == id_treinamento){
                    listaQuestionarios.add(questionario);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        return listaQuestionarios;
    }


    public List<DadosDetalhamentoQuestionario> listarQuestionarios() throws JsonProcessingException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> apiFuture = firestore.collection("questionario").get();
        List<DadosDetalhamentoQuestionario> listaQuestionarios = new ArrayList<>();
        
        try {
            List<QueryDocumentSnapshot> documents = apiFuture.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                DadosDetalhamentoQuestionario questionario = convertToDadosDetalhamentoQuestionario(document);
                listaQuestionarios.add(questionario);
                
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        return listaQuestionarios;
    }


    
private DadosDetalhamentoQuestionario convertToDadosDetalhamentoQuestionario(QueryDocumentSnapshot doc) {
    Long idTreinamento = doc.getLong("id_treinamento");
    List<Map<String, Object>> questoesMaps = (List<Map<String, Object>>) doc.get("questoes");
    
    List<DadosDetalhamentoQuestao> questoes = new ArrayList<>();
    if (questoesMaps != null) {
        for (Map<String, Object> questaoMap : questoesMaps) {
            DadosDetalhamentoQuestao questao = convertToDadosDetalhamentoQuestao(questaoMap);
            questoes.add(questao);
        }
    }
    
    return new DadosDetalhamentoQuestionario(idTreinamento, questoes);
}

private DadosDetalhamentoQuestao convertToDadosDetalhamentoQuestao(Map<String, Object> questaoMap) {
    String resposta = (String) questaoMap.get("resposta");
    List<String> alternativas = (List<String>) questaoMap.get("alternativas");
    String pergunta = (String) questaoMap.get("pergunta");
    
    return new DadosDetalhamentoQuestao(resposta, alternativas, pergunta);
}

}
