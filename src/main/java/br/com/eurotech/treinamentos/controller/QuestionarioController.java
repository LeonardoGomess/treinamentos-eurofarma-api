package br.com.eurotech.treinamentos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eurotech.treinamentos.dto.questionario.DadosCadastroQuestionario;
import br.com.eurotech.treinamentos.dto.questionario.DadosDetalhamentoQuestionario;
import br.com.eurotech.treinamentos.model.Questionario;
import br.com.eurotech.treinamentos.services.QuestionarioService;
import java.util.List;

@RestController
@RequestMapping("/questionario")
public class QuestionarioController {
    
    @Autowired
    QuestionarioService service;
    
    @GetMapping
    public ResponseEntity getQuestionarios(){
        try {
            List<DadosDetalhamentoQuestionario> lista_questionarios_json = service.listarQuestionarios();
            return ResponseEntity.ok(lista_questionarios_json);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id_treinamento}")
    public ResponseEntity getQuestionariosByTreinamento(@PathVariable("id_treinamento") Long id_treinamento){
        try {
            var listaQuestionarios = service.listarQuestionariosByTreinamento(id_treinamento);
            DadosDetalhamentoQuestionario lista_questionarios_json = listaQuestionarios.get(listaQuestionarios.size()-1);
            return ResponseEntity.ok(lista_questionarios_json);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<DadosCadastroQuestionario> insertQuestionario(@RequestBody DadosCadastroQuestionario dto){
        System.out.println(dto);
        service.criarQuestionario(new Questionario(dto));    
        return ResponseEntity.ok(dto);
    }

}
